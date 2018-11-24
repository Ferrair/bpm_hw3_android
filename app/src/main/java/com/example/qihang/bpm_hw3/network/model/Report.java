package com.example.qihang.bpm_hw3.network.model;

import com.example.qihang.bpm_hw3.utils.TimeUtils;

/**
 * Created by qihang on 2018/11/21.
 */

public class Report {
    public String id;
    public String detail;
    public long timestamp;

    public Patient patient_id;
    public Report reporter_id;

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

    public Report getReporter_id() {
        return reporter_id;
    }

    public void setReporter_id(Report reporter_id) {
        this.reporter_id = reporter_id;
    }

    public String getTimeString() {
        return TimeUtils.timestamp2String(getTimestamp());
    }

}
