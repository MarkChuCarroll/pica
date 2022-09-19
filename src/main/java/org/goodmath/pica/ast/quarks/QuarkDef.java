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
package org.goodmath.pica.ast.quarks;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.goodmath.pica.ast.Definition;
import org.goodmath.pica.ast.TypeParamSpec;
import org.goodmath.pica.ast.TypedParameter;
import org.goodmath.pica.ast.actions.Action;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.types.Type;
import org.goodmath.pica.types.Defined;

import lombok.Getter;

@JsonSerialize(using = QuarkDef.QuarkSerializer.class)
public class QuarkDef extends Definition {
    private final List<TypedParameter> params;
    private final List<Type> composes;
    private final List<ChannelDef> channels;
    private final List<SlotDef> slots;
    private final Action action;

    public QuarkDef(String name,
        Optional<List<TypeParamSpec>> typeParams,
        List<TypedParameter> params,
        List<Type> composes,
        List<ChannelDef> channels,
        List<SlotDef> slots,
        Action action,
        Location loc) {
            super(name, typeParams, loc);
            this.params = params;
            this.composes = composes;
            this.channels = channels;
            this.slots = slots;
            this.action = action;
        }

    public List<TypedParameter> getParams() {
        return params;
    }

    public List<Type> getComposes() {
        return composes;
    }

    public List<ChannelDef> getChannels() {
        return channels;
    }

    public List<SlotDef> getSlots() {
        return slots;
    }

    public Action getAction() {
        return action;
    }

    public static class QuarkSerializer extends JsonSerializer<QuarkDef> {

        @Override
        public void serialize(QuarkDef value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "quarkDef");
            gen.writeStringField("name", value.getName());
            if (value.getTypeParams().isPresent()) {
                gen.writeArrayFieldStart("typeParams");
                for (TypeParamSpec tp : value.getTypeParams().get()) {
                    gen.writeObject(tp);
                }
                gen.writeEndArray();
            }
            gen.writeArrayFieldStart("composes");
            for (Type t: value.getComposes()) {
                gen.writeObject(t);
            }
            gen.writeEndArray();
            gen.writeArrayFieldStart("channels");
            for (ChannelDef c: value.getChannels()) {
                gen.writeObject(c);
            }
            gen.writeEndArray();
            gen.writeArrayFieldStart("params");
            for (TypedParameter tp: value.getParams()) {
                gen.writeObject(tp);
            }
            gen.writeEndArray();
            gen.writeObjectField("action", value.getAction());
            gen.writeEndObject();
        }
    }

    @Override
    public List<Defined> getDefinedNames() {
        return List.of(Defined.quarkDefinition(getName(), this));
    }
}
