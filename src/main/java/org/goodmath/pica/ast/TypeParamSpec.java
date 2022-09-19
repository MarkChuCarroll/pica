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
import org.goodmath.pica.ast.types.Type;
import org.goodmath.pica.util.TagTree;

public class TypeParamSpec extends AstNode {
    private final String name;
    private final Optional<Type> constraint;

    public TypeParamSpec(String name, Optional<Type> constraint, Location loc) {
        super(loc);
        this.name = name;
        this.constraint = constraint;
    }

    public String getName() {
        return name;
    }

    public Optional<Type> getConstraint() {
        return constraint;
    }

    @Override
    public TagTree getTree() {
        List<TagTree> children = new ArrayList<>();
        children.add(new TagTree(getName()));
        getConstraint().ifPresent(c ->
            children.add(new TagTree("constraint", List.of(c.getTree()))));
        return new TagTree("TypedParamSpec", children);
    }
}