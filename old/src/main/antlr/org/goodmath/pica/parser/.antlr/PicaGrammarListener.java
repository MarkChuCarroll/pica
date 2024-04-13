// Generated from /Users/mark.chu-carroll/Hack/pica/jsrc/main/antlr/org/goodmath/pica/parser/PicaGrammar.g4 by ANTLR 4.13.1
 package org.goodmath.pica.parser; 
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PicaGrammarParser}.
 */
public interface PicaGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#hadron}.
	 * @param ctx the parse tree
	 */
	void enterHadron(PicaGrammarParser.HadronContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#hadron}.
	 * @param ctx the parse tree
	 */
	void exitHadron(PicaGrammarParser.HadronContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#useDef}.
	 * @param ctx the parse tree
	 */
	void enterUseDef(PicaGrammarParser.UseDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#useDef}.
	 * @param ctx the parse tree
	 */
	void exitUseDef(PicaGrammarParser.UseDefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code quarkDefChoice}
	 * labeled alternative in {@link PicaGrammarParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterQuarkDefChoice(PicaGrammarParser.QuarkDefChoiceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code quarkDefChoice}
	 * labeled alternative in {@link PicaGrammarParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitQuarkDefChoice(PicaGrammarParser.QuarkDefChoiceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code flavorDefChoice}
	 * labeled alternative in {@link PicaGrammarParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterFlavorDefChoice(PicaGrammarParser.FlavorDefChoiceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code flavorDefChoice}
	 * labeled alternative in {@link PicaGrammarParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitFlavorDefChoice(PicaGrammarParser.FlavorDefChoiceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bosonDefChoice}
	 * labeled alternative in {@link PicaGrammarParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterBosonDefChoice(PicaGrammarParser.BosonDefChoiceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bosonDefChoice}
	 * labeled alternative in {@link PicaGrammarParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitBosonDefChoice(PicaGrammarParser.BosonDefChoiceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#flavorDef}.
	 * @param ctx the parse tree
	 */
	void enterFlavorDef(PicaGrammarParser.FlavorDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#flavorDef}.
	 * @param ctx the parse tree
	 */
	void exitFlavorDef(PicaGrammarParser.FlavorDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#channelDef}.
	 * @param ctx the parse tree
	 */
	void enterChannelDef(PicaGrammarParser.ChannelDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#channelDef}.
	 * @param ctx the parse tree
	 */
	void exitChannelDef(PicaGrammarParser.ChannelDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#quarkDef}.
	 * @param ctx the parse tree
	 */
	void enterQuarkDef(PicaGrammarParser.QuarkDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#quarkDef}.
	 * @param ctx the parse tree
	 */
	void exitQuarkDef(PicaGrammarParser.QuarkDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#behaviorDef}.
	 * @param ctx the parse tree
	 */
	void enterBehaviorDef(PicaGrammarParser.BehaviorDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#behaviorDef}.
	 * @param ctx the parse tree
	 */
	void exitBehaviorDef(PicaGrammarParser.BehaviorDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#slotDef}.
	 * @param ctx the parse tree
	 */
	void enterSlotDef(PicaGrammarParser.SlotDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#slotDef}.
	 * @param ctx the parse tree
	 */
	void exitSlotDef(PicaGrammarParser.SlotDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#idList}.
	 * @param ctx the parse tree
	 */
	void enterIdList(PicaGrammarParser.IdListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#idList}.
	 * @param ctx the parse tree
	 */
	void exitIdList(PicaGrammarParser.IdListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#typeParamBlock}.
	 * @param ctx the parse tree
	 */
	void enterTypeParamBlock(PicaGrammarParser.TypeParamBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#typeParamBlock}.
	 * @param ctx the parse tree
	 */
	void exitTypeParamBlock(PicaGrammarParser.TypeParamBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#typeParamSpec}.
	 * @param ctx the parse tree
	 */
	void enterTypeParamSpec(PicaGrammarParser.TypeParamSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#typeParamSpec}.
	 * @param ctx the parse tree
	 */
	void exitTypeParamSpec(PicaGrammarParser.TypeParamSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#typeList}.
	 * @param ctx the parse tree
	 */
	void enterTypeList(PicaGrammarParser.TypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#typeList}.
	 * @param ctx the parse tree
	 */
	void exitTypeList(PicaGrammarParser.TypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#argSpec}.
	 * @param ctx the parse tree
	 */
	void enterArgSpec(PicaGrammarParser.ArgSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#argSpec}.
	 * @param ctx the parse tree
	 */
	void exitArgSpec(PicaGrammarParser.ArgSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(PicaGrammarParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(PicaGrammarParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(PicaGrammarParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(PicaGrammarParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#namedType}.
	 * @param ctx the parse tree
	 */
	void enterNamedType(PicaGrammarParser.NamedTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#namedType}.
	 * @param ctx the parse tree
	 */
	void exitNamedType(PicaGrammarParser.NamedTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#channelType}.
	 * @param ctx the parse tree
	 */
	void enterChannelType(PicaGrammarParser.ChannelTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#channelType}.
	 * @param ctx the parse tree
	 */
	void exitChannelType(PicaGrammarParser.ChannelTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#dir}.
	 * @param ctx the parse tree
	 */
	void enterDir(PicaGrammarParser.DirContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#dir}.
	 * @param ctx the parse tree
	 */
	void exitDir(PicaGrammarParser.DirContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#typeArgBlock}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgBlock(PicaGrammarParser.TypeArgBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#typeArgBlock}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgBlock(PicaGrammarParser.TypeArgBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#bosonDef}.
	 * @param ctx the parse tree
	 */
	void enterBosonDef(PicaGrammarParser.BosonDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#bosonDef}.
	 * @param ctx the parse tree
	 */
	void exitBosonDef(PicaGrammarParser.BosonDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#bosonBody}.
	 * @param ctx the parse tree
	 */
	void enterBosonBody(PicaGrammarParser.BosonBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#bosonBody}.
	 * @param ctx the parse tree
	 */
	void exitBosonBody(PicaGrammarParser.BosonBodyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tupleBosonOption}
	 * labeled alternative in {@link PicaGrammarParser#bosonOption}.
	 * @param ctx the parse tree
	 */
	void enterTupleBosonOption(PicaGrammarParser.TupleBosonOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tupleBosonOption}
	 * labeled alternative in {@link PicaGrammarParser#bosonOption}.
	 * @param ctx the parse tree
	 */
	void exitTupleBosonOption(PicaGrammarParser.TupleBosonOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code structBosonOption}
	 * labeled alternative in {@link PicaGrammarParser#bosonOption}.
	 * @param ctx the parse tree
	 */
	void enterStructBosonOption(PicaGrammarParser.StructBosonOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code structBosonOption}
	 * labeled alternative in {@link PicaGrammarParser#bosonOption}.
	 * @param ctx the parse tree
	 */
	void exitStructBosonOption(PicaGrammarParser.StructBosonOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#typedIdList}.
	 * @param ctx the parse tree
	 */
	void enterTypedIdList(PicaGrammarParser.TypedIdListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#typedIdList}.
	 * @param ctx the parse tree
	 */
	void exitTypedIdList(PicaGrammarParser.TypedIdListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#typedId}.
	 * @param ctx the parse tree
	 */
	void enterTypedId(PicaGrammarParser.TypedIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#typedId}.
	 * @param ctx the parse tree
	 */
	void exitTypedId(PicaGrammarParser.TypedIdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void enterParAction(PicaGrammarParser.ParActionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void exitParAction(PicaGrammarParser.ParActionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code seqAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void enterSeqAction(PicaGrammarParser.SeqActionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code seqAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void exitSeqAction(PicaGrammarParser.SeqActionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code optAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void enterOptAction(PicaGrammarParser.OptActionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code optAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void exitOptAction(PicaGrammarParser.OptActionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sendAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void enterSendAction(PicaGrammarParser.SendActionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sendAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void exitSendAction(PicaGrammarParser.SendActionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code receiveAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void enterReceiveAction(PicaGrammarParser.ReceiveActionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code receiveAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void exitReceiveAction(PicaGrammarParser.ReceiveActionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void enterAssignAction(PicaGrammarParser.AssignActionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void exitAssignAction(PicaGrammarParser.AssignActionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code vardefAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void enterVardefAction(PicaGrammarParser.VardefActionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code vardefAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void exitVardefAction(PicaGrammarParser.VardefActionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code condAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void enterCondAction(PicaGrammarParser.CondActionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code condAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void exitCondAction(PicaGrammarParser.CondActionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void enterWhileAction(PicaGrammarParser.WhileActionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void exitWhileAction(PicaGrammarParser.WhileActionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void enterForAction(PicaGrammarParser.ForActionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void exitForAction(PicaGrammarParser.ForActionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code adoptBehaviorAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void enterAdoptBehaviorAction(PicaGrammarParser.AdoptBehaviorActionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code adoptBehaviorAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void exitAdoptBehaviorAction(PicaGrammarParser.AdoptBehaviorActionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exitAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void enterExitAction(PicaGrammarParser.ExitActionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exitAction}
	 * labeled alternative in {@link PicaGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void exitExitAction(PicaGrammarParser.ExitActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#receiveClause}.
	 * @param ctx the parse tree
	 */
	void enterReceiveClause(PicaGrammarParser.ReceiveClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#receiveClause}.
	 * @param ctx the parse tree
	 */
	void exitReceiveClause(PicaGrammarParser.ReceiveClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCond(PicaGrammarParser.CondContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCond(PicaGrammarParser.CondContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#condClause}.
	 * @param ctx the parse tree
	 */
	void enterCondClause(PicaGrammarParser.CondClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#condClause}.
	 * @param ctx the parse tree
	 */
	void exitCondClause(PicaGrammarParser.CondClauseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code litFloatExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLitFloatExpr(PicaGrammarParser.LitFloatExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code litFloatExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLitFloatExpr(PicaGrammarParser.LitFloatExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code litCharExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLitCharExpr(PicaGrammarParser.LitCharExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code litCharExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLitCharExpr(PicaGrammarParser.LitCharExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bosonTupleExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBosonTupleExpr(PicaGrammarParser.BosonTupleExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bosonTupleExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBosonTupleExpr(PicaGrammarParser.BosonTupleExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code condExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCondExpr(PicaGrammarParser.CondExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code condExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCondExpr(PicaGrammarParser.CondExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lvalueExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLvalueExpr(PicaGrammarParser.LvalueExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lvalueExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLvalueExpr(PicaGrammarParser.LvalueExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code multExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMultExpr(PicaGrammarParser.MultExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code multExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMultExpr(PicaGrammarParser.MultExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParenExpr(PicaGrammarParser.ParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParenExpr(PicaGrammarParser.ParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code negateExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNegateExpr(PicaGrammarParser.NegateExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code negateExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNegateExpr(PicaGrammarParser.NegateExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddExpr(PicaGrammarParser.AddExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddExpr(PicaGrammarParser.AddExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLogicExpr(PicaGrammarParser.LogicExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLogicExpr(PicaGrammarParser.LogicExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code litIntExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLitIntExpr(PicaGrammarParser.LitIntExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code litIntExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLitIntExpr(PicaGrammarParser.LitIntExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bosonStructExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBosonStructExpr(PicaGrammarParser.BosonStructExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bosonStructExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBosonStructExpr(PicaGrammarParser.BosonStructExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code litStrExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLitStrExpr(PicaGrammarParser.LitStrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code litStrExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLitStrExpr(PicaGrammarParser.LitStrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code runExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRunExpr(PicaGrammarParser.RunExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code runExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRunExpr(PicaGrammarParser.RunExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCallExpr(PicaGrammarParser.CallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCallExpr(PicaGrammarParser.CallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code listExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterListExpr(PicaGrammarParser.ListExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code listExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitListExpr(PicaGrammarParser.ListExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code compareExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCompareExpr(PicaGrammarParser.CompareExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code compareExpr}
	 * labeled alternative in {@link PicaGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCompareExpr(PicaGrammarParser.CompareExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPattern(PicaGrammarParser.PatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPattern(PicaGrammarParser.PatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#tuplePattern}.
	 * @param ctx the parse tree
	 */
	void enterTuplePattern(PicaGrammarParser.TuplePatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#tuplePattern}.
	 * @param ctx the parse tree
	 */
	void exitTuplePattern(PicaGrammarParser.TuplePatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#structPattern}.
	 * @param ctx the parse tree
	 */
	void enterStructPattern(PicaGrammarParser.StructPatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#structPattern}.
	 * @param ctx the parse tree
	 */
	void exitStructPattern(PicaGrammarParser.StructPatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#keyedPattern}.
	 * @param ctx the parse tree
	 */
	void enterKeyedPattern(PicaGrammarParser.KeyedPatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#keyedPattern}.
	 * @param ctx the parse tree
	 */
	void exitKeyedPattern(PicaGrammarParser.KeyedPatternContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dotLvalue}
	 * labeled alternative in {@link PicaGrammarParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterDotLvalue(PicaGrammarParser.DotLvalueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dotLvalue}
	 * labeled alternative in {@link PicaGrammarParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitDotLvalue(PicaGrammarParser.DotLvalueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleLvalue}
	 * labeled alternative in {@link PicaGrammarParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterSimpleLvalue(PicaGrammarParser.SimpleLvalueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleLvalue}
	 * labeled alternative in {@link PicaGrammarParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitSimpleLvalue(PicaGrammarParser.SimpleLvalueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#keyValueList}.
	 * @param ctx the parse tree
	 */
	void enterKeyValueList(PicaGrammarParser.KeyValueListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#keyValueList}.
	 * @param ctx the parse tree
	 */
	void exitKeyValueList(PicaGrammarParser.KeyValueListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#keyValuePair}.
	 * @param ctx the parse tree
	 */
	void enterKeyValuePair(PicaGrammarParser.KeyValuePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#keyValuePair}.
	 * @param ctx the parse tree
	 */
	void exitKeyValuePair(PicaGrammarParser.KeyValuePairContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#exprList}.
	 * @param ctx the parse tree
	 */
	void enterExprList(PicaGrammarParser.ExprListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#exprList}.
	 * @param ctx the parse tree
	 */
	void exitExprList(PicaGrammarParser.ExprListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PicaGrammarParser#ident}.
	 * @param ctx the parse tree
	 */
	void enterIdent(PicaGrammarParser.IdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PicaGrammarParser#ident}.
	 * @param ctx the parse tree
	 */
	void exitIdent(PicaGrammarParser.IdentContext ctx);
}