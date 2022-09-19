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

public class ForAction extends Action {
    private final String idx;
    public String getIdx() {
        return idx;
    }

    private final Expr range;
    public Expr getRange() {
        return range;
    }

    private final Action body;

    public Action getBody() {
        return body;
    }

    public ForAction(String idx, Expr range, Action body, Location loc) {
        super(loc);
        this.idx = idx;
        this.range = range;
        this.body = body;
    }

    @Override
    public TagTree getTree() {
        return new TagTree("Action::For",
            List.of(new TagTree(getIdx()),
                getRange().getTree(),
                getBody().getTree()));

    }

}
