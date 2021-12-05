package graph;

/**
 * Generic Node that can have any type of data associated
 * @param <T> Data type
 */
public class Node<T> {
    private int index;
    private T data;

    /**
     * Builds a node
     * @param idx Node identifier
     * @param data Node data
     */
    public Node(int idx, T data){
        index = idx;
        this.data = data;
    }

    /**
     * Gets node identifier
     * @return Node identifier
     */
    public int getIndex(){
        return index;
    }

    /**
     * Gets node data
     * @return Node data
     */
    public T getData(){
        return data;
    }
}
