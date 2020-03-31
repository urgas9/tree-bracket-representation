package com.github.urgas9.treebracketrepresentation;

public class BracketTreeParser {

    private String bracketTree;

    public BracketTreeParser(String bracketTree) {
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

    private static Node parseTreeNodesFromString(String bracketTree, int startIndex, int endIndex) throws ParseException {
        // First, read  root node name
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

        Node node = new Node(sb.toString(), null);
        // parse children nodes
        int childTreeStartIndex = i;
        while (childTreeStartIndex < endIndex) {
            int childTreeEndIndex = getIndexOfClosingBracket(bracketTree, childTreeStartIndex);
            node.addChild(parseTreeNodesFromString(bracketTree, childTreeStartIndex + 1, childTreeEndIndex));
            childTreeStartIndex = childTreeEndIndex + 1;
        }
        return node;
    }

    public Node parse() throws ParseException {
        return BracketTreeParser.parseTreeNodesFromString(this.bracketTree, 0, this.bracketTree.length());
    }
}
