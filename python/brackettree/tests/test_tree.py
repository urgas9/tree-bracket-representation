import json
import pathlib
from typing import List

import pytest
from brackettree.parser import ParseException, parse


def _read_examples_file(file_name: str) -> List[dict]:
    p = pathlib.Path(__file__).parent.parent.parent.parent.absolute().joinpath("examples").joinpath(file_name)
    with p.open("r") as f:
        return json.load(f)


EXAMPLE_VALID_CASES = _read_examples_file("bracket-tree-valid-cases.json")
EXAMPLE_INVALID_CASES = _read_examples_file("bracket-tree-invalid-cases.json")


@pytest.mark.parametrize("test_case", EXAMPLE_VALID_CASES)
def test_node_parse_valid_bracket_tree(test_case):
    n = parse(test_case["bracketTree"])
    assert n is not None
    assert n.bracket_representation() == test_case["bracketTree"]


@pytest.mark.parametrize("test_case", EXAMPLE_INVALID_CASES)
def test_node_parse_invalid_bracket_tree(test_case):
    with pytest.raises(ParseException):
        parse(test_case["bracketTree"])


@pytest.mark.parametrize("test_case", EXAMPLE_VALID_CASES)
def test_node_count_leaves(test_case):
    n = parse(test_case["bracketTree"])
    assert n.count_leaves() == test_case["numLeaves"]


@pytest.mark.parametrize("name_to_find,expected_bracket_string", [
    ("CD", "CD(Arr(CD))"),
    ("A", "A(CD(Arr(CD)))(E(F)(G))(CD)(H(D)(MN))"),
    ("E", "E(F)(G)"),
    ("MN", "MN"),
])
def test_node_find_existing(name_to_find, expected_bracket_string):
    bracket_tree = "A(CD(Arr(CD)))(E(F)(G))(CD)(H(D)(MN))"
    bt = parse(bracket_tree)

    found = bt.find(name_to_find)
    assert found is not None
    assert found.bracket_representation() == expected_bracket_string


@pytest.mark.parametrize("name_to_find", [
    "non-existing",
    "(",
    ")",
])
def test_node_find_non_existing(name_to_find):
    bt = parse("H(D)(MN)")

    found = bt.find(name_to_find)
    assert found is None


def test_node_add_valid():
    bt = parse("H(D)(MN)")

    bt.add_child("A(H)(K)(L)")
    assert bt.bracket_representation() == "H(D)(MN)(A(H)(K)(L))"


def test_node_add_find_valid():
    bt = parse("H(D(A(C)))(MN)")
    assert bt is not None

    c = bt.find("C")
    assert c is not None

    c.add_child("A(H(K))")
    assert c.bracket_representation() == "C(A(H(K)))"
    assert bt.bracket_representation() == "H(D(A(C(A(H(K))))))(MN)"

    c.add_child("B(C)(D)")
    assert c.bracket_representation() == "C(A(H(K)))(B(C)(D))"
    assert bt.bracket_representation() == "H(D(A(C(A(H(K)))(B(C)(D)))))(MN)"

    bt.add_child("A")
    assert bt.bracket_representation() == "H(D(A(C(A(H(K)))(B(C)(D)))))(MN)(A)"


@pytest.mark.parametrize("test_case", EXAMPLE_INVALID_CASES)
def test_node_add_invalid(test_case):
    bt = parse("H(D)(MN)")
    with pytest.raises(ParseException):
        bt.add_child(test_case["bracketTree"])
