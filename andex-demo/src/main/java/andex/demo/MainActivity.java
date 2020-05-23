package andex.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;

import andex.core.BaseFlowActionBarActivity;
import andex.core.controller.ActivityBuilder;
import andex.core.controller.FragmentBuilder;
import andex.utils.StringUtils;

public class MainActivity extends BaseFlowActionBarActivity {

    private Handler handler;

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


//        handler = new Handler();
//        new Thread(() -> {
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            handler.post(()->{
//                toSecond();
//            });
//        }).start();

    }


}
