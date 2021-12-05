package spanningTree;

import graph.Graph;
import graph.Node;
import graph.WeightedEdge;

import java.util.*;

/**
 * Algorithm to find minimum and maximum spanning trees
 */
public class Kruskal implements SpanningTree<Graph>{
    private boolean spanningTreeType = false;

    /**
     * Calculate the minimum spanning tree
     * @param graph fully connected undirected weighted graph
     * @return Minimum spanning Tree
     */
    public Graph minSpanningTree(Graph graph) {
        spanningTreeType = true;
        return spanningTree(graph);
    }

    /**
     * Calculate the maximum spanning tree
     * @param graph fully connected undirected weighted graph
     * @return Maximum spanning Tree
     */
    public Graph maxSpanningTree(Graph graph) {
        spanningTreeType = false;
        return spanningTree(graph);
    }

    /**
     * Builds a minimum or maximum spanning tree from a given graph, according to a flag. By default builds a maximum spanning tree
     * @param graph Graph from which the spanning tree is built
     * @return Spanning tree
     */
     public Graph spanningTree(Graph graph) {
        LinkedList<WeightedEdge> edgeList = graph.getEdgeList();

        if (spanningTreeType) {
            edgeList.sort((o1, o2) -> {float change1 = o1.getAlpha();
                float change2 = o2.getAlpha();
                return Float.compare(change1, change2);
            });
        } else {
            edgeList.sort((o1, o2) -> {float change1 = o1.getAlpha();
                float change2 = o2.getAlpha();
                return Float.compare(change2, change1);
            });
        }

        int totalNodes = graph.numNodes();
        CycleDetector cycleDetector = new CycleDetector(totalNodes);
        int edgeCount = 0;

        Graph spanningTree = new Graph();
        for(Node n : graph.getAllVertices()){
            spanningTree.addVertex(n);
        }

        for (WeightedEdge edge : edgeList) {
            if (cycleDetector.detectCycle(edge.getN1().getIndex(), edge.getN2().getIndex())) {
                continue;
            }

            spanningTree.addEdge(edge.getN1(), edge.getN2());

            spanningTree.addEdgeWeight(edge.getN1(), edge.getN2(), edge.getAlpha());

            edgeCount++;
            if (edgeCount == totalNodes - 1) {
                break;
            }
        }
        return spanningTree;
    }

}
