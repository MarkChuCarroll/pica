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

import org.goodmath.pica.ast.exprs.Expr;
import org.goodmath.pica.util.PPTagNode;
import org.goodmath.pica.util.PrettyPrintTree;

import java.util.List;

public class ExprAction extends Action {
    private final Expr expr;

    public Expr getExpr() {
        return expr;
    }

    public ExprAction(Expr expr) {
        super(expr.getLocation());
        this.expr = expr;
    }

    @Override
    public PrettyPrintTree getTree() {
        return new PPTagNode("Action::Expr",
            List.of(getExpr().getTree()));
    }

}
