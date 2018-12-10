package com.example.qihang.bpm_hw3.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("LongLogTag")
public class ExaminationResultActivity extends AppCompatActivity {

    TextView mID, mTime, mMedicalDoctorName, mResultDetail, mExaminationDetail, mReExamination;
    HospitalInterface mHospitalInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mID = (TextView) findViewById(R.id.examination_result_id);
        mTime = (TextView) findViewById(R.id.examination_time);
        mMedicalDoctorName = (TextView) findViewById(R.id.medical_doctor_name);
        mResultDetail = (TextView) findViewById(R.id.examination_result_detail);
        mExaminationDetail = (TextView) findViewById(R.id.examination_detail);
        mReExamination = (TextView) findViewById(R.id.need_re_examination);

        findViewById(R.id.back).setOnClickListener(v -> finish());

        mHospitalInterface = RemoteManager.create(HospitalInterface.class);
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
                        if (result.getExamination_id() != null && result.getExamination_id().getDetail() != null) {
                            mExaminationDetail.setText(result.getExamination_id().getDetail());
                        }
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

        findViewById(R.id.report).setOnClickListener(v -> {
            Intent intent = new Intent(ExaminationResultActivity.this, PDFActivity.class);
            intent.putExtra("filename", mID.getText().toString());
            intent.putExtra("fileurl", "http://47.107.241.57:8080/file/U1c365fdb24129c/hospital/Examinationresult/" + mID.getText().toString());
            startActivity(intent);
        });
    }
}
