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

import java.util.List;

import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.PPTagNode;
import org.goodmath.pica.util.PrettyPrintTree;

public abstract class Lvalue extends Expr {

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

        @Override
        public PrettyPrintTree getTree() {
            return new PPTagNode("LValue::Indexed",
                List.of(getContext().getTree(), new PPTagNode(getIndex())));
        }
    }

    public static class IdentifierLvalue extends Lvalue {
        private final Identifier id;

        public Identifier getId() { return id; }

        public IdentifierLvalue(Identifier id, Location loc) {
            super(loc);
            this.id = id;
        }

        @Override
        public PrettyPrintTree getTree() {
            return new PPTagNode("Lvalue::Identifier",
                List.of(getId().getTree()));
        }
    }

    public Lvalue(Location loc) {
        super(loc);
    }



}
