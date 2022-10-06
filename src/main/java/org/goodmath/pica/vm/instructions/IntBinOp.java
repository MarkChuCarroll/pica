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
import org.goodmath.pica.vm.Reg;

@AllArgsConstructor
@Getter
public class IntBinOp extends Instruction {
    private Reg target;
    private BinaryNumericOperation op;
    private Reg r1;
    private Reg r2;


    @Override
    public String toString() {
        return String.format("%s%s %s, %s, %s;\n",
                labelMarker(), instr(),
                getTarget().toString(),
                getR1().toString(),
                getR2().toString());
    }

    private String instr() {
        return switch (op) {
            case Greater -> "iGt";
            case Less -> "iLt";
            case GE -> "iGe";
            case LE -> "iLe";
            case Plus -> "iPlus";
            case Minus -> "iMinus";
            case Multiply -> "iTimes";
            case Divide -> "iDiv";
            case Mod -> "iMod";
            default -> throw new RuntimeException("Invalid int op " + getOp());
        };
    }
}
