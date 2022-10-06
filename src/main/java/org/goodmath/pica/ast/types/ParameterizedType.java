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
package org.goodmath.pica.ast.types;

import java.util.List;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.Twist;

public class ParameterizedType extends Type {
    private final NamedType base;
    private final List<Type> params;

    public ParameterizedType(NamedType base, List<Type> params, Location loc) {
        super(loc);
        this.base = base;
        this.params = params;
    }

    public NamedType getBase() {
        return base;
    }

    public List<Type> getParams() {
        return params;
    }

    /**
     * Check that a parameterized type instantiated with these type args are valid.
     *
     * An instantiated parameterized type is valid if:
     * <ol>
     *     <li> It has the correct number of type arguments.</li>
     *     <li> Each type argument satisfies any constraints required by that
     *        type argument.</li>
     * </ol>
     * @return bool if the type is valid. If it isn't valid, errors should be appended
     * to the log in @see{}PicaComplicationError}.
     */
    public boolean validate() {
        return false;
    }

    @Override
    public Twist twist() {
        return Twist.obj("Type::Parameterized",
                Twist.val("baseType", getBase()),
                Twist.arr("typeArgs", getParams()));
    }
}