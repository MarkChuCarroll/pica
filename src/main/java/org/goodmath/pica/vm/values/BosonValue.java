package org.goodmath.pica.vm.values;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.goodmath.pica.ast.Identifier;

@AllArgsConstructor
@Getter
public class BosonValue extends Value {

    /**
     * The fully qualified identifier of the boson option for this value.
     */
    private Identifier bosonType;
    private Value[] fields;

    @Override
    public VType getType() {
        return VType.BosonType;
    }

    public BosonValue setField(int idx, Value v) {
        Value[] newFields = fields.clone();
        newFields[idx] = v;
        return new BosonValue(bosonType, newFields);
    }

    public Value getField(int idx, Value v) {
        // TODO: bounds check??
        return fields[idx];
    }
}
