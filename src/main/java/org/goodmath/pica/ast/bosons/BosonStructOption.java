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
package org.goodmath.pica.ast.bosons;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.types.Type;

@JsonSerialize(using = BosonStructOption.BosonStructOptionSerializer.class)
public class BosonStructOption extends BosonOption {
    private final Map<String, Type> fields;

    public Map<String, Type> getFields() {
        return fields;
    }

    public BosonStructOption(String name, Map<String, Type> fields, Location loc) {
        super(name, loc);
        this.fields = fields;
    }

    public static class BosonStructOptionSerializer extends JsonSerializer<BosonStructOption> {

        @Override
        public void serialize(BosonStructOption value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "BosonStruct");
            gen.writeStringField("name", value.getName());
            gen.writeArrayFieldStart("fields");
            for (Map.Entry<String, Type> e: value.getFields().entrySet()) {
                gen.writeStartObject();
                gen.writeStringField("name", e.getKey());
                gen.writeObjectField("value", e.getValue());
                gen.writeEndObject();
            }
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }

}
