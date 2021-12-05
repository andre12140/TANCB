package metrics;

import java.util.ArrayList;

/**
 * Extends RateCalc to get counts to calculate sensitivity
 */
public class Sensitivity extends RateCalc<ArrayList<Float>>{
    /**
     * @param y_true True class values
     * @param y_pred Predicted class values
     */
    public Sensitivity(int[] y_true, int[] y_pred) {
        super(y_true, y_pred);
    }

    /**
     * Gets sensitivity score
     * @return Sensitivity score
     */
    @Override
    public ArrayList<Float> getScore() {

        ArrayList<Float> sensitivity = new ArrayList<>();
        ArrayList<Integer> TP = super.getTrue_positives();
        ArrayList<Integer> FN = super.getFalse_negatives();
        ArrayList<Integer> iT = super.getInstancesTest();
        int nClasses = super.getNumClass();
        double aux = 0.0;

        for(int i = 0; i < nClasses; i++){
            sensitivity.add(((float)TP.get(i)/(float)(TP.get(i)+FN.get(i))));
            aux += iT.get(i)*sensitivity.get(i);
        }
        sensitivity.add((float) (aux/iT.stream().mapToInt(Integer::intValue).sum()));

        return sensitivity;
    }
}
