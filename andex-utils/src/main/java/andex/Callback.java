package andex;

/**
 * Callback for any delayed invoking.
 *
 * @param <T>
 * @author Swiftech
 */
public interface Callback<T> {

    void invoke();

    void invoke(T arg);

    void invoke(Object... args);

    /**
     * Adapter for Callback interface.
     *
     * @param <T>
     * @author Administrator
     */
    class CallbackAdapter<T> implements Callback<T> {
        @Override
        public void invoke() {
        }

        @Override
        public void invoke(T arg) {
        }

        @Override
        public void invoke(Object... args) {
        }
    }

}
