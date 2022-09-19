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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.goodmath.pica.ast.Definition;
import org.goodmath.pica.ast.TypeParamSpec;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.types.Defined;
import org.goodmath.pica.util.TagTree;

public class BosonDef extends Definition {

    private final List<BosonOption> options;

    public BosonDef(String name, Optional<List<TypeParamSpec>> typeParams,
                    List<BosonOption> options, Location loc) {
                super(name, typeParams, loc);
                this.options = options;
    }

    public List<BosonOption> getOptions() {
        return options;
    }

    @Override
    public List<Defined> getDefinedNames() {
        return List.of(Defined.bosonDefinition(getName(), this));
    }

    @Override
    public TagTree getTree() {
        List<TagTree> children = new ArrayList<>();
        children.add(new TagTree(getName()));
        getTypeParams().ifPresent(tps ->
            children.add(new TagTree("typeParams",
                tps.stream().map(TypeParamSpec::getTree).toList()
            )));
        children.add(new TagTree("options",
            getOptions().stream().map(BosonOption::getTree).toList()));

        return new TagTree("Def::BosonDef",
            children);
    }
}
