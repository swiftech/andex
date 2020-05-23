package andex.demo;

import android.os.Bundle;
import android.util.Log;

import andex.core.BaseFlowActionBarActivity;
import andex.core.controller.FragmentBuilder;

/**
 *
 */
public class FlowDemoActivity extends BaseFlowActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_demo);

        String title = getArgStrFromPrevious("title");
        Log.e(FlowDemoActivity.class.getSimpleName(), title);
        this.setTitle(title);

        FragmentBuilder fragmentBuilder = new FragmentBuilder(this, new FirstFragment());
        fragmentBuilder.replace(R.id.fl_content)
                .with("title", title)
                .start();
    }

    public void toFirst() {
        FragmentBuilder fragmentBuilder = new FragmentBuilder(this, new FirstFragment());
        fragmentBuilder.replace(R.id.fl_content)
                .withId(1001)
                .with("title", "First Fragment")
                .with("content", "This is the first fragment")
                .start();
    }

    public void toSecond() {
        SecondFragment secondFragment = new SecondFragment();
        new FragmentBuilder(this, secondFragment).replace(R.id.fl_content)
                .withId(1001)
                .with("title", "Second Fragment")
                .with("content", "This is the second fragment")
                .start();
    }
}
