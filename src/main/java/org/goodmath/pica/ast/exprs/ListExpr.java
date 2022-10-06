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

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.Twist;

public class ListExpr extends Expr {
    private final List<Expr> values;

    public List<Expr> getValues() {
        return values;
    }

    public ListExpr(List<Expr> values, Location loc) {
        super(loc);
        this.values = values;
    }

    @Override
    public Twist twist() {
        return Twist.obj("Expr::List",
            Twist.arr("values", values));
    }

}
