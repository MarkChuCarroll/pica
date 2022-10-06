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
package org.goodmath.pica.vm.instructions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.goodmath.pica.vm.Reg;

/**
 * Type conversion instructions.
 *
 * <p> This class incorporates a collection of similar instructions:</p>
 *
 * <code>
 *     iToF tgt, src;
 *     iToS tgt, src;
 *     fToI tgt, src;
 *     fToS tgt, src;
 *     sToI tgt, src;
 *     sToF tgt, src;
 * </code>
 */
@AllArgsConstructor
@Getter
public class ConvertValue extends Instruction {
    public enum Kind {
        PrimInt, PrimFloat, PrimStr
    }

    private Reg target;
    private Kind targetKind;
    private Reg Source;
    private Kind sourceKind;

    @Override
    public String toString() {
        return String.format("%s%s %s, %s;\n",
                labelMarker(), getOp(), getTarget().toString(), getSource().toString());
    }

    @SneakyThrows
    private String getOp() {
        if (targetKind == Kind.PrimInt) {
            if (sourceKind == Kind.PrimFloat) {
                return "fToI";
            } else if (sourceKind == Kind.PrimStr) {
                return "sToI";
            } else {
                throw new RuntimeException(
                        String.format("Invalid conversion from %s to %s", sourceKind, targetKind));
            }
        } else if (targetKind == Kind.PrimFloat) {
            if (sourceKind == Kind.PrimInt) {
                return "iToF";
            } else if (sourceKind == Kind.PrimStr) {
                return "sToF";
            } else {
                throw new RuntimeException(
                        String.format("Invalid conversion from %s to %s", sourceKind, targetKind));
            }
        } else if (targetKind == Kind.PrimStr) {
            if (sourceKind == Kind.PrimInt) {
                return "iToS";
            } else if (sourceKind == Kind.PrimFloat) {
                return "fToS";
            } else {
                throw new RuntimeException(
                        String.format("Invalid conversion from %s to %s", sourceKind, targetKind));
            }
        } else {
            throw new RuntimeException(
                    String.format("Invalid conversion from %s to %s", sourceKind, targetKind));
        }
    }
}
