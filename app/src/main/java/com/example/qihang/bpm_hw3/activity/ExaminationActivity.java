package com.example.qihang.bpm_hw3.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.network.RemoteManager;
import com.example.qihang.bpm_hw3.network.model.Examination;
import com.example.qihang.bpm_hw3.network.model.Prescript;
import com.example.qihang.bpm_hw3.network.services.HospitalInterface;
import com.example.qihang.bpm_hw3.utils.JsonUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("LongLogTag")
public class ExaminationActivity extends AppCompatActivity {

    TextView mID, mTime, mMedicalDoctorName, mOutpatientDoctorName, mDetail, goto_payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mID = findViewById(R.id.examination_id);
        mTime = findViewById(R.id.examination_time);
        mMedicalDoctorName = findViewById(R.id.medical_doctor_name);
        mOutpatientDoctorName = findViewById(R.id.outpatient_doctor_name);
        mDetail = findViewById(R.id.examination_detail);
        goto_payment = findViewById(R.id.goto_payment);

        HospitalInterface mHospitalInterface = RemoteManager.create(HospitalInterface.class);
        Call<ResponseBody> call = mHospitalInterface.examinationItem(getIntent().getStringExtra("examination_id"));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        Examination result = JsonUtil.fromJson(json, Examination.class);
                        mID.setText(result.getId());
                        mTime.setText(result.getTimeString());
                        mMedicalDoctorName.setText(result.getMedical_doctor_id().getName());
                        mOutpatientDoctorName.setText(result.getOutpatient_doctor_id().getName());
                        mDetail.setText(result.getDetail());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("ExaminationActivity onFailure", t.toString());
            }
        });

        goto_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to Payment Activity
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
