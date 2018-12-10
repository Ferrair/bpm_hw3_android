package com.example.qihang.bpm_hw3.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by WQH on 2016/4/16  19:13.
 */
public class RemoteManager {

    private Retrofit retrofit;


    private static class ClassHolder {
        private static RemoteManager INSTANCE = new RemoteManager();
    }

    private RemoteManager() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client =
                new OkHttpClient
                        .Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .addInterceptor(chain -> {
                            Request request = chain.request();
                            Request.Builder requestBuilder = request.newBuilder();
                            request = requestBuilder
                                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                                    .build();
                            return chain.proceed(request);
                        })
                        .build();

        retrofit = new Retrofit
                .Builder()
                .baseUrl("http://47.107.241.57:8080/Entity/U1c365fdb24129c/hospital/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static <T> T create(final Class<T> service) {
        return ClassHolder.INSTANCE.retrofit.create(service);
    }
}
