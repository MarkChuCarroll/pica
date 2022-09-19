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
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.types.Type;

@JsonSerialize(using = TypeParamSpec.TypeParamSpecSerializer.class)
public class TypeParamSpec extends AstNode {
    private final String name;
    private final Optional<Type> constraint;

    public TypeParamSpec(String name, Optional<Type> constraint, Location loc) {
        super(loc);
        this.name = name;
        this.constraint = constraint;
    }

    public String getName() {
        return name;
    }

    public Optional<Type> getConstraint() {
        return constraint;
    }

    public static class TypeParamSpecSerializer extends JsonSerializer<TypeParamSpec> {

        @Override
        public void serialize(TypeParamSpec value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "TypeParam");
            gen.writeStringField("name", value.getName());
            if (value.getConstraint().isPresent()) {
                gen.writeObjectField("constraint", value.getConstraint().get());
            }
            gen.writeEndObject();
        }
    }
}