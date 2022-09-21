package org.goodmath.pica.util;

public class PPFieldNode extends PrettyPrintTree {
    private final String fieldName;
    private final String value;

    public PPFieldNode(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    @Override
    public void render(int curIndent, StringBuilder target) {
        indent(curIndent, target);
        target.append(fieldName).append("=").append(value).append("\n");
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }
}
