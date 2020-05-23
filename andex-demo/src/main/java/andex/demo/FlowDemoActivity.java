package andex.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import andex.core.controller.FragmentBuilder;

/**
 *
 */
public class FlowDemoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_demo);
        FragmentBuilder fragmentBuilder = new FragmentBuilder(this, new FirstFragment());
        fragmentBuilder.replace(R.id.fl_content).start();
    }

    public void toFirst() {
        FragmentBuilder fragmentBuilder = new FragmentBuilder(this, new FirstFragment());
        fragmentBuilder.replace(R.id.fl_content).withId(1001).start();
    }

    public void toSecond() {
        SecondFragment secondFragment = new SecondFragment();
        new FragmentBuilder(this, secondFragment).replace(R.id.fl_content).withId(1001).start();
    }
}
