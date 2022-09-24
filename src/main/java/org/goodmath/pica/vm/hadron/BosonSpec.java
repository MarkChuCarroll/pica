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

import java.util.List;

/**
 * A BosonSpec is the representation of a compiled hadron in
 * a hadron file. It provides the name of the boson type,
 * and a list of the alternatives shapes for the boson,
 * with the types for each field.
 */
public record BosonSpec(
        String name,
        List<Pair<String, List<Identifier>>> options) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("boson\n");
        sb.append("\tname=").append(name()).append(";\n");
        for (Pair<String, List<Identifier>> opt: options()) {
            sb.append("\toption ")
                    .append(opt.first())
                    .append("(")
                    .append(String.join(", ",
                            opt.second().stream().map(Identifier::toString).toList()))
                    .append(");\n");
        }
        sb.append("--\n");
        return sb.toString();
    }
}
