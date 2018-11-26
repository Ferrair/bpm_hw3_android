package com.example.qihang.bpm_hw3.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.adapter.DoctorAdapter;
import com.example.qihang.bpm_hw3.custom.ListDialog;
import com.example.qihang.bpm_hw3.network.RemoteManager;
import com.example.qihang.bpm_hw3.network.model.ForeignModel;
import com.example.qihang.bpm_hw3.network.model.OutpatientDoctor;
import com.example.qihang.bpm_hw3.network.model.Registration;
import com.example.qihang.bpm_hw3.network.services.HospitalInterface;
import com.example.qihang.bpm_hw3.utils.TimeUtils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRegister_detail = findViewById(R.id.register_detail);

        mRegister_time = findViewById(R.id.register_time);
        mRegister_time.setInputType(InputType.TYPE_NULL);
        mRegister_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        mRegister_doctor = findViewById(R.id.register_doctor);
        mRegister_doctor.setInputType(InputType.TYPE_NULL);
        mRegister_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListDialog popup = new ListDialog(RegisterActivity.this);
                popup.setmItemClickListener(new DoctorAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(OutpatientDoctor data, int position) {
                        popup.dismiss();
                        mRegister_doctor.setText(data.getName());
                        doctor_id = data.getId();
                        Log.i("RegisterActivity doctor", doctor_id);

                    }
                });
                popup.showPopupWindow();
            }
        });

        final TextView mFinish = findViewById(R.id.finish);
        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
                mFinish.setClickable(false);
            }
        });
    }

    private void register() {
        Log.i("RegisterActivity register", "register");

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
                        Log.i("RegisterActivity register", json);
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

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        mRegister_time.setText(TimeUtils.timestamp2String(millseconds));
    }
}
