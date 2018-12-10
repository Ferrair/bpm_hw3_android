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
import com.example.qihang.bpm_hw3.activity.PaymentActivity;
import com.example.qihang.bpm_hw3.network.model.Payment;

import java.util.List;

/**
 * Created by qihang on 2018/11/27.
 */

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.Holder> {

    private List<Payment> data;
    private Context mContext;


    public PaymentAdapter(Context mContext, List<Payment> data) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public PaymentAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_payment, parent, false);
        return new PaymentAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(PaymentAdapter.Holder holder, int position) {
        final Payment item = data.get(position);

        holder.payment_id.setText(item.getId());
        holder.payment_type.setText(item.getTypeFormatted());
        holder.payment_time.setText(item.getTimeString());
        holder.payment_status.setText(item.getStatusFormatted());
        holder.payment_number.setText(item.getNumber() + "");
        holder.root.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, PaymentActivity.class);
            intent.putExtra("payment_id", item.getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView payment_id, payment_type, payment_time, payment_number, payment_status;
        LinearLayout root;

        //实现的方法
        Holder(View itemView) {
            super(itemView);
            payment_id = itemView.findViewById(R.id.payment_id);
            payment_number = itemView.findViewById(R.id.payment_number);
            payment_status = itemView.findViewById(R.id.paid);
            payment_type = itemView.findViewById(R.id.payment_type);
            payment_time = itemView.findViewById(R.id.payment_time);
            root = itemView.findViewById(R.id.root);
        }
    }
}
