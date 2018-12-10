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
import com.example.qihang.bpm_hw3.network.model.DrugResult;
import com.example.qihang.bpm_hw3.network.services.HospitalInterface;
import com.example.qihang.bpm_hw3.utils.JsonUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("LongLogTag")
public class DrugResultActivity extends AppCompatActivity {
    TextView mID, mTime, mPharmacyName, mResultDetail, mPrescriptDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mID = (TextView) findViewById(R.id.drug_result_id);
        mTime = (TextView) findViewById(R.id.drug_time);
        mPharmacyName = (TextView) findViewById(R.id.pharmacy_name);
        mResultDetail = (TextView) findViewById(R.id.drug_result_detail);
        mPrescriptDetail = (TextView) findViewById(R.id.prescript_detail);

        findViewById(R.id.back).setOnClickListener(v -> finish());

        HospitalInterface mHospitalInterface = RemoteManager.create(HospitalInterface.class);
        Call<ResponseBody> call = mHospitalInterface.drugResultItem(getIntent().getStringExtra("drug_result_id"));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        DrugResult result = JsonUtil.fromJson(json, DrugResult.class);
                        mID.setText(result.getId());
                        mTime.setText(result.getTimeString());
                        mPharmacyName.setText(result.getPharmacy_id().getName());
                        mPrescriptDetail.setText(result.getPrescript_id().getDetail());
                        mResultDetail.setText(result.getDetail());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("DrugResultActivity onFailure", t.toString());
            }
        });

        findViewById(R.id.report).setOnClickListener(v -> {
            Intent intent = new Intent(DrugResultActivity.this, PDFActivity.class);
            intent.putExtra("filename", mID.getText().toString());
            intent.putExtra("fileurl", "http://47.107.241.57:8080/file/U1c365fdb24129c/hospital/Drugresult/" + mID.getText().toString());
            startActivity(intent);
        });

    }
}
