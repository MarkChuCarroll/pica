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
package org.goodmath.pica.vm.hadron;

import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.Pair;
import org.goodmath.pica.vm.CodeLocation;

import java.util.List;

/**
 * A QuarkSpec is the representation of a compiled quark in
 * a hadron file. It provides the names of the channels defined
 * for the quark, the names of the slots defined in the quark's
 * representation, and the entrypoint for the quark's action.
 *
 * TODO: also needs to have the constructor parameters.
 * @param channels name to channel type.
 * @param fields   map from name to type.
 */
public record QuarkSpec(
        String name,
        List<Pair<String, Identifier>> channels,
        List<Pair<String, Identifier>> fields,
        CodeLocation entryPoint) {
    public String toString() {
        return "quark\n" +
                "\tname=" + name + ";\n" +
                "\tchannels=[" +
                String.join(", ",
                        channels.stream()
                                .map(c -> "(" + c.first() + ", " + c.second().toString() + ")")
                                .toList()) +
                "];\n" +
                "\tfields=[" +
                String.join(", ",
                        fields.stream()
                                .map(f -> "(" + f.first() + ", " + f.second().toString() + ")")
                                .toList()) +
                "];\n" +
                "\tentryPoint=" +
                entryPoint.toString() +
                ";\n" +
                "--\n";
    }
}

