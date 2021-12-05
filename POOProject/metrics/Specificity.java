package metrics;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Extends RateCalc to get counts to calculate sensitivity
 */
public class Specificity extends RateCalc<ArrayList<Float>>{

    /**
     * @param y_true True class values
     * @param y_pred Predicted class values
     */
    public Specificity(int[] y_true, int[] y_pred) {
        super(y_true, y_pred);
    }

    /**
     * Gets specificity score
     * @return Specificity score
     */
    @Override
    public ArrayList<Float> getScore() {
        ArrayList<Float> specificity = new ArrayList<>();
        ArrayList<Integer> TN = super.getTrue_negatives();
        ArrayList<Integer> FP = super.getFalse_positives();
        ArrayList<Integer> iT = super.getInstancesTest();
        int nClasses = super.getNumClass();
        double aux = 0.0;

        for(int i = 0; i < nClasses; i++){
            specificity.add(((float)TN.get(i)/(float)(TN.get(i)+FP.get(i))));
            aux += iT.get(i)*specificity.get(i);
        }
        specificity.add((float) (aux/iT.stream().mapToInt(Integer::intValue).sum()));

        return specificity;
    }

}
