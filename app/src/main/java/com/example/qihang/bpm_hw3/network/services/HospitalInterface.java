package com.example.qihang.bpm_hw3.network.services;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by qihang on 2018/11/21.
 */

public interface HospitalInterface {

    /**
     * 挂号
     */
    @POST("Patient")
    Call<ResponseBody> patient(@Body Map<String, Object> params);

    /**
     * 挂号
     */
    @POST("Registration")
    Call<ResponseBody> register(@Body Map<String, Object> params);


    /**
     * 挂号信息
     */
    @GET("Registration")
    Call<ResponseBody> registration(@Query("Registration.patient_id.id") String patient_id);


    /**
     * 开检查单
     */
    @POST("Examination")
    Call<ResponseBody> examinationItem(@Body Map<String, Object> params);

    /**
     * 得到检查单的信息
     */
    @GET("Examination/{id}")
    Call<ResponseBody> examinationItem(@Path("id") String id);

    /**
     * 得到一个病人所有的检查单
     */
    @GET("Examination")
    Call<ResponseBody> examination(@Query("Examination.patient_id.id") String patient_id);

    /**
     * 得到处方的信息
     */
    @GET("Prescript/{id}")
    Call<ResponseBody> prescriptItem(@Path("id") String id);

    /**
     * 得到一个病人所有的处方
     */
    @GET("Prescript")
    Call<ResponseBody> prescript(@Query("Prescript.patient_id.id") String patient_id);

    /**
     * 得到缴费单的信息
     */
    @GET("Payment/{id}")
    Call<ResponseBody> paymentItem(@Path("id") String id);

    /**
     * 得到一个病人所有的缴费单
     */
    @GET("Payment")
    Call<ResponseBody> payment(@Query("Payment.patient_id.id") String patient_id);


    /**
     * 缴费
     */
    @PUT("Payment/{id}")
    Call<ResponseBody> doPay(@Path("id") String id, @Body Map<String, Object> params);

    /**
     * Examination 查找 Payment
     */
    @GET("Payment")
    Call<ResponseBody> searchPaymentByExamination(@Query("Payment.examination_id.id") String examination_id);

    /**
     * Prescript 查找 Payment
     */
    @GET("Payment")
    Call<ResponseBody> searchPaymentByPrescript(@Query("Payment.prescript_id.id") String prescript_id);

    /**
     * 得到检查结果的信息
     */
    @GET("Examinationresult/{id}")
    Call<ResponseBody> examinationResultItem(@Path("id") String id);


    /**
     * 得到一个病人所有检查结果
     */
    @GET("Examinationresult")
    Call<ResponseBody> examinationResult(@Query("Examinationresult.patient_id_1.id") String patient_id);

    /**
     * 得到拿药结果的信息
     */
    @GET("Drugresult/{id}")
    Call<ResponseBody> drugResultItem(@Path("id") String id);

    /**
     * 得到一个病人所有检查结果
     */
    @GET("Drugresult")
    Call<ResponseBody> drugResult(@Query("Drugresult.patient_id_1.id") String patient_id);


    /**
     * 得到报告的信息
     */
    @GET("Report/{id}")
    Call<ResponseBody> reportItem(@Path("id") String id);

    /**
     * 得到一个病人所有报告
     */
    @GET("Report")
    Call<ResponseBody> report(@Query("Report.patient_id.id") String patient_id);

    /**
     * 得到所有的Outpatientdoctor
     */
    @GET("Outpatientdoctor")
    Call<ResponseBody> allDoctor();
}
