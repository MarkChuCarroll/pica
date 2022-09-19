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
import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.locations.Location;

@JsonSerialize(using = BosonTupleExpr.BosonTupleExprSerializer.class)
public class BosonTupleExpr extends Expr {
    private final Identifier tag;
    public Identifier getTag() {
        return tag;
    }

    private final List<Expr> fields;

    public List<Expr> getFields() {
        return fields;
    }

    public BosonTupleExpr(Identifier tag, List<Expr> fields, Location loc) {
        super(loc);
        this.tag = tag;
        this.fields = fields;
    }

    public static class BosonTupleExprSerializer extends JsonSerializer<BosonTupleExpr> {
        @Override
        public void serialize(BosonTupleExpr value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "BosonTupleExpr");
            gen.writeStringField("bosonOptionName", value.getTag().toString());
            gen.writeArrayFieldStart("fields");
            for (Expr e : value.getFields()) {
                gen.writeObject(e);
            }
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }
}
