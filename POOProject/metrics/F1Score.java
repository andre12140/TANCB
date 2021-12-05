package metrics;

import java.util.ArrayList;

/**
 * The F1 score of truth and predictions
 */
public class F1Score extends RateCalc<ArrayList<Float>>{

    /**
     * @param y_true True class values
     * @param y_pred Prediction class values
     */
    public F1Score(int[] y_true, int[] y_pred) {
        super(y_true, y_pred);
    }

    /**
     * Gets F1 score
     * @return F1 score
     */
    @Override
    public ArrayList<Float> getScore() {
        ArrayList<Integer> TP = super.getTrue_positives();
        ArrayList<Integer> FN = super.getFalse_negatives();
        ArrayList<Integer> FP = super.getFalse_positives();
        ArrayList<Integer> iT = super.getInstancesTest();

        ArrayList<Float> f1score = new ArrayList<>();
        ArrayList<Float> recall = new ArrayList<>();
        ArrayList<Float> precision = new ArrayList<>();

        int nClasses = super.getNumClass();
        double aux = 0.0;

        for(int i = 0; i < nClasses; i++){
            recall.add((float)(TP.get(i)/(float)(TP.get(i)+FN.get(i))));
            precision.add((float)(TP.get(i)/(float)(TP.get(i)+FP.get(i))));
            f1score.add((2* precision.get(i) * recall.get(i))/(precision.get(i) + recall.get(i)));

            aux += iT.get(i)*f1score.get(i);
        }
        f1score.add((float) (aux/iT.stream().mapToInt(Integer::intValue).sum()));

        return f1score;
    }
}
