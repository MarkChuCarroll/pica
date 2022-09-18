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

import java.util.List;
import java.util.Optional;

import org.goodmath.pica.ast.Definition;
import org.goodmath.pica.ast.TypeParamSpec;
import org.goodmath.pica.ast.TypedParameter;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.types.Type;

import lombok.Getter;

public class QuarkDef extends Definition {
    private final List<TypedParameter> params;
    private final List<Type> composes;
    private final List<ChannelDef> channels;
    private final List<SlotDef> slots;
    private final List<ActionDef> actions;

    public QuarkDef(String name,
        Optional<List<TypeParamSpec>> typeParams,
        List<TypedParameter> params,
        List<Type> composes,
        List<ChannelDef> channels,
        List<SlotDef> slots,
        List<ActionDef> actions,
        Location loc) {
            super(name, typeParams, loc);
            this.params = params;
            this.composes = composes;
            this.channels = channels;
            this.slots = slots;
            this.actions = actions;
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

    public List<ActionDef> getActions() {
        return actions;
    }
}
