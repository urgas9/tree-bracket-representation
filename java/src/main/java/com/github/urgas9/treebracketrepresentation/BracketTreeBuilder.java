package com.github.urgas9.treebracketrepresentation;

public class BracketTreeBuilder {

    private String bracketTree;

    public BracketTreeBuilder(String bracketTree) {
        this.bracketTree = bracketTree;
    }

    public Node build() throws ParseException {
        return Node.parseTreeFromString(this.bracketTree, 0, this.bracketTree.length());
    }
}
