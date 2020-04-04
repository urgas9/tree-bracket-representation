object BracketTreeParser {
    fun parse(bracketTree: String): Node = this.parseBracketTreeStringToNode(bracketTree, 0, bracketTree.length)

    private fun getIndexOfClosingBracket(bracketTree: String, startIndex: Int): Int {
        if (bracketTree[startIndex] != '(') {
            throw ParseException("expected '${"("}' but found '${bracketTree[startIndex]}' at index $startIndex")
        }

        var parenthesisCounter = 1
        for (i in (startIndex + 1) until bracketTree.length) {
            when (bracketTree[i]) {
                '(' -> parenthesisCounter++
                ')' -> parenthesisCounter--
            }
            if (parenthesisCounter == 0) {
                return i
            }
        }
        return -1
    }

    private fun parseBracketTreeStringToNode(bracketTree: String, startIndex: Int, endIndex: Int): Node {
        val sb = StringBuilder()

        var childTreeStartIndex = startIndex
        while (childTreeStartIndex < endIndex && bracketTree[childTreeStartIndex] != '(') {
            sb.append(bracketTree[childTreeStartIndex])
            childTreeStartIndex++
        }

        if (sb.isEmpty()) {
            throw ParseException("node name of the tree starting at index $startIndex is empty")
        }
        val n = Node(sb.toString(), null)
        while (childTreeStartIndex < endIndex) {
            val childTreeEndIndex = getIndexOfClosingBracket(bracketTree, childTreeStartIndex)
            n.addChild(this.parseBracketTreeStringToNode(bracketTree, childTreeStartIndex + 1, childTreeEndIndex))
            childTreeStartIndex = childTreeEndIndex + 1
        }

        return n
    }
}

class ParseException(message: String) : Exception(message)
