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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.locations.Location;

public abstract class Lvalue extends Expr {

    @JsonSerialize(using = IndexedLValue.IndexedLvalueSerializer.class)
    public static class IndexedLValue extends Lvalue {
        private final Lvalue context;
        private final String index;

        public IndexedLValue(Lvalue context, String index, Location loc) {
            super(loc);
            this.context = context;
            this.index = index;
        }

        public Lvalue getContext() {
            return context;
        }

        public String getIndex() {
            return index;
        }

        public static class IndexedLvalueSerializer extends JsonSerializer<IndexedLValue> {

            @Override
            public void serialize(IndexedLValue value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartObject();
                gen.writeStringField("kind", "IndexedLvalue");
                gen.writeObjectField("context", value.getContext());
                gen.writeStringField("index", value.getIndex());
                gen.writeEndObject();
            }
        }
    }

    @JsonSerialize(using = IdentifierLvalue.IdentifierLvalueSerializer.class)
    public static class IdentifierLvalue extends Lvalue {
        private final Identifier id;

        public Identifier getId() { return id; }

        public IdentifierLvalue(Identifier id, Location loc) {
            super(loc);
            this.id = id;
        }

        public static class IdentifierLvalueSerializer extends JsonSerializer<IdentifierLvalue> {

            @Override
            public void serialize(IdentifierLvalue value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartObject();
                gen.writeStringField("kind", "IdentifierLvalue");
                gen.writeStringField("ident", value.getId().toString());
                gen.writeEndObject();
            }
        }
    }

    public Lvalue(Location loc) {
        super(loc);
    }



}
