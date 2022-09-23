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
package org.goodmath.pica.ast.bosons;

import java.util.List;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.types.Type;
import org.goodmath.pica.util.PPFieldNode;
import org.goodmath.pica.util.PPTagNode;
import org.goodmath.pica.util.PrettyPrintTree;

public class BosonTupleOption extends BosonOption {
        private final List<Type> fields;

    public List<Type> getFields() {
        return fields;
    }

    public BosonTupleOption(String name, List<Type> fields, Location loc) {
        super(name, loc);
        this.fields = fields;
    }

    @Override
    public PrettyPrintTree getTree() {
        return new PPTagNode("BosonOption::Tuple",
            List.of(
                new PPFieldNode("optionName", getName()),
                new PPTagNode("fields",
                    getFields().stream().map(Type::getTree).toList())));
    }

}
