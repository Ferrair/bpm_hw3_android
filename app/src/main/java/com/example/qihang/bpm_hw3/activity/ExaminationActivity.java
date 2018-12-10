package com.example.qihang.bpm_hw3.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.network.RemoteManager;
import com.example.qihang.bpm_hw3.network.model.Examination;
import com.example.qihang.bpm_hw3.network.model.Prescript;
import com.example.qihang.bpm_hw3.network.services.HospitalInterface;
import com.example.qihang.bpm_hw3.utils.JsonUtil;
import com.example.qihang.bpm_hw3.utils.QRCodeUtil;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mID = (TextView) findViewById(R.id.examination_id);
        mTime = (TextView) findViewById(R.id.examination_time);
        mMedicalDoctorName = (TextView) findViewById(R.id.medical_doctor_name);
        mOutpatientDoctorName = (TextView) findViewById(R.id.outpatient_doctor_name);
        mDetail = (TextView) findViewById(R.id.examination_detail);
        goto_payment = (TextView) findViewById(R.id.goto_payment);

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

        goto_payment.setOnClickListener(v -> {
            // Go to Payment Activity
            Intent intent = new Intent(ExaminationActivity.this, PaymentActivity.class);
            intent.putExtra("examination_id", mID.getText().toString());
            startActivity(intent);
        });

        findViewById(R.id.back).setOnClickListener(v -> finish());

        findViewById(R.id.qr_code).setOnClickListener(v -> {
            Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(mID.getText().toString(), 600);
            LayoutInflater inflater = LayoutInflater.from(ExaminationActivity.this);
            View rootLayout = inflater.inflate(R.layout.dialog_qr_code, null);
            // 加载自定义的布局文件
            final AlertDialog dialog = new AlertDialog.Builder(ExaminationActivity.this).create();
            ImageView img = rootLayout.findViewById(R.id.qr_code_image);
            img.setImageBitmap(mBitmap);
            TextView textView = rootLayout.findViewById(R.id.qr_code_desc);
            textView.setText(mID.getText().toString());

            dialog.setView(rootLayout);

            dialog.show();
            rootLayout.setOnClickListener(paramView -> dialog.cancel());

        });
    }

}
