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
package org.goodmath.pica.ast.actions;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.goodmath.pica.ast.exprs.Expr;
import org.goodmath.pica.ast.locations.Location;

import java.io.IOException;

@JsonSerialize(using = IfAction.IfActionSerializer.class)
public class IfAction extends Action {
    private final Expr cond;
    public Expr getCond() {
        return cond;
    }

    private final Action t;
    public Action getT() {
        return t;
    }

    private final Action f;

    public Action getF() {
        return f;
    }

    public IfAction(Expr cond, Action t, Action f, Location loc) {
        super(loc);
        this.cond = cond;
        this.t = t;
        this.f = f;
    }

    public static class IfActionSerializer extends JsonSerializer<IfAction> {

        @Override
        public void serialize(IfAction value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "IfAction");
            gen.writeObjectField("condition", value.getCond());
            gen.writeObjectField("trueBranch", value.getT());
            gen.writeObjectField("falseBranch", value.getF());
            gen.writeEndObject();
        }
    }

}
