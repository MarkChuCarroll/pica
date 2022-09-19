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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.goodmath.pica.ast.actions.Action;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.types.Type;
import org.goodmath.pica.types.Defined;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@JsonSerialize(using = FunctionDef.FDSerializer.class)
public class FunctionDef extends Definition {
    private final List<TypedParameter> params;
    private final Type resultType;
    private final Action action;

    public FunctionDef(String name, Optional<List<TypeParamSpec>> typeParams,
                       List<TypedParameter> params,
                       Type resultType,
                       Action action,
                       Location loc) {
        super(name, typeParams, loc);
        this.params = params;
        this.resultType = resultType;
        this.action = action;
    }

    public List<TypedParameter> getParams() {
        return params;
    }

    public Type getResultType() {
        return resultType;
    }

    public Action getAction() {
        return action;
    }

    public static class FDSerializer extends JsonSerializer<FunctionDef> {

        @Override
        public void serialize(FunctionDef value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "fundef");
            gen.writeStringField("name", value.getName());
            gen.writeArrayFieldStart("params");
            for (TypedParameter tp: value.getParams()) {
                gen.writeObject(tp);
            }
            gen.writeEndArray();
            gen.writeObjectField("returnType", value.getResultType());
            gen.writeObjectField("action", value.getAction());
            gen.writeEndObject();
        }
    }

    @Override
    public List<Defined> getDefinedNames() {
        return List.of(Defined.functionDefinition(getName(), this));
    }
}
