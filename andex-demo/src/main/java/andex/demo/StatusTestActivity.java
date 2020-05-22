package andex.demo;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import andex.mvc.status.ActionBuilder;
import andex.mvc.status.StatusBus;

public class StatusTestActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);


        StatusBus statusBus = StatusBus.newInstance(this.getClass(), this, getWindow().getDecorView());
        statusBus.init()
                .in(ActionBuilder.create()
                        .text(R.id.textView, "Inited")
                )
                .status("hey")
                .in(ActionBuilder.create()
                        .change(R.id.textView, R.id.textView2).text("This will be gone")
                        .change(R.id.textView3).text("this will be invisible")
                        .change(R.id.textView4).text("this will be show lately")
                )
                .in(ActionBuilder.create()
                        .bgColorInt(R.id.textView, Color.GREEN)
//                        .bgColor(R.id.textView2, Color.valueOf(120, 120, 120))
                        .bgColorRes(R.id.textView3, R.color.colorAccent)
                        .hint(R.id.editText, "Are you sure?")
                        .hint(R.id.editText2, R.string.app_name)
                        .invisible(R.id.textView4))
                .out(ActionBuilder.create()
                        .visible(R.id.textView4)
                        .invisible(R.id.textView3)
                        .gone(R.id.textView2, R.id.textView)
                        .change(R.id.textView4).text("this will be show lately")
                )
                .out(() -> {
                    findViewById(R.id.textView4).setBackgroundColor(Color.MAGENTA);
                });

//        new Thread(() -> {
//            try {
//                Thread.sleep(3000);
//                statusBus.post("hey");
//
//                Thread.sleep(3000);
//                statusBus.post("ha");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }).start();

        findViewById(R.id.btn1).setOnClickListener(v -> {
            statusBus.post("hey");
        });
        findViewById(R.id.btn2).setOnClickListener(v -> {
            statusBus.post("ha");
        });
        statusBus.post();
    }

}
