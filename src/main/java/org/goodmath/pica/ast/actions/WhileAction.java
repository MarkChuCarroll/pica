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

@JsonSerialize(using = WhileAction.WhileActionSerializer.class)
public class WhileAction extends Action {
    private final Expr cond;
    public Expr getCond() {
        return cond;
    }

    private final Action body;

    public Action getBody() {
        return body;
    }

    public WhileAction(Expr cond, Action body, Location loc) {
        super(loc);
        this.cond = cond;
        this.body = body;
    }

    public static class WhileActionSerializer extends JsonSerializer<WhileAction> {

        @Override
        public void serialize(WhileAction value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "WhileAction");
            gen.writeObjectField("cond", value.getCond());
            gen.writeObjectField("body", value.getBody());
            gen.writeEndObject();
        }
    }

}
