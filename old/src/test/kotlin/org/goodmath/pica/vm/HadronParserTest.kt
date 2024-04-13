package org.goodmath.pica.vm

import org.antlr.v4.runtime.CharStreams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HadronParserTest {
    @Test
    fun testParseHadron() {
        val input = """
        ==Headers
        hadron = foo::bar;
        requires = [a::b, b::c, c::d::e];
        version = [0, 0, 0, 0, 1];
        author = "Me";
        --
        ==Bosons
        boson
           name=BBB;
           option BOne(Int, A::B, Str);
           option BTwo(Str, Str);
        --
        ==Quarks
        quark
           name=q::p::r;
           channels=[(a, b)];
           fields=[(f, g), (g, h::i)];
           entryPoint=quarkstart;
        --
        ==Instructions
           foo=qNew (1), QuarkType, r1, r2, r3;
              send 3, r1, r4;
              iPlus r0, r1, r2;
              sel foo, bar, baz, bazoom, 1, @;
              jmp foo;
        """.trimIndent()

        val expected = """
            obj Vm::Hadron:
               requires = array:
                  obj Ident:
                     hadron:
                        obj Ident:
                           name = a
                     name = b
                  obj Ident:
                     hadron:
                        obj Ident:
                           name = b
                     name = c
                  obj Ident:
                     hadron:
                        obj Ident:
                           hadron:
                              obj Ident:
                                 name = c
                           name = d
                     name = e
               metaTags = array:
                  version = [0, 0, 0, 0, 1]
                  author = "Me"
               bosons = array:
                  obj Vm::Boson:
                     name = BBB
                     options = array:
                        obj Vm::Boson::Option:
                           name = BOne
                           fields = array:
                              Int
                              A::B
                              Str
                        obj Vm::Boson::Option:
                           name = BTwo
                           fields = array:
                              Str
                              Str
               quarks = array:
                  obj Vm::Quark:
                     channels = array:
                        a = b
                     fields = array:
                        f = g
                        g = h::i
                     entryPoint:
                        namedCodeLoc = quarkstart
               instructions = array:
                  obj Instr::QNew:
                     label = foo
                     quarkType = QuarkType
                     target:
                        obj Reg::Indirect:
                           value:
                              obj Reg::Lit:
                                 1
                     args = array:
                        obj Reg::Named:
                           name = r1
                        obj Reg::Named:
                           name = r2
                        obj Reg::Named:
                           name = r3
                  obj Send:
                     channel:
                        obj Reg::Lit:
                           3
                     msg:
                        obj Reg::Named:
                           name = r1
                     continuation:
                        obj Reg::Named:
                           name = r4
                  obj Instr::IAnd:
                     target:
                        obj Reg::Named:
                           name = r0
                     left:
                        obj Reg::Named:
                           name = r1
                     right:
                        obj Reg::Named:
                           name = r2
                  obj Instr::Sel:
                     continuations = array:
                        obj Reg::Named:
                           name = foo
                        obj Reg::Named:
                           name = bar
                        obj Reg::Named:
                           name = baz
                        obj Reg::Named:
                           name = bazoom
                        obj Reg::Lit:
                           1
                        Reg::CurrentQuark
                  obj Instr::Jmp:
                     codeLoc:
                        namedCodeLoc = foo

        """.trimIndent()

        val theMod = HadronParser().parse(CharStreams.fromString(input));
        assertEquals(expected, theMod.twist().toString());
    }
}