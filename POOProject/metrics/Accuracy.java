package metrics;

/**
 * Extends RateCalc to get counts to calculate accuracy
 */
public class Accuracy extends RateCalc<Float> {

    /**
     * Calculates the accuracy between the true classes and the predicted classes
     * @param y_true True class values
     * @param y_pred Predicted class values
     */
    public Accuracy(int[] y_true, int[] y_pred){
        super(y_true,y_pred);
    }

    /**
     * Gets accuracy score
     * @return The accuracy score
     */
    public Float getScore(){
        int TP = super.getTrue_positives().stream().mapToInt(Integer::intValue).sum();
        int TN = super.getTrue_negatives().stream().mapToInt(Integer::intValue).sum();
        int P = TP + TN;
        int FP = super.getFalse_positives().stream().mapToInt(Integer::intValue).sum();
        int FN = super.getFalse_negatives().stream().mapToInt(Integer::intValue).sum();
        int N = FP + FN;

        return (float) (P) / (P + N);
    }

}
