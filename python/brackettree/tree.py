from typing import List


class Node:

    def __init__(self, name: str) -> None:
        self.name: str = name
        self.children: List[Node] = []

    def find(self, name: str) -> "Node":
        raise NotImplementedError()

    def bracket_representation(self) -> str:
        # avoiding string concatenation since strings are immutable object -
        # thus creating list of string and joining on return
        s = [f'{self.name}']
        s.extend([f'({c.bracket_representation()})' for c in self.children])
        return ''.join(s)

    def count_leaves(self) -> int:
        if len(self.children) == 0:
            return 1
        return sum([c.count_leaves() for c in self.children])

    def add_child(self, bracket_tree: str) -> None:
        raise NotImplementedError()
