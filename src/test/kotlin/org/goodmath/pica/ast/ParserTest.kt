package org.goodmath.pica.ast

import org.antlr.v4.runtime.CharStreams
import org.goodmath.pica.parser.AstParser
import org.goodmath.pica.util.Twist

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class ParserTest {

    fun parse(input: String): HadronDefinition {
        val parser = AstParser("test")
        return parser.parse(CharStreams.fromString(input))
    }


    @Test
    fun testParseQuark() {
        val input: String = """
            quark [T]MyQuark (i: Int) composes a::b is
                  slot sl: Int = i
                  message flip(i: Int) do
                     sl<-flop(i+1)
                  end
               do 
                    C<-flub(Flubber(23 + x))
               end@quark
        """.trimIndent()

        val expected =
            Twist.obj("HadronDefinition",
                Twist.attr("id", "test"),
                Twist.arr("defs",
                    Twist.obj("Def::Quark",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "MyQuark"),
                        Twist.arr("typeParams",
                            Twist.obj("Type::TypeVar",
                                Twist.attr("name", "T"))),
                        Twist.arr("valueParams",
                            Twist.obj("Param::TypedId",
                                Twist.attr("name", "i"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int"))))),
                        Twist.arr("messages",
                            Twist.obj("Def::Quark::Message",
                                Twist.attr("name", "flip"),
                                Twist.arr("params",
                                    Twist.obj("Param::TypedId",
                                        Twist.attr("name", "i"),
                                        Twist.value("type",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "Int"))))),
                                Twist.arr("body",
                                    Twist.obj("Action::Send",
                                        Twist.value("target",
                                            Twist.obj("Expr::Boson::Tuple",
                                                Twist.attr("name", "sl"))),
                                        Twist.attr("messageName", "flop"),
                                        Twist.arr("args",
                                            Twist.obj("Expr::Operator",
                                                Twist.attr("operator", "Plus"),
                                                Twist.arr("operands",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "i")),
                                                    Twist.obj("Expr::Literal",
                                                        Twist.attr("kind", "IntLit"),
                                                        Twist.attr("value", "1"))))))))),
                        Twist.arr("slots",
                            Twist.obj("Def::Quark::Slot",
                                Twist.attr("name", "sl"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int"))),
                                Twist.value("initValue",
                                    Twist.obj("Expr::Boson::Tuple",
                                        Twist.attr("name", "i"))))),
                        Twist.arr("actions",
                            Twist.obj("Action::Send",
                                Twist.value("target",
                                    Twist.obj("Expr::Boson::Tuple",
                                        Twist.attr("name", "C"))),
                                Twist.attr("messageName", "flub"),
                                Twist.arr("args",
                                    Twist.obj("Expr::Boson::Tuple",
                                        Twist.attr("name", "Flubber"),
                                        Twist.arr("fields",
                                            Twist.obj("Expr::Operator",
                                                Twist.attr("operator", "Plus"),
                                                Twist.arr("operands",
                                                    Twist.obj("Expr::Literal",
                                                        Twist.attr("kind", "IntLit"),
                                                        Twist.attr("value", "23")),
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "x"))))))))))))


        val m = parse(input)
        System.out.println(m.twist().renderCode(0))
        assertEquals(expected.toString(), m.twist().toString())
    }

    @Test
    fun testParseBoson() {
        val input: String = """
                use a::bb::ccc::{d,e}
                boson [T, U]Speckle is
                   One(Int, Int),
                  Two{name: String, v: Float}
                end@boson
        """.trimIndent()
        val expected =
            Twist.obj("HadronDefinition",
                Twist.attr("id", "test"),
                Twist.arr("uses",
                    Twist.obj("Use",
                        Twist.attr("hadron", "a::bb::ccc"),
                        Twist.arr("names",
                            Twist.leaf("d"),
                            Twist.leaf("e")))),
                Twist.arr("defs",
                    Twist.obj("Def::Boson",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "Speckle"),
                        Twist.arr("typeParams",
                            Twist.obj("Type::TypeVar",
                                Twist.attr("name", "T")),
                            Twist.obj("Type::TypeVar",
                                Twist.attr("name", "U"))),
                        Twist.arr("options",
                            Twist.obj("Def::Boson::TupleOption",
                                Twist.attr("name", "One"),
                                Twist.arr("fields",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int")),
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int")))),
                            Twist.obj("Def::Boson::StructOption",
                                Twist.attr("name","Two"),
                                Twist.arr("fields",
                                    Twist.obj("Param::TypedId",
                                        Twist.attr("name", "name"),
                                        Twist.value("type",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "String")))),
                                    Twist.obj("Param::TypedId",
                                        Twist.attr("name", "v"),
                                        Twist.value("type",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "Float"))))))))))
        val m = parse(input)
        assertEquals(expected.toString(), m.twist().toString())
    }


    @Test
    fun testBosonAndQuarkParse() {
        val input: String = """            
        boson ScannerOutput is
          Token{type: String, content: String, line: Int},
          EndOfStream(Unit),
          ScanError{msg: String, line: Int}
        end@boson

        quark Scanner(inp: InputStream) is 
          slot input: InputStream = inp
          slot currentToken: String = ""
          message process(s: ScannerOutput) do
              match (s) as
                on More(c) do seq {
                    currentToken = currentToken + c,
                    if (something > other) seq {
                        output<-nextToken(Token{type: "t", content: currentToken, line: 23}),
                        currentToken = ""
                    } else 
                        x = 3
                   }
                 on End(u) do seq {
                    output<-nextToken(Token{type: "t", content: currentToken, line: 18}),
                    output<-EndOfStream
                }
              end@match
           end@message              
           do
                
           end@quark
        """.trimIndent()

        val m = parse(input)
        print(m.twist().renderCode(6))
        val expected =
            Twist.obj("HadronDefinition",
                Twist.attr("id", "test"),
                Twist.arr("defs",
                    Twist.obj("Def::Boson",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "ScannerOutput"),
                        Twist.arr("options",
                            Twist.obj("Def::Boson::StructOption",
                                Twist.attr("name", "Token"),
                                Twist.arr("fields",
                                    Twist.obj("Param::TypedId",
                                        Twist.attr("name", "type"),
                                        Twist.value("type",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "String")))),
                                    Twist.obj("Param::TypedId",
                                        Twist.attr("name", "content"),
                                        Twist.value("type",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "String")))),
                                    Twist.obj("Param::TypedId",
                                        Twist.attr("name", "line"),
                                        Twist.value("type",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "Int")))))),
                            Twist.obj("Def::Boson::TupleOption",
                                Twist.attr("name", "EndOfStream"),
                                Twist.arr("fields",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Unit")))),
                                    Twist.obj("Def::Boson::StructOption",
                                        Twist.attr("name", "ScanError"),
                                        Twist.arr("fields",
                                            Twist.obj("Param::TypedId",
                                                Twist.attr("name", "msg"),
                                                Twist.value("type",
                                                    Twist.obj("Type::Named",
                                                        Twist.attr("id", "String")))),
                                            Twist.obj("Param::TypedId",
                                                Twist.attr("name", "line"),
                                                Twist.value("type",
                                                    Twist.obj("Type::Named",
                                                        Twist.attr("id", "Int")))))))),
                    Twist.obj("Def::Quark",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "Scanner"),
                        Twist.arr("valueParams",
                            Twist.obj("Param::TypedId",
                                Twist.attr("name", "inp"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "InputStream"))))),
                        Twist.arr("messages",
                            Twist.obj("Def::Quark::Message",
                                Twist.attr("name", "process"),
                                Twist.arr("params",
                                    Twist.obj("Param::TypedId",
                                        Twist.attr("name", "s"),
                                        Twist.value("type",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "ScannerOutput"))))),
                                Twist.arr("body",
                                    Twist.obj("Action::Match",
                                        Twist.value("expr",
                                            Twist.obj("Expr::Boson::Tuple",
                                                Twist.attr("name", "s"))),
                            Twist.arr("options",
                    Twist.obj("MatchOption",
                        Twist.value("pattern",
                            Twist.obj("Action::Receive::BosonTuplePattern",
                                Twist.attr("OptionName", "More"),
                                Twist.arr("fields",
                                    Twist.attr("boundVariable", "c")))),
                        Twist.value("action",
                            Twist.obj("Action::Seq",
                                Twist.arr("actions",
                                    Twist.obj("Action::Assignment",
                                        Twist.value("target",
                                            Twist.obj("Expr::LVal::Id",
                                                Twist.attr("id", "currentToken"))),
                                        Twist.value("value",
                                            Twist.obj("Expr::Operator",
                                                Twist.attr("operator", "Plus"),
                                                Twist.arr("operands",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "currentToken")),
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "c")))))),
                                    Twist.obj("Action::If",
                                        Twist.value("cond",
                                            Twist.obj("Expr::Operator",
                                                Twist.attr("operator", "Greater"),
                                                Twist.arr("operands",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "something")),
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "other"))))),
                                        Twist.value("true",
                                            Twist.obj("Action::Seq",
                                                Twist.arr("actions",
                                                    Twist.obj("Action::Send",
                                                        Twist.value("target",
                                                            Twist.obj("Expr::Boson::Tuple",
                                                                Twist.attr("name", "output"))),
                                                        Twist.attr("messageName", "nextToken"),
                                                        Twist.arr("args",
                                                            Twist.obj("Expr::Boson::Struct",
                                                                Twist.attr("name", "Token"),
                                                                Twist.arr("fields",
                                                                    Twist.obj("Expr::Boson::FieldValue",
                                                                        Twist.attr("name", "type"),
                                                                        Twist.value("value",
                                                                            Twist.obj("Expr::Literal",
                                                                                Twist.attr("kind", "StrLit"),
                                                                                Twist.attr("value", "\"t\"")))),
                                                Twist.obj("Expr::Boson::FieldValue",
                                                    Twist.attr("name", "content"),
                                                    Twist.value("value",
                                                        Twist.obj("Expr::Boson::Tuple",
                                                            Twist.attr("name", "currentToken")))),
                                                                    Twist.obj("Expr::Boson::FieldValue",
                                                                        Twist.attr("name", "line"),
                                                                        Twist.value("value",
                                                                            Twist.obj("Expr::Literal",
                                                                                Twist.attr("kind", "IntLit"),
                                                                                Twist.attr("value", "23")))))))),
                                                    Twist.obj("Action::Assignment",
                                                        Twist.value("target",
                                                            Twist.obj("Expr::LVal::Id",
                                                                Twist.attr("id", "currentToken"))),
                                                        Twist.value("value",
                                                            Twist.obj("Expr::Literal",
                                                                Twist.attr("kind", "StrLit"),
                                                                Twist.attr("value", "\"\""))))))),
                                        Twist.value("false",
                                            Twist.obj("Action::Assignment",
                                                Twist.value("target",
                                                    Twist.obj("Expr::LVal::Id",
                                                        Twist.attr("id", "x"))),
                                                Twist.value("value",
                                                    Twist.obj("Expr::Literal",
                                                        Twist.attr("kind", "IntLit"),
                                                        Twist.attr("value", "3")))))))))),
                                Twist.obj("MatchOption",
                                    Twist.value("pattern",
                                        Twist.obj("Action::Receive::BosonTuplePattern",
                                            Twist.attr("OptionName", "End"),
                                            Twist.arr("fields",
                                                Twist.attr("boundVariable", "u")))),
                                    Twist.value("action",
                                        Twist.obj("Action::Seq",
                                            Twist.arr("actions",
                                                Twist.obj("Action::Send",
                                                    Twist.value("target",
                                                        Twist.obj("Expr::Boson::Tuple",
                                                            Twist.attr("name", "output"))),
                                                    Twist.attr("messageName", "nextToken"),
                                                    Twist.arr("args",
                                                        Twist.obj("Expr::Boson::Struct",
                                                            Twist.attr("name", "Token"),
                                                            Twist.arr("fields",
                                                                Twist.obj("Expr::Boson::FieldValue",
                                                                    Twist.attr("name", "type"),
                                                                    Twist.value("value",
                                                                        Twist.obj("Expr::Literal",
                                                                            Twist.attr("kind", "StrLit"),
                                                                            Twist.attr("value", "\"t\"")))),
                                                                Twist.obj("Expr::Boson::FieldValue",
                                                                    Twist.attr("name", "content"),
                                                                    Twist.value("value",
                                                                        Twist.obj("Expr::Boson::Tuple",
                                                                            Twist.attr("name", "currentToken")))),
                                                                Twist.obj("Expr::Boson::FieldValue",
                                                                    Twist.attr("name", "line"),
                                                                    Twist.value("value",
                                                                        Twist.obj("Expr::Literal",
                                                                            Twist.attr("kind", "IntLit"),
                                                                            Twist.attr("value", "18")))))))),
                                                Twist.obj("Action::Send",
                                                    Twist.value("target",
                                                        Twist.obj("Expr::Boson::Tuple",
                                                            Twist.attr("name", "output"))),
                                                    Twist.attr("messageName", "EndOfStream"))))))))))),
                        Twist.arr("slots",
                            Twist.obj("Def::Quark::Slot",
                                Twist.attr("name", "input"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "InputStream"))),
                                Twist.value("initValue",
                                    Twist.obj("Expr::Boson::Tuple",
                                        Twist.attr("name", "inp")))),
                            Twist.obj("Def::Quark::Slot",
                                Twist.attr("name", "currentToken"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "String"))),
                                Twist.value("initValue",
                                    Twist.obj("Expr::Literal",
                                        Twist.attr("kind", "StrLit"),
                                        Twist.attr("value", "\"\""))))))))
        
        assertEquals(expected.toString(), m.twist().toString())
    }


    @Test
    fun testParseQuarkWithPar() {
        val input = """
            quark [T] MyQuark(i: Int) composes a::b is
               slot sl: Int = i
               message m(x: Int) do
                  par {
                    c<-C(Flubber(23 + x, chin)),
                    d<-sendD(foo(false)),                                        
                    seq {
                     e<-E(bar(true)),
                     q<-Q(oops(27)),
                     p<-P(woops(28))
                   }
                 }
               end
              do
                s<-P(woops(27))
              end
        """.trimIndent()
        val expected =
            Twist.obj("HadronDefinition",
                Twist.attr("id", "test"),
                Twist.arr("defs",
                    Twist.obj("Def::Quark",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "MyQuark"),
                        Twist.arr("typeParams",
                            Twist.obj("Type::TypeVar",
                                Twist.attr("name", "T"))),
                        Twist.arr("valueParams",
                            Twist.obj("Param::TypedId",
                                Twist.attr("name", "i"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int"))))),
                        Twist.arr("messages",
                            Twist.obj("Def::Quark::Message",
                                Twist.attr("name", "m"),
                                Twist.arr("params",
                                    Twist.obj("Param::TypedId",
                                        Twist.attr("name", "x"),
                                        Twist.value("type",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "Int"))))),
                                Twist.arr("body",
                                    Twist.obj("Action::Par",
                                        Twist.arr("actions",
                                            Twist.obj("Action::Send",
                                                Twist.value("target",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "c"))),
                                                Twist.attr("messageName", "C"),
                                                Twist.arr("args",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "Flubber"),
                                                        Twist.arr("fields",
                                                            Twist.obj("Expr::Operator",
                                                                Twist.attr("operator", "Plus"),
                                                                Twist.arr("operands",
                                                                    Twist.obj("Expr::Literal",
                                                                        Twist.attr("kind", "IntLit"),
                                                                        Twist.attr("value", "23")),
                                                                            Twist.obj("Expr::Boson::Tuple",
                                                                                Twist.attr("name", "x")))),
                                                            Twist.obj("Expr::Boson::Tuple",
                                                                Twist.attr("name", "chin")))))),
                                            Twist.obj("Action::Send",
                                                Twist.value("target",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "d"))),
                                                Twist.attr("messageName", "sendD"),
                                                Twist.arr("args",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "foo"),
                                                        Twist.arr("fields",
                                                            Twist.obj("Expr::Boson::Tuple",
                                                                Twist.attr("name", "false")))))),
                                            Twist.obj("Action::Seq",
                                                Twist.arr("actions",
                                                    Twist.obj("Action::Send",
                                                        Twist.value("target",
                                                            Twist.obj("Expr::Boson::Tuple",
                                                                Twist.attr("name", "e"))),
                                                        Twist.attr("messageName", "E"),
                                                        Twist.arr("args",
                                                            Twist.obj("Expr::Boson::Tuple",
                                                                Twist.attr("name", "bar"),
                                                                Twist.arr("fields",
                                                                    Twist.obj("Expr::Boson::Tuple",
                                                                        Twist.attr("name", "true")))))),
                                                Twist.obj("Action::Send",
                                                    Twist.value("target",
                                                        Twist.obj("Expr::Boson::Tuple",
                                                            Twist.attr("name", "q"))),
                                                    Twist.attr("messageName", "Q"),
                                                    Twist.arr("args",
                                                        Twist.obj("Expr::Boson::Tuple",
                                                            Twist.attr("name", "oops"),
                                                            Twist.arr("fields",
                                                                Twist.obj("Expr::Literal",
                                                                    Twist.attr("kind", "IntLit"),
                                                                    Twist.attr("value", "27")))))),
                                                Twist.obj("Action::Send",
                                                    Twist.value("target",
                                                        Twist.obj("Expr::Boson::Tuple",
                                                            Twist.attr("name", "p"))),
                                                    Twist.attr("messageName", "P"),
                                                    Twist.arr("args",
                                                        Twist.obj("Expr::Boson::Tuple",
                                                            Twist.attr("name", "woops"),
                                                            Twist.arr("fields",
                                                                Twist.obj("Expr::Literal",
                                                                    Twist.attr("kind", "IntLit"),
                                                                    Twist.attr("value", "28"))))))))))))),
                    Twist.arr("slots",
                        Twist.obj("Def::Quark::Slot",
                            Twist.attr("name", "sl"),
                            Twist.value("type",
                                Twist.obj("Type::Named",
                                    Twist.attr("id", "Int"))),
                            Twist.value("initValue",
                                Twist.obj("Expr::Boson::Tuple",
                                    Twist.attr("name", "i"))))),
                    Twist.arr("actions",
                        Twist.obj("Action::Send",
                            Twist.value("target",
                                Twist.obj("Expr::Boson::Tuple",
                                    Twist.attr("name", "s"))),
                            Twist.attr("messageName", "P"),
                            Twist.arr("args",
                                Twist.obj("Expr::Boson::Tuple",
                                    Twist.attr("name", "woops"),
                                    Twist.arr("fields",
                                        Twist.obj("Expr::Literal",
                                            Twist.attr("kind", "IntLit"),
                                            Twist.attr("value", "27"))))))))))


        val m = parse(input)
        System.err.println(m.twist().renderCode(6))

        assertEquals(expected.toString(), m.twist().toString())

    }


    @Test
    fun testPingPong() {
        val pingPong = """
        boson Ball is
           Ping(Int, Ponger),
           Pong(Int, Pinger)
        end@boson
        
        quark Ponger is
           message ping(p: Ping) do
              p.2<-pong(Pong(p.1 + 1, self))
           end
        do
        end@quark
        
        quark Pinger(ponger: Ponger) is 
           message pong(i: Pong) do
              p.2<-ping(Ping(p.1+1, self))
           end
           do
              ponger<-ping(0)
           end@quark

        """.trimIndent()
        val m = parse(pingPong)

        val expected = Twist.obj("HadronDefinition",
            Twist.attr("id", "test"),
            Twist.arr("defs",
                Twist.obj("Def::Boson",
                    Twist.attr("hadron", "test"),
                    Twist.attr("name", "Ball"),
                    Twist.arr("options",
                        Twist.obj("Def::Boson::TupleOption",
                            Twist.attr("name", "Ping"),
                            Twist.arr("fields",
                                Twist.obj("Type::Named",
                                    Twist.attr("id", "Int")),
                                Twist.obj("Type::Named",
                                    Twist.attr("id", "Ponger")))),
                        Twist.obj("Def::Boson::TupleOption",
                            Twist.attr("name", "Pong"),
                            Twist.arr("fields",
                                Twist.obj("Type::Named",
                                    Twist.attr("id", "Int")),
                                Twist.obj("Type::Named",
                                    Twist.attr("id", "Pinger")))))),
                Twist.obj("Def::Quark",
                    Twist.attr("hadron", "test"),
                    Twist.attr("name", "Ponger"),
                    Twist.arr("messages",
                        Twist.obj("Def::Quark::Message",
                            Twist.attr("name", "ping"),
                            Twist.arr("params",
                                Twist.obj("Param::TypedId",
                                    Twist.attr("name", "p"),
                                    Twist.value("type",
                                        Twist.obj("Type::Named",
                                            Twist.attr("id", "Ping"))))),
                            Twist.arr("body",
                                Twist.obj("Action::Send",
                                    Twist.value("target",
                                        Twist.obj("Expr::LVal::Indexed",
                                            Twist.value("base",
                                                Twist.obj("Expr::LVal::Id",
                                                    Twist.attr("id", "p"))),
                                            Twist.attr("index", "2"))),
                                    Twist.attr("messageName", "pong"),
                                    Twist.arr("args",
                                        Twist.obj("Expr::Boson::Tuple",
                                            Twist.attr("name", "Pong"),
                                            Twist.arr("fields",
                                                Twist.obj("Expr::Operator",
                                                    Twist.attr("operator", "Plus"),
                                                    Twist.arr("operands",
                                                        Twist.obj("Expr::LVal::Indexed",
                                                            Twist.value("base",
                                                                Twist.obj("Expr::LVal::Id",
                                                                    Twist.attr("id", "p"))),
                                                            Twist.attr("index", "1")),
                                                        Twist.obj("Expr::Literal",
                                                            Twist.attr("kind", "IntLit"),
                                                            Twist.attr("value", "1")))),
                                                Twist.obj("Expr::Boson::Tuple",
                                                    Twist.attr("name", "self")))))))))),
                Twist.obj("Def::Quark",
                    Twist.attr("hadron", "test"),
                    Twist.attr("name", "Pinger"),
                    Twist.arr("valueParams",
                        Twist.obj("Param::TypedId",
                            Twist.attr("name", "ponger"),
                            Twist.value("type",
                                Twist.obj("Type::Named",
                                    Twist.attr("id", "Ponger"))))),
                    Twist.arr("messages",
                        Twist.obj("Def::Quark::Message",
                            Twist.attr("name", "pong"),
                            Twist.arr("params",
                                Twist.obj("Param::TypedId",
                                    Twist.attr("name", "i"),
                                    Twist.value("type",
                                        Twist.obj("Type::Named",
                                            Twist.attr("id", "Pong"))))),
                            Twist.arr("body",
                                Twist.obj("Action::Send",
                                    Twist.value("target",
                                        Twist.obj("Expr::LVal::Indexed",
                                            Twist.value("base",
                                                Twist.obj("Expr::LVal::Id",
                                                    Twist.attr("id", "p"))),
                                            Twist.attr("index", "2"))),
                                    Twist.attr("messageName", "ping"),
                                    Twist.arr("args",
                                        Twist.obj("Expr::Boson::Tuple",
                                            Twist.attr("name", "Ping"),
                                            Twist.arr("fields",
                                                Twist.obj("Expr::Operator",
                                                    Twist.attr("operator", "Plus"),
                                                    Twist.arr("operands",
                                                        Twist.obj("Expr::LVal::Indexed",
                                                            Twist.value("base",
                                                                Twist.obj("Expr::LVal::Id",
                                                                    Twist.attr("id", "p"))),
                                                            Twist.attr("index", "1")),
                                                        Twist.obj("Expr::Literal",
                                                            Twist.attr("kind", "IntLit"),
                                                            Twist.attr("value", "1")))),
                                                Twist.obj("Expr::Boson::Tuple",
                                                    Twist.attr("name", "self"))))))))),
                    Twist.arr("actions",
                        Twist.obj("Action::Send",
                            Twist.value("target",
                                Twist.obj("Expr::Boson::Tuple",
                                    Twist.attr("name", "ponger"))),
                            Twist.attr("messageName", "ping"),
                            Twist.arr("args",
                                Twist.obj("Expr::Literal",
                                    Twist.attr("kind", "IntLit"),
                                    Twist.attr("value", "0"))))))))

        assertEquals(expected.toString(), m.twist().toString())
    }

}