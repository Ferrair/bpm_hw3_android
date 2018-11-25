package com.example.qihang.bpm_hw3.network.model;

import com.example.qihang.bpm_hw3.utils.TimeUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qihang on 2018/11/21.
 */

public class Registration implements MapBuilder {
    public String id;
    public String detail;
    public long timestamp;
    public long register_time;

    public Patient patient_id;
    public OutpatientDoctor outpatient_doctor_id;

    public ForeignModel patient_id_post;
    public ForeignModel outpatient_doctor_id_post;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Patient getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Patient patient_id) {
        this.patient_id = patient_id;
    }

    public OutpatientDoctor getOutpatient_doctor_id() {
        return outpatient_doctor_id;
    }

    public void setOutpatient_doctor_id(OutpatientDoctor outpatient_doctor_id) {
        this.outpatient_doctor_id = outpatient_doctor_id;
    }

    public long getRegister_time() {
        return register_time;
    }

    public void setRegister_time(long register_time) {
        this.register_time = register_time;
    }

    public String getTimeString() {
        return TimeUtils.timestamp2String(getTimestamp());
    }

    public String getTiRegistermeString() {
        return TimeUtils.timestamp2String(getRegister_time());
    }

    @Override
    public Map<String, Object> build() {
        Map<String, Object> map = new HashMap<>();
        map.put("detail", getDetail());
        map.put("timestamp", getTimestamp());
        map.put("timestamp", getRegister_time());
        map.put("patient_id", getPatient_id_post());
        map.put("outpatient_doctor_id", getOutpatient_doctor_id_post());
        return map;
    }

    public ForeignModel getPatient_id_post() {
        return patient_id_post;
    }

    public void setPatient_id_post(ForeignModel patient_id_post) {
        this.patient_id_post = patient_id_post;
    }

    public ForeignModel getOutpatient_doctor_id_post() {
        return outpatient_doctor_id_post;
    }

    public void setOutpatient_doctor_id_post(ForeignModel outpatient_doctor_id_post) {
        this.outpatient_doctor_id_post = outpatient_doctor_id_post;
    }
}
