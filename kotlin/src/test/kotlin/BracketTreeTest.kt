import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.FileReader
import java.util.*
import java.util.stream.Stream

data class TestCase(
    val name: String?,
    val bracketTree: String,
    val numLeaves: Int
)

const val VALID_EXAMPLES_FILEPATH = "../examples/bracket-tree-valid-cases.json"
const val INVALID_EXAMPLES_FILEPATH = "../examples/bracket-tree-invalid-cases.json"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BracketTreeTest {

    private var validStringTestCases: Array<TestCase>? = null
    private var invalidStringTestCases: Array<TestCase>? = null

    @BeforeAll
    fun setup() {
        val gson = Gson()
        validStringTestCases = gson.fromJson(FileReader(VALID_EXAMPLES_FILEPATH), Array<TestCase>::class.java)
        invalidStringTestCases = gson.fromJson(FileReader(INVALID_EXAMPLES_FILEPATH), Array<TestCase>::class.java)
    }

    fun getValidCases(): Stream<TestCase> = Arrays.stream(validStringTestCases)
    fun getInvalidCases(): Stream<TestCase> = Arrays.stream(invalidStringTestCases)

    @ParameterizedTest
    @MethodSource("getValidCases")
    fun `init bracket tree with valid string()`(testcase: TestCase) {
        val n = BracketTreeParser.parse(testcase.bracketTree)
        assert(n != null)
    }

}