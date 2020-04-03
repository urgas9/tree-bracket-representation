data class Node(
    val name: String,
    val children: List<Node>?
) {
    fun addChild(childTree: String) {
        throw NotImplementedError()
    }

    fun countLeaves(): Int {
        throw NotImplementedError()
    }

    fun find(name: String): Node {
        throw NotImplementedError()
    }

    fun toBracketRepresentation(): String {
        throw NotImplementedError()
    }

}