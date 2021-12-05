package spanningTree;

/**
 * Use of a tree structure to represent a disjoint set
 */
public class DisjointSetInfo {

    private Integer parentNode;
    private int rank;

    /**
     * @param nodeNumber Defines the parent node of the disjoint set and sets its rank to 1
     */
    public DisjointSetInfo(Integer nodeNumber) {
        setParentNode(nodeNumber);
        setRank(1);
    }

    /**
     * Gets parent node of the disjoint set
     * @return Parent node of the disjoint set
     */
    public Integer getParentNode() {
        return parentNode;
    }

    /**
     * Defines the parent node of the disjoint set
     * @param parentNode Parent node
     */
    public void setParentNode(Integer parentNode) {
        this.parentNode = parentNode;
    }

    /**
     * Gets rank of the Disjoint set
     * @return Rank of the Disjoint set
     */
    public int getRank() {
        return rank;
    }

    /**
     * Assigns a rank to the disjoint set
     * @param rank Rank of the disjoint set
     */
    public void setRank(int rank) {
        this.rank = rank;
    }
}