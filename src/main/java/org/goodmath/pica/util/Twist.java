package org.goodmath.pica.util;

import java.util.ArrayList;
import java.util.List;

public abstract class Twist implements Twistable {
    public abstract void render(int curIndent, StringBuilder target);

    @Override
    public Twist twist() {
        return this;
    }

    private static class ObjectNode extends Twist {
        private String name;
        private List<Twistable> children;
        public ObjectNode(String name, List<Twistable> children) {
            this.name = name;
            this.children = children;
        }

        @Override
        public void render(int i, StringBuilder sb) {
            indent(i, sb);
            sb.append("obj ").append(name).append(":\n");
            for(Twistable child: children) {
                child.twist().render(i+1, sb);
            }
        }
    }

    private static class AttributeNode extends Twist {
        private String name;
        private String value;
        public AttributeNode(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public void render(int curIndent, StringBuilder target) {
            indent(curIndent, target);
            target.append(name).append(" = ").append(value).append("\n");
        }
    }

    private static class ValueNode extends Twist {
        private final String name;
        private final Twistable value;

        public ValueNode(String name, Twistable value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public void render(int curIndent, StringBuilder target) {
            indent(curIndent, target);
            target.append(name).append(":\n");
            value.twist().render(curIndent + 1, target);
        }
    }

    private static class ArrayNode extends Twist {
        private final String name;
        private final List<? extends Twistable> values;
        public ArrayNode(String name, List<? extends Twistable> children) {
            this.name = name;
            this.values = children;
        }

        public ArrayNode(String name, Twistable... children) {
            this(name, List.of(children));
        }

        @Override
        public void render(int curIndent, StringBuilder target) {
            if (!values.isEmpty()) {
                indent(curIndent, target);
                target.append(name).append(" = array:\n");
                for (Twistable t : values) {
                    t.twist().render(curIndent + 1, target);
                }
            }
        }
    }

    public static Twist obj(String name, List<Twistable> children) {
        return new ObjectNode(name, children);
    }

    public static Twist obj(String name, Twistable... children) {
        return new ObjectNode(name, List.of(children));
    }

    public static Twist attr(String name, String val) {
        return new AttributeNode(name, val);
    }

    public static Twist val(String name, Twistable value) {
        return new ValueNode(name, value);
    }

    public static Twist arr(String name, List<? extends Twistable> values) {
        return new ArrayNode(name, values);
    }

    public static Twist arr(String name, Twistable... values) {
        return new ArrayNode(name, List.of(values));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        render(0, sb);
        return sb.toString();
    }

    protected void indent(int count, StringBuilder sb) {
        sb.append("   ".repeat(count));
    }

}
