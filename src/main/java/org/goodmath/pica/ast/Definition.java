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
import java.util.Optional;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.types.Defined;

public abstract class Definition extends AstNode {
    private final String name;
    private final Optional<List<TypeParamSpec>> typeParams;

    public Optional<List<TypeParamSpec>> getTypeParams() {
        return typeParams;
    }

    public String getName() {
        return name;
    }

    public Definition(String name, Optional<List<TypeParamSpec>> typeParams, Location loc) {
        super(loc);
        this.name = name;
        this.typeParams = typeParams;
    }

    public abstract List<Defined> getDefinedNames();
}
