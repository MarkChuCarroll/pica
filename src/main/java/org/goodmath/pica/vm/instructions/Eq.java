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
 * Instruction to test two values for equality.
 *
 * <pre>
 *     eq tgt, r1, r2;
 * </pre>
 *
 * <ul>
 *     <li>tgt: Reg a register where the result will be stored.</li>
 *     <li>r1: Reg with one of the source inputs.</li>
 *     <li>r2: Reg with the other source input. </li>
 * </ul>
 */
@AllArgsConstructor
@Getter
public class Eq extends Instruction {
    private Reg target;
    private Reg r1;
    private Reg r2;

    @Override
    public String toString() {
        return String.format("%seq %s, %s, %s;\n",
                labelMarker(), target.toString(), r1.toString(), r2.toString());
    }
}
