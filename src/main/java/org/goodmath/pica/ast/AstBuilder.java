/* Copyright 2022, Mark C. Chu-Carroll
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.goodmath.pica.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.goodmath.pica.ast.actions.*;
import org.goodmath.pica.ast.bosons.BosonDef;
import org.goodmath.pica.ast.bosons.BosonOption;
import org.goodmath.pica.ast.bosons.BosonStructOption;
import org.goodmath.pica.ast.bosons.BosonTupleOption;
import org.goodmath.pica.ast.exprs.*;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.locations.SourceFileLocation;
import org.goodmath.pica.ast.quarks.*;
import org.goodmath.pica.ast.types.ChannelType;
import org.goodmath.pica.ast.types.NamedType;
import org.goodmath.pica.ast.types.ParameterizedType;
import org.goodmath.pica.ast.types.Type;
import org.goodmath.pica.parser.PicaGrammarListener;
import org.goodmath.pica.parser.PicaGrammarParser;
import org.goodmath.pica.parser.PicaGrammarParser.AddExprContext;
import org.goodmath.pica.parser.PicaGrammarParser.ArgContext;
import org.goodmath.pica.parser.PicaGrammarParser.ArgSpecContext;
import org.goodmath.pica.parser.PicaGrammarParser.AssignActionContext;
import org.goodmath.pica.parser.PicaGrammarParser.BosonBodyContext;
import org.goodmath.pica.parser.PicaGrammarParser.BosonDefContext;
import org.goodmath.pica.parser.PicaGrammarParser.ChannelDefContext;
import org.goodmath.pica.parser.PicaGrammarParser.ChoiceActionContext;
import org.goodmath.pica.parser.PicaGrammarParser.CompareExprContext;
import org.goodmath.pica.parser.PicaGrammarParser.DefinitionContext;
import org.goodmath.pica.parser.PicaGrammarParser.DotLvalueContext;
import org.goodmath.pica.parser.PicaGrammarParser.ExprListContext;
import org.goodmath.pica.parser.PicaGrammarParser.FunTypeContext;
import org.goodmath.pica.parser.PicaGrammarParser.IdListContext;
import org.goodmath.pica.parser.PicaGrammarParser.IdentContext;
import org.goodmath.pica.parser.PicaGrammarParser.KeyValueListContext;
import org.goodmath.pica.parser.PicaGrammarParser.KeyValuePairContext;
import org.goodmath.pica.parser.PicaGrammarParser.ListExprContext;
import org.goodmath.pica.parser.PicaGrammarParser.LitCharExprContext;
import org.goodmath.pica.parser.PicaGrammarParser.LitFloatExprContext;
import org.goodmath.pica.parser.PicaGrammarParser.LitIntExprContext;
import org.goodmath.pica.parser.PicaGrammarParser.LitStrExprContext;
import org.goodmath.pica.parser.PicaGrammarParser.LogicExprContext;
import org.goodmath.pica.parser.PicaGrammarParser.LoopActionContext;
import org.goodmath.pica.parser.PicaGrammarParser.LvalueExprContext;
import org.goodmath.pica.parser.PicaGrammarParser.ModuleContext;
import org.goodmath.pica.parser.PicaGrammarParser.MultExprContext;
import org.goodmath.pica.parser.PicaGrammarParser.NamedTypeContext;
import org.goodmath.pica.parser.PicaGrammarParser.NegateExprContext;
import org.goodmath.pica.parser.PicaGrammarParser.ParActionContext;
import org.goodmath.pica.parser.PicaGrammarParser.ParenActionContext;
import org.goodmath.pica.parser.PicaGrammarParser.ParenExprContext;
import org.goodmath.pica.parser.PicaGrammarParser.PatternContext;
import org.goodmath.pica.parser.PicaGrammarParser.QuarkDefContext;
import org.goodmath.pica.parser.PicaGrammarParser.RunExprContext;
import org.goodmath.pica.parser.PicaGrammarParser.SendActionContext;
import org.goodmath.pica.parser.PicaGrammarParser.SequenceActionContext;
import org.goodmath.pica.parser.PicaGrammarParser.SimpleLvalueContext;
import org.goodmath.pica.parser.PicaGrammarParser.SlotDefContext;
import org.goodmath.pica.parser.PicaGrammarParser.StructPatternContext;
import org.goodmath.pica.parser.PicaGrammarParser.TuplePatternContext;
import org.goodmath.pica.parser.PicaGrammarParser.TypeArgBlockContext;
import org.goodmath.pica.parser.PicaGrammarParser.TypeListContext;
import org.goodmath.pica.parser.PicaGrammarParser.TypeParamBlockContext;
import org.goodmath.pica.parser.PicaGrammarParser.TypeParamSpecContext;
import org.goodmath.pica.parser.PicaGrammarParser.TypedIdContext;
import org.goodmath.pica.parser.PicaGrammarParser.TypedIdListContext;
import org.goodmath.pica.parser.PicaGrammarParser.UseDefContext;
import org.goodmath.pica.parser.PicaGrammarParser.VardefStmtContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AstBuilder implements PicaGrammarListener {

    public AstBuilder(String moduleFileName, Identifier module) {
        this.moduleFile = moduleFileName;
        this.moduleName = module;
    }

    private final ParseTreeProperty<AstNode> values = new ParseTreeProperty<>();

    private final String moduleFile;
    private final Identifier moduleName;

    private PicaModule theModule = null;

    public PicaModule getParsedModule() {
        return theModule;
    }

    private AstNode getAstNodeFor(ParseTree node) {
        return values.get(node);
    }

    private void setAstNodeFor(ParseTree node, AstNode value) {
        values.put(node, value);
    }

    @Override
    public void visitTerminal(TerminalNode node) {
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
    }

    @Override
    public void enterModule(ModuleContext ctx) {
    }

    @Override
    public void exitModule(ModuleContext ctx) {
        var defs = ctx.definition().stream().map(d -> (Definition)getAstNodeFor(d)).toList();
        var uses = ctx.useDef().stream().map(u -> (UseDef)getAstNodeFor(u)).toList();
        theModule = new PicaModule(moduleFile, uses, defs, Location.NO_LOCATION);
    }

    @Override
    public void enterUseDef(UseDefContext ctx) {
    }

    @Override
    public void exitUseDef(UseDefContext ctx) {
        var usedModule =  (Identifier)getAstNodeFor(ctx.ident());
        var usedNames = ctx.ID().stream().map(ParseTree::getText).toList();
        setAstNodeFor(ctx, new UseDef(moduleName, usedModule, usedNames, loc(ctx)));
    }

    @Override
    public void enterDefinition(DefinitionContext ctx) {
    }

    @Override
    public void exitDefinition(DefinitionContext ctx) {
        if (ctx.quarkDef() != null) {
            setAstNodeFor(ctx, getAstNodeFor(ctx.quarkDef()));
        } else if (ctx.bosonDef() != null) {
            setAstNodeFor(ctx, getAstNodeFor(ctx.bosonDef()));
        } else {
            throw new RuntimeException("Invalid definition");
        }

    }

    @Override
    public void enterFlavorDef(PicaGrammarParser.FlavorDefContext ctx) {

    }

    @Override
    public void exitFlavorDef(PicaGrammarParser.FlavorDefContext ctx) {
        Optional<List<TypeParamSpec>> typeParams =
                Optional.ofNullable(ctx.typeParamBlock()).map(tp ->
                        ((TempWrapper<List<TypeParamSpec>>)getAstNodeFor(tp)).value);
        String name = ctx.ID().getText();
        List<Type> composes = Optional.ofNullable(ctx.composes)
                .map(c -> ((TempWrapper<List<Type>>)getAstNodeFor(c)).value)
                .orElse(Collections.emptyList());

        List<ChannelDef> channels = ctx.channelDef().stream().map(c -> (ChannelDef)getAstNodeFor(c)).toList();
        setAstNodeFor(ctx, new FlavorDef(moduleName, name, typeParams, composes, channels, loc(ctx)));
    }

    @Override
    public void enterQuarkDef(QuarkDefContext ctx) {
    }

    @Override
    public void exitQuarkDef(QuarkDefContext ctx) {
         var name = ctx.ID().getText();
        var typeParams =
                Optional.ofNullable(ctx.typeParamBlock()).map(tp ->
                        ((TempWrapper<List<TypeParamSpec>>)getAstNodeFor(tp)).value);
        var channels = ctx.channelDef().stream().map(c -> (ChannelDef)getAstNodeFor(c)).toList();
        var composes = Optional.ofNullable(ctx.composes).map(c ->
            ((TempWrapper<List<Type>>)getAstNodeFor(c)).value)
            .orElse(Collections.emptyList());
        var action = (Action)getAstNodeFor(ctx.action());
        var slots = ctx.slotDef().stream().map(s -> (SlotDef)getAstNodeFor(s)).toList();
        var params = ((TempWrapper<List<TypedParameter>>)getAstNodeFor(ctx.argSpec())).value;
        setAstNodeFor(ctx, new QuarkDef(moduleName, name, typeParams, params, composes, channels, slots, action, loc(ctx)));
    }

    @Override
    public void enterSlotDef(SlotDefContext ctx) {
    }

    @Override
    public void exitSlotDef(SlotDefContext ctx) {
        var name = ctx.ID().getText();
        var type = (Type)getAstNodeFor(ctx.type());
        var value = (Expr)getAstNodeFor(ctx.expr());
        setAstNodeFor(ctx, new SlotDef(name, type, value, loc(ctx)));
    }

    @Override
    public void enterChannelDef(ChannelDefContext ctx) {
    }

    @Override
    public void exitChannelDef(ChannelDefContext ctx) {
        var name = ctx.ID().getText();
        var type = (Type)getAstNodeFor(ctx.type());
        setAstNodeFor(ctx, new ChannelDef(name, type, loc(ctx)));
    }

    @Override
    public void enterOnClause(PicaGrammarParser.OnClauseContext ctx) {
    }

    @Override
    public void exitOnClause(PicaGrammarParser.OnClauseContext ctx) {
        var pattern = (BosonPattern)getAstNodeFor(ctx.pattern());
        var action = (Action)getAstNodeFor(ctx.action());
        setAstNodeFor(ctx, new BosonMessagePatternAction(pattern, action, loc(ctx)));
    }

    @Override
    public void enterPattern(PatternContext ctx) {
    }

    @Override
    public void exitPattern(PatternContext ctx) {
        var id = ctx.ID().getText();
        if (ctx.tuplePattern() != null) {
            var tupleFields = ((TempWrapper<List<String>>)getAstNodeFor(ctx.tuplePattern())).value;
            setAstNodeFor(ctx, new BosonTuplePattern(id, tupleFields, loc(ctx)));
        } else if (ctx.structPattern() != null ){
            var tupleFields = ((TempWrapper<Map<String, String>>)getAstNodeFor(ctx.structPattern())).value;
            setAstNodeFor(ctx, new BosonStructPattern(id, tupleFields, loc(ctx)));
        } else {
            throw new RuntimeException("Invalid pattern: neither struct nor tuple");
        }
    }

    @Override
    public void enterTuplePattern(TuplePatternContext ctx) {
    }

    @Override
    public void exitTuplePattern(TuplePatternContext ctx) {
        setAstNodeFor(ctx, getAstNodeFor(ctx.idList()));
    }

    @Override
    public void enterStructPattern(StructPatternContext ctx) {
    }

    @Override
    public void exitStructPattern(StructPatternContext ctx) {
        var kps = ctx.keyedPattern().stream()
                .map(kp -> ((TempWrapper<Pair<String, String>>)getAstNodeFor(kp)).value).toList();
        Map<String, String> keyMap = new HashMap<>();
        for (Pair<String, String> entry: kps) {
            keyMap.put(entry.first(), entry.second());
        }
        setAstNodeFor(ctx, new TempWrapper<>(keyMap));
    }

    @Override
    public void enterKeyedPattern(PicaGrammarParser.KeyedPatternContext ctx) {
    }

    @Override
    public void exitKeyedPattern(PicaGrammarParser.KeyedPatternContext ctx) {
        String k = ctx.k.getText();
        String f = ctx.v.getText();
        setAstNodeFor(ctx, new TempWrapper<>(new Pair<>(k, f)));
    }

    @Override
    public void enterIdList(IdListContext ctx) {
    }

    @Override
    public void exitIdList(IdListContext ctx) {
        var ids = ctx.ID().stream().map(ParseTree::getText).toList();
        setAstNodeFor(ctx, new TempWrapper<>(ids));
    }

    @Override
    public void enterTypeParamBlock(TypeParamBlockContext ctx) {
    }

    @Override
    public void exitTypeParamBlock(TypeParamBlockContext ctx) {
        var tps = ctx.typeParamSpec().stream().map(t -> (TypeParamSpec)getAstNodeFor(t)).toList();
        setAstNodeFor(ctx, new TempWrapper<>(tps));
    }

    @Override
    public void enterTypeParamSpec(TypeParamSpecContext ctx) {
    }

    @Override
    public void exitTypeParamSpec(TypeParamSpecContext ctx) {
        var name = ctx.ID().getText();
        var constraint = Optional.ofNullable(ctx.typeList()).map(
                c -> ((TempWrapper<List<Type>>)getAstNodeFor(c)).value).orElse(Collections.emptyList());
        setAstNodeFor(ctx, new TypeParamSpec(name, constraint, loc(ctx)));
    }

    @Override
    public void enterTypeList(TypeListContext ctx) {
    }

    @Override
    public void exitTypeList(TypeListContext ctx) {
        var types = ctx.type().stream().map(t -> (Type)getAstNodeFor(t)).toList();
        setAstNodeFor(ctx, new TempWrapper<>(types));
    }

    @Override
    public void enterArgSpec(ArgSpecContext ctx) {
    }

    @Override
    public void exitArgSpec(ArgSpecContext ctx) {
        var params = ctx.arg().stream().map(a -> (TypedParameter)getAstNodeFor(a)).toList();
        setAstNodeFor(ctx, new TempWrapper<>(params));
    }

    @Override
    public void enterArg(ArgContext ctx) {
    }

    @Override
    public void exitArg(ArgContext ctx) {
        var name = ctx.ID().getText();
        var type = (Type)getAstNodeFor(ctx.type());
        setAstNodeFor(ctx, new TypedParameter(name, type, loc(ctx)));
    }

    @Override
    public void enterChannelType(PicaGrammarParser.ChannelTypeContext ctx) {
    }

    @Override
    public void exitChannelType(PicaGrammarParser.ChannelTypeContext ctx) {
        var bosonType = (Type)getAstNodeFor(ctx.type());
        setAstNodeFor(ctx, new ChannelType(bosonType, loc(ctx)));
    }

    @Override
    public void enterNamedType(NamedTypeContext ctx) {
    }

    @Override
    public void exitNamedType(NamedTypeContext ctx) {
        var id = (Identifier)getAstNodeFor(ctx.ident());
        var namedTypeNode = new NamedType(id, loc(ctx));
        if (ctx.typeArgBlock() != null) {
            var typeArgs = ((TempWrapper<List<Type>>) getAstNodeFor(ctx.typeArgBlock())).value;
            setAstNodeFor(ctx, new ParameterizedType(namedTypeNode, typeArgs, loc(ctx)));
        } else {
            setAstNodeFor(ctx, namedTypeNode);
        }
    }

    @Override
    public void enterFunType(FunTypeContext ctx) {
    }

    @Override
    public void exitFunType(FunTypeContext ctx) {


    }

    @Override
    public void enterTypeArgBlock(TypeArgBlockContext ctx) {
    }

    @Override
    public void exitTypeArgBlock(TypeArgBlockContext ctx) {
        var types = ctx.type().stream().map(t -> (Type)getAstNodeFor(t)).toList();
        setAstNodeFor(ctx, new TempWrapper<>(types));
    }

    @Override
    public void enterBosonDef(BosonDefContext ctx) {
    }

    @Override
    public void exitBosonDef(BosonDefContext ctx) {
        var typeParams = Optional.ofNullable(ctx.typeParamBlock()).map(tb ->
                ((TempWrapper<List<TypeParamSpec>>)getAstNodeFor(tb)).value);
        var name = ctx.ID().getText();
        var options = ((TempWrapper<List<BosonOption>>)getAstNodeFor(ctx.bosonBody())).value;
        setAstNodeFor(ctx, new BosonDef(moduleName, name, typeParams, options, loc(ctx)));
    }

    @Override
    public void enterBosonBody(BosonBodyContext ctx) {
    }

    @Override
    public void exitBosonBody(BosonBodyContext ctx) {
        var options = ctx.bosonOption().stream().map(bo ->
                (BosonOption)getAstNodeFor(bo)).toList();        setAstNodeFor(ctx, new TempWrapper<>(options));
    }

    @Override
    public void enterTupleBosonOption(PicaGrammarParser.TupleBosonOptionContext ctx) {
    }

    @Override
    public void exitTupleBosonOption(PicaGrammarParser.TupleBosonOptionContext ctx) {
        var name = ctx.ID().getText();
        var fields = ((TempWrapper<List<Type>>)getAstNodeFor(ctx.typeList())).value;
        setAstNodeFor(ctx, new BosonTupleOption(name, fields, loc(ctx)));
    }

    @Override
    public void enterStructBosonOption(PicaGrammarParser.StructBosonOptionContext ctx) {
    }

    @Override
    public void exitStructBosonOption(PicaGrammarParser.StructBosonOptionContext ctx) {
        var name = ctx.ID().getText();
        var fields = ((TempWrapper<Map<String, Type>>)getAstNodeFor(ctx.typedIdList())).value;
        setAstNodeFor(ctx, new BosonStructOption(name, fields, loc(ctx)));
    }


    @Override
    public void enterTypedIdList(TypedIdListContext ctx) {
    }

    @Override
    public void exitTypedIdList(TypedIdListContext ctx) {
        HashMap<String, Type> fields = new HashMap<>();
        ctx.typedId().forEach(ti -> {
            Pair<String, Type> p = ((TempWrapper<Pair<String, Type>>) getAstNodeFor(ti)).value;
            fields.put(p.first(), p.second());
        });
        setAstNodeFor(ctx, new TempWrapper<>(fields));
    }

    @Override
    public void enterTypedId(TypedIdContext ctx) {
    }

    @Override
    public void exitTypedId(TypedIdContext ctx) {
        var name = ctx.ID().toString();
        var type = (Type)getAstNodeFor(ctx.type());
        setAstNodeFor(ctx, new TempWrapper<>(new Pair<>(name, type)));
    }

    @Override
    public void enterReceiveAction(PicaGrammarParser.ReceiveActionContext ctx) {
    }

    @Override
    public void exitReceiveAction(PicaGrammarParser.ReceiveActionContext ctx) {
        var channelName = ctx.ID().getText();
        var handlers = ctx.onClause().stream().map(o -> (BosonMessagePatternAction)getAstNodeFor(o)).toList();
        setAstNodeFor(ctx, new ReceiveAction(channelName, handlers, loc(ctx)));
    }

    @Override
    public void enterAssignAction(AssignActionContext ctx) {
    }

    @Override
    public void exitAssignAction(AssignActionContext ctx) {
        var lval = (Lvalue)getAstNodeFor(ctx.lvalue());
        var expr = (Expr)getAstNodeFor(ctx.expr());
        setAstNodeFor(ctx, new AssignmentAction(lval, expr, loc(ctx)));
    }

    @Override
    public void enterParenAction(ParenActionContext ctx) {
    }

    @Override
    public void exitParenAction(ParenActionContext ctx) {
        setAstNodeFor(ctx, getAstNodeFor(ctx.action()));
    }

    @Override
    public void enterChoiceAction(ChoiceActionContext ctx) {
    }

    @Override
    public void exitChoiceAction(ChoiceActionContext ctx) {
        var actions = ctx.action().stream().map(a -> (Action)getAstNodeFor(ctx)).toList();
        setAstNodeFor(ctx, new ChoiceAction(actions, loc(ctx)));
    }

    @Override
    public void enterSequenceAction(SequenceActionContext ctx) {
    }

    @Override
    public void exitSequenceAction(SequenceActionContext ctx) {
        var actions = ctx.action().stream().map(a -> (Action)getAstNodeFor(a)).toList();
        setAstNodeFor(ctx, new SequenceAction(actions, loc(ctx)));
    }

    @Override
    public void enterExprAction(PicaGrammarParser.ExprActionContext ctx) {
    }

    @Override
    public void exitExprAction(PicaGrammarParser.ExprActionContext ctx) {
        var e = (Expr)getAstNodeFor(ctx.expr());
        setAstNodeFor(ctx, new ExprAction(e));
    }

    @Override
    public void enterSpawnAction(PicaGrammarParser.SpawnActionContext ctx) {
    }

    @Override
    public void exitSpawnAction(PicaGrammarParser.SpawnActionContext ctx) {
        Action act = (Action)getAstNodeFor(ctx.action());
        setAstNodeFor(ctx, new SpawnAction(act, loc(ctx)));
    }

    @Override
    public void enterIfAction(PicaGrammarParser.IfActionContext ctx) {
    }

    @Override
    public void exitIfAction(PicaGrammarParser.IfActionContext ctx) {
        var cond = (Expr)getAstNodeFor(ctx.cond);
        var t = (Action)getAstNodeFor(ctx.t);
        var f = (Action)getAstNodeFor(ctx.f);
        setAstNodeFor(ctx, new IfAction(cond, t, f, loc(ctx)));
    }

    @Override
    public void enterWhileAction(PicaGrammarParser.WhileActionContext ctx) {
    }

    @Override
    public void exitWhileAction(PicaGrammarParser.WhileActionContext ctx) {
        var cond = (Expr)getAstNodeFor(ctx.expr());
        var body = (Action)getAstNodeFor(ctx.action());
        setAstNodeFor(ctx, new WhileAction(cond, body, loc(ctx)));
    }

    @Override
    public void enterReturnAction(PicaGrammarParser.ReturnActionContext ctx) {
    }

    @Override
    public void exitReturnAction(PicaGrammarParser.ReturnActionContext ctx) {
        var e = (Expr)getAstNodeFor(ctx.expr());
        setAstNodeFor(ctx, new ReturnAction(e, loc(ctx)));
    }

    @Override
    public void enterParAction(ParActionContext ctx) {
    }

    @Override
    public void exitParAction(ParActionContext ctx) {
        var actions = ctx.action().stream().map(a -> (Action)getAstNodeFor(ctx)).toList();
        setAstNodeFor(ctx, new ParAction(actions, loc(ctx)));
    }


    @Override
    public void enterSendAction(SendActionContext ctx) {
    }

    @Override
    public void exitSendAction(SendActionContext ctx) {
        var channel = (Identifier)getAstNodeFor(ctx.ident());
        var expr = (Expr)getAstNodeFor(ctx.expr());
        setAstNodeFor(ctx, new SendAction(channel, expr, loc(ctx)));
    }

    @Override
    public void enterVardefStmt(VardefStmtContext ctx) {
    }

    @Override
    public void exitVardefStmt(VardefStmtContext ctx) {
        var name = ctx.ID().getText();
        var type = (Type)getAstNodeFor(ctx.type());
        var initExpr = (Expr)getAstNodeFor(ctx.expr());
        setAstNodeFor(ctx, new VarDefAction(name, type, initExpr, loc(ctx)));
    }

    @Override
    public void enterForAction(PicaGrammarParser.ForActionContext ctx) {
    }

    @Override
    public void exitForAction(PicaGrammarParser.ForActionContext ctx) {
        var idx = ctx.ID().getText();
        var range = (Expr)getAstNodeFor(ctx.expr());
        var body = (Action)getAstNodeFor(ctx.action());
        setAstNodeFor(ctx, new ForAction(idx, range, body, loc(ctx)));
    }

    @Override
    public void enterDotLvalue(DotLvalueContext ctx) {
    }

    @Override
    public void exitDotLvalue(DotLvalueContext ctx) {
        var lv = (Lvalue)getAstNodeFor(ctx.lvalue());
        var index = ctx.v.getText();
        setAstNodeFor(ctx, new Lvalue.IndexedLValue(lv, index, loc(ctx)));
    }

    @Override
    public void enterSimpleLvalue(SimpleLvalueContext ctx) {
    }

    @Override
    public void exitSimpleLvalue(SimpleLvalueContext ctx) {
        var name = (Identifier)getAstNodeFor(ctx.ident());
        setAstNodeFor(ctx, new Lvalue.IdentifierLvalue(name, loc(ctx)));
    }

    @Override
    public void enterLitCharExpr(LitCharExprContext ctx) {
    }

    @Override
    public void exitLitCharExpr(LitCharExprContext ctx) {
        var lit = ctx.LIT_CHAR().getText();
        setAstNodeFor(ctx, new LiteralExpr(LiteralExpr.Kind.CHAR_LIT, lit, loc(ctx)));
    }

    @Override
    public void enterBosonTupleExpr(PicaGrammarParser.BosonTupleExprContext ctx) {
    }

    @Override
    public void exitBosonTupleExpr(PicaGrammarParser.BosonTupleExprContext ctx) {
        var id = (Identifier)getAstNodeFor(ctx.ident());
        var exprs = ((TempWrapper<List<Expr>>)getAstNodeFor(ctx.exprList())).value;
        setAstNodeFor(ctx,new BosonTupleExpr(id, exprs, loc(ctx)));
    }

    @Override
    public void enterLvalueExpr(LvalueExprContext ctx) {
    }

    @Override
    public void exitLvalueExpr(LvalueExprContext ctx) {
        var lv = (Lvalue)getAstNodeFor(ctx.lvalue());
        setAstNodeFor(ctx, lv);
    }

    @Override
    public void enterMultExpr(MultExprContext ctx) {
    }

    @Override
    public void exitMultExpr(MultExprContext ctx) {
        String op = ctx.op.getText();
        var left = (Expr)getAstNodeFor(ctx.l);
        var right = (Expr)getAstNodeFor(ctx.r);
        setAstNodeFor(ctx, new BinaryExpr(BinaryExpr.Operator.fromString(op),
                left, right, loc(ctx)));
    }

    @Override
    public void enterParenExpr(ParenExprContext ctx) {
    }

    @Override
    public void exitParenExpr(ParenExprContext ctx) {
        setAstNodeFor(ctx, getAstNodeFor(ctx.expr()));
    }

    @Override
    public void enterNegateExpr(NegateExprContext ctx) {
    }

    @Override
    public void exitNegateExpr(NegateExprContext ctx) {
        var opStr = ctx.op.getText();
        UnaryExpr.Operator op;
        if (opStr.equals("not")) {
            op = UnaryExpr.Operator.Not;
        } else {
            op = UnaryExpr.Operator.Negate;
        }
        var target = (Expr)getAstNodeFor(ctx.expr());
        setAstNodeFor(ctx, new UnaryExpr(op, target, loc(ctx)));
    }

    @Override
    public void enterLitIntExpr(LitIntExprContext ctx) {
    }

    @Override
    public void exitLitIntExpr(LitIntExprContext ctx) {
        var v = ctx.LIT_INT().getText();
        setAstNodeFor(ctx, new LiteralExpr(LiteralExpr.Kind.INT_LIT, v, loc(ctx)));
    }

    @Override
    public void enterLitStrExpr(LitStrExprContext ctx) {
    }

    @Override
    public void exitLitStrExpr(LitStrExprContext ctx) {
        var v = ctx.LIT_STRING().getText();
        setAstNodeFor(ctx, new LiteralExpr(LiteralExpr.Kind.STRING_LIT, v, loc(ctx)));
    }

    @Override
    public void enterAddExpr(AddExprContext ctx) {
    }

    @Override
    public void exitAddExpr(AddExprContext ctx) {
        String op = ctx.op.getText();
        var left = (Expr)getAstNodeFor(ctx.l);
        var right = (Expr)getAstNodeFor(ctx.r);
        setAstNodeFor(ctx, new BinaryExpr(BinaryExpr.Operator.fromString(op),
                left, right, loc(ctx)));
    }

    @Override
    public void enterLogicExpr(LogicExprContext ctx) {
    }

    @Override
    public void exitLogicExpr(LogicExprContext ctx) {
        String op = ctx.op.getText();
        var left = (Expr)getAstNodeFor(ctx.l);
        var right = (Expr)getAstNodeFor(ctx.r);
        setAstNodeFor(ctx, new BinaryExpr(BinaryExpr.Operator.fromString(op),
                left, right, loc(ctx)));
    }

    @Override
    public void enterBosonStructExpr(PicaGrammarParser.BosonStructExprContext ctx) {
    }

    @Override
    public void exitBosonStructExpr(PicaGrammarParser.BosonStructExprContext ctx) {
        var id = (Identifier)getAstNodeFor(ctx.ident());
        var kvs = ((TempWrapper<Map<String, Expr>>)getAstNodeFor(ctx.keyValueList())).value;
        setAstNodeFor(ctx, new BosonStructExpr(id, kvs, loc(ctx)));
    }


    @Override
    public void enterRunExpr(RunExprContext ctx) {
    }

    @Override
    public void exitRunExpr(RunExprContext ctx) {
        var type = (Type)getAstNodeFor(ctx.type());
        var params = ((TempWrapper<List<Expr>>)getAstNodeFor(ctx.exprList())).value;
        setAstNodeFor(ctx, new CreateQuarkExpr(type, params, loc(ctx)));
    }

    @Override
    public void enterNewChanExpr(PicaGrammarParser.NewChanExprContext ctx) {

    }

    @Override
    public void exitNewChanExpr(PicaGrammarParser.NewChanExprContext ctx) {
        Type t = (Type)getAstNodeFor(ctx.type());
        setAstNodeFor(ctx, new NewChannelExpr(t, loc(ctx)));
    }

    @Override
    public void enterLitFloatExpr(LitFloatExprContext ctx) {
    }

    @Override
    public void exitLitFloatExpr(LitFloatExprContext ctx) {
        var v = ctx.LIT_FLOAT().getText();
        setAstNodeFor(ctx, new LiteralExpr(LiteralExpr.Kind.FLOAT_LIT, v, loc(ctx)));
    }

    @Override
    public void enterListExpr(ListExprContext ctx) {
    }

    @Override
    public void exitListExpr(ListExprContext ctx) {
        var exprs = ((TempWrapper<List<Expr>>)getAstNodeFor(ctx.exprList())).value;
        setAstNodeFor(ctx, new ListExpr(exprs, loc(ctx)));
    }

    @Override
    public void enterCompareExpr(CompareExprContext ctx) {
    }

    @Override
    public void exitCompareExpr(CompareExprContext ctx) {
        String op = ctx.op.getText();
        var left = (Expr)getAstNodeFor(ctx.l);
        var right = (Expr)getAstNodeFor(ctx.r);
        setAstNodeFor(ctx, new BinaryExpr(BinaryExpr.Operator.fromString(op),
                left, right, loc(ctx)));
    }

    @Override
    public void enterKeyValueList(KeyValueListContext ctx) {
    }

    @Override
    public void exitKeyValueList(KeyValueListContext ctx) {
        Map<String, Expr> kvs = new HashMap<>();
        ctx.keyValuePair().forEach(kv -> {
            Pair<String, Expr> p = ((TempWrapper<Pair<String, Expr>>)getAstNodeFor(kv)).value;
            kvs.put(p.first(), p.second());
        });
        setAstNodeFor(ctx, new TempWrapper<>(kvs));
    }

    @Override
    public void enterKeyValuePair(KeyValuePairContext ctx) {
    }

    @Override
    public void exitKeyValuePair(KeyValuePairContext ctx) {
        var k = ctx.ID().getText();
        var e = (Expr)getAstNodeFor(ctx.expr());
        setAstNodeFor(ctx, new TempWrapper<>(new Pair<>(k, e)));
    }

    @Override
    public void enterExprList(ExprListContext ctx) {
    }

    @Override
    public void exitExprList(ExprListContext ctx) {
        var es = ctx.expr().stream().map(e -> (Expr)getAstNodeFor(e)).toList();
        setAstNodeFor(ctx, new TempWrapper<>(es));
    }

    @Override
    public void enterIdent(IdentContext ctx) {
    }

    @Override
    public void exitIdent(IdentContext ctx) {
        var parts = ctx.ID().stream().map(ParseTree::getText).toList();
        Identifier id = null;
        var l = loc(ctx);
        for (String p: parts) {
            id = new Identifier(Optional.ofNullable(id), p, l);
        }
        setAstNodeFor(ctx, id);
    }

    public Location loc(ParserRuleContext ctx) {
        return new SourceFileLocation(moduleFile,
            ctx.start.getLine(),
            ctx.start.getCharPositionInLine());
    }

    @Override
    public void enterLoopAction(LoopActionContext ctx) {
    }

    @Override
    public void exitLoopAction(LoopActionContext ctx) {
        Action a = (Action)getAstNodeFor(ctx.action());
        setAstNodeFor(ctx, new LoopAction(a, loc(ctx)));

    }

    @Override
    public void enterExitAction(PicaGrammarParser.ExitActionContext ctx) {

    }

    @Override
    public void exitExitAction(PicaGrammarParser.ExitActionContext ctx) {
        setAstNodeFor(ctx, new ExitAction(loc(ctx)));

    }

}