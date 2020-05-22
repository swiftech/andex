package andex.demo;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import andex.core.status.ActionBuilder;
import andex.core.status.StatusBus;

public class StatusDemoActivity extends AppCompatActivity {

    private StatusBus statusBus;

    private TextView tvEdit;
    private Button btnOk;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_demo);

        statusBus = StatusBus.newInstance(StatusDemoActivity.class, StatusDemoActivity.this, getWindow().getDecorView());
        statusBus.status("VIEW")
                .in(ActionBuilder.create()
                        .text(R.id.tv_edit, "Edit")
                        .text(R.id.btn1, "Save")
                        .disable(R.id.btn1, R.id.et1, R.id.et2)
                        .bgColorInt(R.id.btn1, Color.argb(255, 0, 255, 0))
                )
                .in(() -> {
                    ((TextView)findViewById(R.id.tv_edit)).setLines(4);
                })
                .status("EDIT")
                .in(ActionBuilder.create()
                        .text(R.id.tv_edit, "Cancel")
                        .text(R.id.btn1, "Save")
                        .enable(R.id.btn1, R.id.et1, R.id.et2)
                        .bgColorInt(R.id.btn1, Color.argb(255, 255, 0, 0))
                );

        tvEdit = findViewById(R.id.tv_edit);
        tvEdit.setOnClickListener(v -> {
            if (statusBus.isStatus("VIEW")) {
                statusBus.post("EDIT");
            } else if (statusBus.isStatus("EDIT")) {
                statusBus.post("VIEW");
            }
        });

        btnOk = findViewById(R.id.btn1);
        btnOk.setOnClickListener(v -> {
            statusBus.post("VIEW");
        });

        statusBus.post("VIEW");
    }
}
