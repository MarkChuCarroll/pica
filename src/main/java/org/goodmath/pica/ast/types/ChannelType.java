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
import org.goodmath.pica.ast.locations.Location;

import java.io.IOException;

@JsonSerialize(using = ChannelType.ChannelTypeSerializer.class)
public class ChannelType extends Type {
    private final Type bosonType;

    public ChannelType(Type bosonType, Location loc) {
        super(loc);
        this.bosonType = bosonType;
    }

    public Type getBosonType() {
        return bosonType;
    }

    public static class ChannelTypeSerializer extends JsonSerializer<ChannelType> {

        @Override
        public void serialize(ChannelType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "ChannelType");
            gen.writeObjectField("type", value.getBosonType());
            gen.writeEndObject();
        }
    }
}
