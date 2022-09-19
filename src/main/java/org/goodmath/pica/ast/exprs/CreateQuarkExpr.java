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
package org.goodmath.pica.ast.exprs;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.types.Type;

@JsonSerialize(using = CreateQuarkExpr.CreateQuarkExprSerializer.class)
public class CreateQuarkExpr extends Expr {
    private final Type quarkType;
    public Type getQuarkType() {
        return quarkType;
    }

    private final List<Expr> args;

    public List<Expr> getArgs() {
        return args;
    }

    public CreateQuarkExpr(Type quarkType, List<Expr> args, Location loc) {
        super(loc);
        this.quarkType = quarkType;
        this.args = args;
    }

    public static class CreateQuarkExprSerializer extends JsonSerializer<CreateQuarkExpr> {

        @Override
        public void serialize(CreateQuarkExpr value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "CreateQuarkExpr");
            gen.writeObjectField("type", value.getQuarkType());
            gen.writeArrayFieldStart("args");
            for (Expr e: value.getArgs()) {
                gen.writeObject(e);
            }
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }

}
