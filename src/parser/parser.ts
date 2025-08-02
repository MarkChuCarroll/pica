import type { IterationNode, NonterminalNode, TerminalNode } from 'ohm-js'
import { AstNode, Hadron, Ident, UseDecl, Location, type Sym } from "../ast.ts";
import grammar from "./pica_syntax.ohm-bundle";
import type { PicaSemantics } from "./pica_syntax.ohm-bundle";
import { BehaviorDef, BosonDef, BosonOptionDef, BosonStructOptionDef, BosonTupleOptionDef, ChannelDef, Definition, FlavorDef, QuarkDef, strToDirection, TypeParamSpec } from '../defs';
import { Twist } from '../twist.ts';
import { ChannelType, NamedType, SType, TypedName, TypeVar } from '../stypes.ts';
import { BosonStructExpr, BosonTupleExpr, ChannelRefExpr, CreateExpr, Expr, LitCharExpr, LitFloatExpr, LitIntExpr, LitStringExpr, OperatorExpr, stringToOperator, VariableExpr } from '../exprs.ts';
import { Action, AdoptAction, AssignAction, CondAction, CondClause, ExitAction, LocalVarAction, MessagePattern, ParAction, RecvAction, RecvClause, SelectAction, SendAction, SeqAction, StructMessagePattern, TupleMessagePattern, WhileAction } from '../actions.ts';
import { readFileSync } from 'node:fs';

function getLoc(node: NonterminalNode) {
  const lc = node.source.getLineAndColumn();
  return new Location(node.args.sourceFile, lc.lineNum, lc.colNum);
}

class ListNode<T extends AstNode> extends AstNode {
  nodes: T[];

  constructor(loc: Location, nodes: T[]) {
    super(loc);
    this.nodes = nodes;
  }

  twist(): Twist {
    return Twist.obj("ListNode",
      Twist.array("children", this.nodes));

  }

}

class NamePairNode extends AstNode {
  fromName: Sym;
  toName: Sym;
  constructor(loc: Location, fromName: Sym, toName: Sym) {
    super(loc);
    this.fromName = fromName;
    this.toName = toName;
  }

  twist(): Twist {
    return Twist.obj("NameMapping",
      Twist.attr("from", this.fromName),
      Twist.attr("to", this.toName)
    );
  }
}

class NameToValue extends AstNode {
  name: Sym;
  value: Expr;

  constructor(loc: Location, name: Sym, value: Expr) {
    super(loc);
    this.name = name;
    this.value = value;
  }

  twist(): Twist {
    return Twist.obj("NameToValue",
      Twist.attr("name", this.name),
      Twist.value("value", this.value));
  }


}

class PicaParser {
  private semantics: PicaSemantics;

  constructor() {
    this.semantics = grammar.createSemantics();

    this.semantics.addOperation<AstNode>('parse(sourceFile)', {
      Hadron(useNodes, defNodes) {
        const uses = useNodes.children.map((u: NonterminalNode) => u.parse(this.args.sourceFile) as UseDecl);
        const defs = defNodes.children.map((d) => d.parse(this.args.sourceFile) as Definition);
        return new Hadron(this.args.sourceFile, uses, defs);
      },
      Ident_scopedIdent(identNode, _cc, symNode) {
        const ident = identNode.parse() as Ident;
        const name = symNode.sourceString;
        ident.path.push(name);
        return ident;
      },
      Ident_simpleIdent(nameNode) {
        return new Ident(getLoc(this), [nameNode.sourceString]);
      },
      UseDecl_useHadron(_use: TerminalNode, hadron: NonterminalNode) {
        const id = hadron.parse(this.args.sourceFile) as Ident;
        const l = getLoc(this);
        return new UseDecl(l, id, null);
      },
      UseDecl_useNames(_use, _lb, names: IterationNode, _rb, _from, id) {
        const sectId = id.parse(this.args.sourceFile) as Ident;
        const usedNames = names.asIteration().children.map((c) => c.sourceString);
        const l = getLoc(this)
        return new UseDecl(l, sectId, usedNames);
      },
      TypeParamBlock(_lb, tps, _rb) {
        return tps.parse(this.args.sourceFile);
      },
      TypeParamList_list(tpsNode, _comma, tpNode) {
        const tps = tpsNode.parse(this.args.sourceFile) as ListNode<TypeParamSpec>;
        const tp = tpNode.parse(this.args.sourceFile) as TypeParamSpec;
        tps.nodes.push(tp);
        return tps;
      },
      TypeParamList_single(tpNode) {
        const tp = tpNode.parse(this.args.sourceFile) as TypeParamSpec;
        return new ListNode<TypeParamSpec>(getLoc(this), [tp]);
      },
      TypeParamSpec(name, _colon, constraintNode) {
        const tvar = name.sourceString;
        const constraint = (constraintNode.numChildren == 1) ? (constraintNode.child(0).parse(this.args.sourceFile) as SType) : null;
        const l = getLoc(this)
        return new TypeParamSpec(l, tvar, constraint);
      },
      FlavorDef(_fl, name, tpsNode, cs, _is, chs, _end, _at) {
        const flavorName = name.sourceString;
        const tps = tpsNode.parse(this.args.sourceFile) as ListNode<TypeParamSpec>;
        const composes = cs.parse(this.args.sourceFile) as ListNode<SType>;
        const channels = chs.children.map((chan) => chan.parse(this.args.sourceFile) as ChannelDef);
        const l = getLoc(this)
        return new FlavorDef(l, flavorName, tps.nodes, composes.nodes, channels);
      },
      QuarkDef(_kq, nameNode, typeParamsNode,
        valueParamsNode, providesNode, _kIs,
        slotDefNodes, behaviorDefNodes,
        _kwADopt, behNameNode, _lp, behParamsNode,
        _rp, _end, _at) {

        const quarkName = nameNode.sourceString;
        const tps = (typeParamsNode.numChildren > 0) ?
          (typeParamsNode.child(0).parse(this.args.sourceFile) as ListNode<TypeParamSpec>).nodes : [];
        const valueParams = (valueParamsNode.parse(this.args.sourceFile) as ListNode<TypedName>).nodes;
        const provides = (providesNode.numChildren > 0) ? (providesNode.child(0).parse(this.args.sourceFile) as ListNode<ChannelDef>).nodes : [];
        const slots = slotDefNodes.children.map((slot) => slot.parse(this.args.sourceFile) as TypedName);
        const behs = behaviorDefNodes.children.map((beh) => beh.parse(this.args.sourceFile) as BehaviorDef);
        const initialBehName = behNameNode.sourceString;
        const initialBehParams = behParamsNode.asIteration().children.map((p) => p.parse(this.args.sourceFile) as Expr);
        const l = getLoc(this);
        return new QuarkDef(l, quarkName, tps, valueParams, provides, slots, behs, [initialBehName, initialBehParams]);
      },

      BosonDef(_kwB, nameNode, typeParamsNode, _kwIs,
        optionDefsNode, _kwEnd, _at) {
        const bosonName = nameNode.sourceString;
        const typeParams = (typeParamsNode.children.length > 0) ? (typeParamsNode.child(0).parse(this.args.sourceFile) as ListNode<TypeParamSpec>).nodes : [];
        const options = optionDefsNode.children.map((opt) => opt.parse(this.args.sourceFile) as BosonOptionDef)
        const l = getLoc(this)
        return new BosonDef(l, bosonName, typeParams, options);
      },
      BosonOptionDef_bosonTupleOption(nameNode, _lp, fieldsNode, _rp) {
        const name = nameNode.sourceString;
        const fields = fieldsNode.asIteration().children.map((field) => field.parse(this.args.sourceFile) as SType)
        const l = getLoc(this);
        return new BosonTupleOptionDef(l, name, fields);
      },
      BosonOptionDef_bosonStructOption(nameNode, _lb, fieldsNode, _rb) {
        const name = nameNode.sourceString;
        const fields = fieldsNode.asIteration().children.map((fn) => fn.parse(this.args.sourceFile) as TypedName);
        return new BosonStructOptionDef(getLoc(this), name, fields);
      },
      ChannelDef(_kwChan, nameNode, _colon, dirNode, typeNode) {
        const name = nameNode.sourceString;
        const dir = dirNode.sourceString;
        const stype = typeNode.parse(this.args.sourceFile) as SType;
        const channelType = new ChannelType(getLoc(this),
          strToDirection(dir), stype);
        return new ChannelDef(getLoc(this), name, channelType);
      },
      ValueParamBlock(_lp, paramsNode, _rp) {
        const params = paramsNode.asIteration().children.map((p) => p.parse(this.args.sourceFile) as TypedName);
        return new ListNode<TypedName>(getLoc(this), params);
      },
      SlotDef(_kwSl, slotNode) {
        const slot = slotNode.parse(this.args.sourceFile) as TypedName;
        return slot;
      },
      ProvidesBlock(_kwProv, channelsNode) {
        const channels = channelsNode.asIteration().children.map((cn) => cn.parse(this.args.sourceFile) as ChannelDef)
        return new ListNode<ChannelDef>(getLoc(this), channels);
      },
      ComposesBlock(_kwProv, composesNode) {
        const composes = composesNode.asIteration().children.map((comp) => comp.parse(this.args.sourceFile) as SType)
        return new ListNode<SType>(getLoc(this), composes);
      },
      BehaviorDef(_kwBeh, nameNode, paramsNode, _kwIs, actionsNode, _kwEnd, _kwAt) {
        const name = nameNode.sourceString;
        const params = (paramsNode.parse(this.args.sourceFile) as ListNode<TypedName>).nodes;
        const actions = actionsNode.children.map((act) => act.parse(this.args.sourceFile) as Action);
        return new BehaviorDef(getLoc(this), name, params, actions);
      },
      ParAction(_kwPar, _lb, actionNodes, _rb) {
        const actions = actionNodes.children.map((act) => act.parse(this.args.sourceFile) as Action);
        return new ParAction(getLoc(this), actions);
      },
      SeqAction(_kwSeq, _lb, actionNodes, _rb) {
        const actions = actionNodes.children.map((act) => act.parse(this.args.sourceFile) as Action);
        return new SeqAction(getLoc(this), actions);
      },
      SelectAction(_kwSel, _lb, actionNodes, _rb) {
        const actions = actionNodes.children.map((act) => act.parse(this.args.sourceFile) as Action);
        return new SelectAction(getLoc(this), actions);
      },
      AssignAction(nameNode, _ceq, valueNode) {
        const name = nameNode.sourceString;
        const value = valueNode.parse(this.args.sourceFile) as Expr;
        return new AssignAction(getLoc(this), name, value);
      },
      AdoptAction(_kwAdopt, nameNode, _lp, argsNode, _rp) {
        const behName = nameNode.sourceString;
        const args = argsNode.asIteration().children.map((arg) => arg.parse(this.args.sourceFile) as Expr);
        return new AdoptAction(getLoc(this), behName, args);
      },
      LocalAction(_lwLoc, nameNode, _colon, typeNode, _eq, exprNode) {
        const name = nameNode.sourceString;
        const stype = typeNode.parse(this.args.sourceFile) as SType;
        const value = exprNode.parse(this.args.sourceFile) as Expr;
        return new LocalVarAction(getLoc(this), name, stype, value);
      },
      SendAction(_kwSend, chanExprNode, _lp, valueExprNode, _rp) {
        const chanExpr = chanExprNode.parse(this.args.sourceFile) as Expr;
        const valueExpr = valueExprNode.parse(this.args.sourceFile) as Expr;
        return new SendAction(getLoc(this), chanExpr, valueExpr);
      },
      ReceiveAction(_kwRecv, exprNode, _kwDo, clauseNodes, _kwElse, elseActionsNode, _end, _at) {
        const chanExpr = exprNode.parse(this.args.sourceFile) as Expr;
        const clauses = clauseNodes.children.map((cl) => cl.parse(this.args.sourceFile) as RecvClause);
        const elseClause = (elseActionsNode.numChildren > 0) ? elseActionsNode.children.map((a) => a.parse(this.args.sourceFile) as Action) : null;
        return new RecvAction(getLoc(this), chanExpr, clauses, elseClause);
      },
      RecvClause(_kwOn, patternNode, _kwDo, actionsNode, _kwEnd) {
        const pattern = patternNode.parse(this.args.sourceFile) as MessagePattern;
        const actions = actionsNode.children.map((a) => a.parse(this.args.sourceFile) as Action);
        return new RecvClause(getLoc(this), pattern, actions);
      },
      Pattern_tuple(nameNode, _lp, namesNode, _rp) {
        const tupleName = nameNode.parse(this.args.sourceFile) as Ident;
        const names = namesNode.asIteration().children.map((n) => n.sourceString);
        return new TupleMessagePattern(getLoc(this), tupleName, names);
      },
      Pattern_struct(nameNode, _lb, fieldsNode, _rb) {
        const tupleName = nameNode.parse(this.args.sourceFile) as Ident;
        const fields = fieldsNode.asIteration().children.map((f) => f.parse(this.args.sourceFile) as NamePairNode).map((np: NamePairNode): [string, string] => [np.fromName, np.toName]);
        return new StructMessagePattern(getLoc(this), tupleName, fields);
      },
      CondAction(_kwCond, clausesNode, _kwElse, elseActionsNode, _end, _at) {
        const clauses = clausesNode.children.map((cl) => cl.parse(this.args.sourceFile) as CondClause);
        const elseClause = elseActionsNode.children.map((act) => act.parse(this.args.sourceFile) as Action);
        return new CondAction(getLoc(this), clauses, elseClause);
      },
      CondClause(exprNode, _kwThen, actionsNode, _kwEnd) {
        const cond = exprNode.parse(this.args.sourceFile) as Expr;
        const actions = actionsNode.children.map((act) => act.parse(this.args.sourceFile) as Action);
        return new CondClause(getLoc(this), cond, actions);
      },
      WhileAction(_kwWhile, condNode, _do, actionsNode, _end, _at) {
        const cond = condNode.parse(this.args.sourceFile) as Expr;
        const body = actionsNode.children.map((act) => act.parse(this.args.sourceFile) as Action);
        return new WhileAction(getLoc(this), cond, body);
      },
      ExitAction(_exit) {
        return new ExitAction(getLoc(this))
      },
      SType_namedType(identNode, typeParamsNode) {
        const ident = identNode.parse(this.args.sourceFile) as Ident;
        const typeParams = typeParamsNode.asIteration().children.map((tp) => tp.parse(this.args.sourceFile) as SType);
        return new NamedType(getLoc(this), ident, typeParams)
      },
      SType_chanType(_kwChan, dirNode, stypeNode) {
        const dir = strToDirection(dirNode.sourceString);
        const stype = stypeNode.parse(this.args.sourceFile) as SType;
        return new ChannelType(getLoc(this), dir, stype);
      },
      SType_typeVar(tvNode) {
        const name = tvNode.sourceString;
        return new TypeVar(getLoc(this), name);
      },
      LogicExpr_binaryLogic(leftNode, opNode, rightNode) {
        const leftExpr = leftNode.parse(this.args.sourceFile) as Expr;
        const op = stringToOperator(opNode.sourceString);
        const rightExpr = rightNode.parse(this.args.sourceFile) as Expr;
        return new OperatorExpr(getLoc(this), op, [leftExpr, rightExpr])
      },
      CompareExpr_binaryCompare(leftNode, opNode, rightNode) {
        const leftExpr = leftNode.parse(this.args.sourceFile) as Expr;
        const op = stringToOperator(opNode.sourceString);
        const rightExpr = rightNode.parse(this.args.sourceFile) as Expr;
        return new OperatorExpr(getLoc(this), op, [leftExpr, rightExpr])
      },

      AddExpr_binaryAdd(leftNode, opNode, rightNode) {
        const leftExpr = leftNode.parse(this.args.sourceFile) as Expr;
        const op = stringToOperator(opNode.sourceString);
        const rightExpr = rightNode.parse(this.args.sourceFile) as Expr;
        return new OperatorExpr(getLoc(this), op, [leftExpr, rightExpr])
      },

      MultExpr_binaryMult(leftNode, opNode, rightNode) {
        const leftExpr = leftNode.parse(this.args.sourceFile) as Expr;
        const op = stringToOperator(opNode.sourceString);
        const rightExpr = rightNode.parse(this.args.sourceFile) as Expr;
        return new OperatorExpr(getLoc(this), op, [leftExpr, rightExpr])
      },
      ExpExpr_binaryExp(leftNode, opNode, rightNode) {
        const leftExpr = leftNode.parse(this.args.sourceFile) as Expr;
        const op = stringToOperator(opNode.sourceString);
        const rightExpr = rightNode.parse(this.args.sourceFile) as Expr;
        return new OperatorExpr(getLoc(this), op, [leftExpr, rightExpr])
      },
      UnaryExpr_unaryWithOp(opNode, exprNode) {
        const op = stringToOperator(opNode.sourceString);
        const expr = exprNode.parse(this.args.sourceFile) as Expr;
        return new OperatorExpr(getLoc(this), op, [expr]);
      },
      PostfixExpr_postfixField(leftNode, _dot, nameNode) {
        const left = leftNode.parse(this.args.sourceFile) as Expr;
        const name = nameNode.sourceString;
        return new ChannelRefExpr(getLoc(this), left, name);
      },
      ParenExpr(_lp, e, _rp) {
        return e.parse(this.args.sourceFile) as Expr;
      },
      CreateExpr(_kwCreate, typeNode, _lp, exprsNode, _rp) {
        const stype = typeNode.parse(this.args.sourceFile) as SType;
        const args = exprsNode.asIteration().children.map((arg) => arg.parse(this.args.sourceFile) as Expr);
        return new CreateExpr(getLoc(this), stype, args);
      },
      BosonValueExpr_tuple(identNode, _lp, valuesNode, _rp) {
        const ident = identNode.parse(this.args.sourceFile) as Ident;
        const values = valuesNode.asIteration().children.map((expr) => expr.parse(this.args.sourceFile) as Expr);
        return new BosonTupleExpr(getLoc(this), ident, null, values);
      },
      BosonValueExpr_struct(identNode, _lb, fieldsNode, _rb) {
        const ident = identNode.parse(this.args.sourceFile) as Ident;
        const fields = fieldsNode.asIteration().children.map((field) => field.parse(this.args.sourceFile) as NameToValue).map((nv): [Sym, Expr] => [nv.name, nv.value]);
        return new BosonStructExpr(getLoc(this), ident, null, fields);
      },
      BosonStructField(nameNode, _colon, valueNode) {
        const name = nameNode.sourceString;
        const value = valueNode.parse(this.args.sourceFile) as Expr;
        return new NameToValue(getLoc(this), name, value);
      },
      intlit(_sign, _digits) {
        return new LitIntExpr(getLoc(this), Number.parseInt(this.sourceString))
      },
      strlit(_arg0, _arg1, _arg2) {
        return new LitStringExpr(getLoc(this), this.sourceString);
      },
      floatlit(_a0, _a1, _a2, _a3) {
        return new LitFloatExpr(getLoc(this), Number.parseFloat(this.sourceString));
      },
      charlit(_lq, c, _rq) {
        const ch = c.sourceString;
        return new LitCharExpr(getLoc(this), ch);
      },
      VarRefExpr(name) {
        return new VariableExpr(getLoc(this), name.sourceString)
      },
      TypedName(nameNode, _colon, typeNode) {
        const name = nameNode.sourceString;
        const stype = typeNode.parse(this.args.sourceFile) as SType;
        return new TypedName(getLoc(this), name, stype);
      },
      EmptyListOf(): AstNode {
        return new ListNode<AstNode>(getLoc(this), []);
      },

    });
  }

  parse_file(path: string): Hadron {
    const text = readFileSync(path);
    let m = grammar.match(text.toString());
    if (m.failed()) {
      console.error("Syntax error: ", m)
      throw new Error(`Syntax error: ${m.message}`)
    }
    const s = this.semantics(m);
    console.log(s);
    return s.parse(path);
  }
}

function main(args: string[]) {
  const parser = new PicaParser();
  const result = parser.parse_file(args[args.length - 1]!);
  console.log(result.twist().render(0));
}

main(process.argv);

