package org.goodmath.pica.util;

public abstract class PrettyPrintTree {
    public abstract void render(int curIndent, StringBuilder target);

    public abstract boolean hasChildren();

    public abstract String getName();

    protected void indent(int count, StringBuilder sb) {
        for (int i = 0; i < count; i++) {
            sb.append("   ");
        }
    }


}
