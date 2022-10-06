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

/**
 * Instruction to create a modified boson. The new boson is equal to the
 * input, except for one updated field.
 *
 * <pre>
 *     bSet tgt, type, field, val
 * </pre>
 *
 * <ol>
 *     <li> tgt: Reg is the register where the new boson value will be stored.</li>
 *     <li> type: Identifier is the identifier of the boson option type.</li>
 *     <li> field: String is the name of the field to update in the new boson.</li>
 *     <li> val: Reg is a register containing the value of the updated field in the new boson.</li>
 * </ol>
 *
 */
@AllArgsConstructor
@Getter
public class BSetField extends Instruction {
    private Reg target;
    private String bosonType;
    private int bosonField;
    private Reg source;

    @Override
    public String toString() {
        return String.format("%sbSet %s, %s, %s, %s;\n",
                labelMarker(), getTarget().toString(),
                getBosonType(), getBosonField(), source.toString());
    }
}
