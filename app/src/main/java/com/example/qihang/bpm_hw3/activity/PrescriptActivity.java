package com.example.qihang.bpm_hw3.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.network.RemoteManager;
import com.example.qihang.bpm_hw3.network.model.Prescript;
import com.example.qihang.bpm_hw3.network.services.HospitalInterface;
import com.example.qihang.bpm_hw3.utils.JsonUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("LongLogTag")
public class PrescriptActivity extends AppCompatActivity {

    TextView mID, mTime, mPharmacyName, mDoctorName, mDetail, goto_payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescript);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mID = findViewById(R.id.prescript_id);
        mTime = findViewById(R.id.prescript_time);
        mPharmacyName = findViewById(R.id.pharmacy_name);
        mDoctorName = findViewById(R.id.doctor_name);
        mDetail = findViewById(R.id.prescript_detail);
        goto_payment = findViewById(R.id.goto_payment);

        HospitalInterface mHospitalInterface = RemoteManager.create(HospitalInterface.class);
        Call<ResponseBody> call = mHospitalInterface.prescriptItem(getIntent().getStringExtra("prescript_id"));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        Prescript result = JsonUtil.fromJson(json, Prescript.class);
                        mID.setText(result.getId());
                        mTime.setText(result.getTimeString());
                        mPharmacyName.setText(result.getPharmacy_id().getName());
                        mDoctorName.setText(result.getOutpatient_doctor_id().getName());
                        mDetail.setText(stringHandler(result.getDetail()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("PrescriptActivity onFailure", t.toString());
            }
        });

        goto_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to Payment Activity
                Intent intent = new Intent(PrescriptActivity.this, PaymentActivity.class);
                intent.putExtra("prescript_id", mID.getText().toString());
                startActivity(intent);
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String stringHandler(String detail) {
        return detail.replace(';', '\n');
    }

}
