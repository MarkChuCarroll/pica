package org.goodmath.pica.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PPTagNode extends PrettyPrintTree {
    public PPTagNode(String tag, List<PrettyPrintTree> children) {
        this.tag = tag;
        this.children = new ArrayList<>();
        this.children.addAll(children);
    }

    public PPTagNode(String tag) {
        this(tag, Collections.emptyList());
    }

    private final String tag;
    public String getTag() {
        return tag;
    }
    private final List<PrettyPrintTree> children;
    public List<PrettyPrintTree> getChildren() {
        return children;
    }

    @Override
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    @Override
    public String getName() {
        return getTag();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        render(0, sb);
        return sb.toString();
    }

    public void render(int curIndent, StringBuilder target) {
        if (children.isEmpty()) {
            indent(curIndent, target);
            target.append(getTag()).append("\n");
            indent(curIndent + 1, target);
            target.append("<<none>>\n");
        } else {
            indent(curIndent, target);
            target.append(getTag()).append("{\n");
            for (PrettyPrintTree child: children) {
                child.render(curIndent+1, target);
            }
            indent(curIndent, target);
            target.append("}\n");
        }
    }

}
