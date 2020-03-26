import org.junit.jupiter.api.Test;
import com.github.urgas9.treebracketrepresentation.BracketTree;

public class BracketTreeTests {

    @Test
    void validSimpleBracketTreeString() {
        String in = "A(B)(C)";
        BracketTree b = new BracketTree(in);
        b.Parse();
        assert in.equals(b.toBracketRepresentation());
    }

}
