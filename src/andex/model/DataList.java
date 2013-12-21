package andex.model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import andex.Utils;
import android.content.Context;

/**
 * 表示一个数据集合。
 * Represent data record set.
 * 
 * @author 
 * 
 */
public class DataList<T extends DataRow> extends ArrayList<T> {
	
	public DataList() {
		super();
	}
	
	public DataList(Collection<T> collection) {
		super(collection);
	}
	
	/**
	 * 
	 * Wrap all elements in collection to DataRow class type.
	 * @param coll
	 * @return
	 */
	public static DataList wrap(Context ctx,Collection coll) {
		return wrap(ctx, coll, DataRow.class);
	}
	
	/**
	 * Wrap all elements in collection to specified type.
	 * @param coll
	 * @param elType Target class type.
	 * @return
	 */
	public static DataList wrap(Context ctx, Collection coll, Class elType) {
		DataList ret = new DataList();
		if (coll == null || coll.size() == 0) {
			return ret;
		}
		Constructor cst;
		try {
			cst = elType.getConstructor(Context.class, Map.class);
			if (cst == null) {
				return ret;
			}
			Iterator it = coll.iterator();
			while (it.hasNext()) {
				ret.add(cst.newInstance(ctx, (Map) it.next()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ret;
		}
		return ret;
	}

	/**
	 * Add new row as DataRow type.以默认DataRow类型添加一行。
	 * @param keys
	 * @param values
	 */
	public void addRow(String[] keys, Object[] values) {
		addRow(this.size() - 1, DataRow.class, keys, values);
	}

	/**
	 * 
	 * @param idx
	 * @param keys
	 * @param values
	 */
	public void addRow(int idx, String[] keys, Object[] values) {
		addRow(idx, DataRow.class, keys, values);
	}

	/**
	 * 
	 * @param rowType
	 * @param keys
	 * @param values
	 */
	public void addRow(Class rowType, String[] keys, Object[] values) {
		addRow(this.size() - 1, rowType, keys, values);
	}

	/**
	 * 添加一行，存储为指定的类型，此类型必须与T一致
	 * @param rowType
	 * @param keys
	 * @param values
	 */
	public void addRow(int idx, Class rowType, String[] keys, Object[] values) {
		T row;
		try {
			Constructor cst = rowType.getConstructor(Map.class);
			row = (T)cst.newInstance(Utils.arrays2map(keys, values));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}		
		if(idx <0) {
			this.add(row);
		}
		else {
			this.add(idx, row);	
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public DataRow getRow(final long id) {
		DataRow ret = null; 
		for (int i = 0; i < this.size(); i++) {
			DataRow row = this.get(i);
			if(row.getID() == id) {
				ret = row;
				break;
			}			
		}
		return ret;
	}
	
	/**
	 * 遍历表中所有的数据行（DataRow）
	 * @param handler
	 */
	public void traverse(Callback handler) {
		handler.before(this.size());
		for (int i = 0; i < this.size(); i++) {
//			Log.v("andex", "Traverse DataList's items with type: " + this.get(i).getClass().toString());
//			Log.v("andex", this.get(i).toString());
			if (!handler.invoke(i, (T) this.get(i))) {
				break;
			}
		}
		handler.after();
	}
	
	/**
	 * 将某个字段纵向导出为数组。
	 * @param colName
	 * @return
	 */
	public Object[] getColumn(String colName) {
		Object[] ret = new Object[this.size()];
		for(int i = 0; i < this.size(); i++) {
			ret[i] = this.get(i).get(colName);
		}
		return ret;
	}
	
	/**
	 * 将某个字段纵向导出为数组。
	 * @param colName
	 * @return
	 */
	public String[] getColumnAsString(String colName) {
		String[] ret = new String[this.size()];
		for(int i = 0; i < this.size(); i++) {
			ret[i] = this.get(i).get(colName).toString();
		}
		return ret;
	}
	
	/**
	 * Callback for traverse DataList.
	 * @author 
	 *
	 * @param <T>
	 */
	public static interface Callback<T extends DataRow> {
		boolean BREAK = false;
		boolean CONTINUE = true;
		public void before(int size);
		public void after();
		/**
		 * 
		 * @param i
		 * @param row
		 * @return true to continue; false to break the following operations.
		 */
		public boolean invoke(int i, T row);
	}
	
	
	public static class CallbackAdapter<T extends DataRow> implements Callback<T>{

		@Override
		public void before(int size) {}

		@Override
		public void after() {}

		@Override
		public boolean invoke(int i, T row) {
			return false;
		}
		
	}

}
