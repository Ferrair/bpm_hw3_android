package com.example.qihang.bpm_hw3.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.utils.TimeUtils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

@SuppressLint("LongLogTag")
public class RegisterActivity extends AppCompatActivity implements OnDateSetListener {

    EditText mRegister_detail;
    EditText mRegister_time;
    EditText mAge;
    EditText mBMI;
    EditText mGender;
    EditText mRegister_items;

    float[] predictData = new float[15];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRegister_detail = (EditText) findViewById(R.id.register_detail);
        mAge = (EditText) findViewById(R.id.age);
        mBMI = (EditText) findViewById(R.id.bmi);
        mGender = (EditText) findViewById(R.id.gender);
        mRegister_items = (EditText) findViewById(R.id.register_items);
        mRegister_time = (EditText) findViewById(R.id.register_time);

        mRegister_items.setInputType(InputType.TYPE_NULL);
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

        String[] items = this.getResources().getStringArray(R.array.illness);
        Log.i("RegisterActivity AlertDialog", items.length + "");

        mRegister_items.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(this).setTitle("病情选择")
                    .setNegativeButton("取消", (dialog12, which) -> mRegister_items.setText(""))
                    .setPositiveButton("确定", (dialog1, which) -> {
                        StringBuffer stringBuffer = new StringBuffer();
                        for (int i = 3; i < predictData.length; i++) {
                            if (predictData[i] == 1) {
                                stringBuffer.append(items[i - 3]).append(";");
                            }
                        }
                        mRegister_items.setText(stringBuffer);
                    })
                    .setMultiChoiceItems(R.array.illness, null, (dialog13, which, isChecked) -> {
                        predictData[which + 3] = isChecked ? 1 : 0;

                    }).create();
            dialog.show();
        });

        final TextView mFinish = (TextView) findViewById(R.id.finish);
        mFinish.setOnClickListener(v -> {
            gotoSelectDoctor();
            mFinish.setClickable(false);
        });

        findViewById(R.id.back).setOnClickListener(v -> finish());
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        mRegister_time.setText(TimeUtils.timestamp2String(millseconds / 1000));
    }

    private void gotoSelectDoctor() {
        float age = (Float.parseFloat(mAge.getText().toString()) - 4) / (93 - 4);
        float bmi = (Float.parseFloat(mBMI.getText().toString()) - 17) / (29 - 17);
        float gender = mGender.getText().toString().equals("男") ? 1 : 0;

        predictData[0] = age;
        predictData[1] = bmi;
        predictData[2] = gender;

        Intent intent = new Intent(this, SelectDoctorActivity.class);
        intent.putExtra("predict_data", predictData);
        intent.putExtra("register_time", TimeUtils.string2Timestamp(mRegister_time.getText().toString()));
        intent.putExtra("detail", mRegister_detail.getText().toString());
        intent.putExtra("patient_id", getIntent().getStringExtra("patient_id"));
        startActivity(intent);
    }

}
