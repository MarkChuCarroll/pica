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
 * Instruction to create a new boson.
 *
 * <pre>
 *     bNew reg, type, arg0, arg1, ..., argN
 * </pre>
 *
 * <ul>
 *     <li> reg: Reg is the register where the new boson will be stored.</li>
 *     <li> type: Identifier is the identifier of the boson option.</li>
 *     <li> argN: Reg are registers where the fields of the new boson are stored.</li>
 * </ul>
 *
 */
@AllArgsConstructor
@Getter
public class BNew extends Instruction {
    private Reg target;
    private String bosonType;
    private List<Reg> params;


    @Override
    public String toString() {
        if (params != null) {
            return String.format("%sbNew %s, %s, %s;\n",
                    labelMarker(),
                    getTarget().toString(),
                    getBosonType(),
                    String.join(",", getParams().stream().map(Reg::toString).toList()));
        } else {
            return String.format("%sbNew %s, %s;\n",
                    labelMarker(),
                    getTarget().toString(),
                    getBosonType());
        }
    }

}

