package andex.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import andex.SpinnerItem;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * 简化Spinner的操作
 * @author
 * 
 */
public class SimpleSpinner {

	protected Context ctx;

	protected Spinner spinner;

	public SimpleSpinner(Context ctx) {
		super();
		this.ctx = ctx;
	}

	public SimpleSpinner(Spinner spinner) {
		super();
		this.spinner = spinner;
	}

	/**
	 * 用键-值对集合初始化Spinner
	 * Init spinner with key-value data in Map.
	 * 
	 * @param spinner
	 * @param map
	 * @return
	 */
	public Spinner initSpinner(Spinner spinner, Map map) {
		return initSpinner(spinner, mapToList(map).toArray());
	}

	/**
	 * 用户数组初始化Spinner。
	 * Init spinner with data array.
	 * 
	 * @param spinner
	 * @param data
	 * @return
	 */
	public Spinner initSpinner(Spinner spinner, Object[] data) {
		if (ctx == null) {
			throw new RuntimeException("No context specified for this Spinner");
		}
		if (spinner == null) {
			throw new RuntimeException("No spinner found");
		}
		this.spinner = spinner;
		if (data == null || data.length == 0) {
			Log.w("andex", "Nothing to init for Spinner");
			return spinner;
		}
		Log.v("andex", "Init Spinner with " + data.length + " items");
		ArrayAdapter adapter = new ArrayAdapter(ctx, android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		return spinner;
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	public Object setSelection(Object item) {
		return setSelection(spinner, item);
	}

	/**
	 * Set spinner's selection directly by item object.
	 * 
	 * @deprecated
	 * @param spinner
	 * @param item
	 */
	// public SpinnerItem setSelection(Spinner spinner, SpinnerItem item) {
	// if (spinner == null || item == null) {
	// return null;
	// }
	// int n = spinner.getAdapter().getCount();
	// for (int i = 0; i < n; i++) {
	// Object value = spinner.getItemAtPosition(i);
	// if (item.equals(value)) {
	// spinner.setSelection(i);
	// return item;
	// }
	// }
	// return null;
	// }

	/**
	 * 
	 * @param itemId
	 * @return
	 */
	public Object setSelection(long itemId) {
		return setSelection(spinner, itemId);
	}

	/**
	 * 按照ID来选中Spinner项。
	 * Set spinner's selection by item ID.
	 * 
	 * @param spinner
	 * @param itemId
	 */
	public Object setSelection(Spinner spinner, Object itemId) {
		if (spinner == null || itemId == null) {
			return null;
		}
		int n = spinner.getAdapter().getCount();
		for (int i = 0; i < n; i++) {
			Object value = spinner.getItemAtPosition(i);
			if (value instanceof SpinnerItem) {
				SpinnerItem si = (SpinnerItem) value;
				Log.v("", itemId + " -- " + si.getId());
				if (itemId.equals(si.getId())) {
					spinner.setSelection(i);
					return si;
				}
			}
			else {
				if (itemId.equals(value)) {
					spinner.setSelection(i);
					return itemId;
				}
			}
		}
		return null;
	}

	/**
	 * 获取选中项的Key值。
	 * Get key of selected item.
	 * 
	 * @return
	 */
	public Object getSelectedItemKey() {
		SpinnerItem spitem = (SpinnerItem) spinner.getSelectedItem();
		return spitem.getId();
	}

	/**
	 * 
	 * Convert key-value map to SpinnerItem list.
	 * 
	 * @param map
	 * @return
	 */
	protected List<SpinnerItem> mapToList(Map map) {
		final List<SpinnerItem> items = new ArrayList<SpinnerItem>();
		for (Object key : map.keySet()) {
			Object value = map.get(key);
			// Log.v("", "" + value);
			items.add(new SpinnerItem(key, value));
		}
		return items;
	}
}
