package andex.core;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by swiftech on 14-11-26.
 */
public class BasePreferenceActivity extends PreferenceActivity {

    protected Context context;

    protected Resources rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = this;

        this.rs = this.getResources();
    }
}
