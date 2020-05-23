package andex.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;

import andex.Constants;
import andex.core.BaseFlowActionBarActivity;
import andex.core.controller.ActivityBuilder;

public class MainActivity extends BaseFlowActionBarActivity {

    private Handler handler;

    public MainActivity() {
        Constants.debugMode = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View btnFlowDemo = findViewById(R.id.btn_flow_demo);
        btnFlowDemo.setOnClickListener(v->{
            new ActivityBuilder(this, FlowDemoActivity.class).with("title", "Flow Demo").start();
        });
    }


}
