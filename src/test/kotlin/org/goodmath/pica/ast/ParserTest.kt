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
                chan C : F
                  slot sl: Int = i
               do 
                    send C(Flubber(23 + x))
               end@quark
        """.trimIndent()

        val expected =
            Twist.obj("HadronDefinition",
                Twist.attr("id", "test"),
                Twist.arr("defs",
                    Twist.obj("Def::Quark",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "$<MyQuark>"),
                        Twist.arr("typeParams",
                            Twist.obj("Type::TypeVar",
                                Twist.attr("name", "$<T>"))),
                        Twist.arr("valueParams",
                            Twist.obj("Param::TypedId",
                                Twist.attr("name", "$<i>"),
                                        Twist.value("type",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "Int"))))),
                        Twist.arr("channels",
                            Twist.obj("Definition::Channel",
                                Twist.attr("name", "$<C>"),
                                Twist.value("type",
                                    Twist.obj("Type::Channel",
                                        Twist.attr("direction", "Both"),
                                        Twist.value("msgType",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "F"))))))),
                        Twist.arr("slots",
                            Twist.obj("Def::Quark::Slot",
                                Twist.attr("name", "$<sl>"),
                                        Twist.value("type",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "Int"))),
                                Twist.value("initValue",
                                    Twist.obj("Expr::LVal::Id",
                                        Twist.attr("id", "i"))))),
                        Twist.arr("actions",
                            Twist.obj("Action::Send",
                                Twist.value("channel",
                                    Twist.obj("Expr::LVal::Id",
                                        Twist.attr("id", "C"))),
                                    Twist.value("message",
                                        Twist.obj("Expr::Boson::Tuple",
                                            Twist.attr("name", "Flubber"),
                                            Twist.arr("fields",
                                                Twist.obj("Expr::Operator",
                                                    Twist.attr("operator", "Plus"),
                                                    Twist.arr("operands",
                                                        Twist.obj("Expr::Literal",
                                                            Twist.attr("kind", "IntLit"),
                                                            Twist.attr("value", "23")),
                                                        Twist.obj("Expr::LVal::Id",
                                                                Twist.attr("id", "x"))))))))))))

        val m = parse(input)
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
                            Twist.leaf("$<d>"),
                            Twist.leaf("$<e>")))),
                Twist.arr("defs",
                    Twist.obj("Def::Boson",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "$<Speckle>"),
                        Twist.arr("typeParams",
                            Twist.obj("Type::TypeVar",
                                Twist.attr("name", "$<T>")),
                            Twist.obj("Type::TypeVar",
                                Twist.attr("name", "$<U>"))),
                        Twist.arr("options",
                            Twist.obj("Def::Boson::TupleOption",
                                Twist.attr("name", "$<One>"),
                                Twist.arr("fields",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int")),
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int")))),
                            Twist.obj("Def::Boson::StructOption",
                                Twist.attr("name","$<Two>"),
                                Twist.arr("fields",
                                    Twist.obj("Param::TypedId",
                                        Twist.attr("name", "$<name>"),
                                        Twist.value("type",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "String")))),
                                    Twist.obj("Param::TypedId",
                                        Twist.attr("name", "$<v>"),
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
          ScanError{message: String, line: Int}
        end@boson

        quark Scanner 
          args (inp: InputStream) is 
          chan output: ScannerOutput
          slot input: InputStream = inp
          slot currentToken: String = ""
          do
            repeat 
              receive input {
                on More(c) do seq {
                    currentToken = currentToken + c,
                    if (something > other) seq {
                        send output(Token{type: "t", content: currentToken, line: 23}),
                        currentToken = ""
                    } else 
                        x = 3
                 }
                 on End(u) do seq {
                    send output(Token{type: "t", content: currentToken, line: 18}),
                    send output(EndOfStream(Unit)),
                    exit
                }
              }
            end@quark
        

        """.trimIndent()

        val m = parse(input)
        val expected = Twist.obj("HadronDefinition",
            Twist.attr("id", "test"),
            Twist.arr("defs",
                Twist.obj("Def::Boson",
                    Twist.attr("hadron", "test"),
                    Twist.attr("name", "$<ScannerOutput>"),
                    Twist.arr("options",
                        Twist.obj("Def::Boson::StructOption",
                            Twist.attr("name", "$<Token>"),
                            Twist.arr("fields",
                                Twist.obj("Param::TypedId",
                                    Twist.attr("name", "$<type>"),
                                    Twist.value("type",
                                        Twist.obj("Type::Named",
                                            Twist.attr("id", "String")
                                        )
                                    )
                                ),
                                Twist.obj("Param::TypedId",
                                    Twist.attr("name", "$<content>"),
                                    Twist.value("type",
                                        Twist.obj("Type::Named",
                                            Twist.attr("id", "String")))),
                                Twist.obj("Param::TypedId",
                                    Twist.attr("name", "$<line>"),
                                    Twist.value("type",
                                        Twist.obj("Type::Named",
                                            Twist.attr("id", "Int")))))),
                        Twist.obj("Def::Boson::TupleOption",
                            Twist.attr("name", "$<EndOfStream>"),
                            Twist.arr("fields",
                                Twist.obj("Type::Named",
                                    Twist.attr("id", "Unit")))),
                        Twist.obj("Def::Boson::StructOption",
                            Twist.attr("name", "$<ScanError>"),
                            Twist.arr("fields",
                                Twist.obj("Param::TypedId",
                                    Twist.attr("name", "$<message>"),
                                    Twist.value("type",
                                        Twist.obj("Type::Named",
                                            Twist.attr("id", "String")))),
                                Twist.obj("Param::TypedId",
                                    Twist.attr("name", "$<line>"),
                                    Twist.value("type",
                                        Twist.obj("Type::Named",
                                            Twist.attr("id", "Int")))))))),
                Twist.obj("Def::Quark",
                    Twist.attr("hadron", "test"),
                    Twist.attr("name", "$<Scanner>"),
                    Twist.arr("valueParams",
                        Twist.obj("Param::TypedId",
                            Twist.attr("name", "$<inp>"),
                            Twist.value("type",
                                Twist.obj("Type::Named",
                                    Twist.attr("id", "InputStream"))))),
                    Twist.arr("channels",
                        Twist.obj("Definition::Channel",
                            Twist.attr("name", "$<output>"),
                            Twist.value("type",
                                Twist.obj("Type::Channel",
                                    Twist.attr("direction", "Both"),
                                    Twist.value("msgType",
                                        Twist.obj("Type::Named",
                                            Twist.attr("id", "ScannerOutput"))))))),
                    Twist.arr("slots",
                        Twist.obj("Def::Quark::Slot",
                            Twist.attr("name", "$<input>"),
                            Twist.value("type",
                                Twist.obj("Type::Named",
                                    Twist.attr("id", "InputStream"))),
                            Twist.value("initValue",
                                Twist.obj("Expr::LVal::Id",
                                    Twist.attr("id", "inp")))),
                        Twist.obj("Def::Quark::Slot",
                            Twist.attr("name", "$<currentToken>"),
                            Twist.value("type",
                                Twist.obj("Type::Named",
                                    Twist.attr("id", "String"))),
                            Twist.value("initValue",
                                Twist.obj("Expr::Literal",
                                    Twist.attr("kind", "StrLit"),
                                    Twist.attr("value", "\"\""))))),
                    Twist.arr("actions",
                        Twist.obj("Action::Receive",
                            Twist.value("channel",
                                Twist.obj("Expr::LVal::Id",
                                    Twist.attr("id", "input"))),
                            Twist.arr("actions",
                                Twist.obj("Action::Recieve::ReceiveOption",
                                    Twist.value("pattern",
                                        Twist.obj("Action::Receive::BosonTuplePattern",
                                            Twist.attr("OptionName", "$<More>"),
                                            Twist.arr("fields",
                                                Twist.attr("boundVariable", "$<c>")))),
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
                                                                Twist.obj("Expr::LVal::Id",
                                                                        Twist.attr("id", "currentToken")),
                                                                Twist.obj("Expr::LVal::Id",
                                                                    Twist.attr("id", "c")))))),
                                                Twist.obj("Action::If",
                                                    Twist.value("cond",
                                                        Twist.obj("Expr::Operator",
                                                            Twist.attr("operator", "Greater"),
                                                            Twist.arr("operands",
                                                                Twist.obj("Expr::LVal::Id",
                                                                    Twist.attr("id", "something")),
                                                                Twist.obj("Expr::LVal::Id",
                                                                    Twist.attr("id", "other"))))),
                                                    Twist.value("true",
                                                        Twist.obj("Action::Seq",
                                                                Twist.arr("actions",
                                                                    Twist.obj("Action::Send",
                                                                        Twist.value("channel",
                                                                            Twist.obj("Expr::LVal::Id",
                                                                                Twist.attr("id", "output"))),
                                                                        Twist.value("message",
                                                                            Twist.obj("Expr::Boson::Struct",
                                                                                Twist.attr("name", "Token"),
                                                                                Twist.arr("fields",
                                                                                    Twist.obj("Expr::Boson::FieldValue",
                                                                                        Twist.attr("name", "$<type>"),
                                                                                        Twist.value("value",
                                                                                            Twist.obj("Expr::Literal",
                                                                                                Twist.attr("kind", "StrLit"),
                                                                                                Twist.attr("value", "\"t\"")))),
                                                                                    Twist.obj("Expr::Boson::FieldValue",
                                                                                        Twist.attr("name", "$<content>"),
                                                                                        Twist.value("value",
                                                                                            Twist.obj("Expr::LVal::Id",
                                                                                                Twist.attr("id", "currentToken")))),
                                                                                    Twist.obj("Expr::Boson::FieldValue",
                                                                                        Twist.attr("name", "$<line>"),
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
                                Twist.obj("Action::Recieve::ReceiveOption",
                                    Twist.value("pattern",
                                        Twist.obj("Action::Receive::BosonTuplePattern",
                                            Twist.attr("OptionName", "$<End>"),
                                            Twist.arr("fields",
                                                Twist.attr("boundVariable", "$<u>")))),
                                    Twist.value("action",
                                        Twist.obj("Action::Seq",
                                            Twist.arr("actions",
                                                Twist.obj("Action::Send",
                                                    Twist.value("channel",
                                                        Twist.obj("Expr::LVal::Id",
                                                            Twist.attr("id", "output"))),
                                                    Twist.value("message",
                                                        Twist.obj("Expr::Boson::Struct",
                                                            Twist.attr("name", "Token"),
                                                            Twist.arr("fields",
                                                                Twist.obj("Expr::Boson::FieldValue",
                                                                    Twist.attr("name", "$<type>"),
                                                                    Twist.value("value",
                                                                        Twist.obj("Expr::Literal",
                                                                            Twist.attr("kind", "StrLit"),
                                                                            Twist.attr("value", "\"t\"")))),
                                                                Twist.obj("Expr::Boson::FieldValue",
                                                                    Twist.attr("name", "$<content>"),
                                                                    Twist.value("value",
                                                                        Twist.obj("Expr::LVal::Id",
                                                                            Twist.attr("id", "currentToken")))),
                                                                Twist.obj("Expr::Boson::FieldValue",
                                                                    Twist.attr("name", "$<line>"),
                                                                    Twist.value("value",
                                                                        Twist.obj("Expr::Literal",
                                                                            Twist.attr("kind", "IntLit"),
                                                                            Twist.attr("value", "18")))))))),
                                                Twist.obj("Action::Send",
                                                    Twist.value("channel",
                                                        Twist.obj("Expr::LVal::Id",
                                                            Twist.attr("id", "output"))),
                                                    Twist.value("message",
                                                        Twist.obj("Expr::Boson::Tuple",
                                                            Twist.attr("name", "EndOfStream"),
                                                            Twist.arr("fields",
                                                                Twist.obj("Expr::LVal::Id",
                                                                    Twist.attr("id", "Unit")))))),
                                                Twist.leaf("Action::Exit")))))))))))
        assertEquals(expected.toString(), m.twist().toString())
    }

    @Test
    fun testParseChannelType() {
        val input = """
            quark [T]MyQuark(i: Int)  composes a::b is 
               chan C : F
               slot sl: Int = i
               do 
                 seq {
                    var ch : chan String = newchan String,
                    var chin: chan in String = ch,
                    send C(Flubber(23 + x, chin))
                 }
               end@quark
        """.trimIndent()
        val expected =
            Twist.obj("HadronDefinition",
                Twist.attr("id", "test"),
                Twist.arr("defs",
                    Twist.obj("Def::Quark",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "$<MyQuark>"),
                        Twist.arr("typeParams",
                            Twist.obj("Type::TypeVar",
                                Twist.attr("name", "$<T>"))),
                        Twist.arr("valueParams",
                            Twist.obj("Param::TypedId",
                                Twist.attr("name", "$<i>"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int"))))),
                        Twist.arr("channels",
                            Twist.obj("Definition::Channel",
                                Twist.attr("name", "$<C>"),
                                Twist.value("type",
                                    Twist.obj("Type::Channel",
                                        Twist.attr("direction", "Both"),
                                        Twist.value("msgType",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "F"))))))),
                        Twist.arr("slots",
                            Twist.obj("Def::Quark::Slot",
                                Twist.attr("name", "$<sl>"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int"))),
                                    Twist.value("initValue",
                                        Twist.obj("Expr::LVal::Id",
                                            Twist.attr("id", "i"))))),
                        Twist.arr("actions",
                            Twist.obj("Action::Seq",
                                Twist.arr("actions",
                                    Twist.obj("Action::VarDef",
                                        Twist.attr("name", "$<ch>"),
                                        Twist.value("type",
                                            Twist.obj("Type::Channel",
                                                Twist.attr("direction", "Both"),
                                                Twist.value("msgType",
                                                    Twist.obj("Type::Named",
                                                        Twist.attr("id", "String"))))),
                                        Twist.value("value",
                                            Twist.obj("Expr::NewChannel",
                                                Twist.value("type",
                                                    Twist.obj("Type::Named",
                                                        Twist.attr("id", "String")))))),
                                    Twist.obj("Action::VarDef",
                                        Twist.attr("name", "$<chin>"),
                                        Twist.value("type",
                                            Twist.obj("Type::Channel",
                                                Twist.attr("direction", "In"),
                                                Twist.value("msgType",
                                                    Twist.obj("Type::Named",
                                                        Twist.attr("id", "String"))))),
                                        Twist.value("value",
                                            Twist.obj("Expr::LVal::Id",
                                                Twist.attr("id", "ch")))),
                                    Twist.obj("Action::Send",
                                        Twist.value("channel",
                                            Twist.obj("Expr::LVal::Id",
                                                Twist.attr("id", "C"))),
                                        Twist.value("message",
                                            Twist.obj("Expr::Boson::Tuple",
                                                Twist.attr("name", "Flubber"),
                                                Twist.arr("fields",
                                                    Twist.obj("Expr::Operator",
                                                        Twist.attr("operator", "Plus"),
                                                        Twist.arr("operands",
                                                            Twist.obj("Expr::Literal",
                                                                Twist.attr("kind", "IntLit"),
                                                                Twist.attr("value", "23")),
                                                            Twist.obj("Expr::LVal::Id",
                                                                Twist.attr("id", "x")))),
                                                    Twist.obj("Expr::LVal::Id",
                                                        Twist.attr("id", "chin"))))))))))))

        val m = parse(input)
        assertEquals(expected.toString(), m.twist().toString())

    }

    @Test
    fun testParseQuarkWithParAndSel() {
        val input = """
            quark [T] MyQuark(i: Int) composes a::b is
               chan C : F 
               slot sl: Int = i
               do 
                  par {
                    one { 
                       send C(Flubber(23 + x, chin)),
                       send D(foo(false))
                    },
                    send E(bar(true)),
                    seq {
                      send Q(oops(27)),
                      send P(woops(28))
                    }
                  }
               end@quark
        """.trimIndent()
        val expected =
            Twist.obj("HadronDefinition",
                Twist.attr("id", "test"),
                Twist.arr("defs",
                    Twist.obj("Def::Quark",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "$<MyQuark>"),
                        Twist.arr("typeParams",
                            Twist.obj("Type::TypeVar",
                                Twist.attr("name", "$<T>"))),
                        Twist.arr("valueParams",
                            Twist.obj("Param::TypedId",
                                Twist.attr("name", "$<i>"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int"))))),
                        Twist.arr("channels",
                            Twist.obj("Definition::Channel",
                                Twist.attr("name", "$<C>"),
                                Twist.value("type",
                                    Twist.obj("Type::Channel",
                                        Twist.attr("direction", "Both"),
                                        Twist.value("msgType",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "F"))))))),
                        Twist.arr("slots",
                            Twist.obj("Def::Quark::Slot",
                                Twist.attr("name", "$<sl>"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int"))),
                                Twist.value("initValue",
                                    Twist.obj("Expr::LVal::Id",
                                        Twist.attr("id", "i"))))),
                        Twist.arr("actions",
                            Twist.obj("Action::Par",
                                Twist.arr("actions",
                                    Twist.obj("Action::Choice",
                                        Twist.arr("choices",
                                            Twist.obj("Action::Send",
                                                Twist.value("channel",
                                                    Twist.obj("Expr::LVal::Id",
                                                        Twist.attr("id", "C"))),
                                                Twist.value("message",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "Flubber"),
                                                        Twist.arr("fields",
                                                            Twist.obj("Expr::Operator",
                                                                Twist.attr("operator", "Plus"),
                                                                Twist.arr("operands",
                                                                    Twist.obj("Expr::Literal",
                                                                        Twist.attr("kind", "IntLit"),
                                                                        Twist.attr("value", "23")),
                                                                    Twist.obj("Expr::LVal::Id",
                                                                        Twist.attr("id", "x")))),
                                                            Twist.obj("Expr::LVal::Id",
                                                                Twist.attr("id", "chin")))))),
                                            Twist.obj("Action::Send",
                                                Twist.value("channel",
                                                    Twist.obj("Expr::LVal::Id",
                                                        Twist.attr("id", "D"))),
                                                Twist.value("message",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "foo"),
                                                        Twist.arr("fields",
                                                            Twist.obj("Expr::LVal::Id",
                                                                Twist.attr("id", "false")))))))),
                                    Twist.obj("Action::Send",
                                        Twist.value("channel",
                                            Twist.obj("Expr::LVal::Id",
                                                Twist.attr("id", "E"))),
                                        Twist.value("message",
                                            Twist.obj("Expr::Boson::Tuple",
                                                Twist.attr("name", "bar"),
                                                Twist.arr("fields",
                                                    Twist.obj("Expr::LVal::Id",
                                                        Twist.attr("id", "true")))))),
                                    Twist.obj("Action::Seq",
                                        Twist.arr("actions",
                                    Twist.obj("Action::Send",
                                        Twist.value("channel",
                                            Twist.obj("Expr::LVal::Id",
                                                Twist.attr("id", "Q"))),
                                        Twist.value("message",
                                            Twist.obj("Expr::Boson::Tuple",
                                                Twist.attr("name", "oops"),
                                                Twist.arr("fields",
                                                    Twist.obj("Expr::Literal",
                                                        Twist.attr("kind", "IntLit"),
                                                        Twist.attr("value", "27")))))),
                                    Twist.obj("Action::Send",
                                        Twist.value("channel",
                                            Twist.obj("Expr::LVal::Id",
                                                Twist.attr("id", "P"))),
                                        Twist.value("message",
                                            Twist.obj("Expr::Boson::Tuple",
                                                Twist.attr("name", "woops"),
                                                Twist.arr("fields",
                                                    Twist.obj("Expr::Literal",
                                                        Twist.attr("kind", "IntLit"),
                                                        Twist.attr("value", "28"))))))))))))))

        val m = parse(input)
        //System.err.println(m.twist().renderCode(0))

        assertEquals(expected.toString(), m.twist().toString())

    }


    @Test
    fun testPingPong() {
        val pingPong = """
        boson Ball is
           Ping(Int, chan Ball),
          Pong(Int)
        end@boson
        
        quark PingPong
           args(q: chan Ball) is
             chan input: Ball
             chan output: Ball
        do
           par {
              receive input {
                 on Ping(i, reply) do send reply(Pong(i+1))
                 on Pong(i) do send output(Ping(i, input)) 
              },
              send q(Ping(0, q))
          }
        end@quark

        """.trimIndent()
        val m = parse(pingPong)
        val expected =   Twist.obj("HadronDefinition",
            Twist.attr("id", "test"),
            Twist.arr("defs",
                Twist.obj("Def::Boson",
                    Twist.attr("hadron", "test"),
                    Twist.attr("name", "$<Ball>"),
                    Twist.arr("options",
                        Twist.obj("Def::Boson::TupleOption",
                            Twist.attr("name", "$<Ping>"),
                            Twist.arr("fields",
                                Twist.obj("Type::Named",
                                    Twist.attr("id", "Int")),
                                Twist.obj("Type::Channel",
                                    Twist.attr("direction", "Both"),
                                    Twist.value("msgType",
                                        Twist.obj("Type::Named",
                                            Twist.attr("id", "Ball")))))),
                        Twist.obj("Def::Boson::TupleOption",
                            Twist.attr("name", "$<Pong>"),
                            Twist.arr("fields",
                                Twist.obj("Type::Named",
                                    Twist.attr("id", "Int")))))),
                Twist.obj("Def::Quark",
                    Twist.attr("hadron", "test"),
                    Twist.attr("name", "$<PingPong>"),
                    Twist.arr("valueParams",
                        Twist.obj("Param::TypedId",
                            Twist.attr("name", "$<q>"),
                            Twist.value("type",
                                Twist.obj("Type::Channel",
                                    Twist.attr("direction", "Both"),
                                    Twist.value("msgType",
                                        Twist.obj("Type::Named",
                                            Twist.attr("id", "Ball"))))))),
                    Twist.arr("channels",
                        Twist.obj("Definition::Channel",
                            Twist.attr("name", "$<input>"),
                            Twist.value("type",
                                Twist.obj("Type::Channel",
                                    Twist.attr("direction", "Both"),
                                    Twist.value("msgType",
                                        Twist.obj("Type::Named",
                                            Twist.attr("id", "Ball")))))),
                        Twist.obj("Definition::Channel",
                            Twist.attr("name", "$<output>"),
                            Twist.value("type",
                                Twist.obj("Type::Channel",
                                    Twist.attr("direction", "Both"),
                                    Twist.value("msgType",
                                        Twist.obj("Type::Named",
                                            Twist.attr("id", "Ball"))))))),
                    Twist.arr("actions",
                        Twist.obj("Action::Par",
                            Twist.arr("actions",
                                Twist.obj("Action::Receive",
                                    Twist.value("channel",
                                        Twist.obj("Expr::LVal::Id",
                                            Twist.attr("id", "input"))),
                                    Twist.arr("actions",
                                        Twist.obj("Action::Recieve::ReceiveOption",
                                            Twist.value("pattern",
                                                Twist.obj("Action::Receive::BosonTuplePattern",
                                                    Twist.attr("OptionName", "$<Ping>"),
                                                    Twist.arr("fields",
                                                        Twist.attr("boundVariable", "$<i>"),
                                                        Twist.attr("boundVariable", "$<reply>")))),
                                            Twist.value("action",
                                                Twist.obj("Action::Send",
                                                    Twist.value("channel",
                                                        Twist.obj("Expr::LVal::Id",
                                                            Twist.attr("id", "reply"))),
                                                    Twist.value("message",
                                                        Twist.obj("Expr::Boson::Tuple",
                                                            Twist.attr("name", "Pong"),
                                                            Twist.arr("fields",
                                                                Twist.obj("Expr::Operator",
                                                                    Twist.attr("operator", "Plus"),
                                                                    Twist.arr("operands",
                                                                        Twist.obj("Expr::LVal::Id",
                                                                            Twist.attr("id", "i")),
                                                                        Twist.obj("Expr::Literal",
                                                                            Twist.attr("kind", "IntLit"),
                                                                            Twist.attr("value", "1")))))))))),
                                        Twist.obj("Action::Recieve::ReceiveOption",
                                            Twist.value("pattern",
                                                Twist.obj("Action::Receive::BosonTuplePattern",
                                                    Twist.attr("OptionName", "$<Pong>"),
                                                    Twist.arr("fields",
                                                        Twist.attr("boundVariable", "$<i>")))),
                                            Twist.value("action",
                                                Twist.obj("Action::Send",
                                                    Twist.value("channel",
                                                        Twist.obj("Expr::LVal::Id",
                                                            Twist.attr("id", "output"))),
                                                    Twist.value("message",
                                                        Twist.obj("Expr::Boson::Tuple",
                                                            Twist.attr("name", "Ping"),
                                                            Twist.arr("fields",
                                                                Twist.obj("Expr::LVal::Id",
                                                                    Twist.attr("id", "i")),
                                                                Twist.obj("Expr::LVal::Id",
                                                                    Twist.attr("id", "input")))))))))),
                                Twist.obj("Action::Send",
                                    Twist.value("channel",
                                        Twist.obj("Expr::LVal::Id",
                                            Twist.attr("id", "q"))),
                                    Twist.value("message",
                                        Twist.obj("Expr::Boson::Tuple",
                                            Twist.attr("name", "Ping"),
                                            Twist.arr("fields",
                                                Twist.obj("Expr::Literal",
                                                    Twist.attr("kind", "IntLit"),
                                                    Twist.attr("value", "0")),
                                                Twist.obj("Expr::LVal::Id",
                                                    Twist.attr("id", "q"))))))))))))


        assertEquals(expected.toString(), m.twist().toString())
    }

}