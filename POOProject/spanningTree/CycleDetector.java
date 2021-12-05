package spanningTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Determine whether two nodes are in the same disjoint set by comparing results of two find operations
 */

public class CycleDetector {

    private List<DisjointSetInfo> nodes;

    /**
     * @param totalNodes All nodes contained in the graph
     */
    public CycleDetector(int totalNodes) {
        initDisjointSets(totalNodes);
    }

    /**
     * Check if two nodes originate a cycle within the graph
     * @param u Node identifier
     * @param v Node identifier
     * @return True if a cycle is detected, false otherwise
     */
    public boolean detectCycle(Integer u, Integer v) {
        Integer rootU = pathCompressionFind(u);
        Integer rootV = pathCompressionFind(v);
        if (rootU.equals(rootV)) {
            return true;
        }
        unionByRank(rootU, rootV);
        return false;
    }

    /**
     * Initialization of a disjoint set for each node
     */
    private void initDisjointSets(int totalNodes) {
        nodes = new ArrayList<>(totalNodes);
        for (int i = 0; i < totalNodes; i++) {
            nodes.add(new DisjointSetInfo(i));
        }
    }

    /**
     * Finds the set from which a node belongs
     */
    private Integer find(Integer node) {
        Integer parent = nodes.get(node).getParentNode();
        if (parent.equals(node)) {
            return node;
        } else {
            return find(parent);
        }
    }

    /**
     * Improves the find method
     */
    private Integer pathCompressionFind(Integer node) {
        DisjointSetInfo setInfo = nodes.get(node);
        Integer parent = setInfo.getParentNode();
        if (parent.equals(node)) {
            return node;
        } else {
            Integer parentNode = find(parent);
            setInfo.setParentNode(parentNode);
            return parentNode;
        }
    }

    /**
     * Combines two sets if two nodes of an edge are in different sets
     */
    private void union(Integer rootU, Integer rootV) {
        DisjointSetInfo setInfoU = nodes.get(rootU);
        setInfoU.setParentNode(rootV);
    }

    /**
     * Improves the union method
     */
    private void unionByRank(int rootU, int rootV) {
        DisjointSetInfo setInfoU = nodes.get(rootU);
        DisjointSetInfo setInfoV = nodes.get(rootV);
        int rankU = setInfoU.getRank();
        int rankV = setInfoV.getRank();
        if (rankU < rankV) {
            setInfoU.setParentNode(rootV);
        } else {
            setInfoV.setParentNode(rootU);
            if (rankU == rankV) {
                setInfoU.setRank(rankU + 1);
            }
        }
    }

}