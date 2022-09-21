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

import java.util.List;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.PPFieldNode;
import org.goodmath.pica.util.PPTagNode;
import org.goodmath.pica.util.PrettyPrintTree;

public class PicaModule extends AstNode {
    public PicaModule(String name, List<UseDef> useDefs, List<Definition> defs, Location loc) {
        super(loc);
        this.name = name;
        this.useDefs = useDefs;
        this.definitions = defs;
    }

    private final String name;
    private final List<UseDef> useDefs;
    private final List<Definition> definitions;

    public String getName() {
        return name;
    }

    public List<UseDef> getUses() {
        return useDefs;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }


    @Override
    public PrettyPrintTree getTree() {
        return new PPTagNode("PicaModule",
            List.of(
                new PPFieldNode("name", getName()),
                new PPTagNode("uses",
                    getUses().stream().map(UseDef::getTree).toList()),
                new PPTagNode("defs",
                    getDefinitions().stream().map(Definition::getTree).toList())));

    }

}
