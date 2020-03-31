package brackettree

import (
	"fmt"
	"strings"
)

// NewBracketTree parses bracket tree string and return the root node object of the tree
func NewBracketTree(treeString string) (*Node, error) {
	return parseTreeNodesFromRuneArray([]rune(treeString), 0, len(treeString))
}

type Node struct {
	Value    string
	Children []*Node
}

func (n Node) BracketRepresentation() string {
	str := n.Value
	for _, c := range n.Children {
		str += fmt.Sprintf("(%s)", c.BracketRepresentation())
	}
	return str
}

func (n *Node) Find(name string) *Node {
	if n.Value == name {
		return n
	}
	for _, c := range n.Children {
		if n := c.Find(name); n != nil {
			return n
		}
	}
	return nil
}

func (n Node) CountLeaves() int {
	// leaf nodes have 0 children
	if len(n.Children) == 0 {
		return 1
	}
	leaves := 0
	for _, c := range n.Children {
		leaves += c.CountLeaves()
	}
	return leaves
}

func (n *Node) AddChild(childTreeString string) error {
	node, err := parseTreeNodesFromRuneArray([]rune(childTreeString), 0, len(childTreeString))
	if err != nil {
		return err
	}
	n.Children = append(n.Children, node)
	return nil
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

// parseTreeNodesFromRuneArray is a helper recursive function that takes an array of runes as input and returns the tree structure - object Node
func parseTreeNodesFromRuneArray(treeRune []rune, treeStartIndex int, treeEndIndex int) (*Node, error) {
	i := treeStartIndex
	// iterate through the rune array to find the node's value
	var sb strings.Builder
	for i < treeEndIndex && treeRune[i] != '(' && treeRune[i] != ')' {
		sb.WriteRune(treeRune[i])
		i++
	}
	node := &Node{Value: sb.String()}
	if node.Value == "" {
		return nil, fmt.Errorf(fmt.Sprintf("node value at index %v should not be empty", treeStartIndex))
	}

	// iterate through children and recursively call the function to parse children trees
	childTreeStartIndex := i
	for childTreeStartIndex < treeEndIndex {
		if treeRune[childTreeStartIndex] != '(' {
			return nil, fmt.Errorf("expected %q but got %q, failed at index %v", '(', treeRune[childTreeStartIndex], childTreeStartIndex)
		}
		closingBracketIndex, err := indexOfClosingBracket(treeRune, childTreeStartIndex)
		if err != nil {
			return nil, err
		}
		childTree, err := parseTreeNodesFromRuneArray(treeRune, childTreeStartIndex+1, closingBracketIndex)
		if err != nil {
			return nil, err
		}
		node.Children = append(node.Children, childTree)
		childTreeStartIndex = closingBracketIndex + 1
	}
	return node, nil
}
