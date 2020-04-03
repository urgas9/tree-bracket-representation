import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BracketTreeTest {

    @Test
    fun `init bracket tree with valid string`() {
        // given
        val bracketTreeString = "A(B)(C)(D(F))"

        val n = BracketTreeParser.parse(bracketTreeString)
        assert(n != null)
        val b = "ab" + "cd"
        assertEquals(bracketTreeString, b)
    }

}