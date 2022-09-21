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
package org.goodmath.pica.ast;

import org.goodmath.pica.ast.actions.Action;
import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.types.Type;
import org.goodmath.pica.types.Defined;
import org.goodmath.pica.util.PPFieldNode;
import org.goodmath.pica.util.PPTagNode;
import org.goodmath.pica.util.PrettyPrintTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FunctionDef extends Definition {
    private final List<TypedParameter> params;
    private final Type resultType;
    private final Action action;

    public FunctionDef(String name, Optional<List<TypeParamSpec>> typeParams,
                       List<TypedParameter> params,
                       Type resultType,
                       Action action,
                       Location loc) {
        super(name, typeParams, loc);
        this.params = params;
        this.resultType = resultType;
        this.action = action;
    }

    public List<TypedParameter> getParams() {
        return params;
    }

    public Type getResultType() {
        return resultType;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public List<Defined> getDefinedNames() {
        return List.of(Defined.functionDefinition(getName(), this));
    }

    @Override
    public PrettyPrintTree getTree() {
        List<PrettyPrintTree> children = new ArrayList<>();
        children.add(new PPFieldNode("name", getName()));
        getTypeParams().ifPresent(tps ->
            children.add(new PPTagNode("TypeParams",
                tps.stream().map(TypeParamSpec::getTree).toList()))
        );
        children.add(new PPTagNode("Params",
            getParams().stream().map(TypedParameter::getTree).toList()));
        children.add(getResultType().getTree());
        children.add(getAction().getTree());


        return new PPTagNode("Def::Function",
            children);

    }
}
