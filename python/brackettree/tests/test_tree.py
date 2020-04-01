import json
from typing import List

import pytest
from brackettree.parser import ParseException, parse


def read_examples_file(file_path: str) -> List[dict]:
    with open(file_path, 'r') as f:
        return json.load(f)


EXAMPLE_VALID_CASES = read_examples_file('../../../examples/bracket-tree-valid-cases.json')
EXAMPLE_INVALID_CASES = read_examples_file('../../../examples/bracket-tree-invalid-cases.json')


@pytest.mark.parametrize('test_case', EXAMPLE_VALID_CASES)
def test_node_parse_valid_bracket_tree(test_case):
    n = parse(test_case['bracketTree'])
    assert n is not None
    assert n.bracket_representation() == test_case['bracketTree']


@pytest.mark.parametrize('test_case', EXAMPLE_INVALID_CASES)
def test_node_parse_invalid_bracket_tree(test_case):
    with pytest.raises(ParseException):
        parse(test_case['bracketTree'])


@pytest.mark.parametrize('test_case', EXAMPLE_VALID_CASES)
def test_node_count_leaves(test_case):
    n = parse(test_case['bracketTree'])
    assert n.count_leaves() == test_case['numLeaves']


@pytest.mark.parametrize('name_to_find,expected_bracket_string', [
    ("CD", "CD(Arr(CD))"),
    ("A", "A(CD(Arr(CD)))(E(F)(G))(CD)(H(D)(MN))"),
    ("E", "E(F)(G)"),
    ("MN", "MN"),
])
def test_find_existing(name_to_find, expected_bracket_string):
    bracket_tree = "A(CD(Arr(CD)))(E(F)(G))(CD)(H(D)(MN))"
    bt = parse(bracket_tree)

    found = bt.find(name_to_find)
    assert found is not None
    assert found.bracket_representation() == expected_bracket_string


@pytest.mark.parametrize('name_to_find', [
    "non-existing",
    "(",
    ")",
])
def test_find_non_existing(name_to_find):
    bracket_tree = "H(D)(MN)"
    bt = parse(bracket_tree)

    found = bt.find(name_to_find)
    assert found is None
