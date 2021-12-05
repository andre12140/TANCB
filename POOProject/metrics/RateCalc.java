package metrics;


import java.util.*;
import java.util.stream.Collectors;

/**
 * Uses a confusion matrix to get True Positive, False Positive, True Negative and False Negative counts
 */
public abstract class RateCalc<T> implements Metrics<T> {

    private ArrayList<Integer> TP = new ArrayList<>();
    private ArrayList<Integer> FP = new ArrayList<>();
    private ArrayList<Integer> TN = new ArrayList<>();
    private ArrayList<Integer> FN = new ArrayList<>();

    private int numClass;
    private ArrayList<Integer> instancesTest = new ArrayList<>();

    /**
     * @param y_true True class values
     * @param y_pred Predicted class values
     */
    public RateCalc(int[] y_true, int[] y_pred){

        int[][] cm = ConfusionMatrix.of(y_true,y_pred).getMatrix();

        numClass = cm.length;

        List<Integer> list = Arrays.stream(y_pred).boxed().collect(Collectors.toList());

        for(int i=0; i < numClass; i++){
            instancesTest.add(Collections.frequency(list,i));
        }

        int lineCount=0;
        int totalSum=0;

        for(int[] lineCM : cm){

            int TPaux = lineCM[lineCount];
            int FNaux = Arrays.stream(lineCM).sum() - TPaux;

            TP.add(TPaux);
            FN.add(FNaux);
            int FPsumAux = 0;
            for(int col=0 ; col < cm.length ; col++){
                FPsumAux+=cm[col][lineCount];
            }
            int FPaux = FPsumAux - TPaux;
            FP.add(FPaux);

            totalSum += Arrays.stream(lineCM).sum();
            lineCount++;
        }

        for(int i =0; i< TP.size(); i++){
            TN.add(totalSum-(TP.get(i)+FP.get(i)+FN.get(i)));
        }
    }

    @Override
    public abstract T getScore();

    /**
     * @return False negative counts for all classes
     */
    public ArrayList<Integer> getFalse_negatives() {
        return FN;
    }

    /**
     * @return False positive counts for all classes
     */
    public ArrayList<Integer> getFalse_positives() {
        return FP;
    }

    /**
     * @return True negative counts for all classes
     */
    public ArrayList<Integer> getTrue_negatives() {
        return TN;
    }

    /**
     * @return True positive counts for all classes
     */
    public ArrayList<Integer> getTrue_positives() {
        return TP;
    }

    /**
     * @return Number of class values
     */
    public int getNumClass() {
        return numClass;
    }

    /**
     * @return The total of instances for all classes
     */
    public ArrayList<Integer> getInstancesTest() {
        return instancesTest;
    }
}
