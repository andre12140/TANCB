package graph;


/**
 * Weighted edge between two nodes;
 */
public class WeightedEdge {
    private Node N1;
    private Node N2;
    private float alpha;

    /**
     * @param node1 One node of the edge
     * @param node2 One node of the edge
     * @param weight Weight of the edge
     */
    public WeightedEdge(Node node1, Node node2, float weight){
        N1 = node1;
        N2 = node2;
        alpha = weight;
    }

    /**
     * Gets weight of the edge
     * @return Weight of the edge
     */
    public float getAlpha(){
        return alpha;
    }

    /**
     * Gets one node of the edge
     * @return One node of the edge
     */
    public Node getN1() {
        return N1;
    }

    /**
     * Gets one node of the edge
     * @return One node of the edge
     */
    public Node getN2() {
        return N2;
    }
}
