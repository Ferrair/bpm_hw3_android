package com.example.qihang.bpm_hw3.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.qihang.bpm_hw3.R;
import com.example.qihang.bpm_hw3.network.RemoteManager;
import com.example.qihang.bpm_hw3.network.model.Payment;
import com.example.qihang.bpm_hw3.network.services.HospitalInterface;
import com.example.qihang.bpm_hw3.utils.JsonUtil;
import com.example.qihang.bpm_hw3.utils.OrderInfoUtil2_0;
import com.example.qihang.bpm_hw3.utils.PayResult;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("LongLogTag")
public class PaymentActivity extends AppCompatActivity {
    /**
     * 支付宝账户：dyyyti6490@sandbox.com
     * 登录密码：111111
     * 支付密码：111111
     */
    public static final String APPID = "2016092000552193";
    public static final String RSA2_PRIVATE = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC4eAlBYfysMmfnYOkuB6aTk1D/ApOsh99DbZ4t5/TNavekRkQOTLDNFnn9gLAc2YC2nhyrNbWdOPcwMbDL0EFZyeLs+qNAh/1AUpIN5qGQtoozmpnq/WOKXALk4SlxPUXlywe6RmOjIVxMtFqLYIM4WrsnReZT7vvrUNU0OzTKL6gkcwSf8tOdiT9lR7Mr1b8tG/n+aenu0QIDikY7Nwwur6h2TsrHJ8n5/XC+0hHC9hLBbxGXOAVWDNjAspqEsuSbED46j213B2jXpMs0Qrzn+yiI9mcS3E1kDcMK3TsNa2IrmwlGpBp2Yod2b09ouEGtY02P0uV3mkvZVfjHbmMZAgMBAAECggEAcqCEf9B3xjj8P9cEjsnKROHB7vSW9xrnLWssY7JDeHIDuFzBZXFaW9afr1sMFKFDTjcp8WnKlQwU7imyhrO+KCE/laqZTzwxroqgn2q08su7TR3AjzktZYiPc9JGgktk58ol3yZmO7Q7Op7HtNwgLiCTM3BIE9fpSuxLz80KnKe4oQt7Mf/THJWqMyYATPseqtcCPQciJQJ/ArMpAk6ON4QXTjZCdHTMWonqsBB+dcSBXa8mevAyKNZ7itA1XMGDkPKsjHjQvXo80LP5VO92wMWhZ7HukjxgxmAxPHtwj3vhSPQZvQ45geTAL8Nw24Ih1UjXSEevURuC0FsPCDZn0QKBgQDz3yNh09G0gCj2T0g2PqVN5Q12JGJ0ADKsc3HuWBNBiBS3I2m4pLdaVsdYBuZvNuUoEfZFb598u4nuCMH8TmtXyn3zIhxgjDt7vohvre3ScwOlKgmaJMiQsBAfbuLNXrTHHa4/BIoyZ7x0iEKjWi6ik5HAA9wO0H+696lDbqhzkwKBgQDBpJwGSU6GtYXcDXj901gMQx4l/FFxLcdFHFB+X/YzXb77kYVXEJDeLbhdTRwy8KB/2zIQXA5uWIb2eml2k+Az3KB9pF9A+lFBSjBI3IxpnPHd9Z9XDLw9OfTkIHZcaipknElbSV5I0GA1WbizNFyg/Yn2PjKV0wOb8jVMmQjSIwKBgGGw2E6e+Jly4XXRyp6YN8XdZKgX6SyIy8PTNyeANq6AobO/RuGFckGJE6/Ki9KvTdrgXyPvlBb2VUl6TqMQkuWHIy8bUfzHECkA6Uy7gtJT6njmPxR1ebakIMFGvSoBry84T43E9ss8TPztfKxNOvM7ZB5kLWnt0c0G2tI9aNNrAoGAbmZe3ealcn/DVXYI6Q25QSRy58020YP7/BbsfWBQiXCFFgAdOUokjccbOXZhAVnW544cuN8DCz5Fk/iucABW7rp2datk8zLDaKnXZXL4Sq5D5vvGYGbOW0nTqMwpLv8INFw2Jmlp67GBaGlCuEi+Frr2EQHc7TZk9F/eq1EEimkCgYBVhN+hszPtwIpTMTb6RhHabBQidByedSFSkZpla3ip77WKTNsOfOur870FVSr5mVyO4Pa8RyH5ZAllreA8zAuJ5GYkOX8I3Lc9d4s2IuOrDksWGndNMtg9qTU8GEhLoRCsdiuMwjVgk9lzxPOp7jW4kQflgtl5kuXDD+QQKQp59Q==";

    Button mPayButton;
    TextView mId, mTime, mNumber, mDetail, mType;
    Payment mPayment;
    HospitalInterface mHospitalInterface;
    boolean isList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        requestPermission();
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);

        mPayButton = (Button) findViewById(R.id.pay);
        mId = (TextView) findViewById(R.id.payment_id);
        mNumber = (TextView) findViewById(R.id.payment_number);
        mTime = (TextView) findViewById(R.id.payment_time);
        mDetail = (TextView) findViewById(R.id.payment_detail);
        mType = (TextView) findViewById(R.id.payment_type);

        mPayButton.setOnClickListener(v -> pay());

        findViewById(R.id.back).setOnClickListener(v -> finish());

        mHospitalInterface = RemoteManager.create(HospitalInterface.class);

        /*
         * 从不同入口进来
         */
        Call<ResponseBody> call;


        String payment_id = getIntent().getStringExtra("payment_id");
        if (payment_id != null) {
            Log.i(" PaymentActivity payment_id ", payment_id);
            call = mHospitalInterface.paymentItem(payment_id);
            getPayment(call);
        }

        String examination_id = getIntent().getStringExtra("examination_id");
        if (examination_id != null) {
            Log.i(" PaymentActivity examination_id ", examination_id);
            isList = true;
            call = mHospitalInterface.searchPaymentByExamination(examination_id);
            getPayment(call);
        }

        String prescript_id = getIntent().getStringExtra("prescript_id");
        if (prescript_id != null) {
            Log.i(" PaymentActivity prescript_id ", prescript_id);
            isList = true;
            call = mHospitalInterface.searchPaymentByPrescript(prescript_id);
            getPayment(call);
        }
    }

    private void getPayment(Call<ResponseBody> call) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        if (!isList) {
                            mPayment = JsonUtil.fromJson(json, Payment.class);
                        } else {
                            PaymentList result = JsonUtil.fromJson(json, PaymentList.class);
                            mPayment = result.list.get(0);
                        }
                        mId.setText(mPayment.getId());
                        mTime.setText(mPayment.getTimeString());
                        mNumber.setText(mPayment.getNumber() + "");
                        mType.setText(mPayment.getTypeFormatted());
                        mDetail.setText(detail());

                        if (mPayment.getStatus().equals("unpaid")) {
                            mPayButton.setVisibility(View.VISIBLE);
                        } else {
                            mPayButton.setVisibility(View.GONE);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("PaymentActivity onFailure", t.toString());
            }
        });
    }

    private String detail() {
        StringBuilder result = new StringBuilder("");
        if (mPayment.getExamination_id() != null) {
            result.append(mPayment.getExamination_id().getDetail().replace(';', '\n'));
            result.append("\n\n");
        }
        if (mPayment.getPrescript_id() != null) {
            result.append(mPayment.getPrescript_id().getDetail().replace(';', '\n'));
        }
        return result.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO 加载数据
    }

    private void pay() {
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, "医院缴费", mPayment.getNumber());
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String sign = OrderInfoUtil2_0.getSign(params, RSA2_PRIVATE, true);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(PaymentActivity.this);
            Map<String, String> result = alipay.payV2(orderInfo, true);
            Log.i("PaymentActivity Result", result.toString());

            Message msg = new Message();
            msg.what = 1;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 获取权限使用的 RequestCode
     */
    private static final int PERMISSIONS_REQUEST_CODE = 1002;

    /**
     * 检查支付宝 SDK 所需的权限，并在必要的时候动态获取。
     */
    private void requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);

        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /*
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                Log.i("PaymentActivity Handler", "支付成功 " + resultInfo);
                paymentSuccess();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Log.i("PaymentActivity Handler", "支付失败 " + resultInfo);
            }
        }
    };

    /**
     * TODO ERROR
     * 修改Payment的状态
     */
    private void paymentSuccess() {
        Call<ResponseBody> call = mHospitalInterface.doPay(mPayment.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        mPayment = JsonUtil.fromJson(json, Payment.class);
                        mId.setText(mPayment.getId());
                        mTime.setText(mPayment.getTimeString());
                        mNumber.setText(mPayment.getNumber() + "");
                        mType.setText(mPayment.getTypeFormatted());
                        mDetail.setText(detail());

                        if (mPayment.getStatus().equals("unpaid")) {
                            mPayButton.setVisibility(View.VISIBLE);
                        } else {
                            mPayButton.setVisibility(View.GONE);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("PaymentActivity onFailure", t.toString());
            }
        });
    }

    protected class PaymentList {
        @SerializedName(value = "Payment")
        List<Payment> list;
    }

}
