package andex.widget;

/**
 * Callback for dialog.
 *
 * @author
 */
public class DialogCallback<T> {
    /**
     *
     */
    public void onPositive() {
    }

    /**
     * Positive button clicked.
     *
     * @param value
     */
    public void onPositive(T value) {
    }

    /**
     * Positive button clicked with multi-values returned.
     *
     * @param values
     */
    public void onPositive(T... values) {
    }

    /**
     *
     */
    public void onNegative() {
    }

    /**
     * Negative button clicked.
     */
    public void onNegative(T value) {
    }

    /**
     * Invoked after any choices that make.
     */
    public void afterSelected() {
    }
}
