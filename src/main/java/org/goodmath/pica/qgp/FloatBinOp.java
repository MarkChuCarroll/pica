package org.goodmath.pica.qgp;

// == float ops
public class FloatBinOp extends Instruction {
    public enum Op {
        FloatPlus, FloatMinus, FloatMultiply, FloatDivide, FloatMod,
        IntGreater, FloatEq, FloatLess, FloatGE, FloatLE,
    }

    public FloatBinOp(Op operation, Reg target, Reg r1, Reg r2) {

    }
}
