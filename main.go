package main

import (
	"fmt"
)

func main() {
	//Alc(CD(Arr(Haa)))(E(F)(G))
	s := "Alc(CD(Arr(Haa)))(E(F)(G))(I)(H(D)(MN))"
	sRune := []rune(s)

	n := treeFromString(sRune, 0, len(sRune))
	fmt.Println(n.String())
}

func findClosingParenIndex(treeRune []rune, startParenthesisIndex int) int {

	if treeRune[startParenthesisIndex] != '(' {
		fmt.Println(fmt.Sprintf("something weird at index %v", startParenthesisIndex))
		return -1
		//panic(fmt.Sprintf("there is value %q at passed index, but should be %q", treeRune[startParenthesisIndex], '('))
	}
	parenCounter := 1

	for i := startParenthesisIndex + 1; i < len(treeRune); i++ {
		if treeRune[i] == '(' {
			parenCounter++
		}
		if treeRune[i] == ')' {
			parenCounter--
		}
		if parenCounter == 0 {
			return i
		}
	}
	return -1
}

type Node struct {
	Value    string
	Children []Node
}

func (n Node) String() string {
	str := n.Value
	for _, c := range n.Children {
		str += fmt.Sprintf("(%s)", c.String())
	}
	return str
}

func treeFromString(treeRune []rune, treeStartIndex int, treeEndIndex int) Node {

	// Construct tree node value
	i := treeStartIndex
	var val string
	for treeRune[i] != '(' && treeRune[i] != ')' && i < treeEndIndex {
		val += string(treeRune[i])
		i++
	}
	node := Node{Value: val}

	childI := i
	for childI < treeEndIndex {
		if treeRune[childI] != '(' {
			panic(fmt.Sprintf("this is not a valid tree representation, failed at %v with %q", childI, treeRune[childI]))
		}
		closingI := findClosingParenIndex(treeRune, childI)
		node.Children = append(node.Children, treeFromString(treeRune, childI+1, closingI))
		childI = closingI + 1
	}
	return node
}
