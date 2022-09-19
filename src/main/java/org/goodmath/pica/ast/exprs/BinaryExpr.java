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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.goodmath.pica.ast.locations.Location;

import java.io.IOException;
import java.util.HashMap;

@JsonSerialize(using = BinaryExpr.BinaryExprSerializer.class)
public class BinaryExpr extends Expr {
    public enum Operator {
        And, Or, Eq, NotEq, Greater, GreaterEq, Less, LessEq, Plus, Minus,
        Times, Div, Modulo;

        static HashMap<String, Operator> ops = null;


        public static Operator fromString(String s) {
            if (ops == null) {
                ops = new HashMap<>();
                ops.put("and", And);
                ops.put("or", Or);
                ops.put("==", Eq);
                ops.put("!=", NotEq);
                ops.put(">", Greater);
                ops.put(">=", GreaterEq);
                ops.put("<", Less);
                ops.put("<=", LessEq);
                ops.put("+", Plus);
                ops.put("-", Minus);
                ops.put("*", Times);
                ops.put("/", Div);
                ops.put("%", Modulo);
            }
            if (ops.containsKey(s)) {
                return ops.get(s);
            } else {
                throw new RuntimeException("Unknown operator: '" + s + "'");
            }
        }
    }

    private final Operator op;
    public Operator getOp() {
        return op;
    }

    private final Expr left;
    public Expr getLeft() {
        return left;
    }

    private final Expr right;

    public Expr getRight() {
        return right;
    }

    public BinaryExpr(Operator op, Expr left, Expr right, Location loc) {
        super(loc);
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public static class BinaryExprSerializer extends JsonSerializer<BinaryExpr> {

        @Override
        public void serialize(BinaryExpr value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("kind", "BinaryExpr");
            gen.writeStringField("op", value.getOp().toString());
            gen.writeObjectField("left", value.getLeft());
            gen.writeObjectField("right", value.getRight());
            gen.writeEndObject();
        }
    }

}
