package org.goodmath.pica.ast.actions;
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

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.quarks.BosonMessagePatternAction;
import org.goodmath.pica.util.PPFieldNode;
import org.goodmath.pica.util.PPTagNode;
import org.goodmath.pica.util.PrettyPrintTree;

import java.util.List;

public class ReceiveAction extends Action {
    private final String channel;
    private final List<BosonMessagePatternAction> patternActions;

    public ReceiveAction(String channel, List<BosonMessagePatternAction> actions, Location loc) {
        super(loc);
        this.channel = channel;
        this.patternActions = actions;
    }

    public String getChannel() {
        return channel;
    }

    public List<BosonMessagePatternAction> getPatternActions() {
        return patternActions;
    }

    @Override
    public PrettyPrintTree getTree() {
        return new PPTagNode("Action::Receive",
            List.of(new PPFieldNode("channel", getChannel()),
            new PPTagNode("patternActions",
                getPatternActions().stream().map(BosonMessagePatternAction::getTree).toList()
            )
        ));
    }
}