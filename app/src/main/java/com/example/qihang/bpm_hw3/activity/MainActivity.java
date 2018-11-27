package com.example.qihang.bpm_hw3.activity;

import android.content.Intent;
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


        mName = findViewById(R.id.name);
        mRegistration = findViewById(R.id.registration);
        mPayment = findViewById(R.id.payment);
        mExamination_result = findViewById(R.id.examination_result);
        mDrug_result = findViewById(R.id.drug_result);
        mPrescript = findViewById(R.id.prescript);
        mExamination = findViewById(R.id.examination);


        // name = getIntent().getStringExtra("name");
        // patientId = getIntent().getStringExtra("patientId");
        patientId = "1542698662334";

        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationListActivity.class);
                intent.putExtra("patient_id", patientId);
                startActivity(intent);
            }
        });
        mPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PaymentListActivity.class);
                intent.putExtra("patient_id", patientId);
                startActivity(intent);
            }
        });
        mExamination_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mDrug_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mPrescript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PrescriptListActivity.class);
                intent.putExtra("patient_id", patientId);
                startActivity(intent);
            }
        });
        mExamination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExaminationListActivity.class);
                intent.putExtra("patient_id", patientId);
                startActivity(intent);
            }
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
