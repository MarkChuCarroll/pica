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

import org.goodmath.pica.ast.AstNode;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.types.Type;
import org.goodmath.pica.util.Twist;

import java.util.List;

public class ChannelDef extends AstNode {
    private final String name;

    public String getName() {
        return name;
    }

    private final Type type;

    public Type getType() {
        return type;
    }

    public ChannelDef(String name, Type type, Location loc) {
        super(loc);
        this.name = name;
        this.type = type;
    }


    @Override
    public Twist twist() {
        return Twist.obj("ChannelDef",
            Twist.attr("name", getName()),
            Twist.val("type", getType()));
    }

}
