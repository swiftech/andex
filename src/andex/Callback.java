package andex;

/**
 * Callback for any delayed invoking.
 * @author Swiftech
 *
 * @param <T>
 */
public interface Callback<T> {

	void invoke();

	void invoke(T arg);

	void invoke(Object... args);

	/**
	 * Adapter for Callback interface.
	 * @author Administrator
	 *
	 * @param <T>
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
