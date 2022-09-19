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
import org.goodmath.pica.parser.PicaGrammarLexer;
import org.goodmath.pica.parser.PicaGrammarParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {

    public PicaModule parser(String input) {
        var stream = CharStreams.fromString(input);
        var lexer = new PicaGrammarLexer(stream);
        var tokens = new CommonTokenStream(lexer);
        var parser = new PicaGrammarParser(tokens);
        var astBuilder = new AstBuilder("test");
        var walker = new ParseTreeWalker();
        walker.walk(astBuilder, parser.module());
        return astBuilder.getParsedModule();
    }

    @Test
    public void testParseFunction() throws IOException {
        var input = """
                fun foo(x: Int): Int do
                  if x == 0 then\s
                    return 0
                  else\s
                    return x * foo(x-1)
                  end
                end
                """;

        String expected = """
                {
                  "kind" : "Module",
                  "name" : "test",
                  "uses" : [ ],
                  "defs" : [ {
                    "kind" : "fundef",
                    "name" : "foo",
                    "params" : [ {
                      "kind" : "TypedParameter",
                      "name" : "x",
                      "type" : {
                        "kind" : "NamedType",
                        "name" : "Int"
                      }
                    } ],
                    "returnType" : {
                      "kind" : "NamedType",
                      "name" : "Int"
                    },
                    "action" : {
                      "kind" : "IfAction",
                      "condition" : {
                        "kind" : "BinaryExpr",
                        "op" : "Eq",
                        "left" : {
                          "kind" : "IdentifierLvalue",
                          "ident" : "x"
                        },
                        "right" : {
                          "kind" : "LitExpr",
                          "valueKind" : "INTLIT",
                          "value" : "0"
                        }
                      },
                      "trueBranch" : {
                        "kind" : "ReturnAction",
                        "value" : {
                          "kind" : "LitExpr",
                          "valueKind" : "INTLIT",
                          "value" : "0"
                        }
                      },
                      "falseBranch" : {
                        "kind" : "ReturnAction",
                        "value" : {
                          "kind" : "BinaryExpr",
                          "op" : "Times",
                          "left" : {
                            "kind" : "IdentifierLvalue",
                            "ident" : "x"
                          },
                          "right" : {
                            "kind" : "BosonTupleExpr",
                            "bosonOptionName" : "foo",
                            "fields" : [ {
                              "kind" : "IdentifierLvalue",
                              "ident" : "x-1"
                            } ]
                          }
                        }
                      }
                    }
                  } ]
                }""";

        var m = parser(input);
        assertEquals(expected, m.toString());
    }

    @Test
    public void testParseQuark() throws IOException {
        String input = """
                quark [T]MyQuark(i: Int)\s
                  composes a::b
                  channels
                    chan C : F
                  state
                    slot sl: Int = i
                   action
                      !C(Flubber(23 + x))
                end
                """;

        String expected = """
                {
                  "kind" : "Module",
                  "name" : "test",
                  "uses" : [ ],
                  "defs" : [ {
                    "kind" : "quarkDef",
                    "name" : "MyQuark",
                    "typeParams" : [ {
                      "kind" : "TypeParam",
                      "name" : "T"
                    } ],
                    "composes" : [ {
                      "kind" : "NamedType",
                      "name" : "a::b"
                    } ],
                    "channels" : [ {
                      "kind" : "channelDef",
                      "name" : "C",
                      "type" : {
                        "kind" : "NamedType",
                        "name" : "F"
                      }
                    } ],
                    "params" : [ {
                      "kind" : "TypedParameter",
                      "name" : "i",
                      "type" : {
                        "kind" : "NamedType",
                        "name" : "Int"
                      }
                    } ],
                    "action" : {
                      "kind" : "SendAction",
                      "id" : "C",
                      "value" : {
                        "kind" : "BosonTupleExpr",
                        "bosonOptionName" : "Flubber",
                        "fields" : [ {
                          "kind" : "BinaryExpr",
                          "op" : "Plus",
                          "left" : {
                            "kind" : "LitExpr",
                            "valueKind" : "INTLIT",
                            "value" : "23"
                          },
                          "right" : {
                            "kind" : "IdentifierLvalue",
                            "ident" : "x"
                          }
                        } ]
                      }
                    }
                  } ]
                }""";
        var m = parser(input);
        assertEquals(expected, m.toString());
    }

    @Test
    public void testParseBoson() throws IOException {

        String input = """
                use a::bb::ccc::{d,e}
                boson [T, U]Speckle is
                   One(Int, Int)
                 or Two{name: String, v: Float}
                end
                """;

        String expected = """
                {
                  "kind" : "Module",
                  "name" : "test",
                  "uses" : [ {
                    "kind" : "UseDef",
                    "module" : "a::bb::ccc",
                    "names" : [ "d", "e" ]
                  } ],
                  "defs" : [ {
                    "kind" : "BosonDef",
                    "name" : "Speckle",
                    "typeParams" : [ {
                      "kind" : "TypeParam",
                      "name" : "T"
                    }, {
                      "kind" : "TypeParam",
                      "name" : "U"
                    } ],
                    "options" : [ {
                      "kind" : "BosonTuple",
                      "name" : "One",
                      "fields" : [ {
                        "kind" : "NamedType",
                        "name" : "Int"
                      }, {
                        "kind" : "NamedType",
                        "name" : "Int"
                      } ]
                    }, {
                      "kind" : "BosonStruct",
                      "name" : "Two",
                      "fields" : [ {
                        "name" : "v",
                        "value" : {
                          "kind" : "NamedType",
                          "name" : "Float"
                        }
                      }, {
                        "name" : "name",
                        "value" : {
                          "kind" : "NamedType",
                          "name" : "String"
                        }
                      } ]
                    } ]
                  } ]
                }""";
        var m = parser(input);
        assertEquals(expected, m.toString());
    }

}
