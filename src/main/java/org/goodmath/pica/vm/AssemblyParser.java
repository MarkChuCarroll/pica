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
package org.goodmath.pica.vm;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.Pair;
import org.goodmath.pica.vm.QuarkPlasmaAssemblyParser.*;
import org.goodmath.pica.vm.hadron.BosonSpec;
import org.goodmath.pica.vm.hadron.Hadron;
import org.goodmath.pica.vm.hadron.QuarkSpec;
import org.goodmath.pica.vm.instructions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssemblyParser implements QuarkPlasmaAssemblyListener {

    private final ParseTreeProperty<Object> values = new ParseTreeProperty<>();

    private Hadron theModule = null;

    public Hadron getModule() {
        return theModule;
    }

    private Object getValueFor(ParseTree node) {
        return values.get(node);
    }

    private void setValueFor(ParseTree node, Object value) {
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
        Identifier id = Identifier.parseIdentifier(ctx.id.getText());
        List<Identifier> reqs = (List<Identifier>)getValueFor(ctx.idList());
        Map<Identifier, Object> metaValues = new HashMap<>();
        ctx.metaTag().forEach(mt -> {
            Pair<Identifier, Object> entry = (Pair<Identifier, Object>)getValueFor(mt);
            metaValues.put(entry.first(), entry.second());
        });
        List<Instruction> instructions = (List<Instruction>)getValueFor(ctx.body());
        List<QuarkSpec> quarkSpecs = ctx.quark().stream().map(q -> (QuarkSpec)getValueFor(q)).toList();
        List<BosonSpec> bosonSpecs = ctx.boson().stream().map(b -> (BosonSpec)getValueFor(b)).toList();
        theModule = new Hadron(id, reqs, metaValues, bosonSpecs, quarkSpecs, instructions);
    }

    @Override
    public void enterBoson(BosonContext ctx) {

    }

    @Override
    public void exitBoson(BosonContext ctx) {
        String name = ctx.name.getText();
        List<Pair<String, List<Identifier>>> options =
                ctx.bosonOption().stream()
                        .map(o -> (Pair<String, List<Identifier>>)getValueFor(o))
                        .toList();
        setValueFor(ctx, new BosonSpec(name, options));
    }

    @Override
    public void enterBosonOption(BosonOptionContext ctx) {

    }

    @Override
    public void exitBosonOption(BosonOptionContext ctx) {
        String name = ctx.name.getText();
        List<Identifier> types = (List<Identifier>)getValueFor(ctx.idList());
        setValueFor(ctx, new Pair<>(name, types));
    }

    @Override
    public void enterQuark(QuarkContext ctx) {

    }

    @Override
    public void exitQuark(QuarkContext ctx) {
        String name = ctx.name.getText();
        List<Pair<String, Identifier>> channels =
                (List<Pair<String, Identifier>>)getValueFor(ctx.chs);
        List<Pair<String, Identifier>> fields =
                (List<Pair<String, Identifier>>)getValueFor(ctx.fs);
        CodeLocation entry = (CodeLocation)getValueFor(ctx.l);
        setValueFor(ctx, new QuarkSpec(name, channels, fields, entry));
    }

    @Override
    public void enterTypedIdList(TypedIdListContext ctx) {

    }

    @Override
    public void exitTypedIdList(TypedIdListContext ctx) {
        setValueFor(ctx, ctx.typedId().stream().map(id -> (Pair<String, Identifier>)getValueFor(id)).toList());

    }

    @Override
    public void enterTypedId(TypedIdContext ctx) {

    }

    @Override
    public void exitTypedId(TypedIdContext ctx) {
        String name = ctx.k.getText();
        Identifier type = Identifier.parseIdentifier(ctx.v.getText());
        setValueFor(ctx, new Pair<>(name, type));
    }

    @Override
    public void enterMetaTag(MetaTagContext ctx) {

    }

    @Override
    public void exitMetaTag(MetaTagContext ctx) {
        Identifier key = (Identifier)getValueFor(ctx.ident());
        Object val = getValueFor(ctx.metaValue());
        setValueFor(ctx, new Pair<>(key, val));
    }

    @Override
    public void enterMetaId(MetaIdContext ctx) {

    }

    @Override
    public void exitMetaId(MetaIdContext ctx) {
        setValueFor(ctx, getValueFor(ctx.ident()));
    }

    @Override
    public void enterMetaStr(MetaStrContext ctx) {

    }

    @Override
    public void exitMetaStr(MetaStrContext ctx) {
        setValueFor(ctx, ctx.LIT_STRING().getText());
    }

    @Override
    public void enterMetaInt(MetaIntContext ctx) {

    }

    @Override
    public void exitMetaInt(MetaIntContext ctx) {
        setValueFor(ctx, Integer.parseInt(ctx.LIT_INT().getText()));
    }

    @Override
    public void enterMetaList(MetaListContext ctx) {

    }

    @Override
    public void exitMetaList(MetaListContext ctx) {
        setValueFor(ctx, getValueFor(ctx.metaValueList()));
    }

    @Override
    public void enterMetaValueList(MetaValueListContext ctx) {
    }

    @Override
    public void exitMetaValueList(MetaValueListContext ctx) {
        List<Object> values = new ArrayList<>();
        for (MetaValueContext metaValueContext : ctx.metaValue()) {
            values.add(getValueFor(metaValueContext));
        }
        setValueFor(ctx, values);
    }

    @Override
    public void enterIdList(IdListContext ctx) {

    }

    @Override
    public void exitIdList(IdListContext ctx) {
        setValueFor(ctx, ctx.ident().stream().map(id -> (Identifier)getValueFor(id)).toList());
    }

    @Override
    public void enterBody(BodyContext ctx) {

    }

    @Override
    public void exitBody(BodyContext ctx) {
        setValueFor(ctx, ctx.labeledInstruction().stream().map(i -> (Instruction) getValueFor(i)).toList());
    }

    @Override
    public void enterLabeledInstruction(LabeledInstructionContext ctx) {
    }

    @Override
    public void exitLabeledInstruction(LabeledInstructionContext ctx) {
        Instruction i = (Instruction) getValueFor(ctx.instruction());
        if (ctx.ID() != null) {
            i.setLabel(ctx.ID().getText());
        }
        setValueFor(ctx, i);

    }

    @Override
    public void enterIndirectReg(IndirectRegContext ctx) {
    }

    @Override
    public void exitIndirectReg(IndirectRegContext ctx) {
        Reg r = (Reg) getValueFor(ctx.reg());
        setValueFor(ctx, new Reg.IndirectReg(r));
    }

    @Override
    public void enterLocalReg(LocalRegContext ctx) {
    }

    @Override
    public void exitLocalReg(LocalRegContext ctx) {
        int i = Integer.parseInt(ctx.i.getText());
        setValueFor(ctx, new Reg.NumberedReg(i));
    }

    @Override
    public void enterQuarkReg(QuarkRegContext ctx) {
    }

    @Override
    public void exitQuarkReg(QuarkRegContext ctx) {
        setValueFor(ctx, new Reg.QuarkReg());

    }

    @Override
    public void enterNamedReg(NamedRegContext ctx) {

    }

    @Override
    public void exitNamedReg(NamedRegContext ctx) {
        String name = ctx.ID().getText();
        setValueFor(ctx, new Reg.NamedReg(name));

    }

    @Override
    public void enterIntLitReg(IntLitRegContext ctx) {

    }

    @Override
    public void exitIntLitReg(IntLitRegContext ctx) {
        Integer i = Integer.parseInt(ctx.i.getText());
        setValueFor(ctx, new Reg.Literal(i));

    }

    @Override
    public void enterStrLitReg(StrLitRegContext ctx) {

    }

    @Override
    public void exitStrLitReg(StrLitRegContext ctx) {
        String s = ctx.s.getText();
        setValueFor(ctx, new Reg.Literal(s));
    }


    @Override
    public void enterRegList(RegListContext ctx) {

    }

    @Override
    public void exitRegList(RegListContext ctx) {
        setValueFor(ctx,
                ctx.reg().stream().map(r -> (Reg) getValueFor(r)).toList());
    }

    @Override
    public void enterBrIf(BrIfContext ctx) {
    }

    @Override
    public void exitBrIf(BrIfContext ctx) {
        CodeLocation loc = (CodeLocation) getValueFor(ctx.l);
        Reg r = (Reg) getValueFor(ctx.r);
        setValueFor(ctx, new BranchIfTrue(r, loc));

    }

    @Override
    public void enterBget(BgetContext ctx) {
    }

    @Override
    public void exitBget(BgetContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.tgt);
        Reg src = (Reg) getValueFor(ctx.src);
        String fld = ctx.fld.getText();
        setValueFor(ctx, new BGetField(tgt, src, fld));
    }

    @Override
    public void enterBnew(BnewContext ctx) {

    }

    @Override
    public void exitBnew(BnewContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.r);
        String type = ctx.type.getText();
        List<Reg> args = (List<Reg>) getValueFor(ctx.args);
        setValueFor(ctx, new BNew(tgt, type, args));

    }

    @Override
    public void enterBset(BsetContext ctx) {
    }

    @Override
    public void exitBset(BsetContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.tgt);
        String type = ctx.typeName.getText();
        int field = Integer.parseInt(ctx.field.getText());
        Reg val = (Reg) getValueFor(ctx.val);
        setValueFor(ctx, new BSetField(tgt, type, field, val));
    }

    @Override
    public void enterCnew(CnewContext ctx) {

    }

    @Override
    public void exitCnew(CnewContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.tgt);
        CodeLocation loc = (CodeLocation) getValueFor(ctx.l);
        setValueFor(ctx, new CNew(tgt, loc));
    }

    @Override
    public void enterConvert(ConvertContext ctx) {
    }

    @Override
    public void exitConvert(ConvertContext ctx) {
        ConvertValue.Kind srcKind;
        ConvertValue.Kind tgtKind;
        switch (ctx.op.getText()) {
            case "iToF" -> {
                srcKind = ConvertValue.Kind.PrimInt;
                tgtKind = ConvertValue.Kind.PrimFloat;
            }
            case "iToS" -> {
                srcKind = ConvertValue.Kind.PrimInt;
                tgtKind = ConvertValue.Kind.PrimStr;
            }
            case "fToI" -> {
                srcKind = ConvertValue.Kind.PrimFloat;
                tgtKind = ConvertValue.Kind.PrimInt;
            }
            case "fToS" -> {
                srcKind = ConvertValue.Kind.PrimFloat;
                tgtKind = ConvertValue.Kind.PrimStr;
            }
            case "sToI" -> {
                srcKind = ConvertValue.Kind.PrimStr;
                tgtKind = ConvertValue.Kind.PrimInt;
            }
            case "sToF" -> {
                srcKind = ConvertValue.Kind.PrimStr;
                tgtKind = ConvertValue.Kind.PrimFloat;
            }
            default -> throw new RuntimeException("Invalid type conversion " + ctx.op.getText());
        }
        Reg tgt = (Reg) getValueFor(ctx.tgt);
        Reg src = (Reg) getValueFor(ctx.src);
        setValueFor(ctx, new ConvertValue(tgt, tgtKind, src, srcKind));
    }

    @Override
    public void enterEq(EqContext ctx) {

    }

    @Override
    public void exitEq(EqContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.tgt);
        Reg r1 = (Reg) getValueFor(ctx.r1);
        Reg r2 = (Reg) getValueFor(ctx.r2);
        setValueFor(ctx, new Eq(tgt, r1, r2));
    }

    @Override
    public void enterBinop(BinopContext ctx) {
    }


    @Override
    public void exitBinop(BinopContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.tgt);
        Reg r1 = (Reg) getValueFor(ctx.r1);
        Reg r2 = (Reg) getValueFor(ctx.r2);

        String op = ctx.op.getText();
        Instruction instr = switch (op) {
            case "iPlus" -> new IntBinOp(tgt, BinaryNumericOperation.Plus, r1, r2);
            case "iMinus" -> new IntBinOp(tgt, BinaryNumericOperation.Minus, r1, r2);
            case "iTimes" -> new IntBinOp(tgt, BinaryNumericOperation.Multiply, r1, r2);
            case "iDiv" -> new IntBinOp(tgt, BinaryNumericOperation.Divide, r1, r2);
            case "iMod" -> new IntBinOp(tgt, BinaryNumericOperation.Mod, r1, r2);
            case "iGt" -> new IntBinOp(tgt, BinaryNumericOperation.Greater, r1, r2);
            case "iGe" -> new IntBinOp(tgt, BinaryNumericOperation.GE, r1, r2);
            case "iLt" -> new IntBinOp(tgt, BinaryNumericOperation.Less, r1, r2);
            case "iLe" -> new IntBinOp(tgt, BinaryNumericOperation.LE, r1, r2);

            case "fPlus" -> new FloatBinOp(tgt, BinaryNumericOperation.Plus, r1, r2);
            case "fMinus" -> new FloatBinOp(tgt, BinaryNumericOperation.Minus, r1, r2);
            case "fTimes" -> new FloatBinOp(tgt, BinaryNumericOperation.Multiply, r1, r2);
            case "fDiv" -> new FloatBinOp(tgt, BinaryNumericOperation.Divide, r1, r2);
            case "fGt" -> new FloatBinOp(tgt, BinaryNumericOperation.Greater, r1, r2);
            case "fGe" -> new FloatBinOp(tgt, BinaryNumericOperation.GE, r1, r2);
            case "fLt" -> new FloatBinOp(tgt, BinaryNumericOperation.Less, r1, r2);
            case "fLe" -> new FloatBinOp(tgt, BinaryNumericOperation.LE, r1, r2);

            case "sCat" -> new StringBinOp(tgt, StringBinOp.Op.StrConcat, r1, r2);
            case "sGt" -> new StringBinOp(tgt, StringBinOp.Op.StrGreater, r1, r2);
            case "sGe" -> new StringBinOp(tgt, StringBinOp.Op.StrLess, r1, r2);
            case "sLt" -> new StringBinOp(tgt, StringBinOp.Op.StrGE, r1, r2);
            case "sLe" -> new StringBinOp(tgt, StringBinOp.Op.StrLE, r1, r2);
            case "sIdx" -> new SIndex(tgt, r1, r2);
            case "and" -> new And(tgt, r1, r2);
            case "or" -> new Or(tgt, r1, r2);
            default -> throw new RuntimeException("Invalid operator " + op);
        };
        setValueFor(ctx, instr);
    }

    @Override
    public void enterINeg(INegContext ctx) {

    }

    @Override
    public void exitINeg(INegContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.tgt);
        Reg src = (Reg) getValueFor(ctx.src);
        setValueFor(ctx, new INeg(tgt, src));
    }

    @Override
    public void enterNot(NotContext ctx) {
    }

    @Override
    public void exitNot(NotContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.tgt);
        Reg src = (Reg) getValueFor(ctx.src);
        setValueFor(ctx, new Not(tgt, src));
    }

    @Override
    public void enterFNeg(FNegContext ctx) {
    }

    @Override
    public void exitFNeg(FNegContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.tgt);
        Reg src = (Reg) getValueFor(ctx.src);
        setValueFor(ctx, new FNeg(tgt, src));

    }

    @Override
    public void enterFInv(FInvContext ctx) {
    }

    @Override
    public void exitFInv(FInvContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.tgt);
        Reg src = (Reg) getValueFor(ctx.src);
        setValueFor(ctx, new FInv(tgt, src));
    }

    @Override
    public void enterJmp(JmpContext ctx) {

    }

    @Override
    public void exitJmp(JmpContext ctx) {
        CodeLocation loc = (CodeLocation)getValueFor(ctx.loc());
        setValueFor(ctx, new Jmp(loc));
    }

    @Override
    public void enterQgetc(QgetcContext ctx) {
    }

    @Override
    public void exitQgetc(QgetcContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.tgt);
        Reg q = (Reg) getValueFor(ctx.q);
        String type = ctx.qt.getText();
        String name = ctx.name.getText();
        setValueFor(ctx, new QGetChannel(tgt, q, type, name));
    }

    @Override
    public void enterQgets(QgetsContext ctx) {
    }

    @Override
    public void exitQgets(QgetsContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.tgt);
        Reg q = (Reg) getValueFor(ctx.q);
        String type = ctx.qt.getText();
        String name = ctx.name.getText();
        setValueFor(ctx, new QGetState(tgt, q, type, name));

    }

    @Override
    public void enterQsets(QsetsContext ctx) {
    }

    @Override
    public void exitQsets(QsetsContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.tgt);
        String type = ctx.qt.getText();
        String name = ctx.name.getText();
        Reg val = (Reg) getValueFor(ctx.val);
        setValueFor(ctx, new QSetState(tgt, type, name, val));

    }

    @Override
    public void enterQnew(QnewContext ctx) {
    }

    @Override
    public void exitQnew(QnewContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.tgt);
        String type = ctx.qt.getText();
        List<Reg> regs = (List<Reg>) getValueFor(ctx.regList());
        setValueFor(ctx, new QNew(tgt, type, regs));
    }

    @Override
    public void enterRnew(RnewContext ctx) {
    }

    @Override
    public void exitRnew(RnewContext ctx) {
        Reg r = (Reg) getValueFor(ctx.r);
        setValueFor(ctx, new RNew(r));

    }

    @Override
    public void enterSend(SendContext ctx) {
    }

    @Override
    public void exitSend(SendContext ctx) {
        Reg ch = (Reg) getValueFor(ctx.ch);
        Reg msg = (Reg) getValueFor(ctx.msg);
        Reg cont = (Reg) getValueFor(ctx.c);
        setValueFor(ctx, new Send(ch, msg, cont));


    }

    @Override
    public void enterSel(SelContext ctx) {

    }

    @Override
    public void exitSel(SelContext ctx) {
        List<Reg> continuations = (List<Reg>)getValueFor(ctx.regList());
        setValueFor(ctx, new Select(continuations));
    }

    @Override
    public void enterRecv(RecvContext ctx) {
    }

    @Override
    public void exitRecv(RecvContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.target);
        Reg ch = (Reg) getValueFor(ctx.ch);
        Reg cont = (Reg) getValueFor(ctx.c);
        setValueFor(ctx, new Receive(tgt, ch, cont));

    }

    @Override
    public void enterSLen(SLenContext ctx) {
    }

    @Override
    public void exitSLen(SLenContext ctx) {
        Reg tgt = (Reg) getValueFor(ctx.tgt);
        Reg src = (Reg) getValueFor(ctx.src);
        setValueFor(ctx, new SLen(tgt, src));
    }

    @Override
    public void enterSpawn(SpawnContext ctx) {
    }

    @Override
    public void exitSpawn(SpawnContext ctx) {
        List<Reg> conts = (List<Reg>) getValueFor(ctx.r);
        setValueFor(ctx, new Spawn(conts));
    }

    @Override
    public void enterStop(StopContext ctx) {

    }

    @Override
    public void exitStop(StopContext ctx) {
        setValueFor(ctx, new Stop());
    }

    @Override
    public void enterIdent(IdentContext ctx) {

    }

    @Override
    public void exitIdent(IdentContext ctx) {
        List<String> parts = ctx.ID().stream().map(Object::toString).toList();
        Identifier id = Identifier.fromList(parts);
        setValueFor(ctx, id);
    }

    @Override
    public void enterIbget(IbgetContext ctx) {
    }

    @Override
    public void exitIbget(IbgetContext ctx) {
        setValueFor(ctx, getValueFor(ctx.bget()));

    }

    @Override
    public void enterIbnew(IbnewContext ctx) {

    }

    @Override
    public void exitIbnew(IbnewContext ctx) {
        setValueFor(ctx, getValueFor(ctx.bnew()));
    }

    @Override
    public void enterIbrif(IbrifContext ctx) {
    }

    @Override
    public void exitIbrif(IbrifContext ctx) {
        setValueFor(ctx, getValueFor(ctx.brIf()));
    }

    @Override
    public void enterIjmp(IjmpContext ctx) {
    }

    @Override
    public void exitIjmp(IjmpContext ctx) {
        setValueFor(ctx, getValueFor(ctx.jmp()));
    }

    @Override
    public void enterIbset(IbsetContext ctx) {

    }

    @Override
    public void exitIbset(IbsetContext ctx) {
        setValueFor(ctx, getValueFor(ctx.bset()));
    }

    @Override
    public void enterIcnew(IcnewContext ctx) {
    }

    @Override
    public void exitIcnew(IcnewContext ctx) {
        setValueFor(ctx, getValueFor(ctx.cnew()));
    }

    @Override
    public void enterIconvert(IconvertContext ctx) {
    }

    @Override
    public void exitIconvert(IconvertContext ctx) {
        setValueFor(ctx, getValueFor(ctx.convert()));
    }

    @Override
    public void enterIeq(IeqContext ctx) {
    }

    @Override
    public void exitIeq(IeqContext ctx) {
        setValueFor(ctx, getValueFor(ctx.eq()));
    }

    @Override
    public void enterIbinop(IbinopContext ctx) {
    }

    @Override
    public void exitIbinop(IbinopContext ctx) {
        setValueFor(ctx, getValueFor(ctx.binop()));
    }

    @Override
    public void enterIqgetc(IqgetcContext ctx) {
    }

    @Override
    public void exitIqgetc(IqgetcContext ctx) {
        setValueFor(ctx, getValueFor(ctx.qgetc()));
    }

    @Override
    public void enterIqgets(IqgetsContext ctx) {
    }

    @Override
    public void exitIqgets(IqgetsContext ctx) {
        setValueFor(ctx, getValueFor(ctx.qgets()));
    }

    @Override
    public void enterIqsets(IqsetsContext ctx) {
    }

    @Override
    public void exitIqsets(IqsetsContext ctx) {
        setValueFor(ctx, getValueFor(ctx.qsets()));
    }

    @Override
    public void enterIqnew(IqnewContext ctx) {
    }

    @Override
    public void exitIqnew(IqnewContext ctx) {
        setValueFor(ctx, getValueFor(ctx.qnew()));
    }

    @Override
    public void enterIrnew(IrnewContext ctx) {
    }

    @Override
    public void exitIrnew(IrnewContext ctx) {
        setValueFor(ctx, getValueFor(ctx.rnew()));

    }

    @Override
    public void enterIrecv(IrecvContext ctx) {
    }

    @Override
    public void exitIrecv(IrecvContext ctx) {
        setValueFor(ctx, getValueFor(ctx.recv()));
    }

    @Override
    public void enterIsend(IsendContext ctx) {
    }

    @Override
    public void exitIsend(IsendContext ctx) {
        setValueFor(ctx, getValueFor(ctx.send()));
    }

    @Override
    public void enterIspawn(IspawnContext ctx) {
    }

    @Override
    public void exitIspawn(IspawnContext ctx) {
        setValueFor(ctx, getValueFor(ctx.spawn()));
    }

    @Override
    public void enterIstop(IstopContext ctx) {
    }

    @Override
    public void exitIstop(IstopContext ctx) {
        setValueFor(ctx, getValueFor(ctx.stop()));
    }

    @Override
    public void enterIineg(IinegContext ctx) {
    }

    @Override
    public void exitIineg(IinegContext ctx) {
        setValueFor(ctx, getValueFor(ctx.iNeg()));
    }

    @Override
    public void enterIfneg(IfnegContext ctx) {
    }

    @Override
    public void exitIfneg(IfnegContext ctx) {
        setValueFor(ctx, getValueFor(ctx.fNeg()));
    }

    @Override
    public void enterIfinv(IfinvContext ctx) {
    }

    @Override
    public void exitIfinv(IfinvContext ctx) {
        setValueFor(ctx, getValueFor(ctx.fInv()));
    }

    @Override
    public void enterIslen(IslenContext ctx) {
    }

    @Override
    public void exitIslen(IslenContext ctx) {
        setValueFor(ctx, getValueFor(ctx.sLen()));
    }

    @Override
    public void enterInot(InotContext ctx) {
    }

    @Override
    public void exitInot(InotContext ctx) {
        setValueFor(ctx, getValueFor(ctx.not()));
    }

    @Override
    public void enterISel(ISelContext ctx) {

    }

    @Override
    public void exitISel(ISelContext ctx) {
        setValueFor(ctx, getValueFor(ctx.sel()));
    }

    @Override
    public void enterNamedCodeLoc(NamedCodeLocContext ctx) {
    }

    @Override
    public void exitNamedCodeLoc(NamedCodeLocContext ctx) {
        String name = ctx.ID().getText();
        setValueFor(ctx, new CodeLocation.LabeledInstruction(name));
    }

    @Override
    public void enterIndexedCodeLoc(IndexedCodeLocContext ctx) {
    }

    @Override
    public void exitIndexedCodeLoc(IndexedCodeLocContext ctx) {
        int idx = Integer.parseInt(ctx.i.getText());
        setValueFor(ctx, new CodeLocation.IndexedInstruction(idx));
    }

    @Override
    public void enterRelativeCodeLoc(RelativeCodeLocContext ctx) {
    }

    @Override
    public void exitRelativeCodeLoc(RelativeCodeLocContext ctx) {
        int idx = Integer.parseInt(ctx.i.getText());
        setValueFor(ctx, new CodeLocation.RelativeInstruction(idx));
    }

}
