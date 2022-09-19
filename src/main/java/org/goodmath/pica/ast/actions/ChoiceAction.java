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

@JsonSerialize(using = ChoiceAction.ChoiceActionSerializer.class)
public class ChoiceAction extends Action {
    private final List<Action> options;

    public List<Action> getOptions() {
        return options;
    }

    public ChoiceAction(List<Action> options, Location loc) {
        super(loc);
        this.options = options;
    }

    public static class ChoiceActionSerializer extends JsonSerializer<ChoiceAction> {

        @Override
        public void serialize(ChoiceAction value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "ChoiceAction");
            gen.writeArrayFieldStart("options");
            for (Action a: value.getOptions()) {
                gen.writeObject(a);
            }
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }

}
