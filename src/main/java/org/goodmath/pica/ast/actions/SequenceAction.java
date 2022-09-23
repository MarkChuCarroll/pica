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

import java.util.List;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.PPTagNode;
import org.goodmath.pica.util.PrettyPrintTree;

public class SequenceAction extends Action {
    private final List<Action> actions;

    public List<Action> getActions() {
        return actions;
    }

    public SequenceAction(List<Action> actions, Location loc) {
        super(loc);
        this.actions = actions;
    }

    @Override
    public PrettyPrintTree getTree() {
        return new PPTagNode("Action::Sequence",
            getActions().stream().map(Action::getTree).toList());
    }

}
