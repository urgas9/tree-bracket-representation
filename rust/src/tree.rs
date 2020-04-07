use std::fmt;

#[derive(Debug)]
pub struct Node {
    pub name: String,
    pub children: Vec<Node>,
}

impl Node {
    pub(crate) fn add_child_node(&mut self, child: Node) {
        self.children.push(child);
    }

    pub fn add_child(&mut self, child_string: String) -> Result<Node, ParseError> {
        Err(ParseError { message: String::from("not implemented") })
    }

    pub fn count_leaves(&self) -> i32 {
        if self.children.is_empty() {
            return 1;
        }
        let mut sum: i32 = 0;
        for c in &self.children {
            sum += c.count_leaves();
        }
        sum
    }

    pub fn find(&self, name: &str) -> Result<Option<&Node>, ParseError> {
        if self.name == name {
            return Ok(Some(self));
        }
        for c in &self.children {
            let k = c.find(name)?;
            if k.is_some() {
                return Ok(k);
            }
        }
        Ok(None)
    }

    pub fn to_bracket_string(&self) -> String {
        let mut bracket_string = self.name.to_string();
        for c in &self.children {
            bracket_string.push_str("(");
            bracket_string.push_str(&c.to_bracket_string());
            bracket_string.push_str(")");
        }
        bracket_string
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

pub fn parse(bracket_tree: &str) -> Result<Node, ParseError> {
    let char_vec: Vec<char> = bracket_tree.chars().collect();
    parse_bracket_string_to_node(&char_vec, 0, bracket_tree.len())
}

fn get_closing_bracket_index(bracket_tree: &[char], start_index: usize) -> Result<usize, ParseError> {
    if bracket_tree[start_index] != '(' {
        return Err(ParseError { message: format!("expected '{}' but found '{}' at index {}", "(", bracket_tree[start_index], start_index) });
    }

    let mut bracket_counter = 1;
    for (i, ch) in bracket_tree.iter().enumerate().skip(start_index + 1) {
        match ch {
            '(' => bracket_counter += 1,
            ')' => bracket_counter -= 1,
            _ => {}
        }
        if bracket_counter == 0 {
            return Ok(i);
        }
    }

    Err(ParseError { message: String::from("reached the end of string and could not find the matching closing bracket") })
}

fn parse_bracket_string_to_node(bracket_tree: &[char], start_index: usize, end_index: usize) -> Result<Node, ParseError> {
    let mut name = String::new();
    let mut child_start_index = start_index;
    while child_start_index < end_index && bracket_tree[child_start_index] != '(' {
        name.push(bracket_tree[child_start_index]);
        child_start_index += 1;
    }
    if name.is_empty() {
        return Err(ParseError { message: format!("node name of the tree starting at index {} is empty", start_index) });
    }

    let mut children = vec![];
    while child_start_index < end_index {
        let child_end_index = get_closing_bracket_index(bracket_tree, child_start_index)?;
        let child = parse_bracket_string_to_node(bracket_tree, child_start_index + 1, child_end_index)?;
        children.push(child);
        child_start_index = child_end_index + 1;
    }

    Ok(Node { name, children })
}