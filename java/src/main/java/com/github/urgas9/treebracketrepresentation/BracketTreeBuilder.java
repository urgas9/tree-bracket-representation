package com.github.urgas9.treebracketrepresentation;

import java.util.ArrayList;

public class BracketTreeBuilder {

    private String bracketTree;

    public BracketTreeBuilder(String bracketTree) {
        this.bracketTree = bracketTree;
    }

    private static int getIndexOfClosingBracket(String bracketTree, int startIndex) throws ParseException {
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

    static ArrayList<Node> parseChildrenNodesFromString(String bracketTree, int childTreeStartIndex, int endIndex) throws ParseException {
        ArrayList<Node> childrenNodes = new ArrayList<>();
        while (childTreeStartIndex < endIndex) {
            int childTreeEndIndex = getIndexOfClosingBracket(bracketTree, childTreeStartIndex);
            childrenNodes.add(parseTreeNodesFromString(bracketTree, childTreeStartIndex + 1, childTreeEndIndex));
            childTreeStartIndex = childTreeEndIndex + 1;
        }
        return childrenNodes;
    }

    private static Node parseTreeNodesFromString(String bracketTree, int startIndex, int endIndex) throws ParseException {
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

        // recursive call to parse children nodes
        return new Node(sb.toString(), parseChildrenNodesFromString(bracketTree, i, endIndex));
    }

    public Node build() throws ParseException {
        return BracketTreeBuilder.parseTreeNodesFromString(this.bracketTree, 0, this.bracketTree.length());
    }
}
