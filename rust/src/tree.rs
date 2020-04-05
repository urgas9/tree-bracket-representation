use std::error;
use std::fmt;

pub struct Node {
    pub name: String,
    pub children: Vec<Node>,
}

impl Node {
    pub(crate) fn add_child_node(&mut self, child: Node) {
        self.children.push(child);
    }

    pub fn add_child(&mut self, child_string: String) -> Result<Node, ParseError> {
        return Err(ParseError{message: String::from("not implemented")});
    }

    pub fn count_leaves(&self) -> i32 {
        return -1;
    }

    pub fn find(&self, name: String) -> Result<Node, ParseError> {
        return Err(ParseError{message: String::from("not implemented")});
    }

    pub fn to_bracket_string(&self) -> String {
        let mut bracket_string = String::from(&self.name);
        for c in &self.children {
            bracket_string.push_str("(");
            bracket_string.push_str(&c.to_bracket_string());
            bracket_string.push_str(")");
        }
        return bracket_string;
    }
}

impl fmt::Display for Node {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "Node[name: {}, children_num: {}]", self.name, self.children.len())
    }
}


#[derive(Debug, Clone)]
pub struct ParseError {
    pub message: String
}

impl fmt::Display for ParseError {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "Parse Error: {}", self.message)
    }
}

impl error::Error for ParseError {
    fn source(&self) -> Option<&(dyn error::Error + 'static)> {
        // Generic error, underlying cause isn't tracked.
        None
    }
}