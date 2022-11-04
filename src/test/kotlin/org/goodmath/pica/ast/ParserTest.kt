package org.goodmath.pica.ast

import org.antlr.v4.runtime.CharStreams
import org.goodmath.pica.ast.defs.HadronDefinition
import org.goodmath.pica.parser.AstParser
import org.goodmath.pica.util.twist.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class ParserTest {

    fun parse(input: String): HadronDefinition {
        val parser = AstParser("test")
        return parser.parse(CharStreams.fromString(input))
    }


    @Test
    fun testParseQuark() {
        System.err.println("ParseQuark")
        val input: String = """
            quark [T]MyQuark (i: Int)  is
              chan c: Int
              slot sl: Int = i
              do
                rec c do  
                  on Int(i) do sl := sl + i end
                end
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
                        Twist.arr("channels",
                            Twist.obj("ChannelDef",
                                Twist.attr("name", "$<c>"),
                                Twist.value("type",
                                    Twist.obj("Type::Channel",
                                        Twist.attr("direction", "Both"),
                                        Twist.value("bosonType",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "Int"))))))),
                        Twist.arr("slots",
                            Twist.obj("Def::Quark::Slot",
                                Twist.attr("name", "sl"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int"))),
                                Twist.value("initValue",
                                    Twist.obj("Expr::Boson::Tuple",
                                        Twist.attr("name", "i"))))),
                        Twist.arr("body",
                            Twist.obj("Action::Receive",
                                Twist.arr("matches",
                                    Twist.obj("Action::Receive::Clause",
                                        Twist.value("pattern",
                                            Twist.obj("Action::Receive::BosonTuplePattern",
                                                Twist.attr("OptionName", "Int"),
                                                Twist.arr("fields",
                                                    Twist.attr("boundVariable", "i")))),
                                        Twist.arr("body",
                                            Twist.obj("Action::Assignment",
                                                Twist.value("target",
                                                    Twist.obj("Expr::LVal::Id",
                                                        Twist.attr("id", "sl"))),
                                                Twist.value("value",
                                                    Twist.obj("Expr::Operator",
                                                        Twist.attr("operator", "Plus"),
                                                        Twist.arr("operands",
                                                            Twist.obj("Expr::Boson::Tuple",
                                                                Twist.attr("name", "sl")),
                                                            Twist.obj("Expr::Boson::Tuple",
                                                                Twist.attr("name", "i"))))))))))))))

        val m = parse(input)
        assertEquals(expected.toString(), m.twist().toString())
    }

    @Test
    fun testParseQuarkWithFlavor() {
        System.err.println("ParseQuarkWithFlavor")
        val input: String = """
            boson Scoop is
              Vanilla(Int)
            | Chocolate(Int)
            | Strawberry(Int)
            end
            
            flavor [T]Scoopable is
                chan order: Scoop
            end@flavor

            quark [T]Scooper(fr: Freezer): [T]Scoopable is
               do
                  send fr(SetTemp(-10))
                  rec order do   
                     on Vanilla(i) do 
                        send fr.icecream(Remove(Flavor("vanilla", i)))
                     end
                     on Chocolate(i) do
                        send fr.icecream(Remove(Flavor("chocolate", i)))
                     end
                     on Strawberry(i) do
                        send fr.icecream(Remove(Flavor("strawberry", i)))
                     end
                  end
               end
        """.trimIndent()

        val expected =
            Twist.obj("HadronDefinition",
                Twist.attr("id", "test"),
                Twist.arr("defs",
                    Twist.obj("Def::Boson",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "Scoop"),
                        Twist.arr("options",
                            Twist.obj("Def::Boson::TupleOption",
                                Twist.attr("name", "Vanilla"),
                                Twist.arr("fields",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int")))),
                            Twist.obj("Def::Boson::TupleOption",
                                Twist.attr("name", "Chocolate"),
                                Twist.arr("fields",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int")))),
                            Twist.obj("Def::Boson::TupleOption",
                                Twist.attr("name", "Strawberry"),
                                Twist.arr("fields",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int")))))),
                    Twist.obj("Def::Flavor",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "Scoopable"),
                        Twist.arr("typeParams",
                            Twist.obj("Type::TypeVar",
                                Twist.attr("name", "T"))),
                        Twist.arr("channels",
                            Twist.obj("ChannelDef",
                                Twist.attr("name", "$<order>"),
                                Twist.value("type",
                                    Twist.obj("Type::Channel",
                                        Twist.attr("direction", "Both"),
                                        Twist.value("bosonType",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "Scoop")))))))),
                    Twist.obj("Def::Quark",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "Scooper"),
                        Twist.arr("typeParams",
                            Twist.obj("Type::TypeVar",
                                Twist.attr("name", "T"))),
                        Twist.arr("valueParams",
                            Twist.obj("Param::TypedId",
                                Twist.attr("name", "fr"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Freezer"))))),
                        Twist.arr("flavors",
                            Twist.obj("Type::Named",
                                Twist.attr("id", "Scoopable"),
                                Twist.arr("typeArgs",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "T"))))),
                        Twist.arr("body",
                            Twist.obj("Action::Send",
                                Twist.value("target",
                                    Twist.obj("Expr::Boson::Tuple",
                                        Twist.attr("name", "fr"))),
                                Twist.value("message",
                                    Twist.obj("Expr::Boson::Tuple",
                                        Twist.attr("name", "SetTemp"),
                                        Twist.arr("fields",
                                            Twist.obj("Expr::Literal",
                                                Twist.attr("kind", "IntLit"),
                                                Twist.attr("value", "-10")))))),
                            Twist.obj("Action::Receive",
                                Twist.arr("matches",
                                    Twist.obj("Action::Receive::Clause",
                                        Twist.value("pattern",
                                            Twist.obj("Action::Receive::BosonTuplePattern",
                                                Twist.attr("OptionName", "Vanilla"),
                                                Twist.arr("fields",
                                                    Twist.attr("boundVariable", "i")))),
                                        Twist.arr("body",
                                            Twist.obj("Action::Send",
                                                Twist.value("target",
                                                    Twist.obj("Expr::LVal::Field",
                                                        Twist.value("base",
                                                            Twist.obj("Expr::LVal::Id",
                                                                Twist.attr("id", "fr"))),
                                                        Twist.attr("field", "icecream"))),
                                                Twist.value("message",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "Remove"),
                                                        Twist.arr("fields",
                                                            Twist.obj("Expr::Boson::Tuple",
                                                                Twist.attr("name", "Flavor"),
                                                                Twist.arr("fields",
                                                                    Twist.obj("Expr::Literal",
                                                                        Twist.attr("kind", "StrLit"),
                                                                        Twist.attr("value", "\"vanilla\"")),
                                                                    Twist.obj("Expr::Boson::Tuple",
                                                                        Twist.attr("name", "i")))))))))),
                                    Twist.obj("Action::Receive::Clause",
                                        Twist.value("pattern",
                                            Twist.obj("Action::Receive::BosonTuplePattern",
                                                Twist.attr("OptionName", "Chocolate"),
                                                Twist.arr("fields",
                                                    Twist.attr("boundVariable", "i")))),
                                        Twist.arr("body",
                                            Twist.obj("Action::Send",
                                                Twist.value("target",
                                                    Twist.obj("Expr::LVal::Field",
                                                        Twist.value("base",
                                                            Twist.obj("Expr::LVal::Id",
                                                                Twist.attr("id", "fr"))),
                                                        Twist.attr("field", "icecream"))),
                                                Twist.value("message",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "Remove"),
                                                        Twist.arr("fields",
                                                            Twist.obj("Expr::Boson::Tuple",
                                                                Twist.attr("name", "Flavor"),
                                                                Twist.arr("fields",
                                                                    Twist.obj("Expr::Literal",
                                                                        Twist.attr("kind", "StrLit"),
                                                                        Twist.attr("value", "\"chocolate\"")),
                                                                    Twist.obj("Expr::Boson::Tuple",
                                                                        Twist.attr("name", "i")))))))))),
                                    Twist.obj("Action::Receive::Clause",
                                        Twist.value("pattern",
                                            Twist.obj("Action::Receive::BosonTuplePattern",
                                                Twist.attr("OptionName", "Strawberry"),
                                                Twist.arr("fields",
                                                    Twist.attr("boundVariable", "i")))),
                                        Twist.arr("body",
                                            Twist.obj("Action::Send",
                                                Twist.value("target",
                                                    Twist.obj("Expr::LVal::Field",
                                                        Twist.value("base",
                                                            Twist.obj("Expr::LVal::Id",
                                                                Twist.attr("id", "fr"))),
                                                        Twist.attr("field", "icecream"))),
                                                Twist.value("message",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "Remove"),
                                                        Twist.arr("fields",
                                                            Twist.obj("Expr::Boson::Tuple",
                                                                Twist.attr("name", "Flavor"),
                                                                Twist.arr("fields",
                                                                    Twist.obj("Expr::Literal",
                                                                        Twist.attr("kind", "StrLit"),
                                                                        Twist.attr("value", "\"strawberry\"")),
                                                                    Twist.obj("Expr::Boson::Tuple",
                                                                        Twist.attr("name", "i"))))))))))))))))

        val m = parse(input)
        print(m.twist().renderCode(6))

        assertEquals(expected.toString(), m.twist().toString())
    }

    @Test
    fun testParseBoson() {
        System.err.println("ParseBoson")
        val input: String = """
                use a::bb::ccc::{d,e}
                boson [T, U]Speckle is
                   One(Int, Int)
                | Two{name: String, v: Float}
                end@boson
        """.trimIndent()
        val expected =
            obj("HadronDefinition",
                attr("id", "test"),
                arr("uses",
                    obj("Use",
                        attr("hadron", "a::bb::ccc"),
                        arr("names",
                            leaf("d"),
                            leaf("e")))),
                arr("defs",
                    obj("Def::Boson",
                        attr("hadron", "test"),
                        attr("name", "Speckle"),
                        arr("typeParams",
                            obj("Type::TypeVar",
                                attr("name", "T")),
                            obj("Type::TypeVar",
                                attr("name", "U"))),
                        arr("options",
                            obj("Def::Boson::TupleOption",
                                attr("name", "One"),
                                arr("fields",
                                    obj("Type::Named",
                                        attr("id", "Int")),
                                    obj("Type::Named",
                                        attr("id", "Int")))),
                            obj("Def::Boson::StructOption",
                                attr("name","Two"),
                                arr("fields",
                                    obj("Param::TypedId",
                                        attr("name", "name"),
                                        value("type",
                                            obj("Type::Named",
                                                attr("id", "String")))),
                                    obj("Param::TypedId",
                                        attr("name", "v"),
                                        value("type",
                                            obj("Type::Named",
                                                attr("id", "Float"))))))))))
        val m = parse(input)
        assertEquals(expected.toString(), m.twist().toString())
    }


    @Test
    fun testBosonAndQuarkParse() {
        System.err.println("ParseBosonAndQuark")
        val input: String = """
        boson ScannerOutput is
           Token{type: String, content: String, line: Int}
         | EndOfStream(Unit)
         | ScanError{ message: String, line: Int }
        end@boson

        quark Scanner(inp: InputStream) is
          slot input: InputStream = inp
          slot currentToken: String = ""
          chan process[in]: ScannerOut
          chan tokens[out]: TokenStream
          do
              rec process do                
                on More(c) do
                    currentToken := currentToken + c
                    if something > other then
                        send output(Token{type: "t", content: currentToken, line: 23})
                        currentToken := ""
                    else 
                        x := 3
                    end
                end
                on End(u) do
                    send output(Token{type: "t", content: currentToken, line: 18})
                    send output(EndOfStream(0))
                end           
           end@rec
          end@quark
        """.trimIndent()

        val m = parse(input)
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
                                        Twist.attr("name", "message"),
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
                        Twist.arr("channels",
                            Twist.obj("ChannelDef",
                                Twist.attr("name", "$<process>"),
                                Twist.value("type",
                                    Twist.obj("Type::Channel",
                                        Twist.attr("direction", "In"),
                                        Twist.value("bosonType",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "ScannerOut")))))),
                            Twist.obj("ChannelDef",
                                Twist.attr("name", "$<tokens>"),
                                Twist.value("type",
                                    Twist.obj("Type::Channel",
                                        Twist.attr("direction", "Out"),
                                        Twist.value("bosonType",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "TokenStream"))))))),
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
                                        Twist.attr("value", "\"\""))))),
                                    Twist.arr("body",
                                        Twist.obj("Action::Receive",
                                            Twist.arr("matches",
                                                Twist.obj("Action::Receive::Clause",
                                                    Twist.value("pattern",
                                                        Twist.obj("Action::Receive::BosonTuplePattern",
                                                            Twist.attr("OptionName", "More"),
                                                            Twist.arr("fields",
                                                                Twist.attr("boundVariable", "c")))),
                                                    Twist.arr("body",
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
                                                        Twist.obj("Action::Cond",
                                                            Twist.arr("clauses",
                                                                Twist.obj("Action::Cond::CondClause",
                                                                    Twist.value("condition",
                                                                        Twist.obj("Expr::Operator",
                                                                            Twist.attr("operator", "Greater"),
                                                                            Twist.arr("operands",
                                                                                Twist.obj("Expr::Boson::Tuple",
                                                                                    Twist.attr("name", "something")),
                                                                                Twist.obj("Expr::Boson::Tuple",
                                                                                    Twist.attr("name", "other"))))),
                                                                    Twist.arr("body",
                                                                        Twist.obj("Action::Send",
                                                                            Twist.value("target",
                                                                                Twist.obj("Expr::Boson::Tuple",
                                                                                    Twist.attr("name", "output"))),
                                                                            Twist.value("message",
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
                Twist.arr("else",
                    Twist.obj("Action::Assignment",
                        Twist.value("target",
                            Twist.obj("Expr::LVal::Id",
                                Twist.attr("id", "x"))),
                        Twist.value("value",
                            Twist.obj("Expr::Literal",
                                Twist.attr("kind", "IntLit"),
                                Twist.attr("value", "3")))))))),
        Twist.obj("Action::Receive::Clause",
            Twist.value("pattern",
                Twist.obj("Action::Receive::BosonTuplePattern",
                    Twist.attr("OptionName", "End"),
                    Twist.arr("fields",
                        Twist.attr("boundVariable", "u")))),
            Twist.arr("body",
                Twist.obj("Action::Send",
                    Twist.value("target",
                        Twist.obj("Expr::Boson::Tuple",
                            Twist.attr("name", "output"))),
                    Twist.value("message",
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
                    Twist.value("message",
                        Twist.obj("Expr::Boson::Tuple",
                            Twist.attr("name", "EndOfStream"),
                            Twist.arr("fields",
                                Twist.obj("Expr::Literal",
                                    Twist.attr("kind", "IntLit"),
                                    Twist.attr("value", "0"))))))))))))))

        assertEquals(expected.toString(), m.twist().toString())
    }


    @Test
    fun testParseQuarkWithPar() {
        System.err.println("ParseQuarkWithPar")
        val input = """
            quark [T] MyQuark(i: Int) is
               slot sl: Int = i
               chan m: Int
               do
                 par {
                    send m(Flub(23 + x, chin))
                    send d(foo(false))
                    seq {
                     send e(bar(true))
                     send q(oops(27))
                     send p(woops(28))
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
                        Twist.arr("channels",
                            Twist.obj("ChannelDef",
                                Twist.attr("name", "$<m>"),
                                Twist.value("type",
                                    Twist.obj("Type::Channel",
                                        Twist.attr("direction", "Both"),
                                        Twist.value("bosonType",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "Int"))))))),
                        Twist.arr("slots",
                            Twist.obj("Def::Quark::Slot",
                                Twist.attr("name", "sl"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int"))),
                                Twist.value("initValue",
                                    Twist.obj("Expr::Boson::Tuple",
                                        Twist.attr("name", "i"))))),
                        Twist.arr("body",
                            Twist.obj("Action::Par",
                                Twist.arr("body",
                                    Twist.obj("Action::Send",
                                        Twist.value("target",
                                            Twist.obj("Expr::Boson::Tuple",
                                                Twist.attr("name", "m"))),
                                        Twist.value("message",
                                            Twist.obj("Expr::Boson::Tuple",
                                                Twist.attr("name", "Flub"),
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
                                        Twist.value("message",
                                            Twist.obj("Expr::Boson::Tuple",
                                                Twist.attr("name", "foo"),
                                                Twist.arr("fields",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "false")))))),
                                    Twist.obj("Action::Seq",
                                        Twist.arr("body",
                                            Twist.obj("Action::Send",
                                                Twist.value("target",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "e"))),
                                                Twist.value("message",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "bar"),
                                                        Twist.arr("fields",
                                                            Twist.obj("Expr::Boson::Tuple",
                                                                Twist.attr("name", "true")))))),
                                            Twist.obj("Action::Send",
                                                Twist.value("target",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "q"))),
                                                Twist.value("message",
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
                                                Twist.value("message",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "woops"),
                                                        Twist.arr("fields",
                                                            Twist.obj("Expr::Literal",
                                                                Twist.attr("kind", "IntLit"),
                                                                Twist.attr("value", "28"))))))))))))))


        val m = parse(input)
        assertEquals(expected.toString(), m.twist().toString())

    }


    @Test
    fun testPingPong() {
        System.err.println("ParsePingPong")
        val pingPong = """
        boson Ping is
           Ping(Int)         
        end@boson
        
        boson Pong is
          Pong(Int, Ping)
        end

        quark Ponger(pinger: Pinger) is
           chan p: Ping        
           do
             rec p do
                on Ping(x) do
                    send pinger.p(Pong(x+1, self))
                end
             end@rec
        end@quark
                   
        quark Pinger is
           chan p: Pong
           do
              rec p do
                 on Pong(x, ponger) do
                    send ponger(Pong(x+1))
                 end
              end@rec
           end@quark
                   
        quark Main is
        do
           var pinger: Pinger = !Pinger()
           var ponger: Ponger = !Ponger(pinger)
           send ponger.p(Ping(0))
        end@quark

        """.trimIndent()
        val m = parse(pingPong)

        val expected =
            Twist.obj("HadronDefinition",
                Twist.attr("id", "test"),
                Twist.arr("defs",
                    Twist.obj("Def::Boson",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "Ping"),
                        Twist.arr("options",
                            Twist.obj("Def::Boson::TupleOption",
                                Twist.attr("name", "Ping"),
                                Twist.arr("fields",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int")))))),
                    Twist.obj("Def::Boson",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "Pong"),
                        Twist.arr("options",
                            Twist.obj("Def::Boson::TupleOption",
                                Twist.attr("name", "Pong"),
                                Twist.arr("fields",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Int")),
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Ping")))))),
                    Twist.obj("Def::Quark",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "Ponger"),
                        Twist.arr("valueParams",
                            Twist.obj("Param::TypedId",
                                Twist.attr("name", "pinger"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Pinger"))))),
                        Twist.arr("channels",
                            Twist.obj("ChannelDef",
                                Twist.attr("name", "$<p>"),
                                Twist.value("type",
                                    Twist.obj("Type::Channel",
                                        Twist.attr("direction", "Both"),
                                        Twist.value("bosonType",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "Ping"))))))),
                        Twist.arr("body",
                            Twist.obj("Action::Receive",
                                Twist.arr("matches",
                                    Twist.obj("Action::Receive::Clause",
                                        Twist.value("pattern",
                                            Twist.obj("Action::Receive::BosonTuplePattern",
                                                Twist.attr("OptionName", "Ping"),
                                                Twist.arr("fields",
                                                    Twist.attr("boundVariable", "x")))),
                                        Twist.arr("body",
                                            Twist.obj("Action::Send",
                                                Twist.value("target",
                                                    Twist.obj("Expr::LVal::Field",
                                                        Twist.value("base",
                                                            Twist.obj("Expr::LVal::Id",
                                                                Twist.attr("id", "pinger"))),
                                                        Twist.attr("field", "p"))),
                                                Twist.value("message",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "Pong"),
                                                        Twist.arr("fields",
                                                            Twist.obj("Expr::Operator",
                                                                Twist.attr("operator", "Plus"),
                                                                Twist.arr("operands",
                                                                    Twist.obj("Expr::Boson::Tuple",
                                                                        Twist.attr("name", "x")),
                                                                    Twist.obj("Expr::Literal",
                                                                        Twist.attr("kind", "IntLit"),
                                                                        Twist.attr("value", "1")))),
                                                            Twist.obj("Expr::Boson::Tuple",
                                                                Twist.attr("name", "self")))))))))))),
                    Twist.obj("Def::Quark",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "Pinger"),
                        Twist.arr("channels",
                            Twist.obj("ChannelDef",
                                Twist.attr("name", "$<p>"),
                                Twist.value("type",
                                    Twist.obj("Type::Channel",
                                        Twist.attr("direction", "Both"),
                                        Twist.value("bosonType",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "Pong"))))))),
                        Twist.arr("body",
                            Twist.obj("Action::Receive",
                                Twist.arr("matches",
                                    Twist.obj("Action::Receive::Clause",
                                        Twist.value("pattern",
                                            Twist.obj("Action::Receive::BosonTuplePattern",
                                                Twist.attr("OptionName", "Pong"),
                                                Twist.arr("fields",
                                                    Twist.attr("boundVariable", "x"),
                                                    Twist.attr("boundVariable", "ponger")))),
                                        Twist.arr("body",
                                            Twist.obj("Action::Send",
                                                Twist.value("target",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "ponger"))),
                                                Twist.value("message",
                                                    Twist.obj("Expr::Boson::Tuple",
                                                        Twist.attr("name", "Pong"),
                                                        Twist.arr("fields",
                                                            Twist.obj("Expr::Operator",
                                                                Twist.attr("operator", "Plus"),
                                                                Twist.arr("operands",
                                                                    Twist.obj("Expr::Boson::Tuple",
                                                                        Twist.attr("name", "x")),
                                                                    Twist.obj("Expr::Literal",
                                                                        Twist.attr("kind", "IntLit"),
                                                                        Twist.attr("value", "1")))))))))))))),
                    Twist.obj("Def::Quark",
                        Twist.attr("hadron", "test"),
                        Twist.attr("name", "Main"),
                        Twist.arr("body",
                            Twist.obj("Action::VarDef",
                                Twist.attr("name", "pinger"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Pinger"))),
                                Twist.value("value",
                                    Twist.obj("Expr::CreateQuark",
                                        Twist.value("type",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "Pinger")))))),
                            Twist.obj("Action::VarDef",
                                Twist.attr("name", "ponger"),
                                Twist.value("type",
                                    Twist.obj("Type::Named",
                                        Twist.attr("id", "Ponger"))),
                                Twist.value("value",
                                    Twist.obj("Expr::CreateQuark",
                                        Twist.value("type",
                                            Twist.obj("Type::Named",
                                                Twist.attr("id", "Ponger"))),
                                        Twist.arr("valueArgs",
                                            Twist.obj("Expr::Boson::Tuple",
                                                Twist.attr("name", "pinger")))))),
                            Twist.obj("Action::Send",
                                Twist.value("target",
                                    Twist.obj("Expr::LVal::Field",
                                        Twist.value("base",
                                            Twist.obj("Expr::LVal::Id",
                                                Twist.attr("id", "ponger"))),
                                        Twist.attr("field", "p"))),
                                Twist.value("message",
                                    Twist.obj("Expr::Boson::Tuple",
                                        Twist.attr("name", "Ping"),
                                        Twist.arr("fields",
                                            Twist.obj("Expr::Literal",
                                                Twist.attr("kind", "IntLit"),
                                                Twist.attr("value", "0"))))))))))

        print(m.twist().renderCode(6))
        assertEquals(expected.toString(), m.twist().toString())
    }

}
