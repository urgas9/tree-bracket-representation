package com.github.urgas9.treebracketrepresentation;

public class Main {

    public static void main(String[] args) {
        var b = new BracketTree("A(V)(C(D))");
        try {
            System.out.println(b.getIndexOfClosingBracket(1));
            System.out.println(b.getIndexOfClosingBracket(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(b);
    }
}
