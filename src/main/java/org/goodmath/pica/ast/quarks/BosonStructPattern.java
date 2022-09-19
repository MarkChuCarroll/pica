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
package org.goodmath.pica.ast.quarks;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.TagTree;

import java.util.List;
import java.util.Map;

public class BosonStructPattern extends BosonPattern {
    private final Map<String, String> bosonFields;

    public BosonStructPattern(String name, Map<String, String> bosonFields, Location loc) {
        super(name, loc);
        this.bosonFields = bosonFields;
    }

    public Map<String, String> getBosonFields() {
        return bosonFields;
    }

    @Override
    public TagTree getTree() {
        return new TagTree("BosonStructPattern",
            List.of(
                new TagTree(getName()),
                new TagTree("fields",
                    getBosonFields().entrySet().stream().map(entry ->
                       new TagTree("bosonField",
                          List.of(new TagTree(entry.getKey()),
                            new TagTree(entry.getValue())))).toList())));
    }

}
