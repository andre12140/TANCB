package metrics;

import java.util.HashSet;
import java.util.Set;

/**
 * The confusion matrix of truth and predictions.
 */
public class ConfusionMatrix {

    /** Confusion matrix. */
    private final int[][] matrix;

    /**
     * @param matrix Confusion matrix
     */
    public ConfusionMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    /**
     * Creates the confusion matrix.
     * @param truth True class values
     * @param prediction Predicted class values
     * @return Confusion matrix
     */
    public static ConfusionMatrix of(int[] truth, int[] prediction) {
        if (truth.length != prediction.length) {
            throw new IllegalArgumentException(String.format("The vector sizes don't match: %d != %d.", truth.length, prediction.length));
        }

        Set<Integer> y = new HashSet<>();

        // Sometimes, small test data doesn't have all the classes.
        for (int i = 0; i < truth.length; i++) {
            y.add(truth[i]);
            y.add(prediction[i]);
        }

        int k = 0;
        for (int c : y) {
            if (k < c) k = c;
        }

        int[][] matrix = new int[k+1][k+1];
        for (int i = 0; i < truth.length; i++) {
            matrix[truth[i]][prediction[i]] += 1;
        }

        return new ConfusionMatrix(matrix);
    }

    /**
     * Gets confusion matrix
     * @return Confusion matrix
     */
    public int[][] getMatrix() {
        return matrix;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t\tROW=truth and COL=predicted\n");

        for(int i = 0; i < matrix.length; i++){
            sb.append(String.format("\t\tclass %2d |", i));
            for(int j = 0; j < matrix.length; j++){
                sb.append(String.format("%8d |", matrix[i][j]));
            }
            sb.append("\n");
        }

        return sb.toString();
    }



}
