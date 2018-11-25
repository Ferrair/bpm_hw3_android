package com.example.qihang.bpm_hw3.custom;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.adapter.DoctorAdapter;
import com.example.qihang.bpm_hw3.network.RemoteManager;
import com.example.qihang.bpm_hw3.network.model.OutpatientDoctor;
import com.example.qihang.bpm_hw3.network.services.HospitalInterface;
import com.example.qihang.bpm_hw3.utils.DividerItemDecoration;
import com.example.qihang.bpm_hw3.utils.JsonUtil;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import razerdp.basepopup.BasePopupWindow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by qihang on 2018/11/25.
 */

public class ListDialog extends BasePopupWindow {

    private RecyclerView mRecyclerView;
    private DoctorAdapter.ItemClickListener mItemClickListener;

    public ListDialog(Context context) {
        super(context);
        mRecyclerView = findViewById(R.id.recyclerView);
        setAdapter(context);
    }

    public ListDialog(Context context, int w, int h) {
        super(context, w, h);
        mRecyclerView = findViewById(R.id.recyclerView);
        setAdapter(context);
    }

    protected ListDialog(Context context, int w, int h, boolean initImmediately) {
        super(context, w, h, initImmediately);
        mRecyclerView = findViewById(R.id.recyclerView);
        setAdapter(context);
    }

    private void setAdapter(final Context context) {
        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));


        HospitalInterface mHospitalInterface = RemoteManager.create(HospitalInterface.class);
        Call<ResponseBody> call = mHospitalInterface.allDoctor();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        DoctorList result = JsonUtil.fromJson(json, DoctorList.class);
                        DoctorAdapter mAdapter = new DoctorAdapter(context, result.list);
                        if (mItemClickListener != null) {
                            mAdapter.setItemClickListener(mItemClickListener);
                        }
                        mRecyclerView.setAdapter(mAdapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("ListDialog onFailure", t.toString());
            }
        });
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.content_doctor_list);
    }

    public void setmItemClickListener(DoctorAdapter.ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public Animator onCreateShowAnimator() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(mDisplayAnimateView, "rotationX", 90f, 0f).setDuration(400),
                ObjectAnimator.ofFloat(mDisplayAnimateView, "translationY", 250f, 0f).setDuration(400));
        return set;
    }

    protected class DoctorList {
        @SerializedName(value = "Outpatientdoctor")
        List<OutpatientDoctor> list;
    }

}
