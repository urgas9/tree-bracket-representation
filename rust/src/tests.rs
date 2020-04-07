use std::fs;
use std::path::Path;

use serde::{Deserialize, Serialize};

#[path = "./tree.rs"]
mod tree;


#[derive(Serialize, Deserialize, Debug)]
#[serde(rename_all = "camelCase")]
struct TestCase {
    name: Option<String>,
    bracket_tree: String,
    num_leaves: Option<i32>,
}

fn read_examples_file(filename: &str) -> Vec<TestCase> {
    let file_path = Path::new("../examples/").join(Path::new(filename));
    let data = fs::read_to_string(file_path.as_path()).expect("Unable to read file");
    let test_cases: Vec<TestCase> = serde_json::from_str(&data).expect("JSON was not well-formatted");
    return test_cases;
}

#[test]
fn valid_tree_to_bracket_string() {
    let mut n = tree::Node { name: String::from("A"), children: Vec::new() };
    n.add_child_node(tree::Node { name: String::from("Child"), children: Vec::new() });
    assert_eq!(n.to_bracket_representation(), "A(Child)")
}

#[test]
fn valid_bracket_tree_strings() {
    let test_cases = read_examples_file("bracket-tree-valid-cases.json");
    for tc in test_cases {
        let mut n = tree::parse(tc.bracket_tree.as_str()).unwrap();
        assert_eq!(n.to_bracket_representation(), tc.bracket_tree.as_str())
    }
}

#[test]
fn invalid_bracket_tree_strings() {
    let test_cases = read_examples_file("bracket-tree-invalid-cases.json");
    for tc in test_cases {
        let result = tree::parse(tc.bracket_tree.as_str());
        assert!(result.is_err());
    }
}

#[test]
fn count_leaves_valid_bracket_tree_strings() {
    let test_cases = read_examples_file("bracket-tree-valid-cases.json");
    for tc in test_cases {
        let n = tree::parse(tc.bracket_tree.as_str()).unwrap();
        assert_eq!(n.count_leaves(), tc.num_leaves.unwrap())
    }
}

#[test]
fn find_existing_node() {
    let test_cases: Vec<(&str, &str)> = vec![
        ("CD", "CD(Arr(CD))"),
        ("A", "A(CD(Arr(CD)))(E(F)(G))(CD)(H(D)(MN))"),
        ("E", "E(F)(G)"),
        ("MN", "MN")
    ];

    let mut bt = tree::parse("A(CD(Arr(CD)))(E(F)(G))(CD)(H(D)(MN))").unwrap();
    for tc in test_cases {
        let found = bt.find(tc.0).unwrap();
        assert!(found.is_some());
        assert_eq!(found.unwrap().to_bracket_representation(), tc.1)
    }
}

#[test]
fn find_non_existing_node() {
    let test_cases: Vec<&str> = vec![
        "non-existing",
        "(",
        ")",
        "ll"
    ];

    let mut bt = tree::parse("H(D)(MN)").unwrap();
    for tc in test_cases {
        let found = bt.find(tc).unwrap();
        assert!(found.is_none());
    }
}

#[test]
fn add_valid_child() {
    let mut bt = tree::parse("H(D)(MN)").unwrap();

    bt.add_child("A(H)(K)(L)");
    assert_eq!(bt.to_bracket_representation(), "H(D)(MN)(A(H)(K)(L))")
}

#[test]
fn add_child_find_valid() {
    let mut bt = &mut tree::parse("H(D(A(C)))(MN)").unwrap();

    let ch = bt.find("C").unwrap().unwrap();
    ch.add_child("A(H(K))");
    assert_eq!(ch.to_bracket_representation(), "C(A(H(K)))");
    assert_eq!(bt.to_bracket_representation(), "H(D(A(C(A(H(K))))))(MN)");

    // TODO: solve error[E0499]: cannot borrow `*bt` as mutable more than once at a time
    // ch.add_child("B(C)(D)").unwrap();
    // assert_eq!(ch.to_bracket_representation(), "C(A(H(K)))(B(C)(D))");
    // assert_eq!(bt.to_bracket_representation(), "H(D(A(C(A(H(K)))(B(C)(D)))))(MN)");
    //
    // ch.add_child("A").unwrap();
    // assert_eq!(bt.to_bracket_representation(), "H(D(A(C(A(H(K)))(B(C)(D)))))(MN)(A)")
}

#[test]
fn add_child_invalid() {
    let test_cases = read_examples_file("bracket-tree-invalid-cases.json");
    let mut bt = tree::parse("H(D(A(C)))(MN)").unwrap();
    for tc in test_cases {
        let result = bt.add_child(tc.bracket_tree.as_str());
        assert!(result.is_err())
    }
}