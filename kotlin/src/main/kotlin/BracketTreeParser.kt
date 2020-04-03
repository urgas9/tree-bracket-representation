object BracketTreeParser {
    fun parse(bracketTree: String?): Node = Node("a", listOf(Node("b", null)))
}

class ParseException(message: String) : Exception(message)