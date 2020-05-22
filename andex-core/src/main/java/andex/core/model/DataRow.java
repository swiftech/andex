package andex.core.model;

import android.content.Context;

import org.apache.commons.lang3.NotImplementedException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represent data row in SQLite database.
 *
 * @author
 */
public class DataRow extends HashMap<String, Object> implements Serializable {

    // Inject from constructor if needed.
    protected Context context;

    public DataRow() {
        super();
    }

    public DataRow(Context context) {
        super();
        this.context = context;
    }

    public DataRow(Map map) {
        super(map);
    }

    public DataRow(Context context, Map map) {
        super(map);
    }

    public long getID() {
        throw new NotImplementedException("");
    }

    public interface Callback<T1, T2> {
        boolean invoke(T1 key, T2 value);
    }
}
