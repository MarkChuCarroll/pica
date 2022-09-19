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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.goodmath.pica.ast.AstNode;
import org.goodmath.pica.ast.exprs.Expr;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.types.Type;

import java.io.IOException;

@JsonSerialize(using= SlotDef.SlotDefSerializer.class)
public class SlotDef extends AstNode {
    private final String name;
    private final Type type;
    private final Expr initValue;

    public SlotDef(String name, Type type, Expr initValue, Location loc) {
        super(loc);
        this.name = name;
        this.type = type;
        this.initValue = initValue;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Expr getInitValue() {
        return initValue;
    }

    public static class SlotDefSerializer extends JsonSerializer<SlotDef> {

        @Override
        public void serialize(SlotDef value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "SlotDef");
            gen.writeStringField("name", value.name);
            gen.writeObjectField("type", value.getType());
            gen.writeEndObject();
        }
    }
}
