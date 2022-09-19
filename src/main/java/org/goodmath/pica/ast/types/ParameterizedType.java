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

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.goodmath.pica.ast.locations.Location;

@JsonSerialize(using = ParameterizedType.ParameterizedTypeSerializer.class)
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

    public static class ParameterizedTypeSerializer extends JsonSerializer<ParameterizedType> {

        @Override
        public void serialize(ParameterizedType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "ParameterizedType");
            gen.writeArrayFieldStart("typeParams");
            for (Type t: value.getParams()) {
                gen.writeObject(t);
            }
            gen.writeEndArray();
            gen.writeObjectField("baseType", value.getBase());
            gen.writeEndObject();
        }
    }
}