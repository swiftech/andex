package andex.view;

import andex.SpinnerItem;
import andex.model.DataList;
import andex.model.DataRow;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO 目前暂时替换SimpleSpinner和MoreSimpleSpinner，如果成熟则永久替换它们。
 * 简化Spinner的操作
 * 
 * @author
 * 
 */
public class SimpleSpinner2 {

	protected Context ctx;

	protected Activity activity;
	
	protected View view;

	protected Spinner spinner;

	/**
	 * 初始化包含在Activity中的Spinner
	 * @param act
	 * @param ctx
	 * @param resID
	 */
	public SimpleSpinner2(Activity act, Context ctx, int resID) {
		super();
		this.activity = act;
		this.ctx = ctx;
		this.spinner = (Spinner) act.findViewById(resID);
	}
	
	/**
	 * 初始化包含在View中的Spinner
	 * @param view
	 * @param ctx
	 * @param resID
	 */
	public SimpleSpinner2(View view, Context ctx, int resID) {
		super();
		this.view = view;
		this.ctx = ctx;
		this.spinner = (Spinner) view.findViewById(resID);
	}

	/**
	 * 不管Spinner位于何处，直接制定Spinner对象进行初始化。
	 * @param ctx
	 * @param spinner
	 */
	public SimpleSpinner2(Context ctx, Spinner spinner) {
		super();
		this.ctx = ctx;
		this.spinner = spinner;
	}

	public Spinner getSpinner() {
		return spinner;
	}


	/**
	 * 用键-值对集合初始化Spinner Init spinner with key-value data in Map.
	 * 
	 * @param
	 * @param map
	 * @return
	 */
	public Spinner initSpinner(Map map) {
		return initSpinner(mapToList(map).toArray());
	}

	/**
	 * 用户数组初始化Spinner。 Init spinner with data array.
	 * 
	 * @param data
	 * @return
	 */
	public Spinner initSpinner(Object[] data) {
		if (ctx == null) {
			throw new RuntimeException("No context specified for this Spinner");
		}
		if (spinner == null) {
			throw new RuntimeException("No Spinner specified");
		}
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

	public Spinner initSpinner(DataList<DataRow> data, final String idkey, final String valuekey) {
		final List<SpinnerItem> items = new ArrayList<SpinnerItem>();
		data.traverse(new DataList.CallbackAdapter<DataRow>() {

			@Override
			public boolean invoke(int i, DataRow row) {
				items.add(new SpinnerItem(Long.parseLong(row.get(idkey).toString()), row.get(valuekey).toString()));
				return true;
			}

		});
		return initSpinner(items.toArray());
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
	 * 按照ID来选中Spinner项。 Set spinner's selection by item ID.
	 * 
	 * @param itemId
	 */
	public Object setSelection(Object itemId) {
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

	public SpinnerItem getSelectedItem() {
		return (SpinnerItem) spinner.getSelectedItem();
	}

	/**
	 * 获取选中项的Key值。 Get key of selected item.
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
