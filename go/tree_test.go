package brackettree

import (
	"encoding/json"
	"io/ioutil"
	"os"
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

const (
	exampleFilePathValidCases   = "../examples/bracket-tree-valid-cases.json"
	exampleFilePathInvalidCases = "../examples/bracket-tree-invalid-cases.json"
)

type TestCase struct {
	Name        string `json:"name"`
	BracketTree string `json:"bracketTree"`
	NumLeaves   int    `json:"numLeaves"`
}

func readExampleTestCases(t *testing.T, filePath string) []TestCase {
	t.Helper()

	jsonFile, err := os.Open(filePath)
	require.Nil(t, err)
	defer jsonFile.Close()

	byteJSON, err := ioutil.ReadAll(jsonFile)
	require.Nil(t, err)

	var treeObj []TestCase
	err = json.Unmarshal(byteJSON, &treeObj)
	require.Nil(t, err)

	return treeObj
}

func TestNewBracketTree_ValidStrings(t *testing.T) {
	validTestCases := readExampleTestCases(t, exampleFilePathValidCases)
	for _, tc := range validTestCases {
		t.Run(tc.BracketTree, func(t *testing.T) {
			bt, err := NewBracketTree(tc.BracketTree)
			assert.Nil(t, err)
			assert.NotNil(t, bt)
		})
	}
}

func TestNewBracketTree_InvalidStrings(t *testing.T) {
	invalidTestCases := readExampleTestCases(t, exampleFilePathInvalidCases)
	for _, tc := range invalidTestCases {
		t.Run(tc.Name, func(t *testing.T) {
			bt, err := NewBracketTree(tc.BracketTree)
			assert.NotNil(t, err)
			assert.Nil(t, bt)
		})
	}
}

func TestNewBracketTree_BracketPresentationInputOutput(t *testing.T) {
	validTestCases := readExampleTestCases(t, exampleFilePathValidCases)
	for _, tc := range validTestCases {
		t.Run(tc.BracketTree, func(t *testing.T) {
			bt, err := NewBracketTree(tc.BracketTree)
			assert.Nil(t, err)
			assert.NotEmpty(t, bt)

			assert.Equal(t, tc.BracketTree, bt.BracketRepresentation())
		})
	}
}

func TestNewBracketTree_BracketPresentation_Invalid(t *testing.T) {
	invalidTestCases := readExampleTestCases(t, exampleFilePathInvalidCases)
	for _, tc := range invalidTestCases {
		t.Run(tc.Name, func(t *testing.T) {
			bt, err := NewBracketTree(tc.BracketTree)
			assert.NotNil(t, err)
			assert.Nil(t, bt)
		})
	}
}

func TestNewBracketTree_RootNode_Manipulate(t *testing.T) {
	tt := "Alc(CD(Arr(Haa)))(E(F)(G))(I)(H(D)(MN))"
	bt, err := NewBracketTree(tt)
	assert.Nil(t, err)

	// Manipulate some nodes in the tree
	assert.NotEmpty(t, bt)
	bt.Value = "root"
	bt.Children[0].Children[0].Children[0].Value = "Deep child"
	bt.Children[2].Value = "3rd child"

	repr := bt.BracketRepresentation()
	assert.Equal(t, "root(CD(Arr(Deep child)))(E(F)(G))(3rd child)(H(D)(MN))", repr)

	f1 := bt.Find("H")
	assert.NotNil(t, f1)
	assert.Equal(t, "H(D)(MN)", f1.BracketRepresentation())

	f2 := bt.Find("CD")
	assert.NotNil(t, f2)
	assert.Equal(t, "CD(Arr(Deep child))", f2.BracketRepresentation())

	f3 := bt.Find("non-existing")
	assert.Nil(t, f3)
}

func TestNewBracketTree_Find_Existing(t *testing.T) {
	bracketTree := "A(CD(Arr(CD)))(E(F)(G))(CD)(H(D)(MN))"
	bt, err := NewBracketTree(bracketTree)
	assert.Nil(t, err)

	testCases := []struct {
		name                  string
		expectedBracketString string
	}{
		{
			name:                  "CD",
			expectedBracketString: "CD(Arr(CD))",
		}, {
			name:                  "A",
			expectedBracketString: "A(CD(Arr(CD)))(E(F)(G))(CD)(H(D)(MN))",
		}, {
			name:                  "E",
			expectedBracketString: "E(F)(G)",
		}, {
			name:                  "MN",
			expectedBracketString: "MN",
		},
	}
	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			fNode := bt.Find(tc.name)
			assert.NotNil(t, fNode)
			assert.Equal(t, tc.expectedBracketString, fNode.BracketRepresentation())
		})
	}
}

func TestNewBracketTree_Find_NonExisting(t *testing.T) {
	bracketTree := "H(D)(MN)"
	bt, err := NewBracketTree(bracketTree)
	assert.Nil(t, err)

	fNode := bt.Find("non-existing")
	assert.Nil(t, fNode)
	fNode = bt.Find("(")
	assert.Nil(t, fNode)
	fNode = bt.Find(")")
	assert.Nil(t, fNode)
}

func TestNewBracketTree_CountLeaves(t *testing.T) {
	validTestCases := readExampleTestCases(t, exampleFilePathValidCases)
	for _, tc := range validTestCases {
		t.Run(tc.BracketTree, func(t *testing.T) {
			bt, err := NewBracketTree(tc.BracketTree)
			assert.Nil(t, err)

			assert.Equal(t, tc.NumLeaves, bt.CountLeaves())
		})
	}
}

func TestNewBracketTree_Add_Valid(t *testing.T) {
	bracketTree := "H(D)(MN)"
	bt, err := NewBracketTree(bracketTree)
	assert.Nil(t, err)

	err = bt.AddChild("A(H)(K)(L)")
	assert.Nil(t, err)

	assert.Equal(t, "H(D)(MN)(A(H)(K)(L))", bt.BracketRepresentation())
}

func TestNewBracketTree_Add_Find_Valid(t *testing.T) {
	bracketTree := "H(D(A(C)))(MN)"
	bt, err := NewBracketTree(bracketTree)
	assert.Nil(t, err)

	c := bt.Find("C")
	assert.NotNil(t, c)

	err = c.AddChild("A(H(K))")
	assert.Nil(t, err)
	assert.Equal(t, "C(A(H(K)))", c.BracketRepresentation())
	assert.Equal(t, "H(D(A(C(A(H(K))))))(MN)", bt.BracketRepresentation())

	err = c.AddChild("B(C)(D)")
	assert.Nil(t, err)
	assert.Equal(t, "C(A(H(K)))(B(C)(D))", c.BracketRepresentation())
	assert.Equal(t, "H(D(A(C(A(H(K)))(B(C)(D)))))(MN)", bt.BracketRepresentation())

	err = bt.AddChild("A")
	assert.Nil(t, err)
	assert.Equal(t, "H(D(A(C(A(H(K)))(B(C)(D)))))(MN)(A)", bt.BracketRepresentation())
}

func TestNewBracketTree_Add_Invalid(t *testing.T) {
	invalidTestCases := readExampleTestCases(t, exampleFilePathInvalidCases)
	for _, tc := range invalidTestCases {
		t.Run(tc.Name, func(t *testing.T) {
			bt, err := NewBracketTree("A(B)(C)")
			assert.Nil(t, err)

			err = bt.AddChild(tc.BracketTree)
			assert.NotNil(t, err)
		})
	}
}
