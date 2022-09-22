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
package org.goodmath.pica.vm;

/**
 * The machine has unlimited registers, addressed by a unique,
 * system provided name. These registers are garbage collected, so
 * you don't need to worry about releasing them: any register ID
 * which is referenced by a boson, by a quark state field,
 * by a local register in active ray, or by a value stored in
 * another live register will be preserved.
 *
 * <p></p>There's also local state for a ray. The state is only guaranteed
 * to be preserved up to the next send, receive, or parallel branch (which
 * terminate the ray), so it's very much temporary space.
 * The local storage is numbered registers.</p>
 */
public class Reg {

    public static class NamedReg extends Reg {
        private final String name;

        public NamedReg(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    public static class NumberedReg extends Reg {
        private final int num;

        public NumberedReg(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        @Override
        public String toString() {
            return "$" + getNum();
        }
    }

    public static class IndirectReg extends Reg {
        private final Reg r;

        public IndirectReg(Reg r) {
            this.r = r;
        }

        @Override
        public String toString() {
            return "(" + r.toString() + ")";
        }

        public Reg getR() {
            return r;
        }
    }

    // A literal value is treated effectively as a special reg containing the
    // value.
    public static class Literal extends Reg {
        private final Object val;

        public Literal(Object val) {
            this.val = val;
        }

        public Object getVal() {
            return val;
        }

        @Override
        public String toString() {
            return getVal().toString();
        }
    }

    public static class QuarkReg extends Reg {
        public QuarkReg() {
        }

        @Override
        public String toString() {
            return "@";
        }
    }
}
