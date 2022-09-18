package org.goodmath.pica.ast;


import lombok.val;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.goodmath.pica.parser.PicaGrammarLexer;
import org.goodmath.pica.parser.PicaGrammarParser;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {

    /*
    @Test
    fun parseFunDecl() {
        val input = "\nfun myfun ( Int, Int -- Float ) {\ndup / root }\n"
        val parser = Parser(Identifier.SimpleIdentifier("test", Unlocated), input)
        val r: AstNode = parser.parseDeclaration()
        // This is annoying long-winded, but the best way to test parsing, and
        // be able to see the difference when something goes wrong, is to just
        // render out the full structure as json, and use text-diff tools.
        val expected = obj {
            "type" to "FunctionDefinition"
            "name" to "myfun"
            "typeParams" to arr
            "stackEffect" to obj {
                "type" to "FunctionType"
                "inputs" to obj {
                    "type" to "StackImage"
                    "baseImage" to null
                    "values" to arr[
                            obj {
                                "type" to "DeclaredType"
                                "baseType" to obj {
                                    "type" to "NamedType"
                                    "name" to obj {
                                        "type" to "SimpleIdent"
                                        "name" to "Int"
                                    }
                                }
                                "typeParams" to arr
                            },
                            obj {
                                "type" to "DeclaredType"
                                "baseType" to obj {
                                    "type" to "NamedType"
                                    "name" to obj {
                                        "type" to "SimpleIdent"
                                        "name" to "Int"
                                    }
                                }
                                "typeParams" to arr
                            }
                    ]
                }
                "outputs" to obj {
                    "type" to "StackImage"
                    "baseImage" to null
                    "values" to arr[
                            obj {
                                "type" to "DeclaredType"
                                "baseType" to obj {
                                    "type" to "NamedType"
                                    "name" to obj {
                                        "type" to "SimpleIdent"
                                        "name" to "Float"
                                    }
                                }
                                "typeParams" to arr
                            }
                    ]
                }
            }
            "body" to obj {
                "type" to "BlockExpression"
                "body" to arr[
                        obj {
                            "type" to "IdentifierExpression"
                            "id" to obj {
                                "type" to "SimpleIdent"
                                "name" to "dup"
                            }
                        },
                        obj {
                            "type" to "IdentifierExpression"
                            "id" to obj {
                                "type" to "SimpleIdent"
                                "name" to "/"
                            }
                        },
                        obj {
                            "type" to "IdentifierExpression"
                            "id" to obj {
                                "type" to "SimpleIdent"
                                "name" to "root"
                            }
                        }
                ]
            }
        }

        assertEquals(
            expected.pretty(),
            r.json().pretty()
        )

    }

    @Test
    fun testMoreComplicatedParse() {
        val input = """
        fun [T is something::foo::bar, U]myfun ( Int, T -- Float ) {
          dup / x::root
          { block stuff "string" } do
        }
        """
        val parser = Parser(Identifier.SimpleIdentifier("test", Unlocated), input)
        val r: AstNode = parser.parseDeclaration()
        val expected = obj {
            "type" to "FunctionDefinition"
            "name" to "myfun"
            "typeParams" to arr[
                    obj {
                        "type" to "TypeVar"
                        "name" to "T"
                        "constraint" to obj {
                            "type" to "DeclaredType"
                            "baseType" to obj {
                                "type" to "NamedType"
                                "name" to obj {
                                    "type" to "QualifiedIdent"
                                    "module" to obj {
                                        "type" to "QualifiedIdent"
                                        "module" to obj {
                                            "type" to "SimpleIdent"
                                            "name" to "something"
                                        }
                                        "name" to "foo"
                                    }
                                    "name" to "bar"
                                }
                            }
                            "typeParams" to arr
                        }
                    },
                    obj {
                        "type" to "TypeVar"
                        "name" to "U"
                        "constraint" to obj {
                        }
                    }
            ]
            "stackEffect" to obj {
                "type" to "FunctionType"
                "inputs" to obj {
                    "type" to "StackImage"
                    "baseImage" to null
                    "values" to arr[
                            obj {
                                "type" to "DeclaredType"
                                "baseType" to obj {
                                    "type" to "NamedType"
                                    "name" to obj {
                                        "type" to "SimpleIdent"
                                        "name" to "Int"
                                    }
                                }
                                "typeParams" to arr
                            },
                            obj {
                                "type" to "DeclaredType"
                                "baseType" to obj {
                                    "type" to "NamedType"
                                    "name" to obj {
                                        "type" to "SimpleIdent"
                                        "name" to "T"
                                    }
                                }
                                "typeParams" to arr
                            }
                    ]
                }
                "outputs" to obj {
                    "type" to "StackImage"
                    "baseImage" to null
                    "values" to arr[
                            obj {
                                "type" to "DeclaredType"
                                "baseType" to obj {
                                    "type" to "NamedType"
                                    "name" to obj {
                                        "type" to "SimpleIdent"
                                        "name" to "Float"
                                    }
                                }
                                "typeParams" to arr
                            }
                    ]
                }
            }
            "body" to obj {
                "type" to "BlockExpression"
                "body" to arr[
                        obj {
                            "type" to "IdentifierExpression"
                            "id" to obj {
                                "type" to "SimpleIdent"
                                "name" to "dup"
                            }
                        },
                        obj {
                            "type" to "IdentifierExpression"
                            "id" to obj {
                                "type" to "SimpleIdent"
                                "name" to "/"
                            }
                        },
                        obj {
                            "type" to "IdentifierExpression"
                            "id" to obj {
                                "type" to "QualifiedIdent"
                                "module" to obj {
                                    "type" to "SimpleIdent"
                                    "name" to "x"
                                }
                                "name" to "root"
                            }
                        },
                        obj {
                            "type" to "BlockExpression"
                            "body" to arr[
                                    obj {
                                        "type" to "IdentifierExpression"
                                        "id" to obj {
                                            "type" to "SimpleIdent"
                                            "name" to "block"
                                        }
                                    },
                                    obj {
                                        "type" to "IdentifierExpression"
                                        "id" to obj {
                                            "type" to "SimpleIdent"
                                            "name" to "stuff"
                                        }
                                    },
                                    obj {
                                        "type" to "LiteralExpression"
                                        "kind" to "StringLit"
                                        "value" to "\"string\""
                                    }
                            ]
                        },
                        obj {
                            "type" to "IdentifierExpression"
                            "id" to obj {
                                "type" to "SimpleIdent"
                                "name" to "do"
                            }
                        }
                ]
            }
        }
        assertEquals(expected.pretty(), r.json().pretty())
    }

    @Test
    fun testParseProtoDecl() {
        val input = """
        proto [T is hip::chip]SomethingSomething composes a::b, b::c {
            me ( T, Int -- Int, T)
            you ( -- T )
        }
        """
        val parser = Parser(Identifier.SimpleIdentifier("test", Unlocated), input)
        val r: Definition.ProtocolDefinition = parser.parseDeclaration() as Definition.ProtocolDefinition

        val expected =
            obj {
                "type" to "ProtoDefinition"
                "typeParams" to arr[
                        obj {
                            "type" to "TypeVar"
                            "name" to "T"
                            "constraint" to obj {
                                "type" to "DeclaredType"
                                "baseType" to obj {
                                    "type" to "NamedType"
                                    "name" to obj {
                                        "type" to "QualifiedIdent"
                                        "module" to obj {
                                            "type" to "SimpleIdent"
                                            "name" to "hip"
                                        }
                                        "name" to "chip"
                                    }
                                }
                                "typeParams" to arr
                            }
                        }
                ]
                "composes" to arr[
                        obj {
                            "type" to "DeclaredType"
                            "baseType" to obj {
                                "type" to "NamedType"
                                "name" to obj {
                                    "type" to "QualifiedIdent"
                                    "module" to obj {
                                        "type" to "SimpleIdent"
                                        "name" to "a"
                                    }
                                    "name" to "b"
                                }
                            }
                            "typeParams" to arr
                        },
                        obj {
                            "type" to "DeclaredType"
                            "baseType" to obj {
                                "type" to "NamedType"
                                "name" to obj {
                                    "type" to "QualifiedIdent"
                                    "module" to obj {
                                        "type" to "SimpleIdent"
                                        "name" to "b"
                                    }
                                    "name" to "c"
                                }
                            }
                            "typeParams" to arr
                        }
                ]
                "members" to arr[
                        obj {
                            "type" to "ProtoMethod"
                            "name" to "me"
                            "stackEffect" to obj {
                                "type" to "FunctionType"
                                "inputs" to obj {
                                    "type" to "StackImage"
                                    "baseImage" to null
                                    "values" to arr[
                                            obj {
                                                "type" to "DeclaredType"
                                                "baseType" to obj {
                                                    "type" to "NamedType"
                                                    "name" to obj {
                                                        "type" to "SimpleIdent"
                                                        "name" to "T"
                                                    }
                                                }
                                                "typeParams" to arr
                                            },
                                            obj {
                                                "type" to "DeclaredType"
                                                "baseType" to obj {
                                                    "type" to "NamedType"
                                                    "name" to obj {
                                                        "type" to "SimpleIdent"
                                                        "name" to "Int"
                                                    }
                                                }
                                                "typeParams" to arr
                                            }
                                    ]
                                }
                                "outputs" to obj {
                                    "type" to "StackImage"
                                    "baseImage" to null
                                    "values" to arr[
                                            obj {
                                                "type" to "DeclaredType"
                                                "baseType" to obj {
                                                    "type" to "NamedType"
                                                    "name" to obj {
                                                        "type" to "SimpleIdent"
                                                        "name" to "Int"
                                                    }
                                                }
                                                "typeParams" to arr
                                            },
                                            obj {
                                                "type" to "DeclaredType"
                                                "baseType" to obj {
                                                    "type" to "NamedType"
                                                    "name" to obj {
                                                        "type" to "SimpleIdent"
                                                        "name" to "T"
                                                    }
                                                }
                                                "typeParams" to arr
                                            }
                                    ]
                                }
                            }
                        },
                        obj {
                            "type" to "ProtoMethod"
                            "name" to "you"
                            "stackEffect" to obj {
                                "type" to "FunctionType"
                                "inputs" to obj {
                                    "type" to "StackImage"
                                    "baseImage" to null
                                    "values" to arr
                                }
                                "outputs" to obj {
                                    "type" to "StackImage"
                                    "baseImage" to null
                                    "values" to arr[
                                            obj {
                                                "type" to "DeclaredType"
                                                "baseType" to obj {
                                                    "type" to "NamedType"
                                                    "name" to obj {
                                                        "type" to "SimpleIdent"
                                                        "name" to "T"
                                                    }
                                                }
                                                "typeParams" to arr
                                            }
                                    ]
                                }
                            }
                        }
                ]
            }

        assertEquals(expected.pretty(), r.json().pretty())
        assertEquals(
            listOf(
                Pair("SomethingSomething", DefKind.ProtoDef),
                Pair("me", DefKind.ProtoMethodDef),
                Pair("you", DefKind.ProtoMethodDef)),
            r.getDefinedNames().map { Pair(it.name, it.kind) })
    }

    @Test
    fun testParseVarDecl() {
        val input = """
        var myvar: [String]List {
           #[ "a", "b", "c" ]
        }
        """.trimIndent()
        val parser = Parser(Identifier.SimpleIdentifier("test", Unlocated), input)
        val r: Definition = parser.parseDeclaration()

        val expected = obj {
            "type" to "VariableDefinition"
            "name" to "myvar"
            "varType" to obj {
                "type" to "DeclaredType"
                "baseType" to obj {
                    "type" to "NamedType"
                    "name" to obj {
                        "type" to "SimpleIdent"
                        "name" to "List"
                    }
                }
                "typeParams" to arr[
                        obj {
                            "type" to "DeclaredType"
                            "baseType" to obj {
                                "type" to "NamedType"
                                "name" to obj {
                            "type" to "SimpleIdent"
                                    "name" to "String"
                                }
                            }
                            "typeParams" to arr
                        }
                ]
            }
            "initExpr" to obj {
                "type" to "BlockExpression"
                "body" to arr[
                        obj {
                            "type" to "ListExpression"
                            "members" to arr[
                                   obj {
                                       "type" to "LiteralExpression"
                                       "kind" to "StringLit"
                                       "value" to "\"a\""
                                   },
                                    obj {
                                        "type" to "LiteralExpression"
                                        "kind" to "StringLit"
                                        "value" to "\"b\""
                                    },
                                    obj {
                                        "type" to "LiteralExpression"
                                        "kind" to "StringLit"
                                        "value" to "\"c\""
                                    }
                            ]
                        }
                ]
            }
        }
        assertEquals(expected.pretty(), r.json().pretty())
        assertEquals(listOf(
            Pair("myvar", DefKind.GetterDef),
            Pair("myvar!", DefKind.SetterDef)
        ), r.getDefinedNames().map { Pair(it.name, it.kind)})

    }


    @Test
    fun testParseObjectDecl() {
        val input = """
        object [T]MyNewType composes ThatOtherType implements A, B {
          method me ( T, Int -- Int, T) {
             foo bar baz
          }
          slot x: Int
          method them ( T -- ) {
             eat
          }
        }
        """
        val parser = Parser(Identifier.SimpleIdentifier("test", Unlocated), input)
        val r: Definition.ObjectDefinition = parser.parseDeclaration() as Definition.ObjectDefinition

        val expected = obj {
            "type" to "ObjectDefinition"
            "typeParams" to arr [
                    obj {
                        "type" to "TypeVar"
                        "name" to "T"
                        "constraint" to obj {
                        }
                    }
            ]
            "composes" to arr [
                    obj {
                        "type" to "DeclaredType"
                        "baseType" to obj {
                            "type" to "NamedType"
                            "name" to obj {
                                "type" to "SimpleIdent"
                                "name" to "ThatOtherType"
                            }
                        }
                        "typeParams" to arr
                    }
            ]
            "implements" to arr [
                    obj {
                        "type" to "DeclaredType"
                        "baseType" to obj {
                            "type" to "NamedType"
                            "name" to obj {
                                "type" to "SimpleIdent"
                                "name" to "A"
                            }
                        }
                        "typeParams" to arr
                    },
                    obj {
                        "type" to "DeclaredType"
                        "baseType" to obj {
                            "type" to "NamedType"
                            "name" to obj {
                                "type" to "SimpleIdent"
                                "name" to "B"
                            }
                        }
                        "typeParams" to arr
                    }
            ]
            "members" to arr [
                    obj {
                        "type" to "ObjectMethod"
                        "id" to obj {
                            "type" to "SimpleIdent"
                            "name" to "me"
                        }
                        "stackEffect" to obj {
                            "type" to "FunctionType"
                            "inputs" to obj {
                                "type" to "StackImage"
                                "baseImage" to null
                                "values" to arr [
                                        obj {
                                            "type" to "DeclaredType"
                                            "baseType" to obj {
                                                "type" to "NamedType"
                                                "name" to obj {
                                                    "type" to "SimpleIdent"
                                                    "name" to "T"
                                                }
                                            }
                                            "typeParams" to arr
                                        },
                                        obj {
                                            "type" to "DeclaredType"
                                            "baseType" to obj {
                                                "type" to "NamedType"
                                                "name" to obj {
                                                    "type" to "SimpleIdent"
                                                    "name" to "Int"
                                                }
                                            }
                                            "typeParams" to arr
                                        }
                                ]
                            }
                            "outputs" to obj {
                                "type" to "StackImage"
                                "baseImage" to null
                                "values" to arr [
                                        obj {
                                            "type" to "DeclaredType"
                                            "baseType" to obj {
                                                "type" to "NamedType"
                                                "name" to obj {
                                                    "type" to "SimpleIdent"
                                                    "name" to "Int"
                                                }
                                            }
                                            "typeParams" to arr
                                        },
                                        obj {
                                            "type" to "DeclaredType"
                                            "baseType" to obj {
                                                "type" to "NamedType"
                                                "name" to obj {
                                                    "type" to "SimpleIdent"
                                                    "name" to "T"
                                                }
                                            }
                                            "typeParams" to arr
                                        }
                                ]
                            }
                        }
                        "body" to obj {
                            "type" to "BlockExpression"
                            "body" to arr [
                                    obj {
                                        "type" to "IdentifierExpression"
                                        "id" to obj {
                                            "type" to "SimpleIdent"
                                            "name" to "foo"
                                        }
                                    },
                                    obj {
                                        "type" to "IdentifierExpression"
                                        "id" to obj {
                                            "type" to "SimpleIdent"
                                            "name" to "bar"
                                        }
                                    },
                                    obj {
                                        "type" to "IdentifierExpression"
                                        "id" to obj {
                                            "type" to "SimpleIdent"
                                            "name" to "baz"
                                        }
                                    }
                            ]
                        }
                    },
                    obj {
                        "type" to "ObjectSlot"
                        "name" to "x"
                        "valueType" to obj {
                            "type" to "DeclaredType"
                            "baseType" to obj {
                                "type" to "NamedType"
                                "name" to obj {
                                    "type" to "SimpleIdent"
                                    "name" to "Int"
                                }
                            }
                            "typeParams" to arr
                        }
                    },
                    obj {
                        "type" to "ObjectMethod"
                        "id" to obj {
                            "type" to "SimpleIdent"
                            "name" to "them"
                        }
                        "stackEffect" to obj {
                            "type" to "FunctionType"
                            "inputs" to obj {
                                "type" to "StackImage"
                                "baseImage" to null
                                "values" to arr[
                                        obj {
                                            "type" to "DeclaredType"
                                            "baseType" to obj {
                                                "type" to "NamedType"
                                                "name" to obj {
                                                    "type" to "SimpleIdent"
                                                    "name" to "T"
                                                }
                                            }
                                            "typeParams" to arr
                                        }
                                ]
                            }
                            "outputs" to obj {
                                "type" to "StackImage"
                                "baseImage" to null
                                "values" to arr
                            }
                        }
                        "body" to obj {
                            "type" to "BlockExpression"
                            "body" to arr [
                                    obj {
                                        "type" to "IdentifierExpression"
                                        "id" to obj {
                                            "type" to "SimpleIdent"
                                            "name" to "eat"
                                        }
                                    }
                            ]
                        }
                    }
            ]
        }
        assertEquals(expected.pretty(), r.json().pretty())
        assertEquals(
            listOf(
                Pair("MyNewType", DefKind.ObjectDef),
                Pair("make-MyNewType", DefKind.FunDef),
                Pair("MyNewType-me", DefKind.ObjectMethodDef),
                Pair("MyNewType-x", DefKind.GetterDef),
                Pair("MyNewType-x!", DefKind.SetterDef),
                Pair("MyNewType-them", DefKind.ObjectMethodDef)),
            r.getDefinedNames().map { Pair(it.name, it.kind) })
    }

     */

    public PicaModule parser(String input) throws IOException {
        var stream = new ANTLRInputStream(new ByteArrayInputStream(input.getBytes()));
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
        var input = "fun foo(x: Int) -> Int {\n" +
            "  if x == 0 then {\n" +
        "    return 0\n" +
        "  } else {\n" +
                "    return x * foo(x-1);\n" +
            "  }\n" +
        "}\n";

        var m = parser(input);
        System.err.println("M = " + m.toString());
        assertEquals("", m.toString());
    }
}
