package com.example.qihang.bpm_hw3.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.adapter.RegistrationAdapter;
import com.example.qihang.bpm_hw3.network.RemoteManager;
import com.example.qihang.bpm_hw3.network.model.Registration;
import com.example.qihang.bpm_hw3.network.services.HospitalInterface;
import com.example.qihang.bpm_hw3.utils.DividerItemDecoration;
import com.example.qihang.bpm_hw3.utils.JsonUtil;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("LongLogTag")
public class RegistrationListActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_list);
        // initStatus(R.color.colorPrimary);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to register
                Intent intent = new Intent(RegistrationListActivity.this, RegisterActivity.class);
                intent.putExtra("patient_id", getIntent().getStringExtra("patient_id"));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        HospitalInterface mHospitalInterface = RemoteManager.create(HospitalInterface.class);
        Call<ResponseBody> call = mHospitalInterface.registration(getIntent().getStringExtra("patient_id"));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        RegistrationList result = JsonUtil.fromJson(json, RegistrationList.class);
                        Collections.sort(result.list);
                        RegistrationAdapter mAdapter = new RegistrationAdapter(getApplicationContext(), result.list);
                        mRecyclerView.setAdapter(mAdapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("RegistrationListActivity onFailure", t.toString());
            }
        });
    }

    protected class RegistrationList {
        @SerializedName(value = "Registration")
        List<Registration> list;
    }

    public void initStatus(int color) {
        Window window = this.getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(color));
        }
    }


}
