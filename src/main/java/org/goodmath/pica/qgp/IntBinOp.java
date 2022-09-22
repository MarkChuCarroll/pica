package org.goodmath.pica.qgp;

// == INT OPS
public class IntBinOp extends Instruction {
    public enum Op {
        IntPlus, IntMinus, IntMultiply, IntDivide, IntMod,
        IntGreater, IntEq, IntLess, IntGE, IntLE,
    }

    public IntBinOp(Op operation, Reg target, Reg r1, Reg r2) {

    }
}
