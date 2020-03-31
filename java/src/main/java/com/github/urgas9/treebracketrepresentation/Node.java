package com.github.urgas9.treebracketrepresentation;

import java.util.ArrayList;

public class Node {

    private String name;
    private ArrayList<Node> children;

    public Node(String name, ArrayList<Node> children) {
        this.name = name;
        this.children = children;
    }

    public String getName() {
        return this.name;
    }

    public Node find(String name) {
        if (this.name.equals(name)) {
            return this;
        }
        if (this.children != null) {
            for (Node c : this.children) {
                Node h = c.find(name);
                if (h != null) {
                    return h;
                }
            }
        }
        return null;
    }

    public int countLeaves() {
        if (this.children == null || this.children.isEmpty()) {
            return 1;
        }

        int childrenLeavesCount = 0;
        for (Node child : this.children) {
            childrenLeavesCount += child.countLeaves();
        }
        return childrenLeavesCount;
    }

    public void addChild(String childBracketString) throws ParseException {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        Node childNode = new BracketTreeParser(childBracketString).parse();
        this.children.add(childNode);
    }

    public void addChild(Node child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }

    public String toBracketRepresentation() {
        StringBuilder sb = new StringBuilder(this.name);
        if (this.children != null) {
            for (Node child : this.children) {
                sb.append(String.format("(%s)", child.toBracketRepresentation()));
            }
        }
        return sb.toString();
    }
}