package spanningTree;

/**
 * @param <T> Type of graph
 */
public interface SpanningTree<T> {
    /**
     * Creates a spanning tree using a given algorithm
     * @param graph fully connected undirected weighted graph
     * @return spanning tree
     */
    T spanningTree(T graph);
}
