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
public class StringBinOp extends Instruction {
    public enum Op {
        StrConcat, StrEq, StrGreater, StrLess, StrGE, StrLE
    }

    private String getInstr() {
        return switch (op) {
            case StrConcat -> "sCat";
            case StrEq -> "eq";
            case StrGreater -> "sGt";
            case StrLess -> "sLt";
            case StrLE -> "sLe";
            case StrGE -> "sGe";
        };
    }

    @Override
    public String toString() {
        return String.format("%s%s %s, %s, %s;\n",
                labelMarker(), getInstr(), getTarget().toString(),
                getR1().toString(), getR2().toString());
    }

    private Reg target;
    private Op op;
    private Reg r1;
    private Reg r2;
}
