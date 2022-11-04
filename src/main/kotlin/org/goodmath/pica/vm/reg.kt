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

package org.goodmath.pica.vm

import org.goodmath.pica.util.twist.LeafNode
import org.goodmath.pica.util.twist.Twist
import org.goodmath.pica.util.twist.Twistable


/**
 * The machine has unlimited registers, addressed by a unique,
 * system provided name. These registers are garbage collected, so
 * you don't need to worry about releasing them: any register ID
 * which is referenced by a boson, by a quark state field,
 * by a local register in active ray, or by a value stored in
 * another live register will be preserved.
 *
 *
 * There's also local state for a ray. The state is only guaranteed
 * to be preserved up to the next send, receive, or parallel branch (which
 * terminate the ray), so it's very much temporary space.
 * The local storage is numbered registers.
 */
sealed class Reg(): Twistable {
    data class NamedReg(val name: String) : Reg() {
        override fun twist(): Twist =
            Twist.obj("Reg::Named",
                Twist.attr("name", name))
    }

    data class NumberedReg(val num: Int) : Reg() {
        override fun twist(): Twist =
            Twist.obj("Reg::Numbered",
                Twist.attr("number", num.toString()))
    }

    data class IndirectReg(val r: Reg) : Reg() {
        override fun twist(): Twist =
            Twist.obj("Reg::Indirect",
                Twist.value("value", r)
            )

    }

    // A literal value is treated effectively as a special reg containing the
    // value.
    data class LiteralReg(val value: Any) : Reg() {
        override fun twist(): Twist =
            Twist.obj("Reg::Lit",
                Twist.leaf(value.toString())
            )
    }

    class QuarkReg() : Reg() {
        override fun twist(): Twist =
            LeafNode("Reg::CurrentQuark")
    }
}
