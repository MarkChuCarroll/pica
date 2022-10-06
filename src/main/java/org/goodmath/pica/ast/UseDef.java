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
package org.goodmath.pica.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.types.DefinedName;
import org.goodmath.pica.util.Twist;

public class UseDef extends AstNode {
    private final Identifier id;
    private final List<String> names;
    private final Identifier moduleId;


    public List<String> getNames() {
        return names;
    }

    public Identifier getId() {
        return id;
    }

    public UseDef(Identifier moduleId, Identifier id, List<String> names, Location loc) {
        super(loc);
        this.moduleId = moduleId;
        this.id = id;
        this.names = names;
    }

    public List<DefinedName> getDefinedNames() {
        if (getNames().isEmpty()) {
            return List.of(DefinedName.importAlias(getId().getName(), getId()));
        } else {
            List<DefinedName> result = new ArrayList<>();
            for (String name: getNames()) {
                Identifier id = new Identifier(Optional.of(getId()), name, getId().getLocation());
                result.add(DefinedName.importAlias(name, id));
            }
            return result;
        }
    }

    @Override
    public Twist twist() {
        return Twist.obj("Def::Use",
                Twist.attr("module", getModuleId().toString()),
                Twist.arr("names",
                        getNames().stream().map(n -> Twist.attr("name", n)).toList()));
    }

    public Identifier getModuleId() {
        return moduleId;
    }
}
