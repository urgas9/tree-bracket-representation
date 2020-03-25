# tree-bracket-representation

[![Lint Status](https://github.com/urgas9/tree-bracket-representation/workflows/Go/badge.svg)](https://github.com/urgas9/tree-bracket-representation)

Reading and writing of a string tree bracket representation

## What does it solve

It is a simple hoby, created mainly to learn new tools and languages. The goal of the program is to read and write trees
from a bracket string representation. Read more on the representation from this [link](https://www.geeksforgeeks.org/binary-tree-string-brackets/) 
but bare in mind that this library supports reading and writing of general trees as well.
 
For example, the below tree:

            A
      /     |      \
    CD      E       I
           / \
           F  G

Could be simply presented as:

    A(CD)(E(F)(G))(I)


But on the other hand, here are some examples of invalid tree strings:

    A(CD)a
    ()
    (A)
    
## Example usage in Go

    // Create a new tree from string
    bt := NewBracketTree("A(B)(C)")
    
    // Write the tree to a bracket representation
    out, err := bt.BracketRepresentation()
