from typing import Self, List, Optional

from brackettree import parser


class Node:

    name: str
    children: List[Self]

    def __init__(self, name: str) -> None:
        self.name = name
        self.children = []

    def find(self, name: str) -> Optional["Node"]:
        if self.name == name:
            return self
        for c in self.children:
            r = c.find(name)
            if r is not None:
                return r
        return None

    def bracket_representation(self) -> str:
        # avoiding string concatenation since strings are immutable object -
        # thus creating list of string and joining on return
        s = [self.name]
        s.extend(["({})".format(c.bracket_representation()) for c in self.children])
        return "".join(s)

    def count_leaves(self) -> int:
        if len(self.children) == 0:
            return 1
        return sum(c.count_leaves() for c in self.children)

    def add_child(self, bracket_tree: str) -> None:
        self.children.append(parser.parse(bracket_tree))
