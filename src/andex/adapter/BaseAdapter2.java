package andex.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * 具有缓存View缓存功能的适配器。
 */
public class BaseAdapter2 extends BaseAdapter {
	
	protected LayoutInflater inflater;
	
	protected Context context;

	// 用于缓存（有需要的话）每一项的View，
	protected Map<Integer, View> viewCache = new HashMap<Integer, View>();
	
	public BaseAdapter2(Context context) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
