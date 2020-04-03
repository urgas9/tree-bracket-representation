object BracketTreeParser {
    fun parse(bracketTree: String?) = Node("a", listOf(Node("b", null)))
}

data class Node(
    val name: String,
    val children: List<Node>?
) {
    fun addChild(childTree: String) {
        throw NotImplementedError()
    }

    fun countLeaves() {
        throw NotImplementedError()
    }

    fun toBracketRepresentation() {
        throw NotImplementedError()
    }

    fun find(name: String) {
        throw NotImplementedError()
    }

}