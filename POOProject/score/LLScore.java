package score;

import graph.Node;
import graph.NodeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *  Log likelihood Score
 */
public class LLScore implements Scores{
    private ArrayList<Integer> Node1;
    private ArrayList<Integer> Node2;
    private ArrayList<Integer> Class;
    private int r1;
    private int r2;
    private int s;

    /**
     * @param N1 Parent node
     * @param N2 Node to be evaluated
     * @param C Classification node
     */
    public LLScore(Node<NodeInfo> N1, Node<NodeInfo> N2, Node<NodeInfo> C) {
        if(N1 == null){
            this.Node1 = null;
            this.r1 = 1;
        }
        else{
            this.Node1 = N1.getData().getFeatureValues();
            this.r1 = N1.getData().getR();
        }
        this.Node2 = N2.getData().getFeatureValues();
        this.Class = C.getData().getFeatureValues();
        this.r2 = N2.getData().getR();
        this.s = C.getData().getR();
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
     * counts, for all i, k, c.
     */
    private ArrayList<Integer> nInstacesJ(ArrayList<Integer> counts, int r){
        ArrayList<Integer> counter = new ArrayList<Integer>();
        int numCol = counts.size()/(r*2);
        int auxCounter = 0;
        ArrayList<Integer> aux = new ArrayList<Integer>();

        for(int i = 0; i < r*2; i++){
            for(int j=0; j < numCol; j++){
                auxCounter += counts.get(i+(j*(2*r)));
            }
            counter.add(auxCounter);
            auxCounter = 0;
        }

        for(int a=0; a < counter.size()/2; a++){
            aux.add(counter.get(a));
            aux.add(counter.get(a+r));
        }
        return aux;
    }

    /**
     * Nijkc counts, for all i, j, k, c.
     */
    private ArrayList<Integer> Nijkc() {
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

    /**
     * Calculates alpha
     */
    private float edgeWeight(ArrayList<Integer> Nijkc, ArrayList<Integer> NikcJ, ArrayList<Integer> NijcK, ArrayList<Integer> Nc, int N, int q, int r, int c1){
        float alpha = 0;
        int auxIdx;
        float auxDivbyZero;
        double inLog;
        for(int j=0; j < q; j++){
            for(int k=0; k < r; k++){
                for(int c=0; c < c1; c++){
                    auxIdx = c * r + k + j * (2 * r);
                    auxDivbyZero = (float)NikcJ.get(c + k*c1) * (float)NijcK.get(c + j*c1);
                    inLog = ((float)Nijkc.get(auxIdx) * (float)Nc.get(c))/auxDivbyZero;
                    if(!Double.isNaN(inLog) && (inLog!=0 && auxDivbyZero!=0 ) ){ //Ver se inLog e diferente de NaN e de Zero
                        alpha += ((float)Nijkc.get(auxIdx)/N) * (Math.log10(inLog)/Math.log10(2));
                    }
                }
            }
        }
        return alpha;
    }

    /**
     * Calculates edge weight using LL algorithm
     * @return Edge weight
     */
    public float calcWeight(){
        ArrayList<Integer> Nijkc = this.Nijkc();

        ArrayList<Integer> NC = this.nC(this.Class, this.s);

        ArrayList<Integer> NijcK = this.nInstacesK(Nijkc, this.r2);

        ArrayList<Integer> NikcJ = this.nInstacesJ(Nijkc,  this.r2);

       return this.edgeWeight(Nijkc, NikcJ, NijcK, NC, this.Class.size(), this.r1, this.r2, this.s);
    }

    /**
     * Gets classification values
     * @return Classification values
     */
    public ArrayList<Integer> getClassC() {
        return Class;
    }

    /**
     * Gets number of different values for Node1
     * @return Number of different values for Node1
     */
    public int getR1() {
        return r1;
    }

    /**
     * Gets number of different values for Node2
     * @return number of different values for Node2
     */
    public int getR2() {
        return r2;
    }

    /**
     * Gets number of different values for Class
     * @return number of different values for Class
     */
    public int getS() {
        return s;
    }
}