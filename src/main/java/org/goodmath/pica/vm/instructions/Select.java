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
 * Run exactly one of a set of alternative continuations.
 *
 * <p> All of the continuations <em>must</em> start with either a send or a
 * receive as their first instruction.  The instruction suspends until at
 * least one of the continuations' send or receive operations become unblocked;
 * if more than one is unblocked, one will be selected non-deterministically.
 * (Basically, whichever comes first in the data structures.)</p>
 *
 * <pre>
 *     sel c1, c2, ...
 * </pre>
 */
@AllArgsConstructor
@Getter
public class Select extends Instruction {
    private List<Reg> continuations;

    @Override
    public String toString() {
        return String.format("%ssel %s;\n",
                labelMarker(),
                String.join(", ", continuations.stream().map(Reg::toString).toList()));
    }
}
