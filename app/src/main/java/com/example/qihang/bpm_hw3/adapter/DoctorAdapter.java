package com.example.qihang.bpm_hw3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.network.model.OutpatientDoctor;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by qihang on 2018/11/25.
 */

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.Holder> {
    private List<OutpatientDoctor> data;
    private Context mContext;
    private ItemClickListener itemClickListener;

    public DoctorAdapter(Context context, List<OutpatientDoctor> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_doctor, parent, false);
        return new DoctorAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final OutpatientDoctor item = data.get(position);

        holder.doctor_name.setText(item.getName());
        holder.doctor_major.setText(item.getMajor());
        ImageLoader.getInstance().displayImage("drawable://" + R.drawable.head_head, holder.doctor_image);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(item, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public ImageView doctor_image;
        public TextView doctor_name, doctor_major;
        public LinearLayout root;

        public Holder(View itemView) {
            super(itemView);
            doctor_name = itemView.findViewById(R.id.doctor_name);
            doctor_major = itemView.findViewById(R.id.doctor_major);
            doctor_image = itemView.findViewById(R.id.doctor_image);
            root = itemView.findViewById(R.id.root);
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(OutpatientDoctor data, int position);
    }
}