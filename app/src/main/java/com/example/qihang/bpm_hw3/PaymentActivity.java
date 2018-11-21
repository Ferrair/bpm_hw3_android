package com.example.qihang.bpm_hw3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.qihang.bpm_hw3.utils.OrderInfoUtil2_0;
import com.example.qihang.bpm_hw3.utils.PayResult;

import java.util.Map;

public class PaymentActivity extends AppCompatActivity {
    /**
     * 支付宝账户：dyyyti6490@sandbox.com
     * 登录密码：111111
     * 支付密码：111111
     */
    public static final String APPID = "2016092000552193";
    public static final String RSA2_PRIVATE = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC4eAlBYfysMmfnYOkuB6aTk1D/ApOsh99DbZ4t5/TNavekRkQOTLDNFnn9gLAc2YC2nhyrNbWdOPcwMbDL0EFZyeLs+qNAh/1AUpIN5qGQtoozmpnq/WOKXALk4SlxPUXlywe6RmOjIVxMtFqLYIM4WrsnReZT7vvrUNU0OzTKL6gkcwSf8tOdiT9lR7Mr1b8tG/n+aenu0QIDikY7Nwwur6h2TsrHJ8n5/XC+0hHC9hLBbxGXOAVWDNjAspqEsuSbED46j213B2jXpMs0Qrzn+yiI9mcS3E1kDcMK3TsNa2IrmwlGpBp2Yod2b09ouEGtY02P0uV3mkvZVfjHbmMZAgMBAAECggEAcqCEf9B3xjj8P9cEjsnKROHB7vSW9xrnLWssY7JDeHIDuFzBZXFaW9afr1sMFKFDTjcp8WnKlQwU7imyhrO+KCE/laqZTzwxroqgn2q08su7TR3AjzktZYiPc9JGgktk58ol3yZmO7Q7Op7HtNwgLiCTM3BIE9fpSuxLz80KnKe4oQt7Mf/THJWqMyYATPseqtcCPQciJQJ/ArMpAk6ON4QXTjZCdHTMWonqsBB+dcSBXa8mevAyKNZ7itA1XMGDkPKsjHjQvXo80LP5VO92wMWhZ7HukjxgxmAxPHtwj3vhSPQZvQ45geTAL8Nw24Ih1UjXSEevURuC0FsPCDZn0QKBgQDz3yNh09G0gCj2T0g2PqVN5Q12JGJ0ADKsc3HuWBNBiBS3I2m4pLdaVsdYBuZvNuUoEfZFb598u4nuCMH8TmtXyn3zIhxgjDt7vohvre3ScwOlKgmaJMiQsBAfbuLNXrTHHa4/BIoyZ7x0iEKjWi6ik5HAA9wO0H+696lDbqhzkwKBgQDBpJwGSU6GtYXcDXj901gMQx4l/FFxLcdFHFB+X/YzXb77kYVXEJDeLbhdTRwy8KB/2zIQXA5uWIb2eml2k+Az3KB9pF9A+lFBSjBI3IxpnPHd9Z9XDLw9OfTkIHZcaipknElbSV5I0GA1WbizNFyg/Yn2PjKV0wOb8jVMmQjSIwKBgGGw2E6e+Jly4XXRyp6YN8XdZKgX6SyIy8PTNyeANq6AobO/RuGFckGJE6/Ki9KvTdrgXyPvlBb2VUl6TqMQkuWHIy8bUfzHECkA6Uy7gtJT6njmPxR1ebakIMFGvSoBry84T43E9ss8TPztfKxNOvM7ZB5kLWnt0c0G2tI9aNNrAoGAbmZe3ealcn/DVXYI6Q25QSRy58020YP7/BbsfWBQiXCFFgAdOUokjccbOXZhAVnW544cuN8DCz5Fk/iucABW7rp2datk8zLDaKnXZXL4Sq5D5vvGYGbOW0nTqMwpLv8INFw2Jmlp67GBaGlCuEi+Frr2EQHc7TZk9F/eq1EEimkCgYBVhN+hszPtwIpTMTb6RhHabBQidByedSFSkZpla3ip77WKTNsOfOur870FVSr5mVyO4Pa8RyH5ZAllreA8zAuJ5GYkOX8I3Lc9d4s2IuOrDksWGndNMtg9qTU8GEhLoRCsdiuMwjVgk9lzxPOp7jW4kQflgtl5kuXDD+QQKQp59Q==";
    Button mPayButton;
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
                Log.i("PaymentActivity", "支付成功 " + resultInfo);
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Log.i("PaymentActivity", "支付失败 " + resultInfo);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        requestPermission();
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);

        mPayButton = findViewById(R.id.pay);
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, "医院缴费", 100);
                String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
                String sign = OrderInfoUtil2_0.getSign(params, RSA2_PRIVATE, true);
                final String orderInfo = orderParam + "&" + sign;

                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(PaymentActivity.this);
                        Map<String, String> result = alipay.payV2(orderInfo, true);
                        Log.i("msp", result.toString());

                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }
        });
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
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, PERMISSIONS_REQUEST_CODE);

        } else {
            showToast(this, "支付宝 SDK 已有所需的权限");
        }
    }

    private static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
}
