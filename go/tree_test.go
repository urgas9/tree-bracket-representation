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
			bt := NewBracketTree(tc.BracketTree)
			assert.Equal(t, tc.BracketTree, bt.original)
			assert.Nil(t, bt.error)
			assert.NotEmpty(t, bt.node)
			assert.True(t, bt.Valid())
		})
	}
}

func TestNewBracketTree_InvalidStrings(t *testing.T) {
	invalidTestCases := readExampleTestCases(t, exampleFilePathInvalidCases)
	for _, tc := range invalidTestCases {
		t.Run(tc.Name, func(t *testing.T) {
			bt := NewBracketTree(tc.BracketTree)
			assert.Equal(t, tc.BracketTree, bt.original)
			assert.NotEmpty(t, bt.error)
			assert.False(t, bt.Valid())
			//assert.Empty(t, bt.node)
		})
	}
}

func TestNewBracketTree_BracketPresentationInputOutput(t *testing.T) {
	validTestCases := readExampleTestCases(t, exampleFilePathValidCases)
	for _, tc := range validTestCases {
		t.Run(tc.BracketTree, func(t *testing.T) {
			bt := NewBracketTree(tc.BracketTree)
			assert.Equal(t, tc.BracketTree, bt.original)
			assert.Nil(t, bt.error)
			assert.NotEmpty(t, bt.node)
			assert.True(t, bt.Valid())

			repr, err := bt.BracketRepresentation()
			assert.Nil(t, err)
			assert.Equal(t, tc.BracketTree, repr)
		})
	}
}

func TestNewBracketTree_BracketPresentation_Invalid(t *testing.T) {
	invalidTestCases := readExampleTestCases(t, exampleFilePathInvalidCases)
	for _, tc := range invalidTestCases {
		t.Run(tc.Name, func(t *testing.T) {
			bt := NewBracketTree(tc.BracketTree)
			assert.Equal(t, tc.BracketTree, bt.original)
			assert.NotNil(t, bt.error)
			assert.False(t, bt.Valid())

			repr, err := bt.BracketRepresentation()
			assert.Empty(t, repr)
			assert.NotNil(t, err)

			node, err := bt.RootNode()
			assert.Empty(t, node)
			assert.NotNil(t, err)
		})
	}
}

func TestNewBracketTree_RootNode_Manipulate(t *testing.T) {
	tt := "Alc(CD(Arr(Haa)))(E(F)(G))(I)(H(D)(MN))"
	bt := NewBracketTree(tt)
	assert.Equal(t, tt, bt.original)
	assert.Nil(t, bt.error)
	assert.True(t, bt.Valid())

	// Manipulate some nodes in the tree
	node, err := bt.RootNode()
	assert.Nil(t, err)
	assert.NotEmpty(t, node)
	node.Value = "root"
	node.Children[0].Children[0].Children[0].Value = "Deep child"
	node.Children[2].Value = "3rd child"

	repr, err := bt.BracketRepresentation()
	assert.Equal(t, "root(CD(Arr(Deep child)))(E(F)(G))(3rd child)(H(D)(MN))", repr)
	assert.Nil(t, err)
}
