import com.github.urgas9.treebracketrepresentation.BracketTreeParser;
import com.github.urgas9.treebracketrepresentation.Node;
import com.github.urgas9.treebracketrepresentation.ParseException;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        //read file and convert the JSON string back to object using GSON deserializer
        System.out.println("Reading valid test cases from '" + this.VALID_EXAMPLES_FILEPATH + "'");
        BufferedReader brValid = new BufferedReader(
                new FileReader(this.VALID_EXAMPLES_FILEPATH));
        this.validStringTestCases = gson.fromJson(brValid, TestCase[].class);

        System.out.println("Reading valid test cases from '" + this.INVALID_EXAMPLES_FILEPATH + "'");
        BufferedReader brInvalid = new BufferedReader(
                new FileReader(this.INVALID_EXAMPLES_FILEPATH));
        this.invalidStringTestCases = gson.fromJson(brInvalid, TestCase[].class);
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
        Node n = new BracketTreeParser(tc.getBracketTree()).parse();
        assert tc.getBracketTree().equals(n.toBracketRepresentation());
    }

    @ParameterizedTest
    @MethodSource("getInvalidCases")
    void invalidBracketTreeString(TestCase tc) {
        Assertions.assertThrows(ParseException.class, () -> new BracketTreeParser(tc.getBracketTree()).parse());
    }

    @Test
    void findExisting() throws ParseException {
        Node n = new BracketTreeParser("A(CD(Arr(CD)))(E(F)(G))(CD)(H(D)(MN))").parse();
        assert n.find("CD").toBracketRepresentation().equals("CD(Arr(CD))");
        assert n.find("A").toBracketRepresentation().equals("A(CD(Arr(CD)))(E(F)(G))(CD)(H(D)(MN))");
        assert n.find("E").toBracketRepresentation().equals("E(F)(G)");
        assert n.find("MN").toBracketRepresentation().equals("MN");
    }

    @Test
    void findNonExisting() throws ParseException {
        Node n = new BracketTreeParser("A(B)(C)").parse();

        assert n.find("non existing") == null;
        assert n.find("(") == null;
        assert n.find(")") == null;
    }

    @ParameterizedTest
    @MethodSource("getValidCases")
    void validBracketTreeStringCountLeaves(TestCase tc) throws ParseException {
        Node n = new BracketTreeParser(tc.getBracketTree()).parse();
        assert n.countLeaves() == tc.getNumLeaves();
    }

    @Test
    void addValidBracketTreeChild() throws ParseException {
        Node n = new BracketTreeParser("H(D(A(C)))(MN)").parse();
        assert n != null;

        Node c = n.find("C");
        assert c != null;

        c.addChild("A(H(K))");
        assert c.toBracketRepresentation().equals("C(A(H(K)))");
        assert n.toBracketRepresentation().equals("H(D(A(C(A(H(K))))))(MN)");

        c.addChild("B(C)(D)");
        assert c.toBracketRepresentation().equals("C(A(H(K)))(B(C)(D))");
        assert n.toBracketRepresentation().equals("H(D(A(C(A(H(K)))(B(C)(D)))))(MN)");

        n.addChild("A");
        assert n.toBracketRepresentation().equals("H(D(A(C(A(H(K)))(B(C)(D)))))(MN)(A)");
    }

    @ParameterizedTest
    @MethodSource("getInvalidCases")
    void addInvalidBracketTreeChild(TestCase tc) throws ParseException {
        Node n = new BracketTreeParser("A(B)(C)").parse();
        assert n != null;

        Assertions.assertThrows(ParseException.class, () -> n.addChild(tc.getBracketTree()));
    }

}
