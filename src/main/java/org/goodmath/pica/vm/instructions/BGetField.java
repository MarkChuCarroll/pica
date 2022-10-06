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
import lombok.Data;
import lombok.Getter;
import org.goodmath.pica.vm.Reg;

/**
 * Get a field from a boson.
 *
 * <pre>
 *     bGet tgt, type, field
 * </pre>
 *
 * <ul>
 *     <li> tgt: Reg is a register where the result will be stored.</li>
 *     <li> type: Identifier is the identifier of the boson option type.</li>
 *     <li> field: String is the name of the field.</li>
 * </ul>
 * getField(reg, BosonType, BosonField) - retrieve the value of the field of the boson stored
 * in reg, and store it in the target register.
 */
@AllArgsConstructor
@Getter
public class BGetField extends Instruction {
    Reg target;
    Reg boson;
    String field;

    public String toString() {
        return String.format("%sbGet %s, %s, %s;\n",
                labelMarker(),
                target.toString(), boson.toString(), field.toString());
    }
}
