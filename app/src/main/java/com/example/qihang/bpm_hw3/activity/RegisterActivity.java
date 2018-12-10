package com.example.qihang.bpm_hw3.activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.custom.ListDialog;
import com.example.qihang.bpm_hw3.network.RemoteManager;
import com.example.qihang.bpm_hw3.network.model.ForeignModel;
import com.example.qihang.bpm_hw3.network.model.Registration;
import com.example.qihang.bpm_hw3.network.services.HospitalInterface;
import com.example.qihang.bpm_hw3.utils.JsonUtil;
import com.example.qihang.bpm_hw3.utils.TimeUtils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.tylerjroach.eventsource.EventSource;
import com.tylerjroach.eventsource.EventSourceHandler;
import com.tylerjroach.eventsource.MessageEvent;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("LongLogTag")
public class RegisterActivity extends AppCompatActivity implements OnDateSetListener {

    EditText mRegister_detail;
    EditText mRegister_time;
    EditText mRegister_doctor;
    String doctor_id = "1542701863438";
    EventSource eventSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO 加入机器学习
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRegister_detail = (EditText) findViewById(R.id.register_detail);

        mRegister_time = (EditText) findViewById(R.id.register_time);
        mRegister_time.setInputType(InputType.TYPE_NULL);
        mRegister_time.setOnClickListener(v -> {
            TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                    .setCallBack(RegisterActivity.this)
                    .setCancelStringId("取消")
                    .setSureStringId("确定")
                    .setTitleStringId("就诊时间")
                    .setMonthText("月")
                    .setDayText("日")
                    .setCyclic(false)
                    .setType(Type.MONTH_DAY_HOUR_MIN)
                    .setMinMillseconds(System.currentTimeMillis())
                    .setCurrentMillseconds(System.currentTimeMillis())
                    .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                    .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                    .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                    .setWheelItemTextSize(12)
                    .build();
            mDialogAll.show(getSupportFragmentManager(), "month_day_hour_minute");
        });

        mRegister_doctor = (EditText) findViewById(R.id.register_doctor);
        mRegister_doctor.setInputType(InputType.TYPE_NULL);
        mRegister_doctor.setOnClickListener(v -> {
            final ListDialog popup = new ListDialog(RegisterActivity.this);
            popup.setmItemClickListener((data, position) -> {
                popup.dismiss();
                mRegister_doctor.setText(data.getName());
                doctor_id = data.getId();
                Log.i("RegisterActivity doctor", doctor_id);

            });
            popup.showPopupWindow();
        });

        final TextView mFinish = (TextView) findViewById(R.id.finish);
        mFinish.setOnClickListener(v -> {
            register();
            mFinish.setClickable(false);
        });

        findViewById(R.id.back).setOnClickListener(v -> finish());
    }

    private void register() {
        Registration registration = new Registration();
        registration.setDetail(mRegister_detail.getText().toString());
        registration.setRegister_time(TimeUtils.string2Timestamp(mRegister_time.getText().toString()));
        registration.setTimestamp(System.currentTimeMillis());
        registration.setPatient_id_post(new ForeignModel(getIntent().getStringExtra("patient_id"), "Patient"));
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

                        Log.i("RegisterActivity register", result.id);
                        Toast.makeText(getApplicationContext(), "挂号成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("RegisterActivity onFailure", t.toString());
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
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        mRegister_time.setText(TimeUtils.timestamp2String(millseconds / 1000));
    }

    public class RegistrationHandler implements EventSourceHandler {
        @Override
        public void onConnect() throws Exception {
            Log.i("SSE Connected", "True");
        }

        @Override
        public void onMessage(String event, MessageEvent message) throws Exception {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Notification.Builder builder = new Notification.Builder(RegisterActivity.this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
