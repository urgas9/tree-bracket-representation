import com.github.urgas9.treebracketrepresentation.ParseException;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import com.github.urgas9.treebracketrepresentation.BracketTree;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BracketTreeTests {

    private final String VALID_EXAMPLES_FILEPATH = "../examples/bracket-tree-valid-cases.json";
    private final String INVALID_EXAMPLES_FILEPATH = "../examples/bracket-tree-invalid-cases.json";

    private TestCase[] validStringTestCases;
    private TestCase[] invalidStringTestCases;

    @BeforeAll
    public void setup() throws IOException {
        Gson gson = new Gson();

        System.out.println("Reading JSON from a file");
        System.out.println("----------------------------");

        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        //read file and convert the JSON string back to object using GSON deserializer
        BufferedReader brValid = new BufferedReader(
                new FileReader(this.VALID_EXAMPLES_FILEPATH));
        this.validStringTestCases = gson.fromJson(brValid, TestCase[].class);

        BufferedReader brInvalid = new BufferedReader(
                new FileReader(this.INVALID_EXAMPLES_FILEPATH));
        this.invalidStringTestCases = gson.fromJson(brInvalid, TestCase[].class);

        System.out.println("Test cases: " + this.invalidStringTestCases);

    }

    private Stream<TestCase> getValidCases() {
        return Stream.of(this.validStringTestCases);
    }

    private Stream<TestCase> getInvalidCases() {
        return Stream.of(this.invalidStringTestCases);
    }

    @ParameterizedTest
    @MethodSource("getValidCases")
    void validBracketTreeString(TestCase tc) throws ParseException {
        BracketTree b = new BracketTree(tc.getBracketTree());
        b.parse();
        assert tc.getBracketTree().equals(b.toBracketRepresentation());
        assert tc.getBracketTree().equals(b.getOriginal());
    }

    @ParameterizedTest
    @MethodSource("getInvalidCases")
    void invalidBracketTreeString(TestCase tc) {
        BracketTree b = new BracketTree(tc.getBracketTree());

        Assertions.assertThrows(ParseException.class, b::parse);
        assert tc.getBracketTree().equals(b.getOriginal());
    }

}
