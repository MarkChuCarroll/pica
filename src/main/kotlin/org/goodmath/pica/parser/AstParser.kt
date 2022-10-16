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

package org.goodmath.pica.parser

import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeProperty
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.tree.TerminalNode
import org.goodmath.pica.ast.*
import org.goodmath.pica.util.Symbol

class AstParser(val moduleName: String): PicaGrammarListener  {

    private val values: ParseTreeProperty<Any> = ParseTreeProperty()
    val moduleId: Identifier = Identifier.fromString(moduleName, Location.None)
    private var definedModule: HadronDefinition? = null

    fun parse(input: CharStream): HadronDefinition {
        val lexer = PicaGrammarLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = PicaGrammarParser(tokens)
        parser.removeErrorListeners()
        parser.addErrorListener(PicaErrorListener(moduleId))
        val walker = ParseTreeWalker()
        walker.walk(this, parser.hadron())
        return getParsedModule()
    }

    fun getParsedModule(): HadronDefinition = definedModule!!

    private fun setValueFor(node: ParseTree, value: Any) {
        values.put(node, value)
    }

    private fun getValueFor(node: ParseTree): Any {
        return values.get(node)!!
    }

    private fun loc(ctx: ParserRuleContext): Location {
        return Location(moduleName, ctx.start.line, ctx.start.charPositionInLine)
    }

    override fun visitTerminal(node: TerminalNode) {
    }

    override fun visitErrorNode(node: ErrorNode) {
    }

    override fun enterEveryRule(ctx: ParserRuleContext) {
    }

    override fun exitEveryRule(ctx: ParserRuleContext) {
    }

    override fun enterHadron(ctx: PicaGrammarParser.HadronContext) {
    }

    override fun exitHadron(ctx: PicaGrammarParser.HadronContext) {
        val defs = ctx.definition().map { d -> getValueFor(d) as Definition }
        val uses = ctx.useDef().map { d -> getValueFor(d) as UseDef }
        definedModule = HadronDefinition(moduleId, uses, defs)
    }

    override fun enterUseDef(ctx: PicaGrammarParser.UseDefContext) {
    }

    override fun exitUseDef(ctx: PicaGrammarParser.UseDefContext) {
        val module = getValueFor(ctx.ident()) as Identifier
        val names = ctx.ID().map { name -> Symbol.fromString(name.text) }
        setValueFor(ctx, UseDef(module, names, loc(ctx)))
    }

    override fun enterQuarkDefChoice(ctx: PicaGrammarParser.QuarkDefChoiceContext) {
    }

    override fun exitQuarkDefChoice(ctx: PicaGrammarParser.QuarkDefChoiceContext) {
        setValueFor(ctx, getValueFor(ctx.quarkDef()))
    }

    override fun enterFlavorDefChoice(ctx: PicaGrammarParser.FlavorDefChoiceContext) {
    }

    override fun exitFlavorDefChoice(ctx: PicaGrammarParser.FlavorDefChoiceContext) {
        setValueFor(ctx, getValueFor(ctx.flavorDef()))
    }

    override fun enterBosonDefChoice(ctx: PicaGrammarParser.BosonDefChoiceContext) {
    }

    override fun exitBosonDefChoice(ctx: PicaGrammarParser.BosonDefChoiceContext) {
        setValueFor(ctx, getValueFor(ctx.bosonDef()))
    }

    override fun enterFlavorDef(ctx: PicaGrammarParser.FlavorDefContext) {
    }

    override fun exitFlavorDef(ctx: PicaGrammarParser.FlavorDefContext) {
        val name = Symbol.fromString(ctx.ID().text)
        val typeParams = ctx.typeParamBlock()?.let { getValueFor(it) as List<TypeVar> }
        val channels = ctx.channelDef().map { getValueFor(it) as ChannelDef }
        setValueFor(ctx, FlavorDef(moduleId, name, typeParams, channels, loc(ctx)))
    }

    override fun enterQuarkDef(ctx: PicaGrammarParser.QuarkDefContext) {
    }

    override fun exitQuarkDef(ctx: PicaGrammarParser.QuarkDefContext) {
        val name = Symbol.fromString(ctx.ID().text)
        val typeParams = ctx.typeParamBlock()?.let { getValueFor(it) as List<TypeVar> }
        val composes = ctx.composes?.let { getValueFor(it) as List<SType> }.orEmpty()
        val valueArgs = getValueFor(ctx.argSpec()) as List<TypedIdentifier>
        val channels = ctx.channelDef().map { getValueFor(it) as ChannelDef }
        val slots = ctx.slotDef().map { getValueFor(it) as SlotDef }
        val action = getValueFor(ctx.action()) as Action
        setValueFor(ctx, QuarkDefinition(moduleId, name, typeParams, valueArgs,
            composes, channels, slots, action, loc(ctx)))
    }

    override fun enterSlotDef(ctx: PicaGrammarParser.SlotDefContext) {
    }

    override fun exitSlotDef(ctx: PicaGrammarParser.SlotDefContext) {
        val name = Symbol.fromString(ctx.ID().text)
        val type = getValueFor(ctx.type()) as SType
        val initValue = getValueFor(ctx.expr()) as Expr
        setValueFor(ctx, SlotDef(name, type, initValue, loc(ctx)))
    }

    override fun enterChannelDef(ctx: PicaGrammarParser.ChannelDefContext) {
    }

    override fun exitChannelDef(ctx: PicaGrammarParser.ChannelDefContext) {
        val name = Symbol.fromString(ctx.ID().text)
        val type = getValueFor(ctx.type()) as SType
        val dir = getValueFor(ctx.direction()) as ChannelDirection
        val ctype = ChannelType(type, dir, type.loc)
        setValueFor(ctx, ChannelDef(name, ctype, loc(ctx)))
    }

    override fun enterDirIn(ctx: PicaGrammarParser.DirInContext) {
    }

    override fun exitDirIn(ctx: PicaGrammarParser.DirInContext) {
        setValueFor(ctx, ChannelDirection.In)
    }

    override fun enterDirOut(ctx: PicaGrammarParser.DirOutContext) {
    }

    override fun exitDirOut(ctx: PicaGrammarParser.DirOutContext) {
        setValueFor(ctx, ChannelDirection.Out)
    }

    override fun enterDirBoth(ctx: PicaGrammarParser.DirBothContext) {
    }

    override fun exitDirBoth(ctx: PicaGrammarParser.DirBothContext) {
        setValueFor(ctx, ChannelDirection.Both)
    }

    override fun enterDirNone(ctx: PicaGrammarParser.DirNoneContext) {

    }

    override fun exitDirNone(ctx: PicaGrammarParser.DirNoneContext) {
        setValueFor(ctx, ChannelDirection.Both)
    }

    override fun enterIdList(ctx: PicaGrammarParser.IdListContext) {
    }

    override fun exitIdList(ctx: PicaGrammarParser.IdListContext) {
        val ids = ctx.ID().map { Symbol.fromString(it.text) }
        setValueFor(ctx, ids)
    }

    override fun enterTypeParamBlock(ctx: PicaGrammarParser.TypeParamBlockContext) {
    }

    override fun exitTypeParamBlock(ctx: PicaGrammarParser.TypeParamBlockContext) {
        val tps = ctx.typeParamSpec().map { getValueFor(it) as TypeVar }
        setValueFor(ctx, tps)
    }

    override fun enterTypeParamSpec(ctx: PicaGrammarParser.TypeParamSpecContext) {
    }

    override fun exitTypeParamSpec(ctx: PicaGrammarParser.TypeParamSpecContext) {
        val name = Symbol.fromString(ctx.ID().text)
        val constraints = ctx.typeList()?.let {
            getValueFor(it) as List<SType> }
        setValueFor(ctx, TypeVar(name, constraints, loc(ctx)))
    }

    override fun enterTypeList(ctx: PicaGrammarParser.TypeListContext) {
    }

    override fun exitTypeList(ctx: PicaGrammarParser.TypeListContext) {
        val types = ctx.type().map { getValueFor(it) as SType }
        setValueFor(ctx, types)
    }

    override fun enterArgSpec(ctx: PicaGrammarParser.ArgSpecContext) {
    }

    override fun exitArgSpec(ctx: PicaGrammarParser.ArgSpecContext) {
        val args = ctx.arg()?.let { argList ->  argList.map { arg -> getValueFor(arg) as TypedIdentifier }}.orEmpty()
        setValueFor(ctx, args)
    }

    override fun enterArg(ctx: PicaGrammarParser.ArgContext) {
    }

    override fun exitArg(ctx: PicaGrammarParser.ArgContext) {
        val name = Symbol.fromString(ctx.ID().text)
        val type = getValueFor(ctx.type()) as SType
        setValueFor(ctx, TypedIdentifier(name, type, loc(ctx)))
    }

    override fun enterChannelType(ctx: PicaGrammarParser.ChannelTypeContext) {
    }

    override fun exitChannelType(ctx: PicaGrammarParser.ChannelTypeContext) {
        val type = getValueFor(ctx.type()) as SType
        val dir = getValueFor(ctx.direction()) as ChannelDirection
        setValueFor(ctx, ChannelType(type, dir, loc(ctx)))
    }

    override fun enterNamedType(ctx: PicaGrammarParser.NamedTypeContext) {
    }

    override fun exitNamedType(ctx: PicaGrammarParser.NamedTypeContext) {
        val name = getValueFor(ctx.ident()) as Identifier
        val typeArgs = ctx.typeArgBlock()?.let { getValueFor(it) as List<SType> }
        setValueFor(ctx, NamedType(name, typeArgs, loc(ctx)))
    }

    override fun enterTypeArgBlock(ctx: PicaGrammarParser.TypeArgBlockContext) {

    }

    override fun exitTypeArgBlock(ctx: PicaGrammarParser.TypeArgBlockContext) {
        val tab = ctx.type().map { getValueFor(it) as SType }
        setValueFor(ctx, tab)
    }

    override fun enterBosonDef(ctx: PicaGrammarParser.BosonDefContext) {
    }

    override fun exitBosonDef(ctx: PicaGrammarParser.BosonDefContext) {
        val name = Symbol.fromString(ctx.ID().text)
        val typeParams = ctx.typeParamBlock()?.let {
            getValueFor(it) as List<TypeVar>
        }
        val opts = getValueFor(ctx.bosonBody()) as List<BosonOption>
        setValueFor(ctx, BosonDefinition(moduleId, name, typeParams, opts, loc(ctx)))
    }

    override fun enterBosonBody(ctx: PicaGrammarParser.BosonBodyContext) {
    }

    override fun exitBosonBody(ctx: PicaGrammarParser.BosonBodyContext) {
        setValueFor(ctx, ctx.bosonOption().map { getValueFor(it) as BosonOption})
    }

    override fun enterTupleBosonOption(ctx: PicaGrammarParser.TupleBosonOptionContext) {
    }

    override fun exitTupleBosonOption(ctx: PicaGrammarParser.TupleBosonOptionContext) {
        val name = Symbol.fromString(ctx.ID().text)
        val fields = getValueFor(ctx.typeList()) as List<SType>
        setValueFor(ctx, BosonTupleOption(name, fields, loc(ctx)))
    }

    override fun enterStructBosonOption(ctx: PicaGrammarParser.StructBosonOptionContext) {
    }

    override fun exitStructBosonOption(ctx: PicaGrammarParser.StructBosonOptionContext) {
        val name = Symbol.fromString(ctx.ID().text)
        val fields = getValueFor(ctx.typedIdList()) as List<TypedIdentifier>
        setValueFor(ctx, BosonStructOption(name, fields, loc(ctx)))
    }

    override fun enterTypedIdList(ctx: PicaGrammarParser.TypedIdListContext) {
    }

    override fun exitTypedIdList(ctx: PicaGrammarParser.TypedIdListContext) {
        val ids = ctx.typedId().map { getValueFor(it) as TypedIdentifier }
        setValueFor(ctx, ids)
    }

    override fun enterTypedId(ctx: PicaGrammarParser.TypedIdContext) {
    }

    override fun exitTypedId(ctx: PicaGrammarParser.TypedIdContext) {
        val name = Symbol.fromString(ctx.ID().text)
        val type = getValueFor(ctx.type()) as SType
        setValueFor(ctx, TypedIdentifier(name, type, loc(ctx)))
    }

    override fun enterLoopAction(ctx: PicaGrammarParser.LoopActionContext) {
    }

    override fun exitLoopAction(ctx: PicaGrammarParser.LoopActionContext) {
        val act = getValueFor(ctx.action()) as Action
        setValueFor(ctx, act)
    }

    override fun enterExitAction(ctx: PicaGrammarParser.ExitActionContext) {
    }

    override fun exitExitAction(ctx: PicaGrammarParser.ExitActionContext) {
        setValueFor(ctx, ExitAction(loc(ctx)))
    }

    override fun enterReceiveAction(ctx: PicaGrammarParser.ReceiveActionContext) {
    }

    override fun exitReceiveAction(ctx: PicaGrammarParser.ReceiveActionContext) {
        val channel = getValueFor(ctx.chan) as Expr
        val opts = ctx.onClause().map { getValueFor(it) as ReceiveOption }
        setValueFor(ctx, ReceiveAction(channel, opts, loc(ctx)))
    }

    override fun enterSpawnAction(ctx: PicaGrammarParser.SpawnActionContext) {
    }

    override fun exitSpawnAction(ctx: PicaGrammarParser.SpawnActionContext) {
        setValueFor(ctx, SpawnAction(getValueFor(ctx.action()) as Action, loc(ctx)))
    }

    override fun enterParAction(ctx: PicaGrammarParser.ParActionContext) {
    }

    override fun exitParAction(ctx: PicaGrammarParser.ParActionContext) {
        val pars = ctx.action().map { getValueFor(it) as Action }
        setValueFor(ctx, ParAction(pars, loc(ctx)))
    }

    override fun enterAssignAction(ctx: PicaGrammarParser.AssignActionContext) {
    }

    override fun exitAssignAction(ctx: PicaGrammarParser.AssignActionContext) {
        val lval = getValueFor(ctx.lvalue()) as LValExpr
        val value = getValueFor(ctx.expr()) as Expr
        setValueFor(ctx, AssignmentAction(lval, value, loc(ctx)))
    }

    override fun enterParenAction(ctx: PicaGrammarParser.ParenActionContext) {
    }

    override fun exitParenAction(ctx: PicaGrammarParser.ParenActionContext) {
        setValueFor(ctx, getValueFor(ctx.action()))
    }

    override fun enterChoiceAction(ctx: PicaGrammarParser.ChoiceActionContext) {
    }

    override fun exitChoiceAction(ctx: PicaGrammarParser.ChoiceActionContext) {
        val choices = ctx.action().map { getValueFor(it) as Action }
        setValueFor(ctx, ChoiceAction(choices, loc(ctx)))
    }

    override fun enterSequenceAction(ctx: PicaGrammarParser.SequenceActionContext) {
    }

    override fun exitSequenceAction(ctx: PicaGrammarParser.SequenceActionContext) {
        val actions = ctx.action().map { getValueFor(it) as Action }
        setValueFor(ctx, SequenceAction(actions, loc(ctx)))
    }

    override fun enterIfAction(ctx: PicaGrammarParser.IfActionContext) {
    }

    override fun exitIfAction(ctx: PicaGrammarParser.IfActionContext) {
        val cond = getValueFor(ctx.cond) as Expr
        val trueSide = getValueFor(ctx.t) as Action
        val falseSide = getValueFor(ctx.f) as Action
        setValueFor(ctx, IfAction(cond, trueSide, falseSide, loc(ctx)))
    }

    override fun enterWhileAction(ctx: PicaGrammarParser.WhileActionContext) {
    }

    override fun exitWhileAction(ctx: PicaGrammarParser.WhileActionContext) {
        val cond = getValueFor(ctx.expr()) as Expr
        val act = getValueFor(ctx.action()) as Action
        setValueFor(ctx, WhileAction(cond, act, loc(ctx)))
    }

    override fun enterSendAction(ctx: PicaGrammarParser.SendActionContext) {
    }

    override fun exitSendAction(ctx: PicaGrammarParser.SendActionContext) {
        val chan = getValueFor(ctx.chan) as Expr
        val msg = getValueFor(ctx.value) as Expr
        setValueFor(ctx, SendAction(chan, msg, loc(ctx)))
    }

    override fun enterVardefStmt(ctx: PicaGrammarParser.VardefStmtContext) {
    }

    override fun exitVardefStmt(ctx: PicaGrammarParser.VardefStmtContext) {
        val name = Symbol.fromString(ctx.ID().text)
        val type = getValueFor(ctx.type()) as SType
        val initValue = getValueFor(ctx.expr()) as Expr
        setValueFor(ctx, VarDefAction(name, type, initValue, loc(ctx)))
    }

    override fun enterForAction(ctx: PicaGrammarParser.ForActionContext) {
    }

    override fun exitForAction(ctx: PicaGrammarParser.ForActionContext) {
        val idx = Symbol.fromString(ctx.ID().text)
        val coll = getValueFor(ctx.expr()) as Expr
        val act = getValueFor(ctx.action()) as Action
        setValueFor(ctx, ForAction(idx, coll, act, loc(ctx)))
    }

    override fun enterOnClause(ctx: PicaGrammarParser.OnClauseContext) {
    }

    override fun exitOnClause(ctx: PicaGrammarParser.OnClauseContext) {
        val pat = getValueFor(ctx.pattern()) as BosonPattern
        val act = getValueFor(ctx.action()) as Action
        setValueFor(ctx, ReceiveOption(pat, act, loc(ctx)))
    }

    override fun enterPattern(ctx: PicaGrammarParser.PatternContext) {
    }

    override fun exitPattern(ctx: PicaGrammarParser.PatternContext) {
        val optionName = Symbol.fromString(ctx.ID().text)
        if (ctx.tuplePattern() != null) {
            val fields = getValueFor(ctx.tuplePattern()) as List<Symbol>
            setValueFor(ctx, BosonTuplePattern(optionName, fields, loc(ctx)))
        } else {
            val fields = getValueFor(ctx.structPattern()) as List<BosonPatternBinding>
            setValueFor(ctx, BosonStructPattern(optionName, fields, loc(ctx)))
        }
    }

    override fun enterTuplePattern(ctx: PicaGrammarParser.TuplePatternContext) {
    }

    override fun exitTuplePattern(ctx: PicaGrammarParser.TuplePatternContext) {
        setValueFor(ctx, getValueFor(ctx.idList()))
    }

    override fun enterStructPattern(ctx: PicaGrammarParser.StructPatternContext) {
    }

    override fun exitStructPattern(ctx: PicaGrammarParser.StructPatternContext) {
        setValueFor(ctx, ctx.keyedPattern().map { getValueFor(ctx) })
    }

    override fun enterKeyedPattern(ctx: PicaGrammarParser.KeyedPatternContext) {
    }

    override fun exitKeyedPattern(ctx: PicaGrammarParser.KeyedPatternContext) {
        val structField = Symbol.fromString(ctx.k.text)
        val boundVar = Symbol.fromString(ctx.v.text)
        setValueFor(ctx, BosonPatternBinding(structField, boundVar, loc(ctx)))
    }

    override fun enterDotLvalue(ctx: PicaGrammarParser.DotLvalueContext) {
    }

    override fun exitDotLvalue(ctx: PicaGrammarParser.DotLvalueContext) {
        val base = getValueFor(ctx.lvalue()) as LValExpr
        if (ctx.ID() != null) {
            val field = Symbol.fromString(ctx.ID().text)
            setValueFor(ctx, FieldLValueExpr(base, field, loc(ctx)))
        } else {
            val idx = Integer.parseInt(ctx.LIT_INT().text)
            setValueFor(ctx, IndexedLValueExpr(base, idx, loc(ctx)))
        }
    }

    override fun enterSimpleLvalue(ctx: PicaGrammarParser.SimpleLvalueContext) {
    }

    override fun exitSimpleLvalue(ctx: PicaGrammarParser.SimpleLvalueContext) {
        val id = getValueFor(ctx.ident()) as Identifier
        setValueFor(ctx, IdLValueExpr(id, loc(ctx)))
    }

    override fun enterNarrowChanToInExpr(ctx: PicaGrammarParser.NarrowChanToInExprContext) {
    }

    override fun exitNarrowChanToInExpr(ctx: PicaGrammarParser.NarrowChanToInExprContext) {
        val chanExpr = getValueFor(ctx.expr()) as Expr
        setValueFor(ctx, NarrowChannelToInExpr(chanExpr, loc(ctx)))
    }

    override fun enterLitFloatExpr(ctx: PicaGrammarParser.LitFloatExprContext) {
    }

    override fun exitLitFloatExpr(ctx: PicaGrammarParser.LitFloatExprContext) {
        val flt = ctx.LIT_FLOAT().text.toFloat()
        setValueFor(ctx, LiteralExpr(LiteralKind.FloatLit, flt, loc(ctx)))
    }

    override fun enterLitCharExpr(ctx: PicaGrammarParser.LitCharExprContext) {
    }

    override fun exitLitCharExpr(ctx: PicaGrammarParser.LitCharExprContext) {
        val chr = ctx.LIT_CHAR().text
        setValueFor(ctx, LiteralExpr(LiteralKind.CharLit, chr, loc(ctx)))

    }

    override fun enterBosonTupleExpr(ctx: PicaGrammarParser.BosonTupleExprContext) {
    }

    override fun exitBosonTupleExpr(ctx: PicaGrammarParser.BosonTupleExprContext) {
        val optName = getValueFor(ctx.ident()) as Identifier
        val fields = getValueFor(ctx.exprList()) as List<Expr>
        setValueFor(ctx, BosonTupleExpr(optName, fields, loc(ctx)))
    }

    override fun enterLvalueExpr(ctx: PicaGrammarParser.LvalueExprContext) {
    }

    override fun exitLvalueExpr(ctx: PicaGrammarParser.LvalueExprContext) {
        setValueFor(ctx, getValueFor(ctx.lvalue()))
    }

    override fun enterMultExpr(ctx: PicaGrammarParser.MultExprContext) {
    }

    override fun exitMultExpr(ctx: PicaGrammarParser.MultExprContext) {
        val left = getValueFor(ctx.l) as Expr
        val right = getValueFor(ctx.r) as Expr
        val op = Operator.fromString(ctx.op.text)
        setValueFor(ctx, OperatorExpr(op, listOf(left, right), loc(ctx)))
    }

    override fun enterParenExpr(ctx: PicaGrammarParser.ParenExprContext) {
    }

    override fun exitParenExpr(ctx: PicaGrammarParser.ParenExprContext) {
        setValueFor(ctx, getValueFor(ctx.expr()))
    }

    override fun enterNegateExpr(ctx: PicaGrammarParser.NegateExprContext) {
    }

    override fun exitNegateExpr(ctx: PicaGrammarParser.NegateExprContext) {
        val right = getValueFor(ctx.expr()) as Expr
        val op = Operator.fromString(ctx.op.text)
        setValueFor(ctx, OperatorExpr(op, listOf(right), loc(ctx)))

    }

    override fun enterAddExpr(ctx: PicaGrammarParser.AddExprContext) {
        }

    override fun exitAddExpr(ctx: PicaGrammarParser.AddExprContext) {
        val left = getValueFor(ctx.l) as Expr
        val right = getValueFor(ctx.r) as Expr
        val op = Operator.fromString(ctx.op.text)
        setValueFor(ctx, OperatorExpr(op, listOf(left, right), loc(ctx)))
    }

    override fun enterNarrowChanToOutExpr(ctx: PicaGrammarParser.NarrowChanToOutExprContext) {
    }

    override fun exitNarrowChanToOutExpr(ctx: PicaGrammarParser.NarrowChanToOutExprContext) {
        val chanExpr = getValueFor(ctx.expr()) as Expr
        setValueFor(ctx, NarrowChannelToOutExpr(chanExpr, loc(ctx)))
    }

    override fun enterLogicExpr(ctx: PicaGrammarParser.LogicExprContext) {
    }

    override fun exitLogicExpr(ctx: PicaGrammarParser.LogicExprContext) {
        val left = getValueFor(ctx.l) as Expr
        val right = getValueFor(ctx.r) as Expr
        val op = Operator.fromString(ctx.op.text)
        setValueFor(ctx, OperatorExpr(op, listOf(left, right), loc(ctx)))
    }

    override fun enterLitIntExpr(ctx: PicaGrammarParser.LitIntExprContext) {
    }

    override fun exitLitIntExpr(ctx: PicaGrammarParser.LitIntExprContext) {
        val i = Integer.parseInt(ctx.LIT_INT().text)
        setValueFor(ctx, LiteralExpr(LiteralKind.IntLit, i, loc(ctx)))
    }

    override fun enterBosonStructExpr(ctx: PicaGrammarParser.BosonStructExprContext) {
    }

    override fun exitBosonStructExpr(ctx: PicaGrammarParser.BosonStructExprContext) {
        val name = getValueFor(ctx.ident()) as Identifier
        val fields = getValueFor(ctx.keyValueList()) as List<FieldValue>
        setValueFor(ctx, BosonStructExpr(name, fields, loc(ctx)))
    }

    override fun enterLitStrExpr(ctx: PicaGrammarParser.LitStrExprContext) {
    }

    override fun exitLitStrExpr(ctx: PicaGrammarParser.LitStrExprContext) {
        val str = ctx.LIT_STRING().text
        setValueFor(ctx, LiteralExpr(LiteralKind.StrLit, str, loc(ctx)))
    }

    override fun enterRunExpr(ctx: PicaGrammarParser.RunExprContext) {
    }

    override fun exitRunExpr(ctx: PicaGrammarParser.RunExprContext) {
        val qt = getValueFor(ctx.type()) as SType
        val args = getValueFor(ctx.exprList()) as List<Expr>
        setValueFor(ctx, CreateQuarkExpr(qt, args, loc(ctx)))
    }

    override fun enterNewChanExpr(ctx: PicaGrammarParser.NewChanExprContext) {
    }

    override fun exitNewChanExpr(ctx: PicaGrammarParser.NewChanExprContext) {
        val type = getValueFor(ctx.type()) as SType
        setValueFor(ctx, NewChannelExpr(type, loc(ctx)))
    }

    override fun enterListExpr(ctx: PicaGrammarParser.ListExprContext) {
    }

    override fun exitListExpr(ctx: PicaGrammarParser.ListExprContext) {
        val type = getValueFor(ctx.type()) as SType
        val values = getValueFor(ctx.exprList()) as List<Expr>
        setValueFor(ctx, ListExpr(type, values, loc(ctx)))
    }

    override fun enterCompareExpr(ctx: PicaGrammarParser.CompareExprContext) {
    }

    override fun exitCompareExpr(ctx: PicaGrammarParser.CompareExprContext) {
        val left = getValueFor(ctx.l) as Expr
        val right = getValueFor(ctx.r) as Expr
        val op = Operator.fromString(ctx.op.text)
        setValueFor(ctx, OperatorExpr(op, listOf(left, right), loc(ctx)))
    }

    override fun enterKeyValueList(ctx: PicaGrammarParser.KeyValueListContext) {
    }

    override fun exitKeyValueList(ctx: PicaGrammarParser.KeyValueListContext) {
        val pairs = ctx.keyValuePair().map { getValueFor(it) as FieldValue }
        setValueFor(ctx, pairs)
    }

    override fun enterKeyValuePair(ctx: PicaGrammarParser.KeyValuePairContext) {
    }

    override fun exitKeyValuePair(ctx: PicaGrammarParser.KeyValuePairContext) {
        val key = Symbol.fromString(ctx.ID().text)
        val value = getValueFor(ctx.expr()) as Expr
        setValueFor(ctx, FieldValue(key, value, loc(ctx)))
    }

    override fun enterExprList(ctx: PicaGrammarParser.ExprListContext) {
    }

    override fun exitExprList(ctx: PicaGrammarParser.ExprListContext) {
        setValueFor(ctx,
            ctx.expr().map { getValueFor(it) as Expr }
        )
    }

    override fun enterIdent(ctx: PicaGrammarParser.IdentContext) {
    }

    override fun exitIdent(ctx: PicaGrammarParser.IdentContext) {
        setValueFor(ctx, Identifier.fromList(ctx.ID().map { it.text }, loc(ctx)))
    }
}