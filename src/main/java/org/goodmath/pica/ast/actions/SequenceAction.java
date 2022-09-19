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

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.goodmath.pica.ast.locations.Location;

@JsonSerialize(using = SequenceAction.SequenceActionSerializer.class)
public class SequenceAction extends Action {
    private final List<Action> actions;

    public List<Action> getActions() {
        return actions;
    }

    public SequenceAction(List<Action> actions, Location loc) {
        super(loc);
        this.actions = actions;
    }

    public static class SequenceActionSerializer extends JsonSerializer<SequenceAction> {

        @Override
        public void serialize(SequenceAction value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "SequenceAction");
            gen.writeArrayFieldStart("actions");
            for (Action a: value.getActions()) {
                gen.writeObject(a);
            }
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }

}
