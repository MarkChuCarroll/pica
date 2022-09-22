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
import org.goodmath.pica.types.Defined;
import org.goodmath.pica.util.PPLeafNode;
import org.goodmath.pica.util.PPTagNode;
import org.goodmath.pica.util.PrettyPrintTree;

public class UseDef extends AstNode {
    private final Identifier id;
    private final List<String> names;


    public List<String> getNames() {
        return names;
    }

    public Identifier getId() {
        return id;
    }

    public UseDef(Identifier id, List<String> names, Location loc) {
        super(loc);
        this.id = id;
        this.names = names;
    }

    public List<Defined> getDefinedNames() {
        if (getNames().isEmpty()) {
            return List.of(Defined.importAlias(getId().getName(), getId()));
        } else {
            List<Defined> result = new ArrayList<>();
            for (String name: getNames()) {
                Identifier id = new Identifier(Optional.of(getId()), name, getId().getLocation());
                result.add(Defined.importAlias(name, id));
            }
            return result;
        }
    }

    @Override
    public PrettyPrintTree getTree() {
        List<PrettyPrintTree> children = new ArrayList<>();
        children.add(getId().getTree());
        if (!getNames().isEmpty()) {
            children.add(new PPTagNode("names",
                getNames().stream().map(n -> (PrettyPrintTree) new PPLeafNode(n)).toList()));
        }
        return new PPTagNode("Def::Use", children);
    }

}