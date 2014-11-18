package andex;

/**
 * Provide key-value item for Spinner.
 * TODO any other controls else?
 * @author 
 * @deprecated
 */
public class SpinnerItem<K, V> {
	private K id;
	private V value;

	public SpinnerItem(K id, V value) {
		super();
		this.id = id;
		this.value = value;
	}

	public K getId() {
		return id;
	}

	public void setId(K id) {
		this.id = id;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public String toString() {
		if(value == null) {
			return "";
		}
		return value.toString();
	}

}
