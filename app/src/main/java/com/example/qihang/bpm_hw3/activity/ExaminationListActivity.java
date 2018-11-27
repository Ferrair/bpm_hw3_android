package com.example.qihang.bpm_hw3.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.adapter.ExaminationAdapter;
import com.example.qihang.bpm_hw3.network.RemoteManager;
import com.example.qihang.bpm_hw3.network.model.Examination;
import com.example.qihang.bpm_hw3.network.services.HospitalInterface;
import com.example.qihang.bpm_hw3.utils.DividerItemDecoration;
import com.example.qihang.bpm_hw3.utils.JsonUtil;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("LongLogTag")
public class ExaminationListActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        HospitalInterface mHospitalInterface = RemoteManager.create(HospitalInterface.class);
        Call<ResponseBody> call = mHospitalInterface.examination(getIntent().getStringExtra("patient_id"));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        ExaminationList result = JsonUtil.fromJson(json, ExaminationList.class);
                        ExaminationAdapter mAdapter = new ExaminationAdapter(getApplicationContext(), result.list);
                        mRecyclerView.setAdapter(mAdapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("ExaminationListActivity onFailure", t.toString());
            }
        });
    }

    protected class ExaminationList {
        @SerializedName(value = "Examination")
        List<Examination> list;
    }

}
