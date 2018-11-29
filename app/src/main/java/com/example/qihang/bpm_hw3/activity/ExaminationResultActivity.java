package com.example.qihang.bpm_hw3.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.network.RemoteManager;
import com.example.qihang.bpm_hw3.network.model.ExaminationResult;
import com.example.qihang.bpm_hw3.network.services.HospitalInterface;
import com.example.qihang.bpm_hw3.utils.JsonUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("LongLogTag")
public class ExaminationResultActivity extends AppCompatActivity {

    TextView mID, mTime, mMedicalDoctorName, mResultDetail, mExaminationDetail, mReExamination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_result);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mID = findViewById(R.id.examination_result_id);
        mTime = findViewById(R.id.examination_time);
        mMedicalDoctorName = findViewById(R.id.medical_doctor_name);
        mResultDetail = findViewById(R.id.examination_result_detail);
        mExaminationDetail = findViewById(R.id.examination_detail);
        mReExamination = findViewById(R.id.need_re_examination);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        HospitalInterface mHospitalInterface = RemoteManager.create(HospitalInterface.class);
        Call<ResponseBody> call = mHospitalInterface.examinationResultItem(getIntent().getStringExtra("examination_result_id"));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        ExaminationResult result = JsonUtil.fromJson(json, ExaminationResult.class);
                        mID.setText(result.getId());
                        mTime.setText(result.getTimeString());
                        mMedicalDoctorName.setText(result.getMedical_doctor_id().getName());
                        mExaminationDetail.setText(result.getExamination_id().getDetail());
                        mResultDetail.setText(result.getDetail());
                        mReExamination.setText(result.getNeed_re_examination());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("ExaminationResultActivity onFailure", t.toString());
            }
        });
    }
}
