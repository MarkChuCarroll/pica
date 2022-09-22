package org.goodmath.pica.qgp;

public class StringBinOp extends Instruction {
    public enum Op {
        StrConcat, StrEq, StrGreater, StrLess, StrGE, StrLE
    }

    public StringBinOp(Reg target, Op op, Reg r1, Reg r2) {

    }
}
