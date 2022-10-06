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
 * Instruction to create a new continuation for the current quark, with a copy of the
 * current ray's local registers, and the address of an instruction to execute when
 * the continuation starts running in a ray.
 *
 * <pre>
 *     cNew reg, loc;
 * </pre>
 *
 * <ul>
 *     <li> reg: Reg a register where the new continuation will be stored.</li>
 *     <li> loc: the code location of the instruction to start executing.</li>
 * </ul>
 */
@AllArgsConstructor
@Getter
public class CNew extends Instruction {
    private Reg target;
    private CodeLocation instr;

    @Override
    public String toString() {
        return String.format("%scNew %s;\n",
                labelMarker(), getInstr().toString());
    }
}
