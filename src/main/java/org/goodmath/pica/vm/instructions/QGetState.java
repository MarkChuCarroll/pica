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
 * Get the value of a named slot from a quark.
 *
 * <code>
 *     qGetS tgt, q, type, name;
 * </code>
 *
 * <ul>
 *     <li> tgt: Reg is a register where the slot value will be stored.</li>
 *     <li> q: Reg is a register where the quark is stored.</li>
 *     <li> type: Identifier is the quark type.</li>
 *     <li> name: String is the slot name.</li>
 * </ul>
 */
@AllArgsConstructor
@Getter
public class QGetState extends Instruction {
    private Reg target;
    private Reg quark;
    private String quarkType;
    private String fieldName;

    @Override
    public String toString() {
        return String.format("%sqGetS %s, %s, %s, %s;\n",
                labelMarker(), getTarget().toString(),
                getQuark().toString(), getQuarkType(), getFieldName());
    }
}
