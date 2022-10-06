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
import org.goodmath.pica.vm.CodeLocation;
import org.goodmath.pica.vm.Reg;

/**
 * Branch instruction. The PC for the executing ray will be changed to the target
 * if the condition register is true.
 *
 * <pre>
 *     brIf reg, tgt
 * </pre>
 * <ul>
 *     <li>reg: Reg is the register containing the condition value.</li>
 *     <li>tgt: CodeLocation is the location to branch to if the condition is true.</li>
 * </ul>
 */
@AllArgsConstructor
@Getter
public class BranchIfTrue extends Instruction {
    private Reg cond;
    private CodeLocation target;

    @Override
    public String toString() {
        return String.format("%sbrIf %s, %s;\n",
                labelMarker(), cond.toString(), target.toString());
    }

}
