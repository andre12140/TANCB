package tree;

import graph.Graph;
import graph.Node;
import graph.NodeInfo;

import java.util.ArrayList;

/**
 * Bayesian Naive Classifier parameters
 */
public class ParameterLearning {

    private Graph spanningTree;
    private Node<NodeInfo> root;
    private Node<NodeInfo> C;
    private ArrayList<Node<ArrayList<Double>>> thetaNodes = new ArrayList<>();

    /**
     * @param spanningTree Graph to construct a tree
     * @param C Classification Node
     */
    public ParameterLearning(Graph spanningTree, Node<NodeInfo> C){
        this.spanningTree = spanningTree;
        this.C = C;
        root = spanningTree.getFirstNode();
    }

    /**
     * Uses the TreeParameters class to calculate theta parameters for classification node
     * @return Theta parameters for classification node
     */
    public ArrayList<Double> instancesThetaC(){
        TreeParameters tP = new TreeParameters(null, null, C);
        return tP.nInstancesTheta_c();
    }

    /**
     * Recursive method to build the tree and calculate all thetas
     * @param child Child Node
     * @param parent Parent Node
     * @return List of nodes with thetas
     */
    public ArrayList<Node<ArrayList<Double>>> allInstancesThetaijkc(Node<NodeInfo> child, Node<NodeInfo> parent){
        if(parent==null){
            TreeParameters tP = new TreeParameters(null, child, C);
            root.getData().setParent(null);
            thetaNodes.add(new Node<>(root.getIndex(), tP.nInstancesTheta_ijkc()));
        }
        for(Node<NodeInfo> n : spanningTree.getNeighbours(child)){
            if(parent!=null){
                if(n.getIndex()==parent.getIndex()){
                    continue;
                }
            }
            TreeParameters tP = new TreeParameters(child, n, C);
            n.getData().setParent(child);
            thetaNodes.add(new Node<>(n.getIndex(), tP.nInstancesTheta_ijkc()));
            allInstancesThetaijkc(n, child);
        }
        return thetaNodes;
    }

    /**
     * Gets root of the tree
     * @return Root of the tree
     */
    public Node getRoot(){
        return root;
    }

    /**
     * Gets the tree
     * @return The tree
     */
    public Graph getTree(){ return spanningTree;}


}
