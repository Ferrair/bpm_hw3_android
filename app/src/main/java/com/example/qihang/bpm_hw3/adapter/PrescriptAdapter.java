package com.example.qihang.bpm_hw3.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.activity.PrescriptActivity;
import com.example.qihang.bpm_hw3.network.model.Prescript;

import java.util.List;

/**
 * Created by qihang on 2018/11/26.
 */

public class PrescriptAdapter extends RecyclerView.Adapter<PrescriptAdapter.Holder> {


    private List<Prescript> data;
    private Context mContext;


    public PrescriptAdapter(Context mContext, List<Prescript> data) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_prescript, parent, false);
        return new PrescriptAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final Prescript item = data.get(position);

        holder.prescript_id.setText(item.getId());
        holder.prescript_detail.setText(item.getDetail());
        holder.prescript_time.setText(item.getTimeString());
        holder.pharmacy_name.setText(item.getPharmacy_id().getName());
        // holder.doctor_name.setText(item.getOutpatient_doctor_id().getName());
        holder.root.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, PrescriptActivity.class);
            intent.putExtra("prescript_id", item.getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView prescript_id, prescript_detail, prescript_time, doctor_name, pharmacy_name;
        LinearLayout root;

        //实现的方法
        Holder(View itemView) {
            super(itemView);
            prescript_id = itemView.findViewById(R.id.prescript_id);
            prescript_detail = itemView.findViewById(R.id.prescript_detail);
            prescript_time = itemView.findViewById(R.id.prescript_time);
            pharmacy_name = itemView.findViewById(R.id.pharmacy_name);
            root = itemView.findViewById(R.id.root);
            // doctor_name = itemView.findViewById(R.id.doctor_name);
        }
    }
}
