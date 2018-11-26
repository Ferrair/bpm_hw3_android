package com.example.qihang.bpm_hw3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.network.model.Registration;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by qihang on 2018/11/24.
 */

public class RegistrationAdapter extends RecyclerView.Adapter<RegistrationAdapter.Holder> {

    private List<Registration> data;
    protected Context mContext;


    public RegistrationAdapter(Context mContext, List<Registration> data) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_registration, parent, false);
        return new RegistrationAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Registration item = data.get(position);

        holder.registration_time.setText(item.getTiRegistermeString());
        holder.registration_detail.setText(item.getDetail());
        holder.registration_id.setText(item.getId());
        if (position == 0) {
            holder.newest.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView registration_time, registration_detail, newest, registration_id;

        //实现的方法
        public Holder(View itemView) {
            super(itemView);
            registration_time = itemView.findViewById(R.id.registration_time);
            registration_detail = itemView.findViewById(R.id.registration_detail);
            registration_id = itemView.findViewById(R.id.registration_id);
            newest = itemView.findViewById(R.id.newest);
        }
    }
}
