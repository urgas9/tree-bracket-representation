data class Node(
    var name: String,
    var children: ArrayList<Node>?
) {
    fun addChild(childTree: String) {
        throw NotImplementedError()
    }

    fun addChild(childNode: Node) {
        if (this.children == null) {
            this.children = arrayListOf(childNode)
        } else {
            this.children!!.add(childNode)
        }
    }

    fun countLeaves(): Int {
        if (this.children.isNullOrEmpty()) {
            return 1
        }
        var leaves = 0
        for (c in this.children!!.listIterator()) {
            leaves += c.countLeaves()
        }
        return leaves
    }

    fun find(name: String): Node {
        throw NotImplementedError()
    }

    fun toBracketRepresentation(): String {
        val sb = StringBuilder(this.name)
        if (this.children != null) {
            for (c in this.children!!) {
                sb.append("(${c.toBracketRepresentation()})")
            }
        }
        return sb.toString()
    }

}