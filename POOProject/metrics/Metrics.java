package metrics;

/**
 * Metrics to evaluate the quality of the prediction
 */
public interface Metrics<T> {
    /**
     * Gets the score of the metric used
     * @return Score
     */
    T getScore();

}
