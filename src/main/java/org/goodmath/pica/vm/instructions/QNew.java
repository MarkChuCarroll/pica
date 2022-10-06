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

import java.util.List;

/**
 * Create a new quark of a specified type.
 *
 * <pre>
 *     qNew tgt, type, params...
 * </pre>
 * <ul>
 *     <li> tgt: Reg is a register where the new quark will be stored.</li>
 *     <li> type: Identifier is the fully qualified name of the type of the quark.</li>
 *     <li> params: List[Reg] is a list of registers containing the constructor parameters
 *        for the quark.</li>
 * </ul>
 */
@AllArgsConstructor
@Getter
public class QNew extends Instruction {
    private Reg target;
    private String quarkType;
    private List<Reg> params;

    @Override
    public String toString() {
        if (params != null) {
            return String.format("%sqNew %s, %s, %s;\n",
                    labelMarker(),
                    getTarget().toString(),
                    getQuarkType(),
                    String.join(", ", getParams().stream().map(Reg::toString).toList()));
        } else {
            return String.format("%sqNew %s, %s;\n",
                    labelMarker(),
                    getTarget().toString(),
                    getQuarkType());
        }
    }
}
