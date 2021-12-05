package classifier;

/**
 *  Generic classifier interface
 * @param <T>   the type of objects that this object may classify
 */

public interface Classifier<T> {
    /**
     * Allows the implementor to fit a model to a given data
     * @param features an array of type T with the training data.
     * @param classC classification of the given features.
     */
    void fit(T[] features, T classC);

    /**
     * Allows the implementor to fit a model to a given data
     * @param features an array of type T with the test data.
     * @return the classification of the given features
     */
    T predict(T[] features);

}
