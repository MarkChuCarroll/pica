package org.goodmath.pica.qgp;

public class ConvertValue extends Instruction {
    public enum Kind {
        PrimInt, PrimFloat, PrimChar, PrimStr
    }

    public ConvertValue(Reg target, Kind targetKind, Reg source, Kind sourceKind) {

    }
}
