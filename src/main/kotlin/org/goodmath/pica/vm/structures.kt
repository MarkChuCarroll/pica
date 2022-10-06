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

import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.util.Twist
import org.goodmath.pica.util.Twistable

data class BosonSpec(
    val name: String,
    val options: List<Pair<String, List<Identifier>>>
): Twistable {
    override fun twist(): Twist =
        Twist.obj(
            "Vm::Boson",
            Twist.attr("name", name),
            Twist.arr("options",
                options.map { o ->
                    Twist.obj("Vm::Boson::Option",
                        Twist.attr("name", o.first),
                        Twist.arr("fields", o.second.map { Twist.leaf(it.toString()) })
                    )
                })
        )

}

data class HadronSpec(
    val id: Identifier,
    val requires: List<Identifier>,
    val metaTags: List<Pair<Identifier, Any>>,
    val bosons: List<BosonSpec>,
    val quarks: List<QuarkSpec>,
    val instructions: List<Instruction>): Twistable {
    override fun twist(): Twist =
        Twist.obj("Vm::Hadron",
            Twist.arr("requires", requires),
            Twist.arr("metaTags",
                metaTags.map { (k, v) -> Twist.attr(k.toString(), v.toString())}
            ),
            Twist.arr("bosons", bosons),
            Twist.arr("quarks", quarks),
            Twist.arr("instructions", instructions))
}

data class QuarkSpec(
    val name: String,
    val channels: List<Pair<String, Identifier>>,
    val fields: List<Pair<String, Identifier>>,
    val entryPoint: CodeLocation): Twistable {
    override fun twist(): Twist =
        Twist.obj(
            "Vm::Quark",
            Twist.arr("channels",
                channels.map { (name, type) -> Twist.attr(name, type.toString()) }),
            Twist.arr("fields",
                fields.map { (name, type) -> Twist.attr(name, type.toString()) }),
            Twist.value("entryPoint", entryPoint)
        )
}

