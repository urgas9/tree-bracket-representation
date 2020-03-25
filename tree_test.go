package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestNewBracketTree_ValidStrings(t *testing.T) {
	var testCases = []string{
		"A",
		"A(L)",
		"Alc(CD(Arr(Haa)))(E(F)(G))(I)(H(D)(MN))",
		"A(B)(C)(D)(E)(F)(G)(H)",
		"A(B(C(D(E(F(G(H(I(J(K))))))))))",
		"Long(words)(are(my)(friends))(say(hi))",
	}
	for _, tt := range testCases {
		t.Run(tt, func(t *testing.T) {
			bt := NewBracketTree(tt)
			assert.Equal(t, tt, bt.original)
			assert.Nil(t, bt.error)
			assert.NotEmpty(t, bt.node)
			assert.True(t, bt.Valid())
		})
	}
}

func TestNewBracketTree_InvalidStrings(t *testing.T) {
	var testCases = []struct{
		name string
		treeString string
	}{
		{name: "empty string", treeString: ""},
		{name: "empty root", treeString: "(A)"},
		{name: "empty node value", treeString: "A()"},
		{name: "too many closing brackets", treeString: "A(MN))"},
		{name: "missing closing bracket", treeString: "A("},
	}
	for _, tt := range testCases {
		t.Run(tt.name, func(t *testing.T) {
			bt := NewBracketTree(tt.treeString)
			assert.Equal(t, tt.treeString, bt.original)
			assert.NotEmpty(t, bt.error)
			assert.False(t, bt.Valid())
			//assert.Empty(t, bt.node)
		})
	}
}

func TestNewBracketTree_BracketPresentationInputOutput(t *testing.T) {
	var testCases = []string{
		"A",
		"A(L)",
		"Alc(CD(Arr(Haa)))(E(F)(G))(I)(H(D)(MN))",
		"A(B)(C)(D)(E)(F)(G)(H)",
		"A(B(C(D(E(F(G(H(I(J(K))))))))))",
	}
	for _, tt := range testCases {
		t.Run(tt, func(t *testing.T) {
			bt := NewBracketTree(tt)
			assert.NotEmpty(t, bt.original)
			assert.Nil(t, bt.error)
			assert.NotEmpty(t, bt.node)
			assert.True(t, bt.Valid())

			assert.Equal(t, tt, bt.BracketRepresentation())
		})
	}
}
