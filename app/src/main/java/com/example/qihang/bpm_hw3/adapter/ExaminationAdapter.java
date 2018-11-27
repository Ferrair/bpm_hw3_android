package com.example.qihang.bpm_hw3.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.activity.ExaminationActivity;
import com.example.qihang.bpm_hw3.network.model.Examination;

import java.util.List;

/**
 * Created by qihang on 2018/11/27.
 */

public class ExaminationAdapter extends RecyclerView.Adapter<ExaminationAdapter.Holder> {

    private List<Examination> data;
    private Context mContext;


    public ExaminationAdapter(Context mContext, List<Examination> data) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_examination, parent, false);
        return new ExaminationAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final Examination item = data.get(position);

        holder.examination_id.setText(item.getId());
        holder.examination_detail.setText(item.getDetail());
        holder.examination_time.setText(item.getTimeString());
        holder.medical_doctor_name.setText(item.getMedical_doctor_id().getName());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ExaminationActivity.class);
                intent.putExtra("examination_id", item.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView examination_id, examination_detail, examination_time, medical_doctor_name;
        CardView root;

        //实现的方法
        Holder(View itemView) {
            super(itemView);
            examination_id = itemView.findViewById(R.id.examination_id);
            examination_detail = itemView.findViewById(R.id.examination_detail);
            examination_time = itemView.findViewById(R.id.examination_time);
            medical_doctor_name = itemView.findViewById(R.id.medical_doctor_name);
            root = itemView.findViewById(R.id.root);
        }
    }
}
