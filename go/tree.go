package brackettree

import (
	"fmt"
	"strings"
)

type BracketTree struct {
	original string
	node     *Node
	error    error
}

func NewBracketTree(treeString string) BracketTree {
	n, err := parseNodesFromRuneArray([]rune(treeString), 0, len(treeString))

	return BracketTree{
		original: treeString,
		node:     n,
		error:    err,
	}
}

func (b *BracketTree) RootNode() (*Node, error) {
	if b.error != nil {
		return nil, b.error
	}
	return b.node, nil
}

func (b *BracketTree) Valid() bool {
	return b.error == nil
}

func (b *BracketTree) BracketRepresentation() (string, error) {
	if b.error != nil {
		return "", b.error
	}
	return b.node.BracketRepresentation(), nil
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

// parseNodesFromRuneArray is a helper recursive function that takes an array of runes as input and returns the tree structure - object Node
func parseNodesFromRuneArray(treeRune []rune, treeStartIndex int, treeEndIndex int) (*Node, error) {
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
			return node, fmt.Errorf("expected %q but got %q, failed at index %v", '(', treeRune[childTreeStartIndex], childTreeStartIndex)
		}
		closingBracketIndex, err := indexOfClosingBracket(treeRune, childTreeStartIndex)
		if err != nil {
			return node, err
		}
		childTree, err := parseNodesFromRuneArray(treeRune, childTreeStartIndex+1, closingBracketIndex)
		if err != nil {
			return node, err
		}
		node.Children = append(node.Children, *childTree)
		childTreeStartIndex = closingBracketIndex + 1
	}
	return node, nil
}