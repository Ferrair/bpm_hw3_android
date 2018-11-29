package com.example.qihang.bpm_hw3.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.activity.ExaminationResultActivity;
import com.example.qihang.bpm_hw3.network.model.ExaminationResult;

import java.util.List;

/**
 * Created by qihang on 2018/11/29.
 */

public class ExaminationResultAdapter extends RecyclerView.Adapter<ExaminationResultAdapter.Holder> {

    private List<ExaminationResult> data;
    private Context mContext;

    public ExaminationResultAdapter(Context mContext, List<ExaminationResult> data) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_examination_result, parent, false);
        return new ExaminationResultAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final ExaminationResult item = data.get(position);

        holder.examination_result_id.setText(item.getId());
        if (item.getNeed_re_examination().equals("Yes")) {
            holder.need_re_examination.setText("需要再次检查");
        } else {
            holder.need_re_examination.setVisibility(View.GONE);
        }
        holder.examination_result_detail.setText(item.getDetail());
        holder.examination_time.setText(item.getExamination_id().getTimeString());
        holder.medical_doctor_name.setText(item.getMedical_doctor_id().getName());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ExaminationResultActivity.class);
                intent.putExtra("examination_result_id", item.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView examination_result_id, examination_result_detail, examination_time, medical_doctor_name, need_re_examination;
        CardView root;

        //实现的方法
        Holder(View itemView) {
            super(itemView);
            examination_result_id = itemView.findViewById(R.id.examination_result_id);
            examination_result_detail = itemView.findViewById(R.id.examination_result_detail);
            examination_time = itemView.findViewById(R.id.examination_time);
            medical_doctor_name = itemView.findViewById(R.id.medical_doctor_name);
            need_re_examination = itemView.findViewById(R.id.need_re_examination);
            root = itemView.findViewById(R.id.root);
        }
    }
}
