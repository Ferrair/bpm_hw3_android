package com.example.qihang.bpm_hw3.activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.adapter.DoctorAdapter;
import com.example.qihang.bpm_hw3.ml.Recommend;
import com.example.qihang.bpm_hw3.network.RemoteManager;
import com.example.qihang.bpm_hw3.network.model.ForeignModel;
import com.example.qihang.bpm_hw3.network.model.OutpatientDoctor;
import com.example.qihang.bpm_hw3.network.model.Registration;
import com.example.qihang.bpm_hw3.network.services.HospitalInterface;
import com.example.qihang.bpm_hw3.utils.DividerItemDecoration;
import com.example.qihang.bpm_hw3.utils.JsonUtil;
import com.tylerjroach.eventsource.EventSource;
import com.tylerjroach.eventsource.EventSourceHandler;
import com.tylerjroach.eventsource.MessageEvent;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("LongLogTag")
public class SelectDoctorActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    float[] predictData;
    long register_time;
    String detail;
    String patient_id;
    String doctor_id = "1542701863438";
    EventSource eventSource;
    Recommend classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Intent intent = getIntent();
        predictData = intent.getFloatArrayExtra("predict_data");
        register_time = intent.getLongExtra("register_time", 0);
        detail = intent.getStringExtra("detail");
        patient_id = intent.getStringExtra("patient_id");
        final TextView mFinish = (TextView) findViewById(R.id.finish);
        mFinish.setOnClickListener(v -> {
            register();
            mFinish.setClickable(false);
        });

        findViewById(R.id.back).setOnClickListener(v -> finish());

        try {
            classifier = new Recommend(this);
            List<OutpatientDoctor> result = classifier.predict(predictData);
            setAdapter(this, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register() {
        Registration registration = new Registration();
        registration.setDetail(detail);
        registration.setRegister_time(register_time);
        registration.setTimestamp(System.currentTimeMillis());
        registration.setPatient_id_post(new ForeignModel(patient_id, "Patient"));
        registration.setOutpatient_doctor_id_post(new ForeignModel(doctor_id, "outpatientdoctor"));

        HospitalInterface mHospitalInterface = RemoteManager.create(HospitalInterface.class);
        Call<ResponseBody> call = mHospitalInterface.register(registration.build());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        Registration result = JsonUtil.fromJson(json, Registration.class);
                        subscribe(result.id);

                        Log.i("SelectDoctorActivity register", result.id);
                        Toast.makeText(getApplicationContext(), "挂号成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("SelectDoctorActivity onFailure", t.toString());
            }
        });
    }

    /**
     * 订阅资源，当资源更新，发送通知
     *
     * @param id registration id
     */
    private void subscribe(String id) {
        RegistrationHandler handler = new RegistrationHandler();
        eventSource = new EventSource.Builder("http://47.107.241.57:8080/Entity/U1c365fdb24129c/hospital/Registration/" + id + "/syncronize")
                .eventHandler(handler)
                .build();
        eventSource.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setAdapter(final Context context, List<OutpatientDoctor> result) {
        DoctorAdapter mAdapter = new DoctorAdapter(context, result);
        mAdapter.setItemClickListener((data, position) -> {
            this.doctor_id = data.getId();
            // TODO update selected item UI
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }

    public class RegistrationHandler implements EventSourceHandler {
        @Override
        public void onConnect() throws Exception {
            Log.i("SSE Connected", "True");
        }

        @Override
        public void onMessage(String event, MessageEvent message) throws Exception {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Notification.Builder builder = new Notification.Builder(SelectDoctorActivity.this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            builder.setAutoCancel(true);
            builder.setWhen(System.currentTimeMillis());
            builder.setContentTitle("移动医疗信息平台");
            builder.setContentText("缴费单已开，请去缴费");
            mNotificationManager.notify(1, builder.build());
        }

        @Override
        public void onComment(String comment) throws Exception {
            Log.i("SSE onComment", comment);
        }

        @Override
        public void onError(Throwable t) {
            Log.e("SSE onError", "error");
        }

        @Override
        public void onClosed(boolean willReconnect) {
            Log.i("SSE Closed", "reconnect? " + willReconnect);
        }
    }

}
