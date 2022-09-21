package org.goodmath.pica.util;

public class PPLeafNode extends PrettyPrintTree {

    private final String name;

    public PPLeafNode(String name) {
        this.name = name;
    }

    @Override
    public void render(int curIndent, StringBuilder target) {
        indent(curIndent, target);
        target.append(getName());
        target.append("\n");
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public String getName() {
        return "[" + name + "]";
    }
}
