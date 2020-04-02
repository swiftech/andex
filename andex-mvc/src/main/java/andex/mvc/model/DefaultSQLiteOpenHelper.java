package andex.mvc.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * A default SQLite helper which did nothing.
 *
 * @author
 */
public class DefaultSQLiteOpenHelper extends SQLiteOpenHelper {


    public DefaultSQLiteOpenHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    public DefaultSQLiteOpenHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DB", "Create DB");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("DB", String.format("Upgrade DB schema from version %d to %d", oldVersion, newVersion));
    }

}
