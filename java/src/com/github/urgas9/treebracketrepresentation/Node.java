package com.github.urgas9.treebracketrepresentation;

import java.util.ArrayList;

public class Node {

    private String name;
    private ArrayList<Node> children;

    public Node(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public void addChild(Node child) {
        this.children.add(child);
    }
}