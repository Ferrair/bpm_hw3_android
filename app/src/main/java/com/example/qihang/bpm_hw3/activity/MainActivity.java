package com.example.qihang.bpm_hw3.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.qihang.bpm_hw3.R;

public class MainActivity extends AppCompatActivity {

    TextView mName;
    TextView mRegistration, mPayment, mExamination_result, mDrug_result, mPrescript, mExamination;

    String name, patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * change status bar color
         */
        initStatus(R.color.status_bar);


        mName = (TextView) findViewById(R.id.name);
        mRegistration = (TextView) findViewById(R.id.registration);
        mPayment = (TextView) findViewById(R.id.payment);
        mExamination_result = (TextView) findViewById(R.id.examination_result);
        mDrug_result = (TextView) findViewById(R.id.drug_result);
        mPrescript = (TextView) findViewById(R.id.prescript);
        mExamination = (TextView) findViewById(R.id.examination);

        SharedPreferences sharedPreferences = getSharedPreferences("patient", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "王启航");
        // final String patientId = sharedPreferences.getString("patientId", "1542698662334");
        String patientId = "1544412520686";

        mName.setText(name);

        mRegistration.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegistrationListActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });
        mPayment.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PaymentListActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });
        mExamination_result.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ExaminationResultListActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });
        mDrug_result.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), DrugResultListActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);

        });
        mPrescript.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PrescriptListActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });
        mExamination.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ExaminationListActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });
    }

    public void initStatus(int color) {
        Window window = this.getWindow();
        // 取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // 设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(color));
        }
    }
}
