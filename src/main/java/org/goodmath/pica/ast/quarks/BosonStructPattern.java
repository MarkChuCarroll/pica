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
import org.goodmath.pica.ast.locations.Location;

import java.io.IOException;
import java.util.Map;

@JsonSerialize(using = BosonStructPattern.BosonStructPatternSerializer.class)
public class BosonStructPattern extends BosonPattern {
    private final Map<String, String> bosonFields;

    public BosonStructPattern(String name, Map<String, String> bosonFields, Location loc) {
        super(name, loc);
        this.bosonFields = bosonFields;
    }

    public Map<String, String> getBosonFields() {
        return bosonFields;
    }

    public static class BosonStructPatternSerializer extends JsonSerializer<BosonStructPattern> {

        @Override
        public void serialize(BosonStructPattern value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "BosonStructPattern");
            gen.writeStringField("name", value.getName());
            gen.writeObjectFieldStart("fields");
            for (Map.Entry<String, String> e : value.getBosonFields().entrySet()) {
                gen.writeStartObject();
                gen.writeStringField("kind", "BosonFieldPattern");
                gen.writeStringField("bosonFieldName", e.getKey());
                gen.writeStringField("boundVariableName", e.getValue());
                gen.writeEndObject();
            }
            gen.writeEndObject();
            gen.writeEndObject();
        }
    }

}
