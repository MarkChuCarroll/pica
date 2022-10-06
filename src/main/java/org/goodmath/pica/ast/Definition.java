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
import org.goodmath.pica.types.DefinedName;

public abstract class Definition extends AstNode {
    private final String name;
    private final Optional<List<TypeParamSpec>> typeParams;
    private final Identifier module;

    public Optional<List<TypeParamSpec>> getTypeParams() {
        return typeParams;
    }

    public String getName() {
        return name;
    }

    public Definition(Identifier module, String name, Optional<List<TypeParamSpec>> typeParams, Location loc) {
        super(loc);
        this.module = module;
        this.name = name;
        this.typeParams = typeParams;
    }

    public abstract List<DefinedName> getDefinedNames();


    /**
     * Check that this definition is valid. A definition is invalid if:
     * <ul>
     *     <li> (undefined type var error) It uses an undefined type variable anywhere in its definition.</li>
     *     <li> (category error) It uses a type that doesn't match a language requirement. For example,
     *       if a boson type is listed in the composes list of a quark, that's violating teh language
     *       requirement that all types in the compose list of a quark be quark types.
     *       </li>
     * </ul>
     * @return true if the definition is valid. If the definition isn't valid, then errors
     *    describing the problem should have been logged to PicaCompilationError.
     */
    public abstract boolean validate();

    public Identifier getModule() {
        return module;
    }
}
