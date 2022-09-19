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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.locations.Location;

import lombok.Getter;

import java.io.IOException;

@JsonSerialize(using = NamedType.NamedTypeSerializer.class)
public class NamedType extends Type {
    private final Identifier id;
    public NamedType(Identifier id, Location loc) {
        super(loc);
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }


    public static class NamedTypeSerializer extends JsonSerializer<NamedType> {

        @Override
        public void serialize(NamedType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "NamedType");
            gen.writeStringField("name", value.getId().toString());
            gen.writeEndObject();
        }
    }

}
