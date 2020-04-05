#[path = "./tree.rs"]
mod tree;

#[test]
fn valid_tree_to_bracket_string() {
    let mut n = tree::Node { name: String::from("A"), children: Vec::new() };
    n.add_child_node(tree::Node { name: String::from("Child"), children: Vec::new() });
    assert_eq!(n.to_bracket_string(), "A(Child)")
}

#[test]
fn another() {
    assert_eq!(1 + 3, 4);
    //panic!("Make this test fail");
}
