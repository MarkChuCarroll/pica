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
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.TagTree;

import java.util.List;

public class IfAction extends Action {
    private final Expr cond;
    public Expr getCond() {
        return cond;
    }

    private final Action t;
    public Action getT() {
        return t;
    }

    private final Action f;

    public Action getF() {
        return f;
    }

    public IfAction(Expr cond, Action t, Action f, Location loc) {
        super(loc);
        this.cond = cond;
        this.t = t;
        this.f = f;
    }

    @Override
    public TagTree getTree() {
        return new TagTree("Action::If",
            List.of(getCond().getTree(), getT().getTree(), getF().getTree()));
    }

}
