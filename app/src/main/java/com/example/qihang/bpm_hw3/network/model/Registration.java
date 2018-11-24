package com.example.qihang.bpm_hw3.network.model;

import com.example.qihang.bpm_hw3.utils.TimeUtils;

/**
 * Created by qihang on 2018/11/21.
 */

public class Registration {
    public String id;
    public String detail;
    public long timestamp;

    public Patient patient_id;
    public OutpatientDoctor outpatient_doctor_id;

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

    public String getTimeString() {
     return TimeUtils.timestamp2String(getTimestamp());
    }
}
