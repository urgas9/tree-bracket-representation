data class Node(
    var name: String,
    var children: ArrayList<Node>?
) {
    fun addChild(childTree: String) {
        val c = BracketTreeParser.parse(childTree)
        this.addChild(c)
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

    fun find(name: String): Node? {
        if (this.name == name) {
            return this
        }
        if (!this.children.isNullOrEmpty()) {
            for (c in this.children!!.listIterator()) {
                val h = c.find(name)
                if (h != null) {
                    return h
                }
            }
        }
        return null
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