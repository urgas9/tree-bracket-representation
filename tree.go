package main

import "fmt"

type BracketTree struct {
	original string
	node     *Node
	error    error
}

func NewBracketTree(treeString string) BracketTree {
	n, err := nodeTreeFromString([]rune(treeString), 0, len(treeString))

	return BracketTree{
		original: treeString,
		node:     n,
		error:    err,
	}
}

func (b *BracketTree) Parse() error {
	if b.Valid() {
		n, err := nodeTreeFromString([]rune(b.original), 0, len(b.original))
		b.node = n
		return err
	}
	return fmt.Errorf("%q is not a valid tree representation", b.original)
}

func (b *BracketTree) Valid() bool {
	return b.error == nil
}

func (b *BracketTree) BracketRepresentation() string {
	if b.error != nil {
		return b.error.Error()
	}
	return b.node.BracketRepresentation()
}

type Node struct {
	Value    string
	Children []Node
}

func (n Node) BracketRepresentation() string {
	str := n.Value
	for _, c := range n.Children {
		str += fmt.Sprintf("(%s)", c.BracketRepresentation())
	}
	return str
}

// indexOfClosingBracket is a helper function returning an index of the closing bracket
func indexOfClosingBracket(treeRune []rune, startParenthesisIndex int) (int, error) {
	if treeRune[startParenthesisIndex] != '(' {
		return -1, fmt.Errorf(fmt.Sprintf("expected %q at index %v, but got %q", '(', startParenthesisIndex, treeRune[startParenthesisIndex]))
	}
	bracketCounter := 1
	for i := startParenthesisIndex + 1; i < len(treeRune); i++ {
		if treeRune[i] == '(' {
			bracketCounter++
		}
		if treeRune[i] == ')' {
			bracketCounter--
		}
		if bracketCounter == 0 {
			return i, nil
		}
	}
	return -1, fmt.Errorf("ran till the end, but could not find the ending bracket")
}

// nodeTreeFromString is a helper recursive function that takes an array of runes as input and returns the tree structure - object Node
func nodeTreeFromString(treeRune []rune, treeStartIndex int, treeEndIndex int) (*Node, error) {
	i := treeStartIndex
	var val string
	for i < treeEndIndex && treeRune[i] != '(' && treeRune[i] != ')' {
		val += string(treeRune[i])
		i++
	}
	if val == "" {
		return nil, fmt.Errorf(fmt.Sprintf("node value at index %v should not be empty", treeStartIndex))
	}
	node := &Node{Value: val}

	childI := i
	for childI < treeEndIndex {
		if treeRune[childI] != '(' {
			return node, fmt.Errorf("not a valid tree representation, failed at index %v with %q", childI, treeRune[childI])
		}
		closingI, err := indexOfClosingBracket(treeRune, childI)
		if err != nil {
			return node, err
		}
		childTree, err := nodeTreeFromString(treeRune, childI+1, closingI)
		if err != nil {
			return node, err
		}
		node.Children = append(node.Children, *childTree)
		childI = closingI + 1
	}
	return node, nil
}

// validRepresentation is a helper function which
func validRepresentation(treeRune []rune) bool {
	parenCounter := 0
	lastOpening := 0
	for i := 0; i < len(treeRune); i++ {
		if treeRune[i] == '(' {
			if lastOpening >= i {
				return false // there is a tree node without a name, something like ((, or string starting with (
			}
			lastOpening = i
			parenCounter++
		}
		if treeRune[i] == ')' {
			if i-lastOpening <= 1 {
				return false // there is a tree node without a name, something like ()
			}
			parenCounter--
		}
	}
	return parenCounter == 0
}
