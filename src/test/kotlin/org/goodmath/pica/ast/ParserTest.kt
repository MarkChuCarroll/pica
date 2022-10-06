package org.goodmath.pica.ast

import org.antlr.v4.runtime.CharStreams
import org.goodmath.pica.parser.AstParser

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class ParserTest {

    fun parse(input: String): PicaModule {
        val parser = AstParser("test")
        return parser.parse(CharStreams.fromString(input))
    }


    @Test
    fun testParseQuark() {
        val input: String = """
            quark [T]MyQuark(i: Int) composes a::b is
                chan C : F
                var sl: Int = i
                 do
                    send C(Flubber(23 + x))
            end@quark
        """.trimIndent()
        val expected: String = """
            obj PicaModule:
               id = test
               defs = array:
                  obj Definition::Quark:
                     module = test
                     name = MyQuark
                     typeParams = array:
                        obj TypeVar:
                           name = T
                     valueParams = array:
                        obj param:
                           name = i
                           type:
                              obj Type::Named:
                                 id = Int
                     channels = array:
                        obj Definition::Channel:
                           name = C
                           messageType:
                              obj Type::Named:
                                 id = F
                     slots = array:
                        obj Object::Slot:
                           name = sl
                           type:
                              obj Type::Named:
                                 id = Int
                           initValue:
                              obj Expr::LVal::Id:
                                 id = i
                     actions = array:
                        obj Action::Send:
                           channel:
                              obj Expr::LVal::Id:
                                 id = C
                           message:
                              obj Expr::Boson::Tuple:
                                 name = Flubber
                                 fields = array:
                                    obj Expr::Operator:
                                       operator = Plus
                                       operands = array:
                                          obj Expr::Literal:
                                             kind = IntLit
                                             value = 23
                                          obj Expr::LVal::Id:
                                             id = x

        """.trimIndent()
        val m = parse(input)
        assertEquals(expected, m.twist().toString())
    }

    @Test
    fun testParseBoson() {
        val input: String = """
                use a::bb::ccc::{d,e}
                boson [T, U]Speckle is
                   One(Int, Int)
                 or Two{name: String, v: Float}
                end
        """.trimIndent()
        val expected: String = """
            obj PicaModule:
               id = test
               uses = array:
                  obj Use:
                     module = a::bb::ccc
                     names = array:
                        d
                        e
               defs = array:
                  obj Definition::Boson:
                     module = test
                     name = Speckle
                     typeParams = array:
                        obj TypeVar:
                           name = T
                        obj TypeVar:
                           name = U
                     options = array:
                        obj Boson::TupleOption:
                           name = One
                           fields = array:
                              obj Type::Named:
                                 id = Int
                              obj Type::Named:
                                 id = Int
                        obj Boson::StructOption:
                           name = Two
                           fields = array:
                              obj BosonStructField:
                                 name = name
                                 type:
                                    obj Type::Named:
                                       id = String
                              obj BosonStructField:
                                 name = v
                                 type:
                                    obj Type::Named:
                                       id = Float

        """.trimIndent()
        val m = parse(input)
        assertEquals(expected, m.twist().toString())
    }


    @Test
    fun testBosonAndQuarkParse() {
        val input: String = """
        boson ScannerOutput is
          Token{type: String, content: String, line: Int}
        or  EndOfStream(Unit)
        or  ScanError{message: String, line: Int}
        end@boson

        quark Scanner(inp: InputStream) is
          chan output: ScannerOutput
          var input: InputStream = inp
          var currentToken: String = ""
          do
            repeat
              receive input do
                on More(c) do
                    currentToken = currentToken + c;
                    if  something > other then
                        send output(Token{type: "t", content: currentToken, line: 23});
                        currentToken = ""
                    else
                        x = 3
                    end@if
                end@on
                on End(u) do
                    send output(Token{type: "t", content: currentToken, line: 18});
                    send output(EndOfStream(Unit));
                    exit
                end@on
            end@receive
          end@repeat
        end@quark

        """.trimIndent()

        val m = parse(input)
        val expected = """
            obj PicaModule:
               id = test
               defs = array:
                  obj Definition::Boson:
                     module = test
                     name = ScannerOutput
                     options = array:
                        obj Boson::StructOption:
                           name = Token
                           fields = array:
                              obj BosonStructField:
                                 name = type
                                 type:
                                    obj Type::Named:
                                       id = String
                              obj BosonStructField:
                                 name = content
                                 type:
                                    obj Type::Named:
                                       id = String
                              obj BosonStructField:
                                 name = line
                                 type:
                                    obj Type::Named:
                                       id = Int
                        obj Boson::TupleOption:
                           name = EndOfStream
                           fields = array:
                              obj Type::Named:
                                 id = Unit
                        obj Boson::StructOption:
                           name = ScanError
                           fields = array:
                              obj BosonStructField:
                                 name = message
                                 type:
                                    obj Type::Named:
                                       id = String
                              obj BosonStructField:
                                 name = line
                                 type:
                                    obj Type::Named:
                                       id = Int
                  obj Definition::Quark:
                     module = test
                     name = Scanner
                     valueParams = array:
                        obj param:
                           name = inp
                           type:
                              obj Type::Named:
                                 id = InputStream
                     channels = array:
                        obj Definition::Channel:
                           name = output
                           messageType:
                              obj Type::Named:
                                 id = ScannerOutput
                     slots = array:
                        obj Object::Slot:
                           name = input
                           type:
                              obj Type::Named:
                                 id = InputStream
                           initValue:
                              obj Expr::LVal::Id:
                                 id = inp
                        obj Object::Slot:
                           name = currentToken
                           type:
                              obj Type::Named:
                                 id = String
                           initValue:
                              obj Expr::Literal:
                                 kind = StrLit
                                 value = ""
                     actions = array:
                        obj Action::Receive:
                           channel:
                              obj Expr::LVal::Id:
                                 id = input
                           actions = array:
                              obj Option:
                                 pattern:
                                    obj BosonTuplePattern:
                                       OptionName = More
                                       fields = array:
                                          boundVariable = c
                                 action:
                                    obj Action::Seq:
                                       actions = array:
                                          obj Action::Assignment:
                                             target:
                                                obj Expr::LVal::Id:
                                                   id = currentToken
                                             value:
                                                obj Expr::Operator:
                                                   operator = Plus
                                                   operands = array:
                                                      obj Expr::LVal::Id:
                                                         id = currentToken
                                                      obj Expr::LVal::Id:
                                                         id = c
                                          obj Action::If:
                                             cond:
                                                obj Expr::Operator:
                                                   operator = Greater
                                                   operands = array:
                                                      obj Expr::LVal::Id:
                                                         id = something
                                                      obj Expr::LVal::Id:
                                                         id = other
                                             true:
                                                obj Action::Seq:
                                                   actions = array:
                                                      obj Action::Send:
                                                         channel:
                                                            obj Expr::LVal::Id:
                                                               id = output
                                                         message:
                                                            obj Expr::Boson::Struct:
                                                               name = Token
                                                               fields = array:
                                                                  obj field:
                                                                     name = type
                                                                     value:
                                                                        obj Expr::Literal:
                                                                           kind = StrLit
                                                                           value = "t"
                                                                  obj field:
                                                                     name = content
                                                                     value:
                                                                        obj Expr::LVal::Id:
                                                                           id = currentToken
                                                                  obj field:
                                                                     name = line
                                                                     value:
                                                                        obj Expr::Literal:
                                                                           kind = IntLit
                                                                           value = 23
                                                      obj Action::Assignment:
                                                         target:
                                                            obj Expr::LVal::Id:
                                                               id = currentToken
                                                         value:
                                                            obj Expr::Literal:
                                                               kind = StrLit
                                                               value = ""
                                             false:
                                                obj Action::Assignment:
                                                   target:
                                                      obj Expr::LVal::Id:
                                                         id = x
                                                   value:
                                                      obj Expr::Literal:
                                                         kind = IntLit
                                                         value = 3
                              obj Option:
                                 pattern:
                                    obj BosonTuplePattern:
                                       OptionName = End
                                       fields = array:
                                          boundVariable = u
                                 action:
                                    obj Action::Seq:
                                       actions = array:
                                          obj Action::Send:
                                             channel:
                                                obj Expr::LVal::Id:
                                                   id = output
                                             message:
                                                obj Expr::Boson::Struct:
                                                   name = Token
                                                   fields = array:
                                                      obj field:
                                                         name = type
                                                         value:
                                                            obj Expr::Literal:
                                                               kind = StrLit
                                                               value = "t"
                                                      obj field:
                                                         name = content
                                                         value:
                                                            obj Expr::LVal::Id:
                                                               id = currentToken
                                                      obj field:
                                                         name = line
                                                         value:
                                                            obj Expr::Literal:
                                                               kind = IntLit
                                                               value = 18
                                          obj Action::Seq:
                                             actions = array:
                                                obj Action::Send:
                                                   channel:
                                                      obj Expr::LVal::Id:
                                                         id = output
                                                   message:
                                                      obj Expr::Boson::Tuple:
                                                         name = EndOfStream
                                                         fields = array:
                                                            obj Expr::LVal::Id:
                                                               id = Unit
                                                Action::Exit

        """.trimIndent()
        assertEquals(expected, m.twist().toString())
    }

    @Test
    fun testParseChannelType() {
        val input = """
            quark [T]MyQuark(i: Int) composes a::b is
                chan C : F
                var sl: Int = i
                 do
                    var ch : chan String = newchan String;
                    var chin: chan(in) String = ch;
                    send C(Flubber(23 + x, chin))
            end@quark
        """.trimIndent()
        val expected = """
            obj PicaModule:
               id = test
               defs = array:
                  obj Definition::Quark:
                     module = test
                     name = MyQuark
                     typeParams = array:
                        obj TypeVar:
                           name = T
                     valueParams = array:
                        obj param:
                           name = i
                           type:
                              obj Type::Named:
                                 id = Int
                     channels = array:
                        obj Definition::Channel:
                           name = C
                           messageType:
                              obj Type::Named:
                                 id = F
                     slots = array:
                        obj Object::Slot:
                           name = sl
                           type:
                              obj Type::Named:
                                 id = Int
                           initValue:
                              obj Expr::LVal::Id:
                                 id = i
                     actions = array:
                        obj Action::Seq:
                           actions = array:
                              obj Action::VarDef:
                                 name = ch
                                 type:
                                    obj Type::Channel:
                                       direction = Both
                                       msgType:
                                          obj Type::Named:
                                             id = String
                                 value:
                                    obj Expr::NewChannel:
                                       type:
                                          obj Type::Named:
                                             id = String
                              obj Action::Seq:
                                 actions = array:
                                    obj Action::VarDef:
                                       name = chin
                                       type:
                                          obj Type::Channel:
                                             direction = In
                                             msgType:
                                                obj Type::Named:
                                                   id = String
                                       value:
                                          obj Expr::LVal::Id:
                                             id = ch
                                    obj Action::Send:
                                       channel:
                                          obj Expr::LVal::Id:
                                             id = C
                                       message:
                                          obj Expr::Boson::Tuple:
                                             name = Flubber
                                             fields = array:
                                                obj Expr::Operator:
                                                   operator = Plus
                                                   operands = array:
                                                      obj Expr::Literal:
                                                         kind = IntLit
                                                         value = 23
                                                      obj Expr::LVal::Id:
                                                         id = x
                                                obj Expr::LVal::Id:
                                                   id = chin

        """.trimIndent()
        val m = parse(input)
        assertEquals(expected, m.twist().toString())

    }

    @Test
    fun testParseQuarkWithParAndSel() {
        val input = """
            quark [T]MyQuark(i: Int) composes a::b is
                chan C : F
                var sl: Int = i
                 do
                    (send C(Flubber(23 + x, chin)) |
                    send D(foo(false))) &
                    send E(bar(true)) &
                    (send Q(oops(27)) ; send P(woops(28)))
            end@quark
        """.trimIndent()
        val expected = """
            obj PicaModule:
               id = test
               defs = array:
                  obj Definition::Quark:
                     module = test
                     name = MyQuark
                     typeParams = array:
                        obj TypeVar:
                           name = T
                     valueParams = array:
                        obj param:
                           name = i
                           type:
                              obj Type::Named:
                                 id = Int
                     channels = array:
                        obj Definition::Channel:
                           name = C
                           messageType:
                              obj Type::Named:
                                 id = F
                     slots = array:
                        obj Object::Slot:
                           name = sl
                           type:
                              obj Type::Named:
                                 id = Int
                           initValue:
                              obj Expr::LVal::Id:
                                 id = i
                     actions = array:
                        obj Action::Par:
                           actions = array:
                              obj Action::Choice:
                                 choices = array:
                                    obj Action::Send:
                                       channel:
                                          obj Expr::LVal::Id:
                                             id = C
                                       message:
                                          obj Expr::Boson::Tuple:
                                             name = Flubber
                                             fields = array:
                                                obj Expr::Operator:
                                                   operator = Plus
                                                   operands = array:
                                                      obj Expr::Literal:
                                                         kind = IntLit
                                                         value = 23
                                                      obj Expr::LVal::Id:
                                                         id = x
                                                obj Expr::LVal::Id:
                                                   id = chin
                                    obj Action::Send:
                                       channel:
                                          obj Expr::LVal::Id:
                                             id = D
                                       message:
                                          obj Expr::Boson::Tuple:
                                             name = foo
                                             fields = array:
                                                obj Expr::LVal::Id:
                                                   id = false
                              obj Action::Par:
                                 actions = array:
                                    obj Action::Send:
                                       channel:
                                          obj Expr::LVal::Id:
                                             id = E
                                       message:
                                          obj Expr::Boson::Tuple:
                                             name = bar
                                             fields = array:
                                                obj Expr::LVal::Id:
                                                   id = true
                                    obj Action::Seq:
                                       actions = array:
                                          obj Action::Send:
                                             channel:
                                                obj Expr::LVal::Id:
                                                   id = Q
                                             message:
                                                obj Expr::Boson::Tuple:
                                                   name = oops
                                                   fields = array:
                                                      obj Expr::Literal:
                                                         kind = IntLit
                                                         value = 27
                                          obj Action::Send:
                                             channel:
                                                obj Expr::LVal::Id:
                                                   id = P
                                             message:
                                                obj Expr::Boson::Tuple:
                                                   name = woops
                                                   fields = array:
                                                      obj Expr::Literal:
                                                         kind = IntLit
                                                         value = 28

        """.trimIndent()
        val m = parse(input)
        assertEquals(expected, m.twist().toString())

    }


}