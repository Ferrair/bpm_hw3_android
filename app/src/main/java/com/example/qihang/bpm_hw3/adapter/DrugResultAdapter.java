package com.example.qihang.bpm_hw3.adapter;

import android.app.Activity;
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
import com.example.qihang.bpm_hw3.activity.DrugResultActivity;
import com.example.qihang.bpm_hw3.network.model.DrugResult;

import java.util.List;

/**
 * Created by qihang on 2018/11/29.
 */

public class DrugResultAdapter extends RecyclerView.Adapter<DrugResultAdapter.Holder> {

    private List<DrugResult> data;
    private Context mContext;


    public DrugResultAdapter(Context mContext, List<DrugResult> data) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_drug_result, parent, false);
        return new DrugResultAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final DrugResult item = data.get(position);

        holder.drug_result_id.setText(item.getId());
        holder.drug_result_detail.setText(item.getDetail());
        holder.prescript_time.setText(item.getPrescript_id().getTimeString());
        holder.pharmacy_name.setText(item.getPharmacy_id().getName());
        holder.root.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DrugResultActivity.class);
            intent.putExtra("drug_result_id", item.getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView drug_result_id, drug_result_detail, prescript_time, pharmacy_name;
        LinearLayout root;

        //实现的方法
        Holder(View itemView) {
            super(itemView);
            drug_result_id = itemView.findViewById(R.id.drug_result_id);
            drug_result_detail = itemView.findViewById(R.id.drug_result_detail);
            prescript_time = itemView.findViewById(R.id.prescript_time);
            pharmacy_name = itemView.findViewById(R.id.pharmacy_name);
            root = itemView.findViewById(R.id.root);
        }
    }
}
