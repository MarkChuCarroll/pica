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
@file:Suppress("UNCHECKED_CAST")

package org.goodmath.pica.vm

import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeProperty
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.tree.TerminalNode
import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.SystemLocation

class HadronParser: QuarkPlasmaAssemblyListener {
    fun parse(input: CharStream): HadronSpec {
        val lexer = QuarkPlasmaAssemblyLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = QuarkPlasmaAssemblyParser(tokens)
        val walker = ParseTreeWalker()
        walker.walk(this, parser.module())
        return getHadron()
    }

    private val values: ParseTreeProperty<Any> = ParseTreeProperty()

    private fun setValueFor(node: ParseTree, value: Any) {
        values.put(node, value)
    }

    private fun getValueFor(node: ParseTree): Any {
        return values.get(node)!!
    }

    private var hadron: HadronSpec? = null

    private fun getHadron(): HadronSpec = hadron!!


    override fun visitTerminal(node: TerminalNode) {
    }

    override fun visitErrorNode(node: ErrorNode) {
        }

    override fun enterEveryRule(ctx: ParserRuleContext) {
    }

    override fun exitEveryRule(ctx: ParserRuleContext) {
    }

    override fun enterModule(ctx: QuarkPlasmaAssemblyParser.ModuleContext) {
    }

    override fun exitModule(ctx: QuarkPlasmaAssemblyParser.ModuleContext) {
        val id = Identifier.fromString(ctx.id.text, SystemLocation("hadron loader"))
        val reqs = getValueFor(ctx.idList()) as List<Identifier>
        val metaValues: MutableList<Pair<Identifier, Any>> = ArrayList()
        ctx.metaTag().forEach { mt ->
            metaValues.add(getValueFor(mt) as Pair<Identifier, Any>)
        }
        val instructions = getValueFor(ctx.body()) as List<Instruction>
        val quarks = ctx.quark().map { getValueFor(it) as QuarkSpec }
        val bosons = ctx.boson().map { getValueFor(it) as BosonSpec }
        hadron = HadronSpec(id, reqs, metaValues, bosons, quarks,instructions)
    }

    override fun enterBoson(ctx: QuarkPlasmaAssemblyParser.BosonContext) {
    }

    override fun exitBoson(ctx: QuarkPlasmaAssemblyParser.BosonContext) {
        val name = ctx.name.text
        val options = ctx.bosonOption().map { o -> getValueFor(o) as Pair<String, List<Identifier>> }
        setValueFor(ctx, BosonSpec(name, options))
    }

    override fun enterBosonOption(ctx: QuarkPlasmaAssemblyParser.BosonOptionContext) {
    }

    override fun exitBosonOption(ctx: QuarkPlasmaAssemblyParser.BosonOptionContext) {
        val name = ctx.name.text
        val types = getValueFor(ctx.idList()) as List<Identifier>
        setValueFor(ctx, Pair(name, types))
    }

    override fun enterQuark(ctx: QuarkPlasmaAssemblyParser.QuarkContext) {
    }

    override fun exitQuark(ctx: QuarkPlasmaAssemblyParser.QuarkContext) {
        val name = ctx.name.text
        val channels = getValueFor(ctx.chs) as List<Pair<String, Identifier>>
        val fields = getValueFor(ctx.fs) as List<Pair<String, Identifier>>
        val entryPoint = getValueFor(ctx.l) as CodeLocation
        setValueFor(ctx, QuarkSpec(name, channels, fields, entryPoint))
    }

    override fun enterTypedIdList(ctx: QuarkPlasmaAssemblyParser.TypedIdListContext) {
    }

    override fun exitTypedIdList(ctx: QuarkPlasmaAssemblyParser.TypedIdListContext) {
        setValueFor(ctx, ctx.typedId().map { getValueFor(it) as Pair<String, Identifier> })
    }

    override fun enterTypedId(ctx: QuarkPlasmaAssemblyParser.TypedIdContext) {
    }

    override fun exitTypedId(ctx: QuarkPlasmaAssemblyParser.TypedIdContext) {
        val name = ctx.k.text
        val type = getValueFor(ctx.v) as Identifier
        setValueFor(ctx, Pair(name, type))
    }

    override fun enterMetaTag(ctx: QuarkPlasmaAssemblyParser.MetaTagContext) {
    }

    override fun exitMetaTag(ctx: QuarkPlasmaAssemblyParser.MetaTagContext) {
        val key = getValueFor(ctx.ident()) as Identifier
        val value = getValueFor(ctx.metaValue())
        setValueFor(ctx, Pair(key, value))
    }

    override fun enterMetaId(ctx: QuarkPlasmaAssemblyParser.MetaIdContext) {
    }

    override fun exitMetaId(ctx: QuarkPlasmaAssemblyParser.MetaIdContext) {
        setValueFor(ctx, getValueFor(ctx.ident()))
    }

    override fun enterMetaStr(ctx: QuarkPlasmaAssemblyParser.MetaStrContext) {
    }

    override fun exitMetaStr(ctx: QuarkPlasmaAssemblyParser.MetaStrContext) {
        setValueFor(ctx, ctx.LIT_STRING().text)
    }

    override fun enterMetaInt(ctx: QuarkPlasmaAssemblyParser.MetaIntContext) {

    }

    override fun exitMetaInt(ctx: QuarkPlasmaAssemblyParser.MetaIntContext) {
        setValueFor(ctx, Integer.parseInt(ctx.LIT_INT().text))
    }

    override fun enterMetaList(ctx: QuarkPlasmaAssemblyParser.MetaListContext) {
    }

    override fun exitMetaList(ctx: QuarkPlasmaAssemblyParser.MetaListContext) {
        setValueFor(ctx, getValueFor(ctx.metaValueList()))
    }

    override fun enterMetaValueList(ctx: QuarkPlasmaAssemblyParser.MetaValueListContext) {
    }

    override fun exitMetaValueList(ctx: QuarkPlasmaAssemblyParser.MetaValueListContext) {
        val values = ctx.metaValue().map { getValueFor(it)}
        setValueFor(ctx, values)
    }

    override fun enterIdList(ctx: QuarkPlasmaAssemblyParser.IdListContext) {
    }

    override fun exitIdList(ctx: QuarkPlasmaAssemblyParser.IdListContext) {
        val ids = ctx.ident().map { getValueFor(it) as Identifier }
        setValueFor(ctx, ids)
    }

    override fun enterBody(ctx: QuarkPlasmaAssemblyParser.BodyContext) {
    }

    override fun exitBody(ctx: QuarkPlasmaAssemblyParser.BodyContext) {
        setValueFor(ctx, ctx.labeledInstruction().map { i -> getValueFor(i) as Instruction })
    }

    override fun enterLabeledInstruction(ctx: QuarkPlasmaAssemblyParser.LabeledInstructionContext) {
    }

    override fun exitLabeledInstruction(ctx: QuarkPlasmaAssemblyParser.LabeledInstructionContext) {
        val label = ctx.ID()?.text
        val instr = getValueFor(ctx.instruction()) as Instruction
        label?.let {instr.label = it }
        setValueFor(ctx, instr)
    }

    override fun enterIbget(ctx: QuarkPlasmaAssemblyParser.IbgetContext) {
    }

    override fun exitIbget(ctx: QuarkPlasmaAssemblyParser.IbgetContext) {
        setValueFor(ctx, getValueFor(ctx.bget()))
    }

    override fun enterIbnew(ctx: QuarkPlasmaAssemblyParser.IbnewContext) {

    }

    override fun exitIbnew(ctx: QuarkPlasmaAssemblyParser.IbnewContext) {
        setValueFor(ctx, getValueFor(ctx.bnew()))

    }

    override fun enterIbrif(ctx: QuarkPlasmaAssemblyParser.IbrifContext) {
    }

    override fun exitIbrif(ctx: QuarkPlasmaAssemblyParser.IbrifContext) {
        setValueFor(ctx, getValueFor(ctx.brIf()))
    }

    override fun enterIjmp(ctx: QuarkPlasmaAssemblyParser.IjmpContext) {
    }

    override fun exitIjmp(ctx: QuarkPlasmaAssemblyParser.IjmpContext) {
        setValueFor(ctx, getValueFor(ctx.jmp()))
        }

    override fun enterIbset(ctx: QuarkPlasmaAssemblyParser.IbsetContext) {
    }

    override fun exitIbset(ctx: QuarkPlasmaAssemblyParser.IbsetContext) {
        setValueFor(ctx, getValueFor(ctx.bset()))
    }

    override fun enterIcnew(ctx: QuarkPlasmaAssemblyParser.IcnewContext) {
    }

    override fun exitIcnew(ctx: QuarkPlasmaAssemblyParser.IcnewContext) {
        setValueFor(ctx, getValueFor(ctx.cnew()))
    }

    override fun enterIconvert(ctx: QuarkPlasmaAssemblyParser.IconvertContext) {
    }

    override fun exitIconvert(ctx: QuarkPlasmaAssemblyParser.IconvertContext) {
        setValueFor(ctx, getValueFor(ctx.convert()))
    }

    override fun enterIeq(ctx: QuarkPlasmaAssemblyParser.IeqContext) {

    }

    override fun exitIeq(ctx: QuarkPlasmaAssemblyParser.IeqContext) {
        setValueFor(ctx, getValueFor(ctx.eq()))
    }

    override fun enterIbinop(ctx: QuarkPlasmaAssemblyParser.IbinopContext) {
    }

    override fun exitIbinop(ctx: QuarkPlasmaAssemblyParser.IbinopContext) {
        setValueFor(ctx, getValueFor(ctx.binop()))
    }

    override fun enterIqgetc(ctx: QuarkPlasmaAssemblyParser.IqgetcContext) {
    }

    override fun exitIqgetc(ctx: QuarkPlasmaAssemblyParser.IqgetcContext) {
        setValueFor(ctx, getValueFor(ctx.qgetc()))
    }

    override fun enterIqgets(ctx: QuarkPlasmaAssemblyParser.IqgetsContext) {
    }

    override fun exitIqgets(ctx: QuarkPlasmaAssemblyParser.IqgetsContext) {
        setValueFor(ctx, getValueFor(ctx.qgets()))
    }

    override fun enterIqsets(ctx: QuarkPlasmaAssemblyParser.IqsetsContext) {
    }

    override fun exitIqsets(ctx: QuarkPlasmaAssemblyParser.IqsetsContext) {
        setValueFor(ctx, getValueFor(ctx.qsets()))
    }

    override fun enterIqnew(ctx: QuarkPlasmaAssemblyParser.IqnewContext) {
    }

    override fun exitIqnew(ctx: QuarkPlasmaAssemblyParser.IqnewContext) {
        setValueFor(ctx, getValueFor(ctx.qnew()))
    }

    override fun enterIrnew(ctx: QuarkPlasmaAssemblyParser.IrnewContext) {
    }

    override fun exitIrnew(ctx: QuarkPlasmaAssemblyParser.IrnewContext) {
        setValueFor(ctx, getValueFor(ctx.rnew()))
    }

    override fun enterIrecv(ctx: QuarkPlasmaAssemblyParser.IrecvContext) {
    }

    override fun exitIrecv(ctx: QuarkPlasmaAssemblyParser.IrecvContext) {
        setValueFor(ctx, getValueFor(ctx.recv()))
    }

    override fun enterIsend(ctx: QuarkPlasmaAssemblyParser.IsendContext) {
    }

    override fun exitIsend(ctx: QuarkPlasmaAssemblyParser.IsendContext) {
        setValueFor(ctx, getValueFor(ctx.send()))
    }

    override fun enterIspawn(ctx: QuarkPlasmaAssemblyParser.IspawnContext) {
    }

    override fun exitIspawn(ctx: QuarkPlasmaAssemblyParser.IspawnContext) {
        setValueFor(ctx, getValueFor(ctx.spawn()))
    }

    override fun enterIstop(ctx: QuarkPlasmaAssemblyParser.IstopContext) {
    }

    override fun exitIstop(ctx: QuarkPlasmaAssemblyParser.IstopContext) {
        setValueFor(ctx, getValueFor(ctx.stop()))
    }

    override fun enterIineg(ctx: QuarkPlasmaAssemblyParser.IinegContext) {
    }

    override fun exitIineg(ctx: QuarkPlasmaAssemblyParser.IinegContext) {
        setValueFor(ctx, getValueFor(ctx.iNeg()))
    }

    override fun enterIfneg(ctx: QuarkPlasmaAssemblyParser.IfnegContext) {
    }

    override fun exitIfneg(ctx: QuarkPlasmaAssemblyParser.IfnegContext) {
        setValueFor(ctx, getValueFor(ctx.fNeg()))
    }

    override fun enterIfinv(ctx: QuarkPlasmaAssemblyParser.IfinvContext) {
    }

    override fun exitIfinv(ctx: QuarkPlasmaAssemblyParser.IfinvContext) {
        setValueFor(ctx, getValueFor(ctx.fInv()))
    }

    override fun enterIslen(ctx: QuarkPlasmaAssemblyParser.IslenContext) {
    }

    override fun exitIslen(ctx: QuarkPlasmaAssemblyParser.IslenContext) {
        setValueFor(ctx, getValueFor(ctx.sLen()))
    }

    override fun enterInot(ctx: QuarkPlasmaAssemblyParser.InotContext) {
    }

    override fun exitInot(ctx: QuarkPlasmaAssemblyParser.InotContext) {
        setValueFor(ctx, getValueFor(ctx.not()))
    }

    override fun enterISel(ctx: QuarkPlasmaAssemblyParser.ISelContext) {
    }

    override fun exitISel(ctx: QuarkPlasmaAssemblyParser.ISelContext) {
        setValueFor(ctx, getValueFor(ctx.sel()))
    }

    override fun enterIndirectReg(ctx: QuarkPlasmaAssemblyParser.IndirectRegContext) {
    }

    override fun exitIndirectReg(ctx: QuarkPlasmaAssemblyParser.IndirectRegContext) {
        val r = getValueFor(ctx.reg()) as Reg
        setValueFor(ctx, Reg.IndirectReg(r))
    }

    override fun enterLocalReg(ctx: QuarkPlasmaAssemblyParser.LocalRegContext) {
    }

    override fun exitLocalReg(ctx: QuarkPlasmaAssemblyParser.LocalRegContext) {
        val idx = Integer.parseInt(ctx.LIT_INT().text)
        setValueFor(ctx, Reg.NumberedReg(idx))
    }

    override fun enterQuarkReg(ctx: QuarkPlasmaAssemblyParser.QuarkRegContext) {
    }

    override fun exitQuarkReg(ctx: QuarkPlasmaAssemblyParser.QuarkRegContext) {
        setValueFor(ctx, Reg.QuarkReg())
    }

    override fun enterNamedReg(ctx: QuarkPlasmaAssemblyParser.NamedRegContext) {
    }

    override fun exitNamedReg(ctx: QuarkPlasmaAssemblyParser.NamedRegContext) {
        val name = ctx.ID().text
        setValueFor(ctx, Reg.NamedReg(name))
    }

    override fun enterIntLitReg(ctx: QuarkPlasmaAssemblyParser.IntLitRegContext) {
    }

    override fun exitIntLitReg(ctx: QuarkPlasmaAssemblyParser.IntLitRegContext) {
        val i = Integer.parseInt(ctx.LIT_INT().text)
        setValueFor(ctx, Reg.LiteralReg(i))
    }

    override fun enterStrLitReg(ctx: QuarkPlasmaAssemblyParser.StrLitRegContext) {

    }

    override fun exitStrLitReg(ctx: QuarkPlasmaAssemblyParser.StrLitRegContext) {
        setValueFor(ctx, Reg.LiteralReg(ctx.LIT_STRING().text))
    }

    override fun enterNamedCodeLoc(ctx: QuarkPlasmaAssemblyParser.NamedCodeLocContext) {
    }

    override fun exitNamedCodeLoc(ctx: QuarkPlasmaAssemblyParser.NamedCodeLocContext) {
        val name = ctx.ID().text
        setValueFor(ctx, NamedCodeLoc(name))
    }

    override fun enterIndexedCodeLoc(ctx: QuarkPlasmaAssemblyParser.IndexedCodeLocContext) {
    }

    override fun exitIndexedCodeLoc(ctx: QuarkPlasmaAssemblyParser.IndexedCodeLocContext) {
        val i = Integer.parseInt(ctx.LIT_INT().text)
        setValueFor(ctx, IndexedCodeLoc(i))
    }

    override fun enterRelativeCodeLoc(ctx: QuarkPlasmaAssemblyParser.RelativeCodeLocContext) {
    }

    override fun exitRelativeCodeLoc(ctx: QuarkPlasmaAssemblyParser.RelativeCodeLocContext) {
        val i = Integer.parseInt(ctx.LIT_INT().text)
        setValueFor(ctx, RelativeCodeLoc(i))
    }

    override fun enterRegList(ctx: QuarkPlasmaAssemblyParser.RegListContext) {
    }

    override fun exitRegList(ctx: QuarkPlasmaAssemblyParser.RegListContext) {
        val regs = ctx.reg().map { getValueFor(it) as Reg }
        setValueFor(ctx, regs)
    }

    override fun enterBrIf(ctx: QuarkPlasmaAssemblyParser.BrIfContext) {
    }

    override fun exitBrIf(ctx: QuarkPlasmaAssemblyParser.BrIfContext) {
        val reg = getValueFor(ctx.r) as Reg
        val tgt = getValueFor(ctx.l) as CodeLocation
        setValueFor(ctx, BranchIfTrue(null, reg, tgt))
    }

    override fun enterBget(ctx: QuarkPlasmaAssemblyParser.BgetContext) {
    }

    override fun exitBget(ctx: QuarkPlasmaAssemblyParser.BgetContext) {
        val target = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.src) as Reg
        val idx = Integer.parseInt(ctx.fld.text)
        setValueFor(ctx, BGetField(null, target, src, idx))
    }

    override fun enterBnew(ctx: QuarkPlasmaAssemblyParser.BnewContext) {
    }

    override fun exitBnew(ctx: QuarkPlasmaAssemblyParser.BnewContext) {
        val target = getValueFor(ctx.reg()) as Reg
        val type = getValueFor(ctx.type) as Identifier
        val args = getValueFor(ctx.args) as List<Reg>
        setValueFor(ctx, BNew(null, target, type, args))
    }

    override fun enterBset(ctx: QuarkPlasmaAssemblyParser.BsetContext) {
    }

    override fun exitBset(ctx: QuarkPlasmaAssemblyParser.BsetContext) {
        val target = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.value) as Reg
        val type = getValueFor(ctx.ident()) as Identifier
        val idx = Integer.parseInt(ctx.field.text)
        setValueFor(ctx, BSetField(null, target, type, idx, src))
    }

    override fun enterCnew(ctx: QuarkPlasmaAssemblyParser.CnewContext) {
    }

    override fun exitCnew(ctx: QuarkPlasmaAssemblyParser.CnewContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val cl = getValueFor(ctx.l) as CodeLocation
        setValueFor(ctx, CNew(null, tgt,cl))
    }

    override fun enterIToF(ctx: QuarkPlasmaAssemblyParser.IToFContext) {
    }

    override fun exitIToF(ctx: QuarkPlasmaAssemblyParser.IToFContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.src) as Reg
        setValueFor(ctx, IToF(null, tgt, src))
    }

    override fun enterIToS(ctx: QuarkPlasmaAssemblyParser.IToSContext) {
    }

    override fun exitIToS(ctx: QuarkPlasmaAssemblyParser.IToSContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.src) as Reg
        setValueFor(ctx, IToS(null, tgt, src))
    }

    override fun enterFToI(ctx: QuarkPlasmaAssemblyParser.FToIContext) {
    }

    override fun exitFToI(ctx: QuarkPlasmaAssemblyParser.FToIContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.src) as Reg
        setValueFor(ctx, FToI(null, tgt, src))
    }

    override fun enterFToS(ctx: QuarkPlasmaAssemblyParser.FToSContext) {
    }

    override fun exitFToS(ctx: QuarkPlasmaAssemblyParser.FToSContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.src) as Reg
        setValueFor(ctx, FToS(null, tgt, src))
    }

    override fun enterSToI(ctx: QuarkPlasmaAssemblyParser.SToIContext) {
    }

    override fun exitSToI(ctx: QuarkPlasmaAssemblyParser.SToIContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.src) as Reg
        setValueFor(ctx, SToI(null, tgt, src))
    }

    override fun enterSToF(ctx: QuarkPlasmaAssemblyParser.SToFContext) {
    }

    override fun exitSToF(ctx: QuarkPlasmaAssemblyParser.SToFContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.src) as Reg
        setValueFor(ctx, SToF(null, tgt, src))
    }

    override fun enterEq(ctx: QuarkPlasmaAssemblyParser.EqContext) {
    }

    override fun exitEq(ctx: QuarkPlasmaAssemblyParser.EqContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, Eq(null, tgt, r1, r2))
    }

    override fun enterIPlus(ctx: QuarkPlasmaAssemblyParser.IPlusContext) {
    }

    override fun exitIPlus(ctx: QuarkPlasmaAssemblyParser.IPlusContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, IAdd(null, tgt, r1, r2))
    }

    override fun enterIMinus(ctx: QuarkPlasmaAssemblyParser.IMinusContext) {
    }

    override fun exitIMinus(ctx: QuarkPlasmaAssemblyParser.IMinusContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, ISubtract(null, tgt, r1, r2))
    }

    override fun enterITimes(ctx: QuarkPlasmaAssemblyParser.ITimesContext) {
    }

    override fun exitITimes(ctx: QuarkPlasmaAssemblyParser.ITimesContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, IMult(null, tgt, r1, r2))
    }

    override fun enterIDiv(ctx: QuarkPlasmaAssemblyParser.IDivContext) {
    }

    override fun exitIDiv(ctx: QuarkPlasmaAssemblyParser.IDivContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, IDiv(null, tgt, r1, r2))
    }

    override fun enterIMod(ctx: QuarkPlasmaAssemblyParser.IModContext) {
    }

    override fun exitIMod(ctx: QuarkPlasmaAssemblyParser.IModContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, IMod(null, tgt, r1, r2))
    }

    override fun enterIGt(ctx: QuarkPlasmaAssemblyParser.IGtContext) {
    }

    override fun exitIGt(ctx: QuarkPlasmaAssemblyParser.IGtContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, IGt(null, tgt, r1, r2))
    }

    override fun enterIGe(ctx: QuarkPlasmaAssemblyParser.IGeContext) {
    }

    override fun exitIGe(ctx: QuarkPlasmaAssemblyParser.IGeContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, IGe(null, tgt, r1, r2))
    }

    override fun enterILt(ctx: QuarkPlasmaAssemblyParser.ILtContext) {
    }

    override fun exitILt(ctx: QuarkPlasmaAssemblyParser.ILtContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, ILt(null, tgt, r1, r2))
    }

    override fun enterILe(ctx: QuarkPlasmaAssemblyParser.ILeContext) {
    }

    override fun exitILe(ctx: QuarkPlasmaAssemblyParser.ILeContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, ILe(null, tgt, r1, r2))
    }

    override fun enterFPlus(ctx: QuarkPlasmaAssemblyParser.FPlusContext) {
    }

    override fun exitFPlus(ctx: QuarkPlasmaAssemblyParser.FPlusContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, FAdd(null, tgt, r1, r2))
    }

    override fun enterFMinus(ctx: QuarkPlasmaAssemblyParser.FMinusContext) {
    }

    override fun exitFMinus(ctx: QuarkPlasmaAssemblyParser.FMinusContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, FSub(null, tgt, r1, r2))
    }

    override fun enterFTimes(ctx: QuarkPlasmaAssemblyParser.FTimesContext) {
    }

    override fun exitFTimes(ctx: QuarkPlasmaAssemblyParser.FTimesContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, FMult(null, tgt, r1, r2))
    }

    override fun enterFDiv(ctx: QuarkPlasmaAssemblyParser.FDivContext) {
    }

    override fun exitFDiv(ctx: QuarkPlasmaAssemblyParser.FDivContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, FDiv(null, tgt, r1, r2))
    }

    override fun enterFGt(ctx: QuarkPlasmaAssemblyParser.FGtContext) {
    }

    override fun exitFGt(ctx: QuarkPlasmaAssemblyParser.FGtContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, FGt(null, tgt, r1, r2))
    }

    override fun enterFGe(ctx: QuarkPlasmaAssemblyParser.FGeContext) {
    }

    override fun exitFGe(ctx: QuarkPlasmaAssemblyParser.FGeContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, FGe(null, tgt, r1, r2))
    }

    override fun enterFLt(ctx: QuarkPlasmaAssemblyParser.FLtContext) {
    }

    override fun exitFLt(ctx: QuarkPlasmaAssemblyParser.FLtContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, FLt(null, tgt, r1, r2))
    }

    override fun enterFLe(ctx: QuarkPlasmaAssemblyParser.FLeContext) {
    }

    override fun exitFLe(ctx: QuarkPlasmaAssemblyParser.FLeContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, FLe(null, tgt, r1, r2))
    }

    override fun enterSCat(ctx: QuarkPlasmaAssemblyParser.SCatContext) {
    }

    override fun exitSCat(ctx: QuarkPlasmaAssemblyParser.SCatContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, StrCat(null, tgt, r1, r2))
    }

    override fun enterSGt(ctx: QuarkPlasmaAssemblyParser.SGtContext) {
    }

    override fun exitSGt(ctx: QuarkPlasmaAssemblyParser.SGtContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, SGt(null, tgt, r1, r2))
    }

    override fun enterSGe(ctx: QuarkPlasmaAssemblyParser.SGeContext) {
    }

    override fun exitSGe(ctx: QuarkPlasmaAssemblyParser.SGeContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, SGe(null, tgt, r1, r2))
    }

    override fun enterSLt(ctx: QuarkPlasmaAssemblyParser.SLtContext) {
    }

    override fun exitSLt(ctx: QuarkPlasmaAssemblyParser.SLtContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, SLt(null, tgt, r1, r2))
    }

    override fun enterSLe(ctx: QuarkPlasmaAssemblyParser.SLeContext) {
    }

    override fun exitSLe(ctx: QuarkPlasmaAssemblyParser.SLeContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, SLe(null, tgt, r1, r2))
    }

    override fun enterSIdx(ctx: QuarkPlasmaAssemblyParser.SIdxContext) {
    }

    override fun exitSIdx(ctx: QuarkPlasmaAssemblyParser.SIdxContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, SIndex(null, tgt, r1, r2))
    }

    override fun enterAnd(ctx: QuarkPlasmaAssemblyParser.AndContext) {
    }

    override fun exitAnd(ctx: QuarkPlasmaAssemblyParser.AndContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, And(null, tgt, r1, r2))
    }

    override fun enterOr(ctx: QuarkPlasmaAssemblyParser.OrContext) {
    }

    override fun exitOr(ctx: QuarkPlasmaAssemblyParser.OrContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val r1 = getValueFor(ctx.r1) as Reg
        val r2 = getValueFor(ctx.r2) as Reg
        setValueFor(ctx, Or(null, tgt, r1, r2))
    }

    override fun enterINeg(ctx: QuarkPlasmaAssemblyParser.INegContext) {
    }

    override fun exitINeg(ctx: QuarkPlasmaAssemblyParser.INegContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.src) as Reg
        setValueFor(ctx, INeg(null, tgt, src))
    }

    override fun enterNot(ctx: QuarkPlasmaAssemblyParser.NotContext) {
    }

    override fun exitNot(ctx: QuarkPlasmaAssemblyParser.NotContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.src) as Reg
        setValueFor(ctx, Not(null, tgt, src))

    }

    override fun enterFNeg(ctx: QuarkPlasmaAssemblyParser.FNegContext) {
    }

    override fun exitFNeg(ctx: QuarkPlasmaAssemblyParser.FNegContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.src) as Reg
        setValueFor(ctx, FNeg(null, tgt, src))

    }

    override fun enterFInv(ctx: QuarkPlasmaAssemblyParser.FInvContext) {
    }

    override fun exitFInv(ctx: QuarkPlasmaAssemblyParser.FInvContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.src) as Reg
        setValueFor(ctx, FInv(null, tgt, src))
    }

    override fun enterJmp(ctx: QuarkPlasmaAssemblyParser.JmpContext) {
    }

    override fun exitJmp(ctx: QuarkPlasmaAssemblyParser.JmpContext) {
        val loc = getValueFor(ctx.loc()) as CodeLocation
        setValueFor(ctx, Jmp(null, loc))
    }

    override fun enterQgetc(ctx: QuarkPlasmaAssemblyParser.QgetcContext) {
    }

    override fun exitQgetc(ctx: QuarkPlasmaAssemblyParser.QgetcContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.q) as Reg
        val channelName = ctx.ID().text
        val quarkType = getValueFor(ctx.ident()) as Identifier
        setValueFor(ctx, QGetChannel(null, quarkType, tgt, src, channelName))
    }

    override fun enterQgets(ctx: QuarkPlasmaAssemblyParser.QgetsContext) {
    }

    override fun exitQgets(ctx: QuarkPlasmaAssemblyParser.QgetsContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.q) as Reg
        val slot = ctx.ID().text
        val quarkType = getValueFor(ctx.ident()) as Identifier
        setValueFor(ctx, QGetState(null, quarkType, tgt, src, slot))

    }

    override fun enterQsets(ctx: QuarkPlasmaAssemblyParser.QsetsContext) {
    }

    override fun exitQsets(ctx: QuarkPlasmaAssemblyParser.QsetsContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.value) as Reg
        val slot = ctx.ID().text
        val quarkType = getValueFor(ctx.ident()) as Identifier
        setValueFor(ctx, QSetState(null, quarkType, tgt, slot, src))
    }

    override fun enterQnew(ctx: QuarkPlasmaAssemblyParser.QnewContext) {
    }

    override fun exitQnew(ctx: QuarkPlasmaAssemblyParser.QnewContext) {
        val quarkType = getValueFor(ctx.ident()) as Identifier
        val tgt = getValueFor(ctx.reg()) as Reg
        val args = getValueFor(ctx.regList()) as List<Reg>
        setValueFor(ctx, QNew(null, tgt, quarkType, args))
    }

    override fun enterRnew(ctx: QuarkPlasmaAssemblyParser.RnewContext) {
    }

    override fun exitRnew(ctx: QuarkPlasmaAssemblyParser.RnewContext) {
        val tgt = getValueFor(ctx.r) as Reg
        setValueFor(ctx, RNew(null, tgt))
    }

    override fun enterSend(ctx: QuarkPlasmaAssemblyParser.SendContext) {
    }

    override fun exitSend(ctx: QuarkPlasmaAssemblyParser.SendContext) {
        val chan = getValueFor(ctx.ch) as Reg
        val msg = getValueFor(ctx.msg) as Reg
        val cont = getValueFor(ctx.c) as Reg
        setValueFor(ctx, Send(null, chan, msg, cont))
    }

    override fun enterSel(ctx: QuarkPlasmaAssemblyParser.SelContext) {
    }

    override fun exitSel(ctx: QuarkPlasmaAssemblyParser.SelContext) {
        val continuations = getValueFor(ctx.regList()) as List<Reg>
        setValueFor(ctx, Select(null, continuations))
    }

    override fun enterRecv(ctx: QuarkPlasmaAssemblyParser.RecvContext) {
    }

    override fun exitRecv(ctx: QuarkPlasmaAssemblyParser.RecvContext) {
        val tgt = getValueFor(ctx.target) as Reg
        val channel = getValueFor(ctx.ch) as Reg
        val cont = getValueFor(ctx.c) as Reg
        setValueFor(ctx, Receive(null, tgt, channel, cont))
    }

    override fun enterSLen(ctx: QuarkPlasmaAssemblyParser.SLenContext) {
    }

    override fun exitSLen(ctx: QuarkPlasmaAssemblyParser.SLenContext) {
        val tgt = getValueFor(ctx.tgt) as Reg
        val src = getValueFor(ctx.src) as Reg
        setValueFor(ctx, SLen(null, tgt, src))
    }

    override fun enterSpawn(ctx: QuarkPlasmaAssemblyParser.SpawnContext) {
    }

    override fun exitSpawn(ctx: QuarkPlasmaAssemblyParser.SpawnContext) {
        val cont = getValueFor(ctx.regList()) as List<Reg>
        setValueFor(ctx, Spawn(null, cont))
    }

    override fun enterStop(ctx: QuarkPlasmaAssemblyParser.StopContext) {
    }

    override fun exitStop(ctx: QuarkPlasmaAssemblyParser.StopContext) {
        setValueFor(ctx, Stop(null))
    }

    override fun enterIdent(ctx: QuarkPlasmaAssemblyParser.IdentContext) {
    }

    override fun exitIdent(ctx: QuarkPlasmaAssemblyParser.IdentContext) {
        setValueFor(ctx, Identifier.fromList(ctx.ID().map { it.text }, SystemLocation("hadron reader")))
    }
}