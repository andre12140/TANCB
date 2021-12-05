package graph;

import java.util.ArrayList;

/**
 * Information to be stored in each node
 */
public class NodeInfo {

    private ArrayList<Integer> featureValues;
    private int r;
    private Node<NodeInfo> parent;
    private String featureName;

    /**
     * @param data Feature values
     * @param r Number of different feature values
     */
    public NodeInfo(ArrayList<Integer> data, int r){
        this.featureValues = data;
        this.r = r;
    }

    /**
     * Gets number of different feature values
     * @return Number of different feature values
     */
    public int getR() {
        return r;
    }

    /**
     * Gets feature values
     * @return Feature values
     */
    public ArrayList<Integer> getFeatureValues() {
        return featureValues;
    }

    /**
     * Assigns a parent to a given node
     * @param parent Parent node
     */
    public void setParent(Node<NodeInfo> parent){
        this.parent = parent;
    }

    /**
     * Gets the parent of the node
     * @return Parent of the node
     */
    public Node<NodeInfo> getParent() {
        return parent;
    }

    /**
     * Adds the feature name
     * @param featureName Feature name
     */
    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    @Override
    public String toString() {
        if(parent==null){
            return "" + featureName + " : ";
        }
        else{
            return "" + featureName + " : " + parent.getData().featureName;
        }
    }
}
