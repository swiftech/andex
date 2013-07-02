package andex.model;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.widget.ListAdapter;

/**
 * 列表适配器基类。
 * @author 
 *
 */
public abstract class BaseListAdapter implements ListAdapter{

	protected LayoutInflater inflater;
	
	protected String[] keys;
	protected Context context;
	protected int layoutResId;
	protected int[] itemResIds;

	protected List<Map<String, ?>> data;
	
	// Label that displayed while no data for this View.
//	protected String defaultLabel = "";
	
	/**
	 * 基本的ListAdapter实现，注入Context和初始化了Inflater
	 * @param context
	 */
	public BaseListAdapter(Context context) {
		super();
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}
	
	public BaseListAdapter(Context context, List<Map<String, ?>> data, String[] keys) {
		this(context);
		this.keys = keys;
		this.data = data;
	}
	
	public BaseListAdapter(Context context, List<Map<String, ?>> data, String[] keys, int layoutResId,  int[] itemResIds) {
		this(context, data, keys);
		this.layoutResId = layoutResId;
		this.itemResIds = itemResIds;
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}
	
	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		
//		inflater = LayoutInflater.from(context);
//		
//		return null;
//	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

}