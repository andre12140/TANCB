package graph;

import java.util.*;

/**
 * Undirected graph
 */
public class Graph {
    /**
     * Each key corresponds to all feature nodes and each mapped value corresponds to adjacent nodes
     */
    private final HashMap<Node, Set<Node>> adjacencyList = new HashMap<>();

    /**
     * A list of edges with the two connected nodes and the respective wight
     */
    private LinkedList<WeightedEdge> edgeList = new LinkedList<>();

    /**
     * Adds a key to the adjacency list
     * @param v node to be added to the adjacency list
     */
    public void addVertex(Node v) {
        if (this.adjacencyList.containsKey(v)) {
            throw new IllegalArgumentException("Vertex already exists.");
        } else {
            this.adjacencyList.put(v, new HashSet());
        }
    }

    /**
     * Removes the given key from the adjacency list
     * @param v Node to be removed
     */
    public void removeVertex(Node v) {
        if (!this.adjacencyList.containsKey(v)) {
            throw new IllegalArgumentException("Vertex doesn't exist.");
        } else {
            this.adjacencyList.remove(v);
            Iterator var3 = this.getAllVertices().iterator();

            while(var3.hasNext()) {
                Node u = (Node) var3.next();
                ((Set)this.adjacencyList.get(u)).remove(v);
            }

        }
    }

    /**
     * Maps a node to a given key
     * @param v One node of the edge
     * @param u One node of the edge
     */
    public void addEdge(Node v, Node u) {
        if (this.adjacencyList.containsKey(v) && this.adjacencyList.containsKey(u)) {
            ((Set)this.adjacencyList.get(v)).add(u);
            ((Set)this.adjacencyList.get(u)).add(v);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Removes edge from the adjacency list
     * @param v Node of the edge
     * @param u Node of the edge
     */
    public void removeEdge(Node v, Node u) {
        if (this.adjacencyList.containsKey(v) && this.adjacencyList.containsKey(u)) {
            ((Set)this.adjacencyList.get(v)).remove(u);
            ((Set)this.adjacencyList.get(u)).remove(v);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Adds an edge with a weight between two nodes
     * @param v Node of the edge
     * @param u Node of the edge
     * @param weight weight of the edge
     */
    public void addEdgeWeight(Node v, Node u, float weight){
        edgeList.add(new WeightedEdge(v,u,weight));
    }

    /**
     * Verifyes if node V is adjacent to node U
     * @param v Node key
     * @param u Node to be verified
     * @return If a node is adjacent or not
     */
    public boolean isAdjacent(Node v, Node u) {
        return ((Set)this.adjacencyList.get(v)).contains(u);
    }

    /**
     * Get all adjacent nodes from a give node
     * @param v Node key
     * @return  All adjacent nodes
     */
    public Iterable<Node> getNeighbours(Node v) {
        return (Iterable)this.adjacencyList.get(v);
    }

    /**
     * Get all nodes
     * @return  All nodes
     */
    public Iterable<Node> getAllVertices() {
        return this.adjacencyList.keySet();
    }

    /**
     * Sorts all nodes by their index
     * @return Sorted list of nodes
     */
    public ArrayList<Node<NodeInfo>> getSortedVertices(){
        ArrayList<Node<NodeInfo>> aux = new ArrayList<>();
        for(int idx = 0; idx < numNodes(); idx++){
            for(Node<NodeInfo> n : getAllVertices()){
                if(n.getIndex()==idx){
                    aux.add(n);
                }
            }
        }
        return aux;
    }

    /**
     * Gets number of nodes in the graph
     * @return Number of nodes in the graph
     */
    public int numNodes(){ return this.adjacencyList.size();}

    /**
     * Gets first node of the adjacency list
     * @return first node of the adjacency list
     */
    public Node getFirstNode(){ return this.adjacencyList.entrySet().iterator().next().getKey(); }

    /**
     * Gets edge list
     * @return Edge list
     */
    public LinkedList<WeightedEdge> getEdgeList() {
        return edgeList;
    }
}