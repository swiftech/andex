package andex.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;

import andex.core.BaseFlowActionBarActivity;
import andex.core.controller.FragmentBuilder;

public class MainActivity extends BaseFlowActionBarActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handler = new Handler();
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.post(()->{
                toSecond();
            });
        }).start();

    }

    public void toSecond() {
        new FragmentBuilder(this, new SecondFragment()).replace(R.id.fl_content).withId(1001).start();
    }

}
