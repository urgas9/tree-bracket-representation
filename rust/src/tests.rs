#[path = "./tree.rs"]
mod tree;

#[test]
fn valid_tree_to_bracket_string() {
    let mut n = tree::Node { name: String::from("A"), children: Vec::new() };
    n.add_child_node(tree::Node { name: String::from("Child"), children: Vec::new() });
    assert_eq!(n.to_bracket_string(), "A(Child)")
}

#[test]
fn valid_bracket_tree_strings() {
    let bracket_tree_string = "A(B)(C)(D(E(F)))(HAHA)";
    let n = tree::parse(bracket_tree_string).unwrap();
    assert_eq!(n.to_bracket_string(), bracket_tree_string)
}
