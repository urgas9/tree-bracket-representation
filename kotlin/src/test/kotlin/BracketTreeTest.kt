import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.FileReader
import java.util.*
import java.util.stream.Stream

const val VALID_EXAMPLES_FILEPATH = "../examples/bracket-tree-valid-cases.json"
const val INVALID_EXAMPLES_FILEPATH = "../examples/bracket-tree-invalid-cases.json"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BracketTreeTest {

    private var validStringTestCases: Array<TestCase>? = null
    private var invalidStringTestCases: Array<TestCase>? = null

    @BeforeAll
    fun setup() {
        val gson = Gson()
        this.validStringTestCases = gson.fromJson(FileReader(VALID_EXAMPLES_FILEPATH), Array<TestCase>::class.java)
        this.invalidStringTestCases = gson.fromJson(FileReader(INVALID_EXAMPLES_FILEPATH), Array<TestCase>::class.java)
    }

    private fun getValidCases(): Stream<TestCase> = Arrays.stream(this.validStringTestCases)
    private fun getInvalidCases(): Stream<TestCase> = Arrays.stream(this.invalidStringTestCases)

    @ParameterizedTest
    @MethodSource("getValidCases")
    fun `init bracket tree with valid string`(testcase: TestCase) {
        val bt = BracketTreeParser.parse(testcase.bracketTree)
        assertEquals(testcase.bracketTree, bt.toBracketRepresentation())
    }

    @ParameterizedTest
    @MethodSource("getInvalidCases")
    fun `init bracket tree with invalid string`(testcase: TestCase) {
        assertThrows(ParseException::class.java) {
            BracketTreeParser.parse(testcase.bracketTree)
        }
    }

    @ParameterizedTest
    @MethodSource("getValidCases")
    fun `node count leaves`(testcase: TestCase) {
        assertEquals(testcase.numLeaves, BracketTreeParser.parse(testcase.bracketTree).countLeaves())
    }

    private fun getValidCasesForFind(): Stream<Arguments> =
        Stream.of(
            Arguments.of("CD", "CD(Arr(CD))"),
            Arguments.of("A", "A(CD(Arr(CD)))(E(F)(G))(CD)(H(D)(MN))"),
            Arguments.of("E", "E(F)(G)"),
            Arguments.of("MN", "MN")
        )

    @ParameterizedTest
    @MethodSource("getValidCasesForFind")
    fun `node find valid names`(name: String, expectedBracketTree: String) {
        val bt = BracketTreeParser.parse("A(CD(Arr(CD)))(E(F)(G))(CD)(H(D)(MN))")
        val r = bt.find(name)
        assertNotNull(r)
        assertEquals(expectedBracketTree, r!!.toBracketRepresentation())
    }

    private fun getInvalidCasesForFind(): Stream<String> = Arrays.stream(arrayOf("non-existing", "(", ")"))

    @ParameterizedTest
    @MethodSource("getInvalidCasesForFind")
    fun `node find invalid names`(name: String) {
        val bt = BracketTreeParser.parse("H(D)(MN)")
        assertNull(bt.find(name))
    }

    @Test
    fun `node add valid`() {
        val bt = BracketTreeParser.parse("H(D)(MN)")
        bt.addChild("A(H)(K)(L)")
        assertEquals("H(D)(MN)(A(H)(K)(L))", bt.toBracketRepresentation())
    }

    @Test
    fun `node add and find`() {
        val bt = BracketTreeParser.parse("H(D(A(C)))(MN)")
        assertNotNull(bt)

        val c = bt.find("C")
        assertNotNull(c)

        c!!.addChild("A(H(K))")
        assertEquals("C(A(H(K)))", c.toBracketRepresentation())
        assertEquals("H(D(A(C(A(H(K))))))(MN)", bt.toBracketRepresentation())

        c.addChild("B(C)(D)")
        assertEquals("C(A(H(K)))(B(C)(D))", c.toBracketRepresentation())
        assertEquals("H(D(A(C(A(H(K)))(B(C)(D)))))(MN)", bt.toBracketRepresentation())

        bt.addChild("A")
        assertEquals("H(D(A(C(A(H(K)))(B(C)(D)))))(MN)(A)", bt.toBracketRepresentation())
    }

    @ParameterizedTest
    @MethodSource("getInvalidCases")
    fun `node add child invalid bracket string`(testcase: TestCase) {
        val bt = BracketTreeParser.parse("H(D)(MN)")

        assertThrows(ParseException::class.java) {
            bt.addChild(testcase.bracketTree)
        }
    }
}