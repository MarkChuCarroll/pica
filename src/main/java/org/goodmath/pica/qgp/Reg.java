package org.goodmath.pica.qgp;

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
    public static class StackReg extends Reg {
        public StackReg() {
        }
    }

    public static class NamedReg extends Reg {
        private final String name;

        public NamedReg(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
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
    }

    public static class IndirectReg extends Reg {
        private final Reg r;

        public IndirectReg(Reg r) {
            this.r = r;
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
    }
}
