# tree-bracket-representation
Reading and writing of a string tree bracket representation

## What does it solve

A tree:

            A
      /     |      \
    CD      E       I
           / \
           F  G

Could be simply presented as:

    A(CD)(E(F)(G))(I)


Should fail

    A(CD)a
