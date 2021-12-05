package score;

import graph.Node;
import graph.NodeInfo;

/**
 *  Minimum Direction Score
 */
public class MDLScore extends LLScore {

    /**
     * @param N1 Parent node
     * @param N2 Node to be evaluated
     * @param C Classification node
     */
    public MDLScore(Node<NodeInfo> N1, Node<NodeInfo> N2, Node<NodeInfo> C) {
        super(N1, N2, C);
    }

    /**
     * Calculates edge weight using MDL algorithm
     * @return MDL score
     */
    public float mdlCalcWeight(){
        float constMDL = (float) ((getS() * (getR1() - 1) * (getR2() - 1) * Math.log(getClassC().size()))/2);

        return super.calcWeight() - constMDL;
    }

}
