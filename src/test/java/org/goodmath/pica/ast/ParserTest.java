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
        var astBuilder = new AstBuilder("test");
        var walker = new ParseTreeWalker();
        walker.walk(astBuilder, parser.module());
        return astBuilder.getParsedModule();
    }

    @Test
    public void testParseFunction()  {
        var input = """
                fun foo(x: Int): Int do
                  if x == 0 then
                    return 0
                  else
                    return x * foo(x - 1)
                  end@if
                end@fun
                """;

        String expected = """
         PicaModule{
            name=test
            uses
               <<none>>
            defs{
               Def::Function{
                  name=foo
                  Params{
                     TypedParameter{
                        name=x
                        Type::Named{
                           Identifier::Simple{
                              [Int]
                           }
                        }
                     }
                  }
                  Type::Named{
                     Identifier::Simple{
                        [Int]
                     }
                  }
                  Action::If{
                     Expr::Binary{
                        [Eq]
                        Lvalue::Identifier{
                           Identifier::Simple{
                              [x]
                           }
                        }
                        Expr::Literal::INT_LIT{
                           [0]
                        }
                     }
                     Action::Return{
                        Expr::Literal::INT_LIT{
                           [0]
                        }
                     }
                     Action::Return{
                        Expr::Binary{
                           [Times]
                           Lvalue::Identifier{
                              Identifier::Simple{
                                 [x]
                              }
                           }
                           Expr::BosonTuple{
                              Identifier::Simple{
                                 [foo]
                              }
                              fields{
                                 Expr::Binary{
                                    [Minus]
                                    Lvalue::Identifier{
                                       Identifier::Simple{
                                          [x]
                                       }
                                    }
                                    Expr::Literal::INT_LIT{
                                       [1]
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
         """;

        var m = parser(input);
        assertEquals(expected, m.toString());
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
        PicaModule{
           name=test
           uses
              <<none>>
           defs{
              Def::Quark{
                 typeParams{
                    TypedParamSpec{
                       [T]
                    }
                 }
                 params{
                    TypedParameter{
                       name=i
                       Type::Named{
                          Identifier::Simple{
                             [Int]
                          }
                       }
                    }
                 }
                 channels{
                    ChannelDef{
                       name=C
                       Type::Named{
                          Identifier::Simple{
                             [F]
                          }
                       }
                    }
                 }
                 slots{
                    SlotDef{
                       name=sl
                       Type::Named{
                          Identifier::Simple{
                             [Int]
                          }
                       }
                       Lvalue::Identifier{
                          Identifier::Simple{
                             [i]
                          }
                       }
                    }
                 }
                 Action::Send{
                    Identifier::Simple{
                       [C]
                    }
                    Expr::BosonTuple{
                       Identifier::Simple{
                          [Flubber]
                       }
                       fields{
                          Expr::Binary{
                             [Plus]
                             Expr::Literal::INT_LIT{
                                [23]
                             }
                             Lvalue::Identifier{
                                Identifier::Simple{
                                   [x]
                                }
                             }
                          }
                       }
                    }
                 }
              }
           }
        }
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
        PicaModule{
           name=test
           uses{
              Def::Use{
                 Identifier::Scoped{
                    Identifier::Scoped{
                       Identifier::Simple{
                          [a]
                       }
                       [bb]
                    }
                    [ccc]
                 }
                 names{
                    [d]
                    [e]
                 }
              }
           }
           defs{
              Def::BosonDef{
                 [Speckle]
                 typeParams{
                    TypedParamSpec{
                       [T]
                    }
                    TypedParamSpec{
                       [U]
                    }
                 }
                 options{
                    BosonOption::Tuple{
                       optionName=One
                       fields{
                          Type::Named{
                             Identifier::Simple{
                                [Int]
                             }
                          }
                          Type::Named{
                             Identifier::Simple{
                                [Int]
                             }
                          }
                       }
                    }
                    BosonOption::Struct{
                       optionName=Two
                       fields{
                          field{
                             name=v
                             Type::Named{
                                Identifier::Simple{
                                   [Float]
                                }
                             }
                          }
                          field{
                             name=name
                             Type::Named{
                                Identifier::Simple{
                                   [String]
                                }
                             }
                          }
                       }
                    }
                 }
              }
           }
        }
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
        PicaModule{
           name=test
           uses
              <<none>>
           defs{
              Def::BosonDef{
                 [ScannerOutput]
                 options{
                    BosonOption::Struct{
                       optionName=Token
                       fields{
                          field{
                             name=line
                             Type::Named{
                                Identifier::Simple{
                                   [Int]
                                }
                             }
                          }
                          field{
                             name=type
                             Type::Named{
                                Identifier::Simple{
                                   [String]
                                }
                             }
                          }
                          field{
                             name=content
                             Type::Named{
                                Identifier::Simple{
                                   [String]
                                }
                             }
                          }
                       }
                    }
                    BosonOption::Tuple{
                       optionName=EndOfStream
                       fields{
                          Type::Named{
                             Identifier::Simple{
                                [Unit]
                             }
                          }
                       }
                    }
                    BosonOption::Struct{
                       optionName=ScanError
                       fields{
                          field{
                             name=line
                             Type::Named{
                                Identifier::Simple{
                                   [Int]
                                }
                             }
                          }
                          field{
                             name=message
                             Type::Named{
                                Identifier::Simple{
                                   [String]
                                }
                             }
                          }
                       }
                    }
                 }
              }
              Def::Quark{
                 params{
                    TypedParameter{
                       name=inp
                       Type::Named{
                          Identifier::Simple{
                             [InputStream]
                          }
                       }
                    }
                 }
                 channels{
                    ChannelDef{
                       name=output
                       Type::Named{
                          Identifier::Simple{
                             [ScannerOutput]
                          }
                       }
                    }
                 }
                 slots{
                    SlotDef{
                       name=input
                       Type::Named{
                          Identifier::Simple{
                             [InputStream]
                          }
                       }
                       Lvalue::Identifier{
                          Identifier::Simple{
                             [inp]
                          }
                       }
                    }
                    SlotDef{
                       name=currentToken
                       Type::Named{
                          Identifier::Simple{
                             [String]
                          }
                       }
                       Expr::Literal::STRING_LIT{
                          [""]
                       }
                    }
                 }
                 Action::Loop{
                    Action::Receive{
                       channel=input
                       patternActions{
                          BosonMessagePatternAction{
                             BosonTuplePattern{
                                name=More
                                fields{
                                   [c]
                                }
                             }
                             Action::Sequence{
                                Action::Assignment{
                                   Lvalue::Identifier{
                                      Identifier::Simple{
                                         [currentToken]
                                      }
                                   }
                                   Expr::Binary{
                                      [Plus]
                                      Lvalue::Identifier{
                                         Identifier::Simple{
                                            [currentToken]
                                         }
                                      }
                                      Lvalue::Identifier{
                                         Identifier::Simple{
                                            [c]
                                         }
                                      }
                                   }
                                }
                                Action::If{
                                   Expr::Binary{
                                      [Greater]
                                      Lvalue::Identifier{
                                         Identifier::Simple{
                                            [something]
                                         }
                                      }
                                      Lvalue::Identifier{
                                         Identifier::Simple{
                                            [other]
                                         }
                                      }
                                   }
                                   Action::Sequence{
                                      Action::Send{
                                         Identifier::Simple{
                                            [output]
                                         }
                                         BosonStructExpr{
                                            Identifier::Simple{
                                               [Token]
                                            }
                                            fields{
                                               field{
                                                  name=line
                                                  Expr::Literal::INT_LIT{
                                                     [23]
                                                  }
                                               }
                                               field{
                                                  name=type
                                                  Expr::Literal::STRING_LIT{
                                                     ["t"]
                                                  }
                                               }
                                               field{
                                                  name=content
                                                  Lvalue::Identifier{
                                                     Identifier::Simple{
                                                        [currentToken]
                                                     }
                                                  }
                                               }
                                            }
                                         }
                                      }
                                      Action::Assignment{
                                         Lvalue::Identifier{
                                            Identifier::Simple{
                                               [currentToken]
                                            }
                                         }
                                         Expr::Literal::STRING_LIT{
                                            [""]
                                         }
                                      }
                                   }
                                   Action::Expr{
                                      Lvalue::Identifier{
                                         Identifier::Simple{
                                            [Unit]
                                         }
                                      }
                                   }
                                }
                             }
                          }
                          BosonMessagePatternAction{
                             BosonTuplePattern{
                                name=End
                                fields{
                                   [u]
                                }
                             }
                             Action::Sequence{
                                Action::Send{
                                   Identifier::Simple{
                                      [output]
                                   }
                                   BosonStructExpr{
                                      Identifier::Simple{
                                         [Token]
                                      }
                                      fields{
                                         field{
                                            name=line
                                            Expr::Literal::INT_LIT{
                                               [18]
                                            }
                                         }
                                         field{
                                            name=type
                                            Expr::Literal::STRING_LIT{
                                               ["t"]
                                            }
                                         }
                                         field{
                                            name=content
                                            Lvalue::Identifier{
                                               Identifier::Simple{
                                                  [currentToken]
                                               }
                                            }
                                         }
                                      }
                                   }
                                }
                                Action::Sequence{
                                   Action::Send{
                                      Identifier::Simple{
                                         [output]
                                      }
                                      Expr::BosonTuple{
                                         Identifier::Simple{
                                            [EndOfStream]
                                         }
                                         fields{
                                            Lvalue::Identifier{
                                               Identifier::Simple{
                                                  [Unit]
                                               }
                                            }
                                         }
                                      }
                                   }
                                   [Action::Exit]
                                }
                             }
                          }
                       }
                    }
                 }
              }
           }
        }
        """;

      assertEquals(expected, m.toString());
    }

}
