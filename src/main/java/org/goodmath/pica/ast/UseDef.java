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

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.goodmath.pica.ast.locations.Location;

@JsonSerialize(using = UseDef.UseSerializer.class)
public class UseDef extends AstNode {
    private final Identifier id;
    private final List<String> names;


    public List<String> getNames() {
        return names;
    }

    public Identifier getId() {
        return id;
    }

    public UseDef(Identifier id, List<String> names, Location loc) {
        super(loc);
        this.id = id;
        this.names = names;
    }

    public static class UseSerializer extends JsonSerializer<UseDef> {

        @Override
        public void serialize(UseDef value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "UseDef");
            gen.writeStringField("module", value.getId().toString());
            if (!value.getNames().isEmpty()) {
                gen.writeArrayFieldStart("names");
                for (String name : value.getNames()) {
                    gen.writeString(name);
                }
                gen.writeEndArray();
            }
            gen.writeEndObject();
        }
    }

}
