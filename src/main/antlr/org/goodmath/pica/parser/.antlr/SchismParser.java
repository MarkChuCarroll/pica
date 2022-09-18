// Generated from /Users/markc/Code/schism/src/main/antlr/org/goodmath/schism/parser/Schism.g4 by ANTLR 4.9.2
 package org.goodmath.pica.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SchismParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9,
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17,
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24,
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31,
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38,
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, T__44=45,
		T__45=46, T__46=47, T__47=48, T__48=49, T__49=50, T__50=51, T__51=52,
		T__52=53, T__53=54, ID=55, LIT_SYMBOL=56, LIT_STRING=57, LIT_CHAR=58,
		LIT_INT=59, LIT_FLOAT=60, COMMENT=61, LINE_COMMENT=62, WS=63;
	public static final int
		RULE_module = 0, RULE_useDef = 1, RULE_definition = 2, RULE_quarkDef = 3,
		RULE_slotDef = 4, RULE_channelDef = 5, RULE_actionDef = 6, RULE_pattern = 7,
		RULE_tuplePattern = 8, RULE_structPattern = 9, RULE_idList = 10, RULE_typeParamBlock = 11,
		RULE_typeParamSpec = 12, RULE_typeList = 13, RULE_funDef = 14, RULE_argSpec = 15,
		RULE_arg = 16, RULE_type = 17, RULE_typeArgBlock = 18, RULE_bosonDef = 19,
		RULE_bosonBody = 20, RULE_bosonOption = 21, RULE_typedIdList = 22, RULE_typedId = 23,
		RULE_action = 24, RULE_lvalue = 25, RULE_expr = 26, RULE_keyValueList = 27,
		RULE_keyValuePair = 28, RULE_exprList = 29, RULE_ident = 30;
	private static String[] makeRuleNames() {
		return new String[] {
			"module", "useDef", "definition", "quarkDef", "slotDef", "channelDef",
			"actionDef", "pattern", "tuplePattern", "structPattern", "idList", "typeParamBlock",
			"typeParamSpec", "typeList", "funDef", "argSpec", "arg", "type", "typeArgBlock",
			"bosonDef", "bosonBody", "bosonOption", "typedIdList", "typedId", "action",
			"lvalue", "expr", "keyValueList", "keyValuePair", "exprList", "ident"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'use'", "'::'", "'{'", "','", "'}'", "'quark'", "'composes'",
			"'channels'", "';'", "'state'", "'actions'", "'end'", "'var'", "':'",
			"'='", "'chan'", "'?'", "'do'", "'on'", "'=>'", "'('", "')'", "'['",
			"']'", "'<'", "'fun'", "'->'", "'boson'", "'is'", "'or'", "'|'", "'&'",
			"'!'", "'if'", "'then'", "'else'", "'while'", "'for'", "'in'", "'return'",
			"'.'", "'^'", "'and'", "'=='", "'!='", "'>'", "'>='", "'<='", "'+'",
			"'-'", "'*'", "'/'", "'%'", "'not'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, "ID", "LIT_SYMBOL", "LIT_STRING",
			"LIT_CHAR", "LIT_INT", "LIT_FLOAT", "COMMENT", "LINE_COMMENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Schism.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SchismParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ModuleContext extends ParserRuleContext {
		public List<UseDefContext> useDef() {
			return getRuleContexts(UseDefContext.class);
		}
		public UseDefContext useDef(int i) {
			return getRuleContext(UseDefContext.class,i);
		}
		public List<DefinitionContext> definition() {
			return getRuleContexts(DefinitionContext.class);
		}
		public DefinitionContext definition(int i) {
			return getRuleContext(DefinitionContext.class,i);
		}
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_module);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(62);
				useDef();
				}
				}
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(71);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__25) | (1L << T__27))) != 0)) {
				{
				{
				setState(68);
				definition();
				}
				}
				setState(73);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UseDefContext extends ParserRuleContext {
		public IdentContext ident() {
			return getRuleContext(IdentContext.class,0);
		}
		public List<TerminalNode> ID() { return getTokens(SchismParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(SchismParser.ID, i);
		}
		public UseDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_useDef; }
	}

	public final UseDefContext useDef() throws RecognitionException {
		UseDefContext _localctx = new UseDefContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_useDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			match(T__0);
			setState(75);
			ident();
			setState(87);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(76);
				match(T__1);
				setState(77);
				match(T__2);
				setState(78);
				match(ID);
				setState(83);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(79);
					match(T__3);
					setState(80);
					match(ID);
					}
					}
					setState(85);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(86);
				match(T__4);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefinitionContext extends ParserRuleContext {
		public QuarkDefContext quarkDef() {
			return getRuleContext(QuarkDefContext.class,0);
		}
		public BosonDefContext bosonDef() {
			return getRuleContext(BosonDefContext.class,0);
		}
		public FunDefContext funDef() {
			return getRuleContext(FunDefContext.class,0);
		}
		public DefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definition; }
	}

	public final DefinitionContext definition() throws RecognitionException {
		DefinitionContext _localctx = new DefinitionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_definition);
		try {
			setState(92);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
				enterOuterAlt(_localctx, 1);
				{
				setState(89);
				quarkDef();
				}
				break;
			case T__27:
				enterOuterAlt(_localctx, 2);
				{
				setState(90);
				bosonDef();
				}
				break;
			case T__25:
				enterOuterAlt(_localctx, 3);
				{
				setState(91);
				funDef();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QuarkDefContext extends ParserRuleContext {
		public TypeListContext composes;
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public ArgSpecContext argSpec() {
			return getRuleContext(ArgSpecContext.class,0);
		}
		public TypeParamBlockContext typeParamBlock() {
			return getRuleContext(TypeParamBlockContext.class,0);
		}
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public List<ChannelDefContext> channelDef() {
			return getRuleContexts(ChannelDefContext.class);
		}
		public ChannelDefContext channelDef(int i) {
			return getRuleContext(ChannelDefContext.class,i);
		}
		public List<SlotDefContext> slotDef() {
			return getRuleContexts(SlotDefContext.class);
		}
		public SlotDefContext slotDef(int i) {
			return getRuleContext(SlotDefContext.class,i);
		}
		public List<ActionDefContext> actionDef() {
			return getRuleContexts(ActionDefContext.class);
		}
		public ActionDefContext actionDef(int i) {
			return getRuleContext(ActionDefContext.class,i);
		}
		public QuarkDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quarkDef; }
	}

	public final QuarkDefContext quarkDef() throws RecognitionException {
		QuarkDefContext _localctx = new QuarkDefContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_quarkDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			match(T__5);
			setState(96);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__22) {
				{
				setState(95);
				typeParamBlock();
				}
			}

			setState(98);
			match(ID);
			setState(99);
			argSpec();
			setState(102);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(100);
				match(T__6);
				setState(101);
				((QuarkDefContext)_localctx).composes = typeList();
				}
			}

			setState(112);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(104);
				match(T__7);
				setState(108);
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(105);
					channelDef();
					setState(106);
					match(T__8);
					}
					}
					setState(110);
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__15 );
				}
			}

			setState(122);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__9) {
				{
				setState(114);
				match(T__9);
				setState(118);
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(115);
					slotDef();
					setState(116);
					match(T__8);
					}
					}
					setState(120);
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__12 );
				}
			}

			setState(132);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(124);
				match(T__10);
				setState(128);
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(125);
					actionDef();
					setState(126);
					match(T__8);
					}
					}
					setState(130);
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__16 );
				}
			}

			setState(134);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SlotDefContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SlotDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_slotDef; }
	}

	public final SlotDefContext slotDef() throws RecognitionException {
		SlotDefContext _localctx = new SlotDefContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_slotDef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
			match(T__12);
			setState(137);
			match(ID);
			setState(138);
			match(T__13);
			setState(139);
			type();
			setState(140);
			match(T__14);
			setState(141);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ChannelDefContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ChannelDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_channelDef; }
	}

	public final ChannelDefContext channelDef() throws RecognitionException {
		ChannelDefContext _localctx = new ChannelDefContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_channelDef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			match(T__15);
			setState(144);
			match(ID);
			setState(145);
			match(T__13);
			setState(146);
			type();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ActionDefContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public List<PatternContext> pattern() {
			return getRuleContexts(PatternContext.class);
		}
		public PatternContext pattern(int i) {
			return getRuleContext(PatternContext.class,i);
		}
		public List<ActionContext> action() {
			return getRuleContexts(ActionContext.class);
		}
		public ActionContext action(int i) {
			return getRuleContext(ActionContext.class,i);
		}
		public ActionDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actionDef; }
	}

	public final ActionDefContext actionDef() throws RecognitionException {
		ActionDefContext _localctx = new ActionDefContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_actionDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(T__16);
			setState(149);
			match(ID);
			setState(150);
			match(T__17);
			setState(156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(151);
				match(T__18);
				setState(152);
				pattern();
				setState(153);
				match(T__19);
				setState(154);
				action(0);
				}
				}
				setState(158);
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__18 );
			setState(160);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PatternContext extends ParserRuleContext {
		public IdentContext ident() {
			return getRuleContext(IdentContext.class,0);
		}
		public TuplePatternContext tuplePattern() {
			return getRuleContext(TuplePatternContext.class,0);
		}
		public StructPatternContext structPattern() {
			return getRuleContext(StructPatternContext.class,0);
		}
		public PatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pattern; }
	}

	public final PatternContext pattern() throws RecognitionException {
		PatternContext _localctx = new PatternContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_pattern);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			ident();
			setState(165);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__20:
				{
				setState(163);
				tuplePattern();
				}
				break;
			case T__2:
				{
				setState(164);
				structPattern();
				}
				break;
			case T__19:
			case T__21:
				break;
			default:
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TuplePatternContext extends ParserRuleContext {
		public IdListContext idList() {
			return getRuleContext(IdListContext.class,0);
		}
		public TuplePatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tuplePattern; }
	}

	public final TuplePatternContext tuplePattern() throws RecognitionException {
		TuplePatternContext _localctx = new TuplePatternContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_tuplePattern);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(167);
			match(T__20);
			setState(168);
			idList();
			setState(169);
			match(T__21);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StructPatternContext extends ParserRuleContext {
		public IdListContext idList() {
			return getRuleContext(IdListContext.class,0);
		}
		public StructPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structPattern; }
	}

	public final StructPatternContext structPattern() throws RecognitionException {
		StructPatternContext _localctx = new StructPatternContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_structPattern);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(171);
			match(T__2);
			setState(172);
			idList();
			setState(173);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdListContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(SchismParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(SchismParser.ID, i);
		}
		public IdListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idList; }
	}

	public final IdListContext idList() throws RecognitionException {
		IdListContext _localctx = new IdListContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_idList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(175);
			match(ID);
			setState(180);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(176);
				match(T__3);
				setState(177);
				match(ID);
				}
				}
				setState(182);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeParamBlockContext extends ParserRuleContext {
		public List<TypeParamSpecContext> typeParamSpec() {
			return getRuleContexts(TypeParamSpecContext.class);
		}
		public TypeParamSpecContext typeParamSpec(int i) {
			return getRuleContext(TypeParamSpecContext.class,i);
		}
		public TypeParamBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeParamBlock; }
	}

	public final TypeParamBlockContext typeParamBlock() throws RecognitionException {
		TypeParamBlockContext _localctx = new TypeParamBlockContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_typeParamBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(T__22);
			setState(184);
			typeParamSpec();
			setState(189);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(185);
				match(T__3);
				setState(186);
				typeParamSpec();
				}
				}
				setState(191);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(192);
			match(T__23);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeParamSpecContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TypeParamSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeParamSpec; }
	}

	public final TypeParamSpecContext typeParamSpec() throws RecognitionException {
		TypeParamSpecContext _localctx = new TypeParamSpecContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_typeParamSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			match(ID);
			setState(197);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__24) {
				{
				setState(195);
				match(T__24);
				setState(196);
				type();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeListContext extends ParserRuleContext {
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public TypeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeList; }
	}

	public final TypeListContext typeList() throws RecognitionException {
		TypeListContext _localctx = new TypeListContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			type();
			setState(204);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(200);
				match(T__3);
				setState(201);
				type();
				}
				}
				setState(206);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunDefContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public ArgSpecContext argSpec() {
			return getRuleContext(ArgSpecContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TypeParamBlockContext typeParamBlock() {
			return getRuleContext(TypeParamBlockContext.class,0);
		}
		public List<ActionContext> action() {
			return getRuleContexts(ActionContext.class);
		}
		public ActionContext action(int i) {
			return getRuleContext(ActionContext.class,i);
		}
		public FunDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funDef; }
	}

	public final FunDefContext funDef() throws RecognitionException {
		FunDefContext _localctx = new FunDefContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_funDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			match(T__25);
			setState(208);
			match(ID);
			setState(210);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__22) {
				{
				setState(209);
				typeParamBlock();
				}
			}

			setState(212);
			argSpec();
			setState(213);
			match(T__13);
			setState(214);
			type();
			setState(215);
			match(T__17);
			setState(217);
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(216);
				action(0);
				}
				}
				setState(219);
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__12) | (1L << T__16) | (1L << T__20) | (1L << T__22) | (1L << T__32) | (1L << T__33) | (1L << T__36) | (1L << T__37) | (1L << T__39) | (1L << T__41) | (1L << T__49) | (1L << T__53) | (1L << ID) | (1L << LIT_STRING) | (1L << LIT_CHAR) | (1L << LIT_INT) | (1L << LIT_FLOAT))) != 0) );
			setState(221);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgSpecContext extends ParserRuleContext {
		public List<ArgContext> arg() {
			return getRuleContexts(ArgContext.class);
		}
		public ArgContext arg(int i) {
			return getRuleContext(ArgContext.class,i);
		}
		public ArgSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argSpec; }
	}

	public final ArgSpecContext argSpec() throws RecognitionException {
		ArgSpecContext _localctx = new ArgSpecContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_argSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(223);
			match(T__20);
			setState(232);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(224);
				arg();
				setState(229);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(225);
					match(T__3);
					setState(226);
					arg();
					}
					}
					setState(231);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(234);
			match(T__21);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ArgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arg; }
	}

	public final ArgContext arg() throws RecognitionException {
		ArgContext _localctx = new ArgContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_arg);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			match(ID);
			setState(237);
			match(T__13);
			setState(238);
			type();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }

		public TypeContext() { }
		public void copyFrom(TypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NamedTypeContext extends TypeContext {
		public IdentContext ident() {
			return getRuleContext(IdentContext.class,0);
		}
		public TypeArgBlockContext typeArgBlock() {
			return getRuleContext(TypeArgBlockContext.class,0);
		}
		public NamedTypeContext(TypeContext ctx) { copyFrom(ctx); }
	}
	public static class FunTypeContext extends TypeContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public FunTypeContext(TypeContext ctx) { copyFrom(ctx); }
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_type);
		int _la;
		try {
			setState(252);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__22:
			case ID:
				_localctx = new NamedTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(241);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__22) {
					{
					setState(240);
					typeArgBlock();
					}
				}

				setState(243);
				ident();
				}
				break;
			case T__25:
				_localctx = new FunTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(244);
				match(T__25);
				setState(245);
				match(T__20);
				setState(247);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__22) | (1L << T__25) | (1L << ID))) != 0)) {
					{
					setState(246);
					typeList();
					}
				}

				setState(249);
				match(T__21);
				setState(250);
				match(T__26);
				setState(251);
				type();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeArgBlockContext extends ParserRuleContext {
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public TypeArgBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeArgBlock; }
	}

	public final TypeArgBlockContext typeArgBlock() throws RecognitionException {
		TypeArgBlockContext _localctx = new TypeArgBlockContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_typeArgBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(254);
			match(T__22);
			setState(255);
			type();
			setState(260);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(256);
				match(T__3);
				setState(257);
				type();
				}
				}
				setState(262);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(263);
			match(T__23);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BosonDefContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public BosonBodyContext bosonBody() {
			return getRuleContext(BosonBodyContext.class,0);
		}
		public TypeArgBlockContext typeArgBlock() {
			return getRuleContext(TypeArgBlockContext.class,0);
		}
		public BosonDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bosonDef; }
	}

	public final BosonDefContext bosonDef() throws RecognitionException {
		BosonDefContext _localctx = new BosonDefContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_bosonDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(265);
			match(T__27);
			setState(267);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__22) {
				{
				setState(266);
				typeArgBlock();
				}
			}

			setState(269);
			match(ID);
			setState(270);
			match(T__28);
			setState(271);
			bosonBody();
			setState(272);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BosonBodyContext extends ParserRuleContext {
		public List<BosonOptionContext> bosonOption() {
			return getRuleContexts(BosonOptionContext.class);
		}
		public BosonOptionContext bosonOption(int i) {
			return getRuleContext(BosonOptionContext.class,i);
		}
		public BosonBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bosonBody; }
	}

	public final BosonBodyContext bosonBody() throws RecognitionException {
		BosonBodyContext _localctx = new BosonBodyContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_bosonBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(274);
			bosonOption();
			setState(279);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__29) {
				{
				{
				setState(275);
				match(T__29);
				setState(276);
				bosonOption();
				}
				}
				setState(281);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BosonOptionContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public TypedIdListContext typedIdList() {
			return getRuleContext(TypedIdListContext.class,0);
		}
		public BosonOptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bosonOption; }
	}

	public final BosonOptionContext bosonOption() throws RecognitionException {
		BosonOptionContext _localctx = new BosonOptionContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_bosonOption);
		try {
			setState(293);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(282);
				match(ID);
				setState(283);
				match(T__20);
				setState(284);
				typeList();
				setState(285);
				match(T__21);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(287);
				match(ID);
				setState(288);
				match(T__2);
				setState(289);
				typedIdList();
				setState(290);
				match(T__4);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(292);
				match(ID);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypedIdListContext extends ParserRuleContext {
		public List<TypedIdContext> typedId() {
			return getRuleContexts(TypedIdContext.class);
		}
		public TypedIdContext typedId(int i) {
			return getRuleContext(TypedIdContext.class,i);
		}
		public TypedIdListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedIdList; }
	}

	public final TypedIdListContext typedIdList() throws RecognitionException {
		TypedIdListContext _localctx = new TypedIdListContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_typedIdList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			typedId();
			setState(300);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(296);
				match(T__3);
				setState(297);
				typedId();
				}
				}
				setState(302);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypedIdContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TypedIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedId; }
	}

	public final TypedIdContext typedId() throws RecognitionException {
		TypedIdContext _localctx = new TypedIdContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_typedId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(303);
			match(ID);
			setState(304);
			match(T__13);
			setState(305);
			type();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ActionContext extends ParserRuleContext {
		public ActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action; }

		public ActionContext() { }
		public void copyFrom(ActionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ForStmtContext extends ActionContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ActionContext action() {
			return getRuleContext(ActionContext.class,0);
		}
		public ForStmtContext(ActionContext ctx) { copyFrom(ctx); }
	}
	public static class ExprStmtContext extends ActionContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprStmtContext(ActionContext ctx) { copyFrom(ctx); }
	}
	public static class AssignActionContext extends ActionContext {
		public LvalueContext lvalue() {
			return getRuleContext(LvalueContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public AssignActionContext(ActionContext ctx) { copyFrom(ctx); }
	}
	public static class WhileStmtContext extends ActionContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ActionContext action() {
			return getRuleContext(ActionContext.class,0);
		}
		public WhileStmtContext(ActionContext ctx) { copyFrom(ctx); }
	}
	public static class ParenActionContext extends ActionContext {
		public ActionContext action() {
			return getRuleContext(ActionContext.class,0);
		}
		public ParenActionContext(ActionContext ctx) { copyFrom(ctx); }
	}
	public static class IfStmtContext extends ActionContext {
		public ExprContext cond;
		public ActionContext t;
		public ActionContext f;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<ActionContext> action() {
			return getRuleContexts(ActionContext.class);
		}
		public ActionContext action(int i) {
			return getRuleContext(ActionContext.class,i);
		}
		public IfStmtContext(ActionContext ctx) { copyFrom(ctx); }
	}
	public static class ChoiceActionContext extends ActionContext {
		public List<ActionContext> action() {
			return getRuleContexts(ActionContext.class);
		}
		public ActionContext action(int i) {
			return getRuleContext(ActionContext.class,i);
		}
		public ChoiceActionContext(ActionContext ctx) { copyFrom(ctx); }
	}
	public static class SequenceActionContext extends ActionContext {
		public List<ActionContext> action() {
			return getRuleContexts(ActionContext.class);
		}
		public ActionContext action(int i) {
			return getRuleContext(ActionContext.class,i);
		}
		public SequenceActionContext(ActionContext ctx) { copyFrom(ctx); }
	}
	public static class ParActionContext extends ActionContext {
		public List<ActionContext> action() {
			return getRuleContexts(ActionContext.class);
		}
		public ActionContext action(int i) {
			return getRuleContext(ActionContext.class,i);
		}
		public ParActionContext(ActionContext ctx) { copyFrom(ctx); }
	}
	public static class ReturnStmtContext extends ActionContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ReturnStmtContext(ActionContext ctx) { copyFrom(ctx); }
	}
	public static class SendActionContext extends ActionContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SendActionContext(ActionContext ctx) { copyFrom(ctx); }
	}
	public static class VardefStmtContext extends ActionContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public VardefStmtContext(ActionContext ctx) { copyFrom(ctx); }
	}

	public final ActionContext action() throws RecognitionException {
		return action(0);
	}

	private ActionContext action(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ActionContext _localctx = new ActionContext(_ctx, _parentState);
		ActionContext _prevctx = _localctx;
		int _startState = 48;
		enterRecursionRule(_localctx, 48, RULE_action, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(356);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				_localctx = new ParenActionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(308);
				match(T__20);
				setState(309);
				action(0);
				setState(310);
				match(T__21);
				}
				break;
			case 2:
				{
				_localctx = new AssignActionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(312);
				lvalue(0);
				setState(313);
				match(T__14);
				setState(314);
				expr(0);
				}
				break;
			case 3:
				{
				_localctx = new SendActionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(316);
				match(T__32);
				setState(317);
				match(ID);
				setState(322);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
				case 1:
					{
					setState(318);
					match(T__20);
					setState(319);
					expr(0);
					setState(320);
					match(T__21);
					}
					break;
				}
				}
				break;
			case 4:
				{
				_localctx = new VardefStmtContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(324);
				match(T__12);
				setState(325);
				match(ID);
				setState(326);
				match(T__13);
				setState(327);
				type();
				setState(328);
				match(T__14);
				setState(329);
				expr(0);
				}
				break;
			case 5:
				{
				_localctx = new IfStmtContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(331);
				match(T__33);
				setState(332);
				((IfStmtContext)_localctx).cond = expr(0);
				setState(333);
				match(T__34);
				setState(334);
				((IfStmtContext)_localctx).t = action(0);
				setState(335);
				match(T__35);
				setState(336);
				((IfStmtContext)_localctx).f = action(0);
				setState(337);
				match(T__11);
				}
				break;
			case 6:
				{
				_localctx = new WhileStmtContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(339);
				match(T__36);
				setState(340);
				expr(0);
				setState(341);
				match(T__17);
				setState(342);
				action(0);
				setState(343);
				match(T__11);
				}
				break;
			case 7:
				{
				_localctx = new ForStmtContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(345);
				match(T__37);
				setState(346);
				match(ID);
				setState(347);
				match(T__38);
				setState(348);
				expr(0);
				setState(349);
				match(T__17);
				setState(350);
				action(0);
				setState(351);
				match(T__11);
				}
				break;
			case 8:
				{
				_localctx = new ReturnStmtContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(353);
				match(T__39);
				setState(354);
				expr(0);
				}
				break;
			case 9:
				{
				_localctx = new ExprStmtContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(355);
				expr(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(381);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(379);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
					case 1:
						{
						_localctx = new ChoiceActionContext(new ActionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_action);
						setState(358);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(361);
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(359);
								match(T__30);
								setState(360);
								action(0);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(363);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						break;
					case 2:
						{
						_localctx = new SequenceActionContext(new ActionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_action);
						setState(365);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(368);
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(366);
								match(T__8);
								setState(367);
								action(0);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(370);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						break;
					case 3:
						{
						_localctx = new ParActionContext(new ActionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_action);
						setState(372);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(375);
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(373);
								match(T__31);
								setState(374);
								action(0);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(377);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						break;
					}
					}
				}
				setState(383);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class LvalueContext extends ParserRuleContext {
		public LvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lvalue; }

		public LvalueContext() { }
		public void copyFrom(LvalueContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DotLvalueContext extends LvalueContext {
		public LvalueContext lvalue() {
			return getRuleContext(LvalueContext.class,0);
		}
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public TerminalNode LIT_INT() { return getToken(SchismParser.LIT_INT, 0); }
		public DotLvalueContext(LvalueContext ctx) { copyFrom(ctx); }
	}
	public static class SimpleLvalueContext extends LvalueContext {
		public IdentContext ident() {
			return getRuleContext(IdentContext.class,0);
		}
		public SimpleLvalueContext(LvalueContext ctx) { copyFrom(ctx); }
	}

	public final LvalueContext lvalue() throws RecognitionException {
		return lvalue(0);
	}

	private LvalueContext lvalue(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		LvalueContext _localctx = new LvalueContext(_ctx, _parentState);
		LvalueContext _prevctx = _localctx;
		int _startState = 50;
		enterRecursionRule(_localctx, 50, RULE_lvalue, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new SimpleLvalueContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(385);
			ident();
			}
			_ctx.stop = _input.LT(-1);
			setState(392);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new DotLvalueContext(new LvalueContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_lvalue);
					setState(387);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(388);
					match(T__40);
					setState(389);
					_la = _input.LA(1);
					if ( !(_la==ID || _la==LIT_INT) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
				}
				setState(394);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }

		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class LitcharExprContext extends ExprContext {
		public TerminalNode LIT_CHAR() { return getToken(SchismParser.LIT_CHAR, 0); }
		public LitcharExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class LvalueExprContext extends ExprContext {
		public LvalueContext lvalue() {
			return getRuleContext(LvalueContext.class,0);
		}
		public LvalueExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class MultExprContext extends ExprContext {
		public ExprContext l;
		public Token op;
		public ExprContext r;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public MultExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class ParenExprContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParenExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class NegateExprContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public NegateExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class LitintExprContext extends ExprContext {
		public TerminalNode LIT_INT() { return getToken(SchismParser.LIT_INT, 0); }
		public LitintExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class LitstrExprContext extends ExprContext {
		public TerminalNode LIT_STRING() { return getToken(SchismParser.LIT_STRING, 0); }
		public LitstrExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class FunCallExprContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public FunCallExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class RecieveExprContext extends ExprContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public PatternContext pattern() {
			return getRuleContext(PatternContext.class,0);
		}
		public RecieveExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class AddExprContext extends ExprContext {
		public ExprContext l;
		public Token op;
		public ExprContext r;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public AddExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class LogicExprContext extends ExprContext {
		public ExprContext l;
		public Token op;
		public ExprContext r;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public LogicExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class TupleDictExprContext extends ExprContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public KeyValueListContext keyValueList() {
			return getRuleContext(KeyValueListContext.class,0);
		}
		public TupleDictExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class RunExprContext extends ExprContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public RunExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class TupleExprContext extends ExprContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public TupleExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class LitfloatExprContext extends ExprContext {
		public TerminalNode LIT_FLOAT() { return getToken(SchismParser.LIT_FLOAT, 0); }
		public LitfloatExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class ListExprContext extends ExprContext {
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public ListExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class CompareExprContext extends ExprContext {
		public ExprContext l;
		public Token op;
		public ExprContext r;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public CompareExprContext(ExprContext ctx) { copyFrom(ctx); }
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 52;
		enterRecursionRule(_localctx, 52, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(437);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				{
				_localctx = new RunExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(396);
				match(T__41);
				setState(397);
				type();
				setState(398);
				match(T__20);
				setState(400);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__16) | (1L << T__20) | (1L << T__22) | (1L << T__41) | (1L << T__49) | (1L << T__53) | (1L << ID) | (1L << LIT_STRING) | (1L << LIT_CHAR) | (1L << LIT_INT) | (1L << LIT_FLOAT))) != 0)) {
					{
					setState(399);
					exprList();
					}
				}

				setState(402);
				match(T__21);
				}
				break;
			case 2:
				{
				_localctx = new NegateExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(404);
				((NegateExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__49 || _la==T__53) ) {
					((NegateExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(405);
				expr(12);
				}
				break;
			case 3:
				{
				_localctx = new RecieveExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(406);
				match(T__16);
				setState(407);
				match(ID);
				setState(412);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
				case 1:
					{
					setState(408);
					match(T__20);
					setState(409);
					pattern();
					setState(410);
					match(T__21);
					}
					break;
				}
				}
				break;
			case 4:
				{
				_localctx = new ParenExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(414);
				match(T__20);
				setState(415);
				expr(0);
				setState(416);
				match(T__21);
				}
				break;
			case 5:
				{
				_localctx = new TupleExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(418);
				match(ID);
				setState(419);
				match(T__20);
				setState(420);
				exprList();
				setState(421);
				match(T__21);
				}
				break;
			case 6:
				{
				_localctx = new TupleDictExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(423);
				match(ID);
				setState(424);
				match(T__2);
				setState(425);
				keyValueList();
				setState(426);
				match(T__4);
				}
				break;
			case 7:
				{
				_localctx = new LitstrExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(428);
				match(LIT_STRING);
				}
				break;
			case 8:
				{
				_localctx = new LitintExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(429);
				match(LIT_INT);
				}
				break;
			case 9:
				{
				_localctx = new LitfloatExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(430);
				match(LIT_FLOAT);
				}
				break;
			case 10:
				{
				_localctx = new LitcharExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(431);
				match(LIT_CHAR);
				}
				break;
			case 11:
				{
				_localctx = new ListExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(432);
				match(T__22);
				setState(433);
				exprList();
				setState(434);
				match(T__23);
				}
				break;
			case 12:
				{
				_localctx = new LvalueExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(436);
				lvalue(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(459);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(457);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
					case 1:
						{
						_localctx = new LogicExprContext(new ExprContext(_parentctx, _parentState));
						((LogicExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(439);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(440);
						((LogicExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__29 || _la==T__42) ) {
							((LogicExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(441);
						((LogicExprContext)_localctx).r = expr(17);
						}
						break;
					case 2:
						{
						_localctx = new CompareExprContext(new ExprContext(_parentctx, _parentState));
						((CompareExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(442);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(443);
						((CompareExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__24) | (1L << T__43) | (1L << T__44) | (1L << T__45) | (1L << T__46) | (1L << T__47))) != 0)) ) {
							((CompareExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(444);
						((CompareExprContext)_localctx).r = expr(16);
						}
						break;
					case 3:
						{
						_localctx = new AddExprContext(new ExprContext(_parentctx, _parentState));
						((AddExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(445);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(446);
						((AddExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__48 || _la==T__49) ) {
							((AddExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(447);
						((AddExprContext)_localctx).r = expr(15);
						}
						break;
					case 4:
						{
						_localctx = new MultExprContext(new ExprContext(_parentctx, _parentState));
						((MultExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(448);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(449);
						((MultExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__50) | (1L << T__51) | (1L << T__52))) != 0)) ) {
							((MultExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(450);
						((MultExprContext)_localctx).r = expr(14);
						}
						break;
					case 5:
						{
						_localctx = new FunCallExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(451);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(452);
						match(T__20);
						setState(454);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__16) | (1L << T__20) | (1L << T__22) | (1L << T__41) | (1L << T__49) | (1L << T__53) | (1L << ID) | (1L << LIT_STRING) | (1L << LIT_CHAR) | (1L << LIT_INT) | (1L << LIT_FLOAT))) != 0)) {
							{
							setState(453);
							exprList();
							}
						}

						setState(456);
						match(T__21);
						}
						break;
					}
					}
				}
				setState(461);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class KeyValueListContext extends ParserRuleContext {
		public List<KeyValuePairContext> keyValuePair() {
			return getRuleContexts(KeyValuePairContext.class);
		}
		public KeyValuePairContext keyValuePair(int i) {
			return getRuleContext(KeyValuePairContext.class,i);
		}
		public KeyValueListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyValueList; }
	}

	public final KeyValueListContext keyValueList() throws RecognitionException {
		KeyValueListContext _localctx = new KeyValueListContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_keyValueList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(462);
			keyValuePair();
			setState(467);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(463);
				match(T__3);
				setState(464);
				keyValuePair();
				}
				}
				setState(469);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeyValuePairContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SchismParser.ID, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public KeyValuePairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyValuePair; }
	}

	public final KeyValuePairContext keyValuePair() throws RecognitionException {
		KeyValuePairContext _localctx = new KeyValuePairContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_keyValuePair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(470);
			match(ID);
			setState(471);
			match(T__13);
			setState(472);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprListContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprList; }
	}

	public final ExprListContext exprList() throws RecognitionException {
		ExprListContext _localctx = new ExprListContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_exprList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(474);
			expr(0);
			setState(479);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(475);
				match(T__3);
				setState(476);
				expr(0);
				}
				}
				setState(481);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(SchismParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(SchismParser.ID, i);
		}
		public IdentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ident; }
	}

	public final IdentContext ident() throws RecognitionException {
		IdentContext _localctx = new IdentContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_ident);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(486);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(482);
					match(ID);
					setState(483);
					match(T__1);
					}
					}
				}
				setState(488);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			}
			setState(489);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 24:
			return action_sempred((ActionContext)_localctx, predIndex);
		case 25:
			return lvalue_sempred((LvalueContext)_localctx, predIndex);
		case 26:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean action_sempred(ActionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 12);
		case 1:
			return precpred(_ctx, 11);
		case 2:
			return precpred(_ctx, 10);
		}
		return true;
	}
	private boolean lvalue_sempred(LvalueContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return precpred(_ctx, 16);
		case 5:
			return precpred(_ctx, 15);
		case 6:
			return precpred(_ctx, 14);
		case 7:
			return precpred(_ctx, 13);
		case 8:
			return precpred(_ctx, 10);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3A\u01ee\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \3\2"+
		"\7\2B\n\2\f\2\16\2E\13\2\3\2\7\2H\n\2\f\2\16\2K\13\2\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\7\3T\n\3\f\3\16\3W\13\3\3\3\5\3Z\n\3\3\4\3\4\3\4\5\4_\n\4\3"+
		"\5\3\5\5\5c\n\5\3\5\3\5\3\5\3\5\5\5i\n\5\3\5\3\5\3\5\3\5\6\5o\n\5\r\5"+
		"\16\5p\5\5s\n\5\3\5\3\5\3\5\3\5\6\5y\n\5\r\5\16\5z\5\5}\n\5\3\5\3\5\3"+
		"\5\3\5\6\5\u0083\n\5\r\5\16\5\u0084\5\5\u0087\n\5\3\5\3\5\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\6"+
		"\b\u009f\n\b\r\b\16\b\u00a0\3\b\3\b\3\t\3\t\3\t\5\t\u00a8\n\t\3\n\3\n"+
		"\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f\3\f\7\f\u00b5\n\f\f\f\16\f\u00b8"+
		"\13\f\3\r\3\r\3\r\3\r\7\r\u00be\n\r\f\r\16\r\u00c1\13\r\3\r\3\r\3\16\3"+
		"\16\3\16\5\16\u00c8\n\16\3\17\3\17\3\17\7\17\u00cd\n\17\f\17\16\17\u00d0"+
		"\13\17\3\20\3\20\3\20\5\20\u00d5\n\20\3\20\3\20\3\20\3\20\3\20\6\20\u00dc"+
		"\n\20\r\20\16\20\u00dd\3\20\3\20\3\21\3\21\3\21\3\21\7\21\u00e6\n\21\f"+
		"\21\16\21\u00e9\13\21\5\21\u00eb\n\21\3\21\3\21\3\22\3\22\3\22\3\22\3"+
		"\23\5\23\u00f4\n\23\3\23\3\23\3\23\3\23\5\23\u00fa\n\23\3\23\3\23\3\23"+
		"\5\23\u00ff\n\23\3\24\3\24\3\24\3\24\7\24\u0105\n\24\f\24\16\24\u0108"+
		"\13\24\3\24\3\24\3\25\3\25\5\25\u010e\n\25\3\25\3\25\3\25\3\25\3\25\3"+
		"\26\3\26\3\26\7\26\u0118\n\26\f\26\16\26\u011b\13\26\3\27\3\27\3\27\3"+
		"\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u0128\n\27\3\30\3\30\3\30"+
		"\7\30\u012d\n\30\f\30\16\30\u0130\13\30\3\31\3\31\3\31\3\31\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\5\32"+
		"\u0145\n\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\5\32\u0167\n\32\3\32\3\32\3\32\6\32\u016c"+
		"\n\32\r\32\16\32\u016d\3\32\3\32\3\32\6\32\u0173\n\32\r\32\16\32\u0174"+
		"\3\32\3\32\3\32\6\32\u017a\n\32\r\32\16\32\u017b\7\32\u017e\n\32\f\32"+
		"\16\32\u0181\13\32\3\33\3\33\3\33\3\33\3\33\3\33\7\33\u0189\n\33\f\33"+
		"\16\33\u018c\13\33\3\34\3\34\3\34\3\34\3\34\5\34\u0193\n\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\5\34\u019f\n\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\5\34\u01b8\n\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\5\34\u01c9\n\34\3\34"+
		"\7\34\u01cc\n\34\f\34\16\34\u01cf\13\34\3\35\3\35\3\35\7\35\u01d4\n\35"+
		"\f\35\16\35\u01d7\13\35\3\36\3\36\3\36\3\36\3\37\3\37\3\37\7\37\u01e0"+
		"\n\37\f\37\16\37\u01e3\13\37\3 \3 \7 \u01e7\n \f \16 \u01ea\13 \3 \3 "+
		"\3 \2\5\62\64\66!\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62"+
		"\64\668:<>\2\b\4\299==\4\2\64\6488\4\2  --\4\2\33\33.\62\3\2\63\64\3\2"+
		"\65\67\2\u0216\2C\3\2\2\2\4L\3\2\2\2\6^\3\2\2\2\b`\3\2\2\2\n\u008a\3\2"+
		"\2\2\f\u0091\3\2\2\2\16\u0096\3\2\2\2\20\u00a4\3\2\2\2\22\u00a9\3\2\2"+
		"\2\24\u00ad\3\2\2\2\26\u00b1\3\2\2\2\30\u00b9\3\2\2\2\32\u00c4\3\2\2\2"+
		"\34\u00c9\3\2\2\2\36\u00d1\3\2\2\2 \u00e1\3\2\2\2\"\u00ee\3\2\2\2$\u00fe"+
		"\3\2\2\2&\u0100\3\2\2\2(\u010b\3\2\2\2*\u0114\3\2\2\2,\u0127\3\2\2\2."+
		"\u0129\3\2\2\2\60\u0131\3\2\2\2\62\u0166\3\2\2\2\64\u0182\3\2\2\2\66\u01b7"+
		"\3\2\2\28\u01d0\3\2\2\2:\u01d8\3\2\2\2<\u01dc\3\2\2\2>\u01e8\3\2\2\2@"+
		"B\5\4\3\2A@\3\2\2\2BE\3\2\2\2CA\3\2\2\2CD\3\2\2\2DI\3\2\2\2EC\3\2\2\2"+
		"FH\5\6\4\2GF\3\2\2\2HK\3\2\2\2IG\3\2\2\2IJ\3\2\2\2J\3\3\2\2\2KI\3\2\2"+
		"\2LM\7\3\2\2MY\5> \2NO\7\4\2\2OP\7\5\2\2PU\79\2\2QR\7\6\2\2RT\79\2\2S"+
		"Q\3\2\2\2TW\3\2\2\2US\3\2\2\2UV\3\2\2\2VX\3\2\2\2WU\3\2\2\2XZ\7\7\2\2"+
		"YN\3\2\2\2YZ\3\2\2\2Z\5\3\2\2\2[_\5\b\5\2\\_\5(\25\2]_\5\36\20\2^[\3\2"+
		"\2\2^\\\3\2\2\2^]\3\2\2\2_\7\3\2\2\2`b\7\b\2\2ac\5\30\r\2ba\3\2\2\2bc"+
		"\3\2\2\2cd\3\2\2\2de\79\2\2eh\5 \21\2fg\7\t\2\2gi\5\34\17\2hf\3\2\2\2"+
		"hi\3\2\2\2ir\3\2\2\2jn\7\n\2\2kl\5\f\7\2lm\7\13\2\2mo\3\2\2\2nk\3\2\2"+
		"\2op\3\2\2\2pn\3\2\2\2pq\3\2\2\2qs\3\2\2\2rj\3\2\2\2rs\3\2\2\2s|\3\2\2"+
		"\2tx\7\f\2\2uv\5\n\6\2vw\7\13\2\2wy\3\2\2\2xu\3\2\2\2yz\3\2\2\2zx\3\2"+
		"\2\2z{\3\2\2\2{}\3\2\2\2|t\3\2\2\2|}\3\2\2\2}\u0086\3\2\2\2~\u0082\7\r"+
		"\2\2\177\u0080\5\16\b\2\u0080\u0081\7\13\2\2\u0081\u0083\3\2\2\2\u0082"+
		"\177\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0082\3\2\2\2\u0084\u0085\3\2\2"+
		"\2\u0085\u0087\3\2\2\2\u0086~\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0088"+
		"\3\2\2\2\u0088\u0089\7\16\2\2\u0089\t\3\2\2\2\u008a\u008b\7\17\2\2\u008b"+
		"\u008c\79\2\2\u008c\u008d\7\20\2\2\u008d\u008e\5$\23\2\u008e\u008f\7\21"+
		"\2\2\u008f\u0090\5\66\34\2\u0090\13\3\2\2\2\u0091\u0092\7\22\2\2\u0092"+
		"\u0093\79\2\2\u0093\u0094\7\20\2\2\u0094\u0095\5$\23\2\u0095\r\3\2\2\2"+
		"\u0096\u0097\7\23\2\2\u0097\u0098\79\2\2\u0098\u009e\7\24\2\2\u0099\u009a"+
		"\7\25\2\2\u009a\u009b\5\20\t\2\u009b\u009c\7\26\2\2\u009c\u009d\5\62\32"+
		"\2\u009d\u009f\3\2\2\2\u009e\u0099\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u009e"+
		"\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2\u00a3\7\16\2\2"+
		"\u00a3\17\3\2\2\2\u00a4\u00a7\5> \2\u00a5\u00a8\5\22\n\2\u00a6\u00a8\5"+
		"\24\13\2\u00a7\u00a5\3\2\2\2\u00a7\u00a6\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8"+
		"\21\3\2\2\2\u00a9\u00aa\7\27\2\2\u00aa\u00ab\5\26\f\2\u00ab\u00ac\7\30"+
		"\2\2\u00ac\23\3\2\2\2\u00ad\u00ae\7\5\2\2\u00ae\u00af\5\26\f\2\u00af\u00b0"+
		"\7\7\2\2\u00b0\25\3\2\2\2\u00b1\u00b6\79\2\2\u00b2\u00b3\7\6\2\2\u00b3"+
		"\u00b5\79\2\2\u00b4\u00b2\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b4\3\2"+
		"\2\2\u00b6\u00b7\3\2\2\2\u00b7\27\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b9\u00ba"+
		"\7\31\2\2\u00ba\u00bf\5\32\16\2\u00bb\u00bc\7\6\2\2\u00bc\u00be\5\32\16"+
		"\2\u00bd\u00bb\3\2\2\2\u00be\u00c1\3\2\2\2\u00bf\u00bd\3\2\2\2\u00bf\u00c0"+
		"\3\2\2\2\u00c0\u00c2\3\2\2\2\u00c1\u00bf\3\2\2\2\u00c2\u00c3\7\32\2\2"+
		"\u00c3\31\3\2\2\2\u00c4\u00c7\79\2\2\u00c5\u00c6\7\33\2\2\u00c6\u00c8"+
		"\5$\23\2\u00c7\u00c5\3\2\2\2\u00c7\u00c8\3\2\2\2\u00c8\33\3\2\2\2\u00c9"+
		"\u00ce\5$\23\2\u00ca\u00cb\7\6\2\2\u00cb\u00cd\5$\23\2\u00cc\u00ca\3\2"+
		"\2\2\u00cd\u00d0\3\2\2\2\u00ce\u00cc\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf"+
		"\35\3\2\2\2\u00d0\u00ce\3\2\2\2\u00d1\u00d2\7\34\2\2\u00d2\u00d4\79\2"+
		"\2\u00d3\u00d5\5\30\r\2\u00d4\u00d3\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5"+
		"\u00d6\3\2\2\2\u00d6\u00d7\5 \21\2\u00d7\u00d8\7\20\2\2\u00d8\u00d9\5"+
		"$\23\2\u00d9\u00db\7\24\2\2\u00da\u00dc\5\62\32\2\u00db\u00da\3\2\2\2"+
		"\u00dc\u00dd\3\2\2\2\u00dd\u00db\3\2\2\2\u00dd\u00de\3\2\2\2\u00de\u00df"+
		"\3\2\2\2\u00df\u00e0\7\16\2\2\u00e0\37\3\2\2\2\u00e1\u00ea\7\27\2\2\u00e2"+
		"\u00e7\5\"\22\2\u00e3\u00e4\7\6\2\2\u00e4\u00e6\5\"\22\2\u00e5\u00e3\3"+
		"\2\2\2\u00e6\u00e9\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8"+
		"\u00eb\3\2\2\2\u00e9\u00e7\3\2\2\2\u00ea\u00e2\3\2\2\2\u00ea\u00eb\3\2"+
		"\2\2\u00eb\u00ec\3\2\2\2\u00ec\u00ed\7\30\2\2\u00ed!\3\2\2\2\u00ee\u00ef"+
		"\79\2\2\u00ef\u00f0\7\20\2\2\u00f0\u00f1\5$\23\2\u00f1#\3\2\2\2\u00f2"+
		"\u00f4\5&\24\2\u00f3\u00f2\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4\u00f5\3\2"+
		"\2\2\u00f5\u00ff\5> \2\u00f6\u00f7\7\34\2\2\u00f7\u00f9\7\27\2\2\u00f8"+
		"\u00fa\5\34\17\2\u00f9\u00f8\3\2\2\2\u00f9\u00fa\3\2\2\2\u00fa\u00fb\3"+
		"\2\2\2\u00fb\u00fc\7\30\2\2\u00fc\u00fd\7\35\2\2\u00fd\u00ff\5$\23\2\u00fe"+
		"\u00f3\3\2\2\2\u00fe\u00f6\3\2\2\2\u00ff%\3\2\2\2\u0100\u0101\7\31\2\2"+
		"\u0101\u0106\5$\23\2\u0102\u0103\7\6\2\2\u0103\u0105\5$\23\2\u0104\u0102"+
		"\3\2\2\2\u0105\u0108\3\2\2\2\u0106\u0104\3\2\2\2\u0106\u0107\3\2\2\2\u0107"+
		"\u0109\3\2\2\2\u0108\u0106\3\2\2\2\u0109\u010a\7\32\2\2\u010a\'\3\2\2"+
		"\2\u010b\u010d\7\36\2\2\u010c\u010e\5&\24\2\u010d\u010c\3\2\2\2\u010d"+
		"\u010e\3\2\2\2\u010e\u010f\3\2\2\2\u010f\u0110\79\2\2\u0110\u0111\7\37"+
		"\2\2\u0111\u0112\5*\26\2\u0112\u0113\7\16\2\2\u0113)\3\2\2\2\u0114\u0119"+
		"\5,\27\2\u0115\u0116\7 \2\2\u0116\u0118\5,\27\2\u0117\u0115\3\2\2\2\u0118"+
		"\u011b\3\2\2\2\u0119\u0117\3\2\2\2\u0119\u011a\3\2\2\2\u011a+\3\2\2\2"+
		"\u011b\u0119\3\2\2\2\u011c\u011d\79\2\2\u011d\u011e\7\27\2\2\u011e\u011f"+
		"\5\34\17\2\u011f\u0120\7\30\2\2\u0120\u0128\3\2\2\2\u0121\u0122\79\2\2"+
		"\u0122\u0123\7\5\2\2\u0123\u0124\5.\30\2\u0124\u0125\7\7\2\2\u0125\u0128"+
		"\3\2\2\2\u0126\u0128\79\2\2\u0127\u011c\3\2\2\2\u0127\u0121\3\2\2\2\u0127"+
		"\u0126\3\2\2\2\u0128-\3\2\2\2\u0129\u012e\5\60\31\2\u012a\u012b\7\6\2"+
		"\2\u012b\u012d\5\60\31\2\u012c\u012a\3\2\2\2\u012d\u0130\3\2\2\2\u012e"+
		"\u012c\3\2\2\2\u012e\u012f\3\2\2\2\u012f/\3\2\2\2\u0130\u012e\3\2\2\2"+
		"\u0131\u0132\79\2\2\u0132\u0133\7\20\2\2\u0133\u0134\5$\23\2\u0134\61"+
		"\3\2\2\2\u0135\u0136\b\32\1\2\u0136\u0137\7\27\2\2\u0137\u0138\5\62\32"+
		"\2\u0138\u0139\7\30\2\2\u0139\u0167\3\2\2\2\u013a\u013b\5\64\33\2\u013b"+
		"\u013c\7\21\2\2\u013c\u013d\5\66\34\2\u013d\u0167\3\2\2\2\u013e\u013f"+
		"\7#\2\2\u013f\u0144\79\2\2\u0140\u0141\7\27\2\2\u0141\u0142\5\66\34\2"+
		"\u0142\u0143\7\30\2\2\u0143\u0145\3\2\2\2\u0144\u0140\3\2\2\2\u0144\u0145"+
		"\3\2\2\2\u0145\u0167\3\2\2\2\u0146\u0147\7\17\2\2\u0147\u0148\79\2\2\u0148"+
		"\u0149\7\20\2\2\u0149\u014a\5$\23\2\u014a\u014b\7\21\2\2\u014b\u014c\5"+
		"\66\34\2\u014c\u0167\3\2\2\2\u014d\u014e\7$\2\2\u014e\u014f\5\66\34\2"+
		"\u014f\u0150\7%\2\2\u0150\u0151\5\62\32\2\u0151\u0152\7&\2\2\u0152\u0153"+
		"\5\62\32\2\u0153\u0154\7\16\2\2\u0154\u0167\3\2\2\2\u0155\u0156\7\'\2"+
		"\2\u0156\u0157\5\66\34\2\u0157\u0158\7\24\2\2\u0158\u0159\5\62\32\2\u0159"+
		"\u015a\7\16\2\2\u015a\u0167\3\2\2\2\u015b\u015c\7(\2\2\u015c\u015d\79"+
		"\2\2\u015d\u015e\7)\2\2\u015e\u015f\5\66\34\2\u015f\u0160\7\24\2\2\u0160"+
		"\u0161\5\62\32\2\u0161\u0162\7\16\2\2\u0162\u0167\3\2\2\2\u0163\u0164"+
		"\7*\2\2\u0164\u0167\5\66\34\2\u0165\u0167\5\66\34\2\u0166\u0135\3\2\2"+
		"\2\u0166\u013a\3\2\2\2\u0166\u013e\3\2\2\2\u0166\u0146\3\2\2\2\u0166\u014d"+
		"\3\2\2\2\u0166\u0155\3\2\2\2\u0166\u015b\3\2\2\2\u0166\u0163\3\2\2\2\u0166"+
		"\u0165\3\2\2\2\u0167\u017f\3\2\2\2\u0168\u016b\f\16\2\2\u0169\u016a\7"+
		"!\2\2\u016a\u016c\5\62\32\2\u016b\u0169\3\2\2\2\u016c\u016d\3\2\2\2\u016d"+
		"\u016b\3\2\2\2\u016d\u016e\3\2\2\2\u016e\u017e\3\2\2\2\u016f\u0172\f\r"+
		"\2\2\u0170\u0171\7\13\2\2\u0171\u0173\5\62\32\2\u0172\u0170\3\2\2\2\u0173"+
		"\u0174\3\2\2\2\u0174\u0172\3\2\2\2\u0174\u0175\3\2\2\2\u0175\u017e\3\2"+
		"\2\2\u0176\u0179\f\f\2\2\u0177\u0178\7\"\2\2\u0178\u017a\5\62\32\2\u0179"+
		"\u0177\3\2\2\2\u017a\u017b\3\2\2\2\u017b\u0179\3\2\2\2\u017b\u017c\3\2"+
		"\2\2\u017c\u017e\3\2\2\2\u017d\u0168\3\2\2\2\u017d\u016f\3\2\2\2\u017d"+
		"\u0176\3\2\2\2\u017e\u0181\3\2\2\2\u017f\u017d\3\2\2\2\u017f\u0180\3\2"+
		"\2\2\u0180\63\3\2\2\2\u0181\u017f\3\2\2\2\u0182\u0183\b\33\1\2\u0183\u0184"+
		"\5> \2\u0184\u018a\3\2\2\2\u0185\u0186\f\3\2\2\u0186\u0187\7+\2\2\u0187"+
		"\u0189\t\2\2\2\u0188\u0185\3\2\2\2\u0189\u018c\3\2\2\2\u018a\u0188\3\2"+
		"\2\2\u018a\u018b\3\2\2\2\u018b\65\3\2\2\2\u018c\u018a\3\2\2\2\u018d\u018e"+
		"\b\34\1\2\u018e\u018f\7,\2\2\u018f\u0190\5$\23\2\u0190\u0192\7\27\2\2"+
		"\u0191\u0193\5<\37\2\u0192\u0191\3\2\2\2\u0192\u0193\3\2\2\2\u0193\u0194"+
		"\3\2\2\2\u0194\u0195\7\30\2\2\u0195\u01b8\3\2\2\2\u0196\u0197\t\3\2\2"+
		"\u0197\u01b8\5\66\34\16\u0198\u0199\7\23\2\2\u0199\u019e\79\2\2\u019a"+
		"\u019b\7\27\2\2\u019b\u019c\5\20\t\2\u019c\u019d\7\30\2\2\u019d\u019f"+
		"\3\2\2\2\u019e\u019a\3\2\2\2\u019e\u019f\3\2\2\2\u019f\u01b8\3\2\2\2\u01a0"+
		"\u01a1\7\27\2\2\u01a1\u01a2\5\66\34\2\u01a2\u01a3\7\30\2\2\u01a3\u01b8"+
		"\3\2\2\2\u01a4\u01a5\79\2\2\u01a5\u01a6\7\27\2\2\u01a6\u01a7\5<\37\2\u01a7"+
		"\u01a8\7\30\2\2\u01a8\u01b8\3\2\2\2\u01a9\u01aa\79\2\2\u01aa\u01ab\7\5"+
		"\2\2\u01ab\u01ac\58\35\2\u01ac\u01ad\7\7\2\2\u01ad\u01b8\3\2\2\2\u01ae"+
		"\u01b8\7;\2\2\u01af\u01b8\7=\2\2\u01b0\u01b8\7>\2\2\u01b1\u01b8\7<\2\2"+
		"\u01b2\u01b3\7\31\2\2\u01b3\u01b4\5<\37\2\u01b4\u01b5\7\32\2\2\u01b5\u01b8"+
		"\3\2\2\2\u01b6\u01b8\5\64\33\2\u01b7\u018d\3\2\2\2\u01b7\u0196\3\2\2\2"+
		"\u01b7\u0198\3\2\2\2\u01b7\u01a0\3\2\2\2\u01b7\u01a4\3\2\2\2\u01b7\u01a9"+
		"\3\2\2\2\u01b7\u01ae\3\2\2\2\u01b7\u01af\3\2\2\2\u01b7\u01b0\3\2\2\2\u01b7"+
		"\u01b1\3\2\2\2\u01b7\u01b2\3\2\2\2\u01b7\u01b6\3\2\2\2\u01b8\u01cd\3\2"+
		"\2\2\u01b9\u01ba\f\22\2\2\u01ba\u01bb\t\4\2\2\u01bb\u01cc\5\66\34\23\u01bc"+
		"\u01bd\f\21\2\2\u01bd\u01be\t\5\2\2\u01be\u01cc\5\66\34\22\u01bf\u01c0"+
		"\f\20\2\2\u01c0\u01c1\t\6\2\2\u01c1\u01cc\5\66\34\21\u01c2\u01c3\f\17"+
		"\2\2\u01c3\u01c4\t\7\2\2\u01c4\u01cc\5\66\34\20\u01c5\u01c6\f\f\2\2\u01c6"+
		"\u01c8\7\27\2\2\u01c7\u01c9\5<\37\2\u01c8\u01c7\3\2\2\2\u01c8\u01c9\3"+
		"\2\2\2\u01c9\u01ca\3\2\2\2\u01ca\u01cc\7\30\2\2\u01cb\u01b9\3\2\2\2\u01cb"+
		"\u01bc\3\2\2\2\u01cb\u01bf\3\2\2\2\u01cb\u01c2\3\2\2\2\u01cb\u01c5\3\2"+
		"\2\2\u01cc\u01cf\3\2\2\2\u01cd\u01cb\3\2\2\2\u01cd\u01ce\3\2\2\2\u01ce"+
		"\67\3\2\2\2\u01cf\u01cd\3\2\2\2\u01d0\u01d5\5:\36\2\u01d1\u01d2\7\6\2"+
		"\2\u01d2\u01d4\5:\36\2\u01d3\u01d1\3\2\2\2\u01d4\u01d7\3\2\2\2\u01d5\u01d3"+
		"\3\2\2\2\u01d5\u01d6\3\2\2\2\u01d69\3\2\2\2\u01d7\u01d5\3\2\2\2\u01d8"+
		"\u01d9\79\2\2\u01d9\u01da\7\20\2\2\u01da\u01db\5\66\34\2\u01db;\3\2\2"+
		"\2\u01dc\u01e1\5\66\34\2\u01dd\u01de\7\6\2\2\u01de\u01e0\5\66\34\2\u01df"+
		"\u01dd\3\2\2\2\u01e0\u01e3\3\2\2\2\u01e1\u01df\3\2\2\2\u01e1\u01e2\3\2"+
		"\2\2\u01e2=\3\2\2\2\u01e3\u01e1\3\2\2\2\u01e4\u01e5\79\2\2\u01e5\u01e7"+
		"\7\4\2\2\u01e6\u01e4\3\2\2\2\u01e7\u01ea\3\2\2\2\u01e8\u01e6\3\2\2\2\u01e8"+
		"\u01e9\3\2\2\2\u01e9\u01eb\3\2\2\2\u01ea\u01e8\3\2\2\2\u01eb\u01ec\79"+
		"\2\2\u01ec?\3\2\2\2\62CIUY^bhprz|\u0084\u0086\u00a0\u00a7\u00b6\u00bf"+
		"\u00c7\u00ce\u00d4\u00dd\u00e7\u00ea\u00f3\u00f9\u00fe\u0106\u010d\u0119"+
		"\u0127\u012e\u0144\u0166\u016d\u0174\u017b\u017d\u017f\u018a\u0192\u019e"+
		"\u01b7\u01c8\u01cb\u01cd\u01d5\u01e1\u01e8";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}