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
 * Start a collection of new continuations. Each of the new continuations starts
 * with a copy of the ray-state of the current continuation. Once this instruction
 * is complete, the new continuations will be in the scheduling queue. Unlike the
 * <code>sel</code> instruction, <code>spawn</code> runs <em>all</em> of the
 * continuations, not just one of them.
 *
 * <pre>
 *     spawn c1, c2, ...;
 * </pre>
 *
 */
@AllArgsConstructor
@Getter
public class Spawn extends Instruction {
    private List<Reg> continuation;

    @Override
    public String toString() {
        return String.format("%sspawn %s;\n",
                labelMarker(),
                String.join(", ", getContinuation().stream().map(Reg::toString).toList()));
    }
}
