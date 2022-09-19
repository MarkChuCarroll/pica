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


import org.goodmath.pica.ast.AstNode;
import org.goodmath.pica.ast.actions.Action;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.TagTree;

import java.util.List;

public class BosonMessagePatternAction extends AstNode {
    private final BosonPattern pattern;
    private final Action action;

    public BosonMessagePatternAction(BosonPattern pattern, Action action, Location loc) {
        super(loc);
        this.pattern = pattern;
        this.action = action;
    }

    public BosonPattern getPattern() {
        return pattern;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public TagTree getTree() {
        return new TagTree("BosonMessagePatternAction",
            List.of(getPattern().getTree(), getAction().getTree()));
    }
}


