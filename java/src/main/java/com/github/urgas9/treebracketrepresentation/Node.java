package com.github.urgas9.treebracketrepresentation;

import java.util.ArrayList;

public class Node {

    private String name;
    private ArrayList<Node> children;

    public Node(String name) {
        this.name = name;
        this.children = null;
    }

    public void addChild(Node child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }

    public String getName() {
        return this.name;
    }

    public String toBracketRepresentation() {
        var sb = new StringBuilder(this.name);
        if (this.children != null) {
            for (var child : this.children) {
                sb.append(String.format("(%s)", child.toBracketRepresentation()));
            }
        }
        return sb.toString();
    }
}