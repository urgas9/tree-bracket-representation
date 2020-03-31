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
        StringBuilder sb = new StringBuilder(this.name);
        if (this.children != null) {
            for (Node child : this.children) {
                sb.append(String.format("(%s)", child.toBracketRepresentation()));
            }
        }
        return sb.toString();
    }

    protected static int getIndexOfClosingBracket(String bracketTree, int startIndex) throws ParseException {
        if (bracketTree.charAt(startIndex) != '(') {
            throw new ParseException(String.format("expected '%s' but found '%s' at index %s", '(', bracketTree.charAt(startIndex), startIndex));
        }
        int bracketsCount = 1;
        for (int i = startIndex + 1; i < bracketTree.length(); i++) {
            if (bracketTree.charAt(i) == '(') {
                bracketsCount++;
            }
            if (bracketTree.charAt(i) == ')') {
                bracketsCount--;
            }
            if (bracketsCount == 0) {
                return i;
            }
        }
        return -1;
    }

    protected static Node parseTreeFromString(String bracketTree, int startIndex, int endIndex) throws ParseException {
        // First, read node name
        StringBuilder sb = new StringBuilder();
        int i = startIndex;
        char currentChar;
        while (i < endIndex && (currentChar = bracketTree.charAt(i)) != '(') {
            sb.append(currentChar);
            i++;
        }

        if (sb.length() == 0) {
            throw new ParseException(String.format("node name of the tree starting at index %x is empty", startIndex));
        }

        // Create root node
        Node node = new Node(sb.toString());
        // start parsing children
        int childTreeStartIndex = i;
        while (childTreeStartIndex < endIndex) {
            int childTreeEndIndex = getIndexOfClosingBracket(bracketTree, childTreeStartIndex);
            node.addChild(parseTreeFromString(bracketTree, childTreeStartIndex + 1, childTreeEndIndex));
            childTreeStartIndex = childTreeEndIndex + 1;
        }
        return node;
    }
}