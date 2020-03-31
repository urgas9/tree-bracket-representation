public class TestCase {
    private String name;
    private String bracketTree;
    private int numLeaves;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBracketTree() {
        return bracketTree;
    }

    public void setBracketTree(String bracketTree) {
        this.bracketTree = bracketTree;
    }

    public int getNumLeaves() {
        return numLeaves;
    }

    public void setNumLeaves(int numLeaves) {
        this.numLeaves = numLeaves;
    }

    @Override
    public String toString() {
        if (name != null) {
            return name + '{' + bracketTree + '}';
        }
        return bracketTree;
    }
}
