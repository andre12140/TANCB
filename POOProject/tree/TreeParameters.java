package tree;

import graph.Node;
import graph.NodeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Calculate the tree parameters
 */
public class TreeParameters {

    private ArrayList<Integer> Node1;
    private ArrayList<Integer> Node2;
    private ArrayList<Integer> Class;
    private int r1;
    private int r2;
    private int s;

    /**
     * @param N1 Node 1
     * @param N2 Node 2
     * @param C Classification Node
     */
    public TreeParameters(Node<NodeInfo> N1, Node<NodeInfo> N2, Node<NodeInfo> C){
        if(N1 == null){
            this.Node1 = null;
            this.r1 = 1;
        }
        else{
            this.Node1 = N1.getData().getFeatureValues();
            this.r1 = N1.getData().getR();
        }
        if(N2 == null){
            this.Node2 = null;
            this.r2 = 1;
        }
        else{
            this.Node2 = N2.getData().getFeatureValues();
            this.r2 = N2.getData().getR();
        }
        this.Class = C.getData().getFeatureValues();
        this.s = C.getData().getR();
    }

    /**
     * @return parameters of the BNC C, for all features
     */
    protected ArrayList<Double> nInstancesTheta_ijkc(){
        final double N_tilda = 0.5;

        ArrayList<Integer> Nijkc = nInstancesNijkc();

        ArrayList<Integer> kInstances = nInstacesK(Nijkc , r2);

        ArrayList<Double> theta = new ArrayList<>();

        for(int j=0; j < r1; j++){
            for(int k=0; k < r2; k++){
                for(int c=0; c < s; c++){
                    theta.add((Nijkc.get(c * r2 + k + j * (2 * r2))+N_tilda)/(kInstances.get(c + j*s)+r2*N_tilda));
                }
            }
        }
        return theta;
    }

    /**
     * @return parameters of BNC B, for Class
     */
    protected ArrayList<Double> nInstancesTheta_c(){
        final double N_tilda = 0.5;
        final int N = Class.size();

        ArrayList<Integer> nC = nC(Class, s);

        ArrayList<Double> theta = new ArrayList<>();

        for(int c=0; c < s; c++){
            theta.add((nC.get(c) + N_tilda)/(N + s * N_tilda));
        }
        return theta;
    }

    /**
     * counts, for all i, j, c.
     */
    private ArrayList<Integer> nInstacesK(ArrayList<Integer> counts, int r){
        ArrayList<Integer> counter = new ArrayList<Integer>();
        int auxCounter = 0;
        int index = 0;
        for(Integer countsaux : counts){
            auxCounter += countsaux;
            index++;
            if(index==r){
                index = 0;
                counter.add(auxCounter);
                auxCounter = 0;
            }
        }
        return counter;
    }

    /**
     * Nijkc counts, for all i, j, k, c.
     */
    private ArrayList<Integer> nInstancesNijkc() {
        //Se o no pai for Node1, o filho que estamos a calcular Node2
        ArrayList<Integer> aux = new ArrayList<>();

        for(int j = 0; j < r1; j++){
            for(int c = 0; c < s; c++){
                for(int k = 0; k < r2; k++){
                    ArrayList<Integer> countsFilho = Counter(Node2, k);
                    ArrayList<Integer> countsC = Counter(Class, c);
                    if(Node1!=null){
                        ArrayList<Integer> countsPai = Counter(Node1, j);
                        countsFilho.retainAll(countsPai);
                    }
                    countsC.retainAll(countsFilho);
                    aux.add(countsC.size());
                }
            }
        }
        return aux;
    }

    /**
     * Counts the number of occurrences for the different feature values
     */
    private ArrayList<Integer> Counter(ArrayList<Integer> node, int counterValue){
        ArrayList<Integer> aux1 = new ArrayList<>();
        List<Integer> arrSubList = (ArrayList<Integer>) node.clone();

        while(arrSubList.indexOf(counterValue)!=-1){
            if(aux1.size()==0){
                aux1.add(arrSubList.indexOf(counterValue));
            }
            else{
                aux1.add(arrSubList.indexOf(counterValue) + aux1.get(aux1.size()-1) + 1);
            }
            if((aux1.get(aux1.size()-1)+1)==node.size()){
                break;
            }
            arrSubList = node.subList(aux1.get(aux1.size()-1)+1, node.size());
        }
        return aux1;
    }

    /**
     * Counts for all C
     */
    private ArrayList<Integer> nC(ArrayList<Integer> node, int s){
        ArrayList<Integer> ret = new ArrayList<Integer>();

        for(int aux=0; aux<s;aux++){
            ret.add(Counter(node,aux).size());
        }
        return ret;
    }

}
