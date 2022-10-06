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

import lombok.EqualsAndHashCode;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.Twist;

import java.util.List;
import java.util.Optional;

@EqualsAndHashCode(callSuper = false)
public class Identifier extends AstNode {
    private final Optional<Identifier> moduleId;
    private final String name;

    public Identifier(Optional<Identifier> moduleId, String name, Location loc) {
        super(loc);
        this.moduleId = moduleId;
        this.name = name;
    }

    public Optional<Identifier> getModule() {
        return moduleId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (getModule().isPresent()) {
            return getModule().get() + "::" + getName();
        } else {
            return getName();
        }
    }

    public static Identifier parseIdentifier(String idStr) {
        String[] bits = idStr.split("::");
        return fromList(List.of(bits));
    }

    public static Identifier fromList(List<String> bits) {
        Identifier current = null;
        for (String bit: bits) {
            current = new Identifier(Optional.ofNullable(current),
                bit, Location.NO_LOCATION);
        }
        return current;
    }

    @Override
    public Twist twist() {
        return Twist.obj("Identifier",
                Twist.attr("name", toString()));

    }


}
