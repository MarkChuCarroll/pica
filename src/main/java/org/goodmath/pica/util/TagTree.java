package org.goodmath.pica.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TagTree {
    public TagTree(String tag, List<TagTree> children) {
        this.tag = tag;
        this.children = new ArrayList<>();
        this.children.addAll(children);
    }

    public TagTree(String tag) {
        this(tag, Collections.emptyList());
    }

    private String tag;
    public String getTag() {
        return tag;
    }
    private List<TagTree> children;
    public List<TagTree> getChildren() {
        return children;
    }

    private static void indent(int count, StringBuilder target) {
        for (int i = 0; i < count; i++) {
            target.append("   ");
        }
    }

    protected void render(int curIndent, StringBuilder target) {
        if (children.isEmpty()) {
            indent(curIndent, target);
            target.append(getTag());
            target.append("\n");
        } else {
            indent(curIndent, target);
            target.append(getTag());
            target.append("{\n");
            for (TagTree child: children) {
                child.render(curIndent+1, target);
            }
            indent(curIndent, target);
            target.append("}\n");
        }
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        render(0, sb);
        return sb.toString();
    }

}
