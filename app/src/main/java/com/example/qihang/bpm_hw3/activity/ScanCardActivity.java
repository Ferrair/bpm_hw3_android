package com.example.qihang.bpm_hw3.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.network.RemoteManager;
import com.example.qihang.bpm_hw3.network.model.Patient;
import com.example.qihang.bpm_hw3.network.services.HospitalInterface;
import com.example.qihang.bpm_hw3.utils.FileUtil;
import com.example.qihang.bpm_hw3.utils.JsonUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("LongLogTag")
public class ScanCardActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CAMERA = 102;
    private HospitalInterface mHospitalInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_card);
        OCR.getInstance(getApplicationContext()).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                Log.i("initAccessTokenWithAkSk", token);
                initCamera();
            }

            @Override
            public void onError(OCRError error) {
                Log.i("initAccessTokenWithAkSk", error.getMessage());
            }
        }, getApplicationContext(), "mrInNmdlyqgYHy6Xq7SjaWHd", "DumM9Z5H0dkqURk7M0jpFxibGZHpa6uI");

        // Retrofit
        mHospitalInterface = RemoteManager.create(HospitalInterface.class);

        // 身份证反面拍照
        findViewById(R.id.id_card_back_button).setOnClickListener(v -> {
            Intent intent = new Intent(ScanCardActivity.this, CameraActivity.class);
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    FileUtil.getSaveFile(getApplication()).getAbsolutePath());
            intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
            intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true);
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);

        });
    }

    /**
     * Register a Patient on Server
     *
     * @param name patient' name
     */
    private void registerPatient(String name) {
        Patient patient = new Patient(name);
        Call<ResponseBody> call = mHospitalInterface.patient(patient.build());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        Patient patient = JsonUtil.fromJson(json, Patient.class);
                        SharedPreferences.Editor editor = getSharedPreferences("patient", MODE_PRIVATE).edit();
                        editor.putString("name", patient.getName());
                        editor.putString("patientId", patient.getId());
                        editor.apply();
                        Log.i("ScanCardActivity onResponse", json);
                        Intent intent = new Intent(ScanCardActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Log.e("ScanCardActivity onResponse", response.errorBody().string());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("ScanCardActivity onResponse", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ScanCardActivity onFailure", t.toString());
            }
        });
    }

    private void initCamera() {
        CameraNativeHelper.init(this, OCR.getInstance(this).getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        Log.e("ScanCardActivity initCamera", e.getMessage());
                    }
                });
    }

    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        param.setIdCardSide(idCardSide);
        param.setDetectDirection(true);
        param.setImageQuality(20);

        OCR.getInstance(this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    Log.i("ScanCardActivity recIDCard", result.toString());
                    registerPatient(result.getName().toString());
                }
            }

            @Override
            public void onError(OCRError error) {
                Log.e("ScanCardActivity recIDCard", error.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        CameraNativeHelper.release();
        super.onDestroy();
    }

}
