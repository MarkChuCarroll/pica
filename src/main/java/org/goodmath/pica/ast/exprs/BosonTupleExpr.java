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
import org.goodmath.pica.util.Twist;

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

    @Override
    public Twist twist() {
        return Twist.obj("Expr::BosonTuple",
                Twist.attr("tag", tag.toString()),
                Twist.arr("fields", getFields()));
    }
}
