package com.example.qihang.bpm_hw3.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.network.RemoteManager;
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


        findViewById(R.id.qr_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(mID.getText().toString(), 600);
                LayoutInflater inflater = LayoutInflater.from(PrescriptActivity.this);
                View rootLayout = inflater.inflate(R.layout.dialog_qr_code, null);
                // 加载自定义的布局文件
                final AlertDialog dialog = new AlertDialog.Builder(PrescriptActivity.this).create();
                ImageView img = rootLayout.findViewById(R.id.qr_code_image);
                img.setImageBitmap(mBitmap);
                TextView textView = rootLayout.findViewById(R.id.qr_code_desc);
                textView.setText(mID.getText().toString());

                dialog.setView(rootLayout);

                dialog.show();
                rootLayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramView) {
                        dialog.cancel();
                    }
                });

            }
        });
    }

    private String stringHandler(String detail) {
        return detail.replace(';', '\n');
    }

}
