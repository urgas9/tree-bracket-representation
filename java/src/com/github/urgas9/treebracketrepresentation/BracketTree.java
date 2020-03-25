package com.github.urgas9.treebracketrepresentation;

public class BracketTree {
    private String original;
    private Node rootNode;

    public BracketTree(String treeString) {
        this.original = treeString;
    }

    protected int getIndexOfClosingBracket(int startIndex) throws Exception {
        if (this.original.charAt(startIndex) != '(') {
            throw new Exception("expected '(' but found " + this.original.charAt(startIndex));
        }
        var bracketsCount = 1;
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
    
}
