package com.github.urgas9.treebracketrepresentation;

public class BracketTree {
    private String original;
    private Node rootNode;

    public BracketTree(String treeString) {
        this.original = treeString;
    }

    public Node getRootNode() {
        return this.rootNode;
    }

    public String toBracketRepresentation() {
        return this.rootNode.toBracketRepresentation();
    }

    public void parse() {
        try {
            this.rootNode = this.parseTreeFromString(0, this.original.length());
        } catch (Exception e) {
            System.err.println("cannot parse tree " + e.getMessage());
        }
    }

    protected int getIndexOfClosingBracket(int startIndex) throws ParseException {
        if (this.original.charAt(startIndex) != '(') {
            throw new ParseException(String.format("expected '%s' but found '%s' at index %s", '(', this.original.charAt(startIndex), startIndex));
        }
        int bracketsCount = 1;
        for (int i = startIndex + 1; i < this.original.length(); i++) {
            if (this.original.charAt(i) == '(') {
                bracketsCount++;
            }
            if (this.original.charAt(i) == ')') {
                bracketsCount--;
            }
            if (bracketsCount == 0) {
                return i;
            }
        }
        return -1;
    }

    protected Node parseTreeFromString(int startIndex, int endIndex) throws ParseException {
        // First, read node name
        StringBuilder sb = new StringBuilder();
        int i = startIndex;
        char currentChar;
        while (i < endIndex && (currentChar = this.original.charAt(i)) != '(') {
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
            int childTreeEndIndex = this.getIndexOfClosingBracket(childTreeStartIndex);
            node.addChild(this.parseTreeFromString(childTreeStartIndex + 1, childTreeEndIndex));
            childTreeStartIndex = childTreeEndIndex + 1;
        }
        return node;
    }
}
