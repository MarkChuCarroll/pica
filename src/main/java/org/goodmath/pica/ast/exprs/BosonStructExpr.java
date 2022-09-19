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
import java.util.Map;

import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.TagTree;

public class BosonStructExpr extends Expr {
    private final Identifier tag;
    public Identifier getTag() {
        return tag;
    }

    private final Map<String, Expr> fields;

    public Map<String, Expr> getFields() {
        return fields;
    }

    public BosonStructExpr(Identifier tag, Map<String, Expr> fields, Location loc) {
        super(loc);
        this.tag = tag;
        this.fields = fields;
    }

    @Override
    public TagTree getTree() {
        return new TagTree("BosonStructExpr",
            List.of(getTag().getTree(),
                new TagTree("fields",
                    getFields().entrySet().stream().map(entry ->
                        new TagTree("field",
                            List.of(new TagTree(entry.getKey()),
                                entry.getValue().getTree()))).toList())));
    }

}
