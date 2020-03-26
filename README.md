# tree-bracket-representation

[![Lint Status](https://github.com/urgas9/tree-bracket-representation/workflows/Go/badge.svg)](https://github.com/urgas9/tree-bracket-representation)

Reading and writing of a string tree bracket representation in multiple programming languages.

## What is this project about?

It is a simple hoby project, created mainly to learn new tools and languages and to be able to directly compare pros and cons of each language. 
So there will be many solutions in the repo, divided by programming languages, e.g. `java` and `go`.

The goal of the program is to read and write tree structure from a bracket string representation. 
Read more on the representation from this [link](https://www.geeksforgeeks.org/binary-tree-string-brackets/) 
but bare in mind that this library supports reading and writing of general trees as well.
 
For example, the below tree:

            A
      /     |      \
    CD      E       I
           / \
           F  G

could simply be presented as:

    A(CD)(E(F)(G))(I)


on the other hand, here are some examples of invalid tree strings:

    A(CD)a      // there is undefined node a
    ()          // two errors: the root node and its first child have no name
    (A)         // the root node has no name
    
See more [valid](./examples/bracket-tree-valid-cases.json) and [invalid](./examples/bracket-tree-invalid-cases.json) 
examples in the files used for tests across different implementations in `./examples` folder.

## Example usage in Go

    // Create a new tree from string
    bt := NewBracketTree("A(B)(C)")
    
    // Write the tree to a bracket representation
    out, err := bt.BracketRepresentation()
