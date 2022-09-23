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

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.PPLeafNode;
import org.goodmath.pica.util.PPTagNode;
import org.goodmath.pica.util.PrettyPrintTree;

import java.util.List;

public class LiteralExpr extends Expr {
    public enum Kind {
        STRING_LIT, INT_LIT, FLOAT_LIT, CHAR_LIT, SYM_LIT
    }

    private final Kind kind;
    public Kind getKind() {
        return kind;
    }

    private final String value;

    public String getValue() {
        return value;
    }

    public LiteralExpr(Kind kind, String value, Location loc) {
        super(loc);
        this.kind = kind;
        this.value = value;
    }

    @Override
    public PrettyPrintTree getTree() {
            return new PPTagNode("Expr::Literal::" + getKind().toString(),
                    List.of(new PPLeafNode(getValue())));
    }

}
