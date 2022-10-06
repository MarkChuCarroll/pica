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
 * Receive a message on a channel. This suspends the current ray, and places a continuation into
 * the scheduling queue with the code to be executed once the message is received. Note that
 * you can receive from any channel that you have access to; if you have the channel, you can
 * both send and recieve with it.
 *
 * <pre>
 *     recv tgt, chan, cont;
 * </pre>
 *
 * <ul>
 *     <li>tgt: Reg is the register where the received boson value will be stored.</li>
 *     <li>chan: Reg is the register containing the channel to receive the message.</li>
 *     <li>cont: Continuation is the continuation to be scheduled when the message
 *        has been received.</li>
 * </ul>
 */
@AllArgsConstructor
@Getter
public class Receive extends Instruction {
    private Reg target;
    private Reg channel;
    private Reg continuation;

    @Override
    public String toString() {
        return String.format("%srecv %s, %s, %s;\n",
                labelMarker(), getTarget().toString(),
                getChannel().toString(),
                getContinuation().toString());
    }
}
