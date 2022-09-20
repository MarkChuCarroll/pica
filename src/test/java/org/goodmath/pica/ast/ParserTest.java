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
                    return x * foo(x - 1)
                  end
                end
                """;

        String expected = """
         PicaModule{
            test
            uses
            defs{
               Def::Function{
                  foo
                  Params{
                     TypedParameter{
                        x
                        Type::Named{
                           Identifier::Simple{
                              Int
                           }
                        }
                     }
                  }
                  Type::Named{
                     Identifier::Simple{
                        Int
                     }
                  }
                  Action::If{
                     Expr::Binary{
                        Eq
                        Lvalue::Identifier{
                           Identifier::Simple{
                              x
                           }
                        }
                        Expr::Literal::INTLIT{
                           0
                        }
                     }
                     Action::Return{
                        Expr::Literal::INTLIT{
                           0
                        }
                     }
                     Action::Return{
                        Expr::Binary{
                           Times
                           Lvalue::Identifier{
                              Identifier::Simple{
                                 x
                              }
                           }
                           Expr::BosonTuple{
                              Identifier::Simple{
                                 foo
                              }
                              fields{
                                 Expr::Binary{
                                    Minus
                                    Lvalue::Identifier{
                                       Identifier::Simple{
                                          x
                                       }
                                    }
                                    Expr::Literal::INTLIT{
                                       1
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
         PicaModule{
            test
            uses
            defs{
               Def::Quark{
                  typeParams{
                     TypedParamSpec{
                        T
                     }
                  }
                  params{
                     TypedParameter{
                        i
                        Type::Named{
                           Identifier::Simple{
                              Int
                           }
                        }
                     }
                  }
                  channels{
                     ChannelDef{
                        C
                        Type::Named{
                           Identifier::Simple{
                              F
                           }
                        }
                     }
                  }
                  slots{
                     SlotDef{
                        sl
                        Type::Named{
                           Identifier::Simple{
                              Int
                           }
                        }
                        Lvalue::Identifier{
                           Identifier::Simple{
                              i
                           }
                        }
                     }
                  }
                  Action::Send{
                     Identifier::Simple{
                        C
                     }
                     Expr::BosonTuple{
                        Identifier::Simple{
                           Flubber
                        }
                        fields{
                           Expr::Binary{
                              Plus
                              Expr::Literal::INTLIT{
                                 23
                              }
                              Lvalue::Identifier{
                                 Identifier::Simple{
                                    x
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
    public void testParseBoson() throws IOException {

        String input = """
                use a::bb::ccc::{d,e}
                boson [T, U]Speckle is
                   One(Int, Int)
                 or Two{name: String, v: Float}
                end
                """;

        String expected = """
         PicaModule{
            test
            uses{
               Def::Use{
                  Identifier::Scoped{
                     Identifier::Scoped{
                        Identifier::Simple{
                           a
                        }
                        bb
                     }
                     ccc
                  }
                  names{
                     d
                     e
                  }
               }
            }
            defs{
               Def::BosonDef{
                  Speckle
                  typeParams{
                     TypedParamSpec{
                        T
                     }
                     TypedParamSpec{
                        U
                     }
                  }
                  options{
                     BosonOption::Tuple{
                        One
                        fields{
                           Type::Named{
                              Identifier::Simple{
                                 Int
                              }
                           }
                           Type::Named{
                              Identifier::Simple{
                                 Int
                              }
                           }
                        }
                     }
                     BosonOption::Struct{
                        Two
                        fields{
                           field{
                              v
                              Type::Named{
                                 Identifier::Simple{
                                    Float
                                 }
                              }
                           }
                           field{
                              name
                              Type::Named{
                                 Identifier::Simple{
                                    String
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
    public void testAnother() {
      String input = """
        boson ScannerOutput is
          Token{type: String, content: String, line: Int}
        or  EndOfStream(Unit)
        or  ScanError{message: String, line: Int}
        end

        quark Scanner(inp: InputStream)
          channels
            chan output: ScannerOutput
          state
            slot input: InputStream = inp
            slot currentToken: String = ""
        action
          repeat
            ? input do
                on More(c) do
                    currentToken = currentToken + c;
                    if  something > other then
                        !output(Token{type: "t", content: currentToken, line: 23});
                        currentToken = ""
                    else
                        Unit
                    end
                end
                on End(u) do
                    !output(Token{type: "t", content: currentToken, line: 18});
                    !output(EndOfStream(Unit));
                    exit
                end
            end
          end
        end
      """;
      var m = parser(input);
      String expected = """
       PicaModule{
          test
          uses
          defs{
             Def::BosonDef{
                ScannerOutput
                options{
                   BosonOption::Struct{
                      Token
                      fields{
                         field{
                            line
                            Type::Named{
                               Identifier::Simple{
                                  Int
                               }
                            }
                         }
                         field{
                            type
                            Type::Named{
                               Identifier::Simple{
                                  String
                               }
                            }
                         }
                         field{
                            content
                            Type::Named{
                               Identifier::Simple{
                                  String
                               }
                            }
                         }
                      }
                   }
                   BosonOption::Tuple{
                      EndOfStream
                      fields{
                         Type::Named{
                            Identifier::Simple{
                               Unit
                            }
                         }
                      }
                   }
                   BosonOption::Struct{
                      ScanError
                      fields{
                         field{
                            line
                            Type::Named{
                               Identifier::Simple{
                                  Int
                               }
                            }
                         }
                         field{
                            message
                            Type::Named{
                               Identifier::Simple{
                                  String
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
                      inp
                      Type::Named{
                         Identifier::Simple{
                            InputStream
                         }
                      }
                   }
                }
                channels{
                   ChannelDef{
                      output
                      Type::Named{
                         Identifier::Simple{
                            ScannerOutput
                         }
                      }
                   }
                }
                slots{
                   SlotDef{
                      input
                      Type::Named{
                         Identifier::Simple{
                            InputStream
                         }
                      }
                      Lvalue::Identifier{
                         Identifier::Simple{
                            inp
                         }
                      }
                   }
                   SlotDef{
                      currentToken
                      Type::Named{
                         Identifier::Simple{
                            String
                         }
                      }
                      Expr::Literal::STRLIT{
                         ""
                      }
                   }
                }
                Action::Loop{
                   Action::Receive{
                      input
                      patternActions{
                         BosonMessagePatternAction{
                            BosonTuplePattern{
                               More
                               fields{
                                  c
                               }
                            }
                            Action::Sequence{
                               Action/Assignment{
                                  Lvalue::Identifier{
                                     Identifier::Simple{
                                        currentToken
                                     }
                                  }
                                  Expr::Binary{
                                     Plus
                                     Lvalue::Identifier{
                                        Identifier::Simple{
                                           currentToken
                                        }
                                     }
                                     Lvalue::Identifier{
                                        Identifier::Simple{
                                           c
                                        }
                                     }
                                  }
                               }
                               Action::If{
                                  Expr::Binary{
                                     Greater
                                     Lvalue::Identifier{
                                        Identifier::Simple{
                                           something
                                        }
                                     }
                                     Lvalue::Identifier{
                                        Identifier::Simple{
                                           other
                                        }
                                     }
                                  }
                                  Action::Sequence{
                                     Action::Send{
                                        Identifier::Simple{
                                           output
                                        }
                                        BosonStructExpr{
                                           Identifier::Simple{
                                              Token
                                           }
                                           fields{
                                              field{
                                                 line
                                                 Expr::Literal::INTLIT{
                                                    23
                                                 }
                                              }
                                              field{
                                                 type
                                                 Expr::Literal::STRLIT{
                                                    "t"
                                                 }
                                              }
                                              field{
                                                 content
                                                 Lvalue::Identifier{
                                                    Identifier::Simple{
                                                       currentToken
                                                    }
                                                 }
                                              }
                                           }
                                        }
                                     }
                                     Action/Assignment{
                                        Lvalue::Identifier{
                                           Identifier::Simple{
                                              currentToken
                                           }
                                        }
                                        Expr::Literal::STRLIT{
                                           ""
                                        }
                                     }
                                  }
                                  Action::Expr{
                                     Lvalue::Identifier{
                                        Identifier::Simple{
                                           Unit
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
          }
       }
       """;

      assertEquals(expected, m.toString());
    }

}
