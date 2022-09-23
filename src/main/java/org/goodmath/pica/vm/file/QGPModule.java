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
package org.goodmath.pica.vm.file;

import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.vm.instructions.Instruction;

import java.util.List;
import java.util.Map;

/**
 * The compiled form of a Pica module.
 */
public record QGPModule(
        Identifier id,
        List<Identifier> requires,
        Map<String, Object> metaTags,
        List<Boson> bosons,
        List<Quark> quarks,
        List<Instruction> code) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("module = ").append(id.toString()).append(";\n");
        if (requires() != null) {
            sb.append("requires = [")
                    .append(String.join(", ", requires().stream().map(Identifier::toString).toList()))
                    .append("];\n");
        }
        for (Map.Entry<String, Object> meta : metaTags().entrySet()) {
            sb.append(meta.getKey())
                    .append(" = ")
                    .append(meta.getValue().toString())
                    .append(";\n");
        }
        sb.append("--\n");
        sb.append("==Bosons\n");
        for (Boson b : bosons()) {
            sb.append(b.toString());
        }
        sb.append("==Quarks\n");
        for (Quark q : quarks()) {
            sb.append(q.toString());
        }
        sb.append("==Instructions\n");
        for (Instruction i : code) {
            sb.append(i.toString());
        }
        return sb.toString();
    }
}
