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
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.goodmath.pica.ast.Definition;
import org.goodmath.pica.ast.TypeParamSpec;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.types.Defined;

@JsonSerialize(using = BosonDef.BosonDefSerializer.class)
public class BosonDef extends Definition {

    private final List<BosonOption> options;

    public BosonDef(String name, Optional<List<TypeParamSpec>> typeParams,
                    List<BosonOption> options, Location loc) {
                super(name, typeParams, loc);
                this.options = options;
    }

    public List<BosonOption> getOptions() {
        return options;
    }

    public static class BosonDefSerializer extends JsonSerializer<BosonDef> {

        @Override
        public void serialize(BosonDef value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "BosonDef");
            gen.writeStringField("name", value.getName());
            if (value.getTypeParams().isPresent()) {
                gen.writeArrayFieldStart("typeParams");
                for (TypeParamSpec tp : value.getTypeParams().get()) {
                    gen.writeObject(tp);
                }
                gen.writeEndArray();
            }
            gen.writeArrayFieldStart("options");
            for (BosonOption bo : value.getOptions()) {
                gen.writeObject(bo);
            }
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }

    @Override
    public List<Defined> getDefinedNames() {
        return List.of(Defined.bosonDefinition(getName(), this));
    }
}
