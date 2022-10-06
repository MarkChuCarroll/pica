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


import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.goodmath.pica.errors.ErrorLog;
import org.goodmath.pica.parser.PicaGrammarLexer;
import org.goodmath.pica.parser.PicaGrammarParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests of the parser and AST generation.
 *
 * <p>This is kind-of ugly, but the best way I know to make sure that the
 * parse tree is being generated correctly is to compare the entire parse
 * tree; and java syntax being what it is, the easiest way to do that is
 * to do a pretty-print rendering of the tree, and use string comparison.</p>
 */
class ParserTest {

    public PicaModule parser(String input) {
        var stream = CharStreams.fromString(input);
        var lexer = new PicaGrammarLexer(stream);
        var tokens = new CommonTokenStream(lexer);
        var parser = new PicaGrammarParser(tokens);
        var astBuilder = new AstBuilder("test", Identifier.parseIdentifier("test"));
        var walker = new ParseTreeWalker();
        walker.walk(astBuilder, parser.module());
        return astBuilder.getParsedModule();
    }


    @Test
    public void testParseQuark()  {
        String input = """
                quark [T]MyQuark(i: Int) composes a::b is
                  chan C : F
                  var sl: Int = i
                 do
                    send C(Flubber(23 + x))
                end@quark
                """;

        String expected = """
            obj PicaModule:
               name = test
               defs = array:
                  obj Def::Quark:
                     name = MyQuark
                     typeParams = array:
                        obj TypeParamSpec:
                           name = T
                     params = array:
                        obj TypedParameter:
                           name = i
                           type:
                              obj Type::Named:
                                 name = Int
                     channels = array:
                        obj ChannelDef:
                           name = C
                           type:
                              obj Type::Named:
                                 name = F
                     slots = array:
                        obj SlotDef:
                           name = sl
                           type:
                              obj Type::Named:
                                 name = Int
                           initValue:
                              obj Lvalue::Identifier:
                                 id = i
                     action:
                        obj Action::Send:
                           channel = C
                           value:
                              obj Expr::BosonTuple:
                                 tag = Flubber
                                 fields = array:
                                    obj Expr::Binary:
                                       op = Plus
                                       obj Expr::Literal:
                                          kind = INT_LIT
                                          value = 23
                                       obj Lvalue::Identifier:
                                          id = x
                        """;
        var m = parser(input);
        assertEquals(expected, m.toString());
    }

    @Test
    public void testParseBoson()  {

        String input = """
                use a::bb::ccc::{d,e}
                boson [T, U]Speckle is
                   One(Int, Int)
                 or Two{name: String, v: Float}
                end
                """;

        String expected = """
            obj PicaModule:
               name = test
               uses = array:
                  obj Def::Use:
                     module = test
                     names = array:
                        name = d
                        name = e
               defs = array:
                  obj Boson:
                     name = Speckle
                     typeParams = array:
                        obj TypeParamSpec:
                           name = T
                        obj TypeParamSpec:
                           name = U
                     options = array:
                        obj BosonOption::Tuple:
                           name = One
                           fields = array:
                              obj Type::Named:
                                 name = Int
                              obj Type::Named:
                                 name = Int
                        obj BosonOption::Struct:
                           name = Two
                           fields = array:
                              v:
                                 obj Type::Named:
                                    name = Float
                              name:
                                 obj Type::Named:
                                    name = String
                 """;

        var m = parser(input);
        assertEquals(expected, m.toString());
    }

    @Test
    public void testBosonAndQuarkParse() {
      String input = """
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
                        Unit
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
      """;
      var m = parser(input);
      String expected = """
        obj PicaModule:
           name = test
           defs = array:
              obj Boson:
                 name = ScannerOutput
                 options = array:
                    obj BosonOption::Struct:
                       name = Token
                       fields = array:
                          line:
                             obj Type::Named:
                                name = Int
                          type:
                             obj Type::Named:
                                name = String
                          content:
                             obj Type::Named:
                                name = String
                    obj BosonOption::Tuple:
                       name = EndOfStream
                       fields = array:
                          obj Type::Named:
                             name = Unit
                    obj BosonOption::Struct:
                       name = ScanError
                       fields = array:
                          line:
                             obj Type::Named:
                                name = Int
                          message:
                             obj Type::Named:
                                name = String
              obj Def::Quark:
                 name = Scanner
                 params = array:
                    obj TypedParameter:
                       name = inp
                       type:
                          obj Type::Named:
                             name = InputStream
                 channels = array:
                    obj ChannelDef:
                       name = output
                       type:
                          obj Type::Named:
                             name = ScannerOutput
                 slots = array:
                    obj SlotDef:
                       name = input
                       type:
                          obj Type::Named:
                             name = InputStream
                       initValue:
                          obj Lvalue::Identifier:
                             id = inp
                    obj SlotDef:
                       name = currentToken
                       type:
                          obj Type::Named:
                             name = String
                       initValue:
                          obj Expr::Literal:
                             kind = STRING_LIT
                             value = ""
                 action:
                    obj Action::Loop:
                       obj Action::Receive:
                          channel = input
                          actions = array:
                             obj BosonMessagePatternAction:
                                pattern:
                                   obj BosonTuplePattern:
                                      name = More
                                      fields = array:
                                         boundVarName = c
                                action:
                                   obj Action::Sequence:
                                      actions = array:
                                         obj Action::Assignment:
                                            target:
                                               obj Lvalue::Identifier:
                                                  id = currentToken
                                            value:
                                               obj Expr::Binary:
                                                  op = Plus
                                                  obj Lvalue::Identifier:
                                                     id = currentToken
                                                  obj Lvalue::Identifier:
                                                     id = c
                                         obj Action::If:
                                            cond:
                                               obj Expr::Binary:
                                                  op = Greater
                                                  obj Lvalue::Identifier:
                                                     id = something
                                                  obj Lvalue::Identifier:
                                                     id = other
                                            true:
                                               obj Action::Sequence:
                                                  actions = array:
                                                     obj Action::Send:
                                                        channel = output
                                                        value:
                                                           obj BosonStructExpr:
                                                              id = Token
                                                              fields = array:
                                                                 line:
                                                                    obj Expr::Literal:
                                                                       kind = INT_LIT
                                                                       value = 23
                                                                 type:
                                                                    obj Expr::Literal:
                                                                       kind = STRING_LIT
                                                                       value = "t"
                                                                 content:
                                                                    obj Lvalue::Identifier:
                                                                       id = currentToken
                                                     obj Action::Assignment:
                                                        target:
                                                           obj Lvalue::Identifier:
                                                              id = currentToken
                                                        value:
                                                           obj Expr::Literal:
                                                              kind = STRING_LIT
                                                              value = ""
                                            false:
                                               obj Action::Expr:
                                                  obj Lvalue::Identifier:
                                                     id = Unit
                             obj BosonMessagePatternAction:
                                pattern:
                                   obj BosonTuplePattern:
                                      name = End
                                      fields = array:
                                         boundVarName = u
                                action:
                                   obj Action::Sequence:
                                      actions = array:
                                         obj Action::Send:
                                            channel = output
                                            value:
                                               obj BosonStructExpr:
                                                  id = Token
                                                  fields = array:
                                                     line:
                                                        obj Expr::Literal:
                                                           kind = INT_LIT
                                                           value = 18
                                                     type:
                                                        obj Expr::Literal:
                                                           kind = STRING_LIT
                                                           value = "t"
                                                     content:
                                                        obj Lvalue::Identifier:
                                                           id = currentToken
                                         obj Action::Sequence:
                                            actions = array:
                                               obj Action::Send:
                                                  channel = output
                                                  value:
                                                     obj Expr::BosonTuple:
                                                        tag = EndOfStream
                                                        fields = array:
                                                           obj Lvalue::Identifier:
                                                              id = Unit
                                               obj Action::Exit:
        """;

      assertEquals(expected, m.toString());
    }

}
