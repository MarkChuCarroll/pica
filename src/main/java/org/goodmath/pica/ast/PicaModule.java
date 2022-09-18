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

public class PicaModule extends AstNode {
    public PicaModule(String name, List<Use> uses, List<Definition> defs, Location loc) {
        super(loc);
        this.name = name;
        this.uses = uses;
        this.definitions = defs;
    }

    private final String name;
    private final List<Use> uses;
    private final List<Definition> definitions;

    public String getName() {
        return name;
    }

    public List<Use> getUses() {
        return uses;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }
}
