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

/**
 * An instruction for the plasma VM.
 *
 * <p> The Plasma VM is a register machine, so all instructions take registers
 * as inputs, and store their results to registers. The overall execution model
 * is based on <em>continuations</em> and <em>rays</em>.</p>
 *
 * <p> A continuation is basically the standard PL version of a continuation - it's
 * a structure that captures everything needed to continue a computation after
 * an interruption. In Pica, that means that it's got a register set, an owner quark,
 * and an instruction address.</p>
 *
 * <p> The scheduler keeps track of a set of inactive continuations,
 * and the messaging operation that they're waiting for. When that condition is
 * satisfied, it moves the continuation to the ready queue, where it can be
 * picked up for execution by a ray.</p>
 *
 * <p> A ray is an execution context that can pick up and run continuations. There are
 * many rays operating concurrently in a Pica program. The scheduler (which is running in
 * its own thread) moves continuations to a ready queue, and as soon as the continuation
 * a ray is running suspends, it takes the next continuation from the queue and starts
 * running it.</p>
 */
public class Instruction {
    public void setLabel(String label) {
        this.label = label;
    }
    private String label = null;

    public String getLabel() {
        return label;
    }

    protected String labelMarker() {
        if (label == null) {
            return "\t\t";
        } else {
            return "\t" + label + "=";
        }
    }
}