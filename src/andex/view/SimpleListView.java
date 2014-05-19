package andex.view;

import andex.model.BaseListAdapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.andex.R;

import java.util.List;
import java.util.Map;

/**
 * The ListView with SimpleAdapter is not simple actually, so this SimpleListView
 * is born to make it really simple.
 * <p/>
 * If you want to create list view with one icon, inherit me and
 * override getAdapter() method with SimpleIconListViewAdapter returned.
 * @deprecated
 * @author
 */
public class SimpleListView extends SimpleCompositeView {

	public SimpleListView(Context context, ListView lv) {
		super(context, lv);
	}

	public SimpleListView(Context context, ListView lv, String defaultLabel) {
		super(context, lv, defaultLabel);
	}

	@Override
	protected ListAdapter getAdapter(Context context) {
		return new SimpleAdapter(context, data, android.R.layout.simple_list_item_2, keys, new int[]{
				android.R.id.text1, android.R.id.text2});
	}


	/**
	 * Adapter for ListView to show icon and text(title and description).
	 *
	 * @author
	 * @deprecated
	 */
	public static class SimpleIconListViewAdapter extends BaseListAdapter {

		/**
		 * @param context
		 * @param data
		 * @param keys    分别是 icon, title, description
		 */
		public SimpleIconListViewAdapter(Context context, List<Map<String, ?>> data, String[] keys) {
			super(context, data, keys);
			layoutResId = R.layout.ax_listview_item_icon_left;
			itemResIds = new int[]{R.id.ax_iv_lv_item_icon, R.id.ax_tv_lv_item_title, R.id.ax_tv_lv_item_desc};
		}

		/**
		 * @param context
		 * @param data
		 * @param keys
		 * @param layoutResId 自定义资源文件
		 * @param itemResIds  自定义资源文件中的资源ID
		 */
		public SimpleIconListViewAdapter(Context context, List<Map<String, ?>> data, String[] keys, int layoutResId,
										 int[] itemResIds) {
			super(context, data, keys, layoutResId, itemResIds);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			inflater = LayoutInflater.from(context);
			LinearLayout layout = (LinearLayout) inflater.inflate(layoutResId, null);

			Map row = data.get(position);
			if (row == null) {
				Log.v("", String.format("No item at position: %d", position));
				return layout;
			}

			// Icon
			ImageView imgView = (ImageView) layout.findViewById(itemResIds[0]);
			Object img = row.get(keys[0]);
			if (img instanceof Drawable) {
				imgView.setImageDrawable((Drawable) img);
			}
			else if (img instanceof Integer) {
				imgView.setImageResource((Integer) img);
			}
			else {
				return layout;
			}

			// Title
			if (itemResIds.length > 1 && layout.findViewById(itemResIds[1]) != null) {
				TextView tvTitle = (TextView) layout.findViewById(itemResIds[1]);
				Object o = row.get(keys[1]);
				if (o instanceof Integer) {
					tvTitle.setText(context.getString((Integer) o));
				}
				else {
					tvTitle.setText(new String(o.toString().toString().getBytes()));
				}
			}

			// Description
			if (itemResIds.length > 2 && layout.findViewById(itemResIds[2]) != null) {
				TextView tvDesc = (TextView) layout.findViewById(itemResIds[2]);
				if (keys.length <= 2) {
					tvDesc.setVisibility(View.GONE);
				}
				else {
					tvDesc.setVisibility(View.VISIBLE);
					tvDesc.setText(new String(row.get(keys[2]).toString().getBytes()));
				}
			}
			return layout;
		}


	}
}
