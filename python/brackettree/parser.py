from python.brackettree.tree import Node


class ParseException(Exception):
    pass


def parse(bracket_tree: str) -> Node:
    return _parse_bracket_tree_string(bracket_tree, 0, len(bracket_tree))


def _index_of_closing_bracket(bracket_tree: str, start_index: int) -> int:
    if bracket_tree[start_index] != '(':
        raise ParseException(f"expected '(' at index '{start_index}', but got '{bracket_tree[start_index]}'")

    bracket_counter = 1
    for i in range(start_index + 1, len(bracket_tree)):
        if bracket_tree[i] == '(':
            bracket_counter += 1
        if bracket_tree[i] == ')':
            bracket_counter -= 1
        if bracket_counter == 0:
            return i

    raise ParseException("ran till the end, but could not find the ending bracket")


def _parse_bracket_tree_string(bracket_tree: str, start_index: int, end_index: int) -> Node:
    # find name of the root node
    children_start_index = bracket_tree.find('(', start_index, end_index)
    if children_start_index == -1:
        children_start_index = end_index
    node_name = bracket_tree[start_index:children_start_index]
    if node_name == '':
        raise ParseException(f"node value at index {start_index} should not be empty")

    n = Node(node_name)

    while children_start_index < end_index:
        child_end_index = _index_of_closing_bracket(bracket_tree, children_start_index)
        n.children.append(_parse_bracket_tree_string(bracket_tree, children_start_index + 1, child_end_index))
        children_start_index = child_end_index + 1

    return n
