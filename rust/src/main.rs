mod tree;

fn main() {
    let mut n = tree::Node { name: String::from("sad"), children: Vec::new() };
    n.add_child_node(tree::Node { name: String::from("first child"), children: Vec::new() });
    print!("{}", n);

    let e = tree::ParseError { message: String::from("weird error") };
    print!("{}", e)
}
