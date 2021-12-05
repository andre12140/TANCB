package classifier;

import graph.Graph;
import graph.Node;
import graph.NodeInfo;
import score.LLScore;
import score.MDLScore;
import tree.ParameterLearning;
import spanningTree.Kruskal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tree Augmented Naive Bayes Classifier
 */
public class TAN implements Classifier<ArrayList<Integer>>{

    /** Nodes used to store de training data*/
    private ArrayList<Node<NodeInfo>> trainNodes = new ArrayList<>();
    /** Node index identifier */
    private int nodeIdx=0;
    /** Node used to store de classification of the training data*/
    private Node<NodeInfo> cNode;

    /** List of the parameter theta for the different classes */
    private ArrayList<Double> theta_c;

    /** List of the parameter theta for the different features of the training data */
    private ArrayList<Node<ArrayList<Double>>> theta_ijkc;

    /** Tree with the nodes of the features */
    private Graph tree;

    /** Flag to select the type of score; 0- Uses LL score; 1- Uses MDL score  */
    private int flagScore;

    /**
     *  Assigns the type of score to be used
     * @param scoreID The type of score to use;
     * */
    public TAN(String scoreID)  {
        if(scoreID.equals("LL")){
            flagScore=0;
        } else if(scoreID.equals("MDL")){
            flagScore=1;
        } else {
            throw new IllegalArgumentException("Invalid Score Type: Please enter LL or MDL.");
        }
    }

    /**
     * Constructs undirected Graph;
     * @param features The data assigned to features nodes
     * @param classC   The data assigned to the classification node
     * @return The fully connected undirected graph
     */
    private Graph undirectedGraph(ArrayList<Integer>[] features, ArrayList<Integer> classC){

        for(ArrayList<Integer> f : features){
            trainNodes.add(new Node<>(nodeIdx, new NodeInfo(f, calcMaxVar(f))));
            nodeIdx++;
        }

        cNode = new Node<>(nodeIdx, new NodeInfo(classC, calcMaxVar(classC)));
        trainNodes.add(cNode);
        nodeIdx++;
        return fullyConnectedGraph();
    }

    /**
     * Fully connects a given graph
     * @return Fully connected graph with weights
     */
    private Graph fullyConnectedGraph(){
        Graph g = new Graph();
        int counts=0;

        for (int i = 0; i < trainNodes.size()-1; i++) {
            // each element is a vertex
            g.addVertex(trainNodes.get(i));
            // Connecting all vertices to each other
            if (i != 0) {
                for (int j = 0; j <= counts; j++) {
                    g.addEdge(trainNodes.get(j), trainNodes.get(counts + 1));
                    if(flagScore == 0){
                        LLScore score = new LLScore(trainNodes.get(j), trainNodes.get(counts + 1), cNode);
                        g.addEdgeWeight(trainNodes.get(j), trainNodes.get(counts + 1),score.calcWeight());
                    } else {
                        MDLScore score = new MDLScore(trainNodes.get(j), trainNodes.get(counts + 1), cNode);
                        g.addEdgeWeight(trainNodes.get(j), trainNodes.get(counts + 1),score.mdlCalcWeight());
                    }
                }
                counts++;
            }
        }
        return g;
    }

    /**
     *  Returns the number of different values of a given feature/class;
     */
    private int calcMaxVar(ArrayList<Integer> var){
        return Collections.max(var) + 1;
    }

    /**
     * Fits the given data by applying the Tree Augmented Naive Bayes Classifier algorithm
     * @param features an array of type T with the training data.
     * @param classC classification of the given features.
     */
    @Override
    public void fit(ArrayList<Integer>[] features, ArrayList<Integer> classC) {
        Graph undirectedG = undirectedGraph(features, classC);

        Kruskal kruskal = new Kruskal();
        Graph gKruskal = kruskal.maxSpanningTree(undirectedG);

        ParameterLearning pLTeste = new ParameterLearning(gKruskal, cNode);

        theta_ijkc = pLTeste.allInstancesThetaijkc(pLTeste.getRoot(), null);

        tree = pLTeste.getTree();

        theta_c = pLTeste.instancesThetaC();
    }

    /**
     * Assigns a name to node
     * @param featureNames node name
     */
    public void setNodeNames(List<String> featureNames){
        for(Node<NodeInfo> n : tree.getAllVertices()){
            n.getData().setFeatureName(featureNames.get(n.getIndex()));
        }
    }

    /**
     * Classifies the given data according to the fitted model
     * @param unseenFeatures data to classify
     * @return Classification of each instance
     */
    @Override
    public ArrayList<Integer> predict(ArrayList<Integer>[] unseenFeatures) {
        ArrayList<Integer> predicted = new ArrayList<>();

        ArrayList<Double> numerator = new ArrayList<>();

        for(int numLine=0; numLine < unseenFeatures[0].size() ; numLine++){

            ArrayList<Integer> line = new ArrayList<>();
            for(ArrayList<Integer> v : unseenFeatures){
                line.add(v.get(numLine));
            }

            for(int i=0; i < cNode.getData().getR(); i++){
                numerator.add(theta_c.get(i));
                for(Double d : getNodeThetas(line, i)){
                    numerator.set(i+numLine*cNode.getData().getR(), numerator.get(i+numLine*cNode.getData().getR())*d);
                }
            }
        }

        for(int i=0; i < numerator.size(); i=i+cNode.getData().getR()){

            ArrayList<Double> prob = new ArrayList<>();

            List<Double> aux = numerator.subList(i, i+cNode.getData().getR());

            Double denominator = aux.stream().mapToDouble(Double::doubleValue).sum();

            for(Double d : aux){
                prob.add(d/denominator);
            }
            Double d = Collections.max(prob);
            predicted.add(prob.indexOf(d));
        }
        return predicted;
    }

    /**
     * Gets the theta parameters for all nodes
     */
    private ArrayList<Double> getNodeThetas(ArrayList<Integer> unseenFeature, int c_val){

        ArrayList<Double> thetaijkc = new ArrayList<>();

        for(Node<NodeInfo> n : tree.getAllVertices()){

            Node<NodeInfo> parent = n.getData().getParent();
            int k = unseenFeature.get(n.getIndex());

            if(parent==null){
                for(Node<ArrayList<Double>> auxN : theta_ijkc){
                    if(auxN.getIndex()==n.getIndex()){
                        thetaijkc.add((auxN.getData().get(c_val + k*cNode.getData().getR())));
                        break;
                    }
                }

                continue;
            }

            int j = unseenFeature.get(parent.getIndex());

            for(Node<ArrayList<Double>> auxN : theta_ijkc){
                if(auxN.getIndex()==n.getIndex()){
                    thetaijkc.add((auxN.getData().get(c_val + k*cNode.getData().getR() + j*n.getData().getR()*cNode.getData().getR())));
                    break;
                }
            }
        }
        return thetaijkc;
    }

    /**
     * Gets tree with the nodes of the features
     * @return Tree with the nodes of the features
     */
    public Graph getTree() {
        return tree;
    }

    /**
     * Gets the classifications node
     * @return  classifications node
     */
    public Node<NodeInfo> getcNode() {
        return cNode;
    }
}
