package com.example.qihang.bpm_hw3.network.model;

import android.util.Log;

import com.example.qihang.bpm_hw3.utils.TimeUtils;

/**
 * Created by qihang on 2018/11/21.
 */

public class Payment {
    public String id;
    public String status;  // unpaid, paid
    public String type; // Examination, Prescript
    public long timestamp;
    public float number;

    public Patient patient_id;
    public OutpatientDoctor outpatient_doctor_id;
    public Examination examination_id;
    public Prescript prescript_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusFormatted() {

        if (status.equals("unpaid"))
            return "未付款";
        if (status.equals("paid"))
            return "已付款";
        return "";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public String getTypeFormatted() {
        if (type.equals("Examination"))
            return "检查项目缴费";
        if (type.equals("Prescript"))
            return "药品缴费";
        if (type.equals("Both"))
            return "检查项目缴费 + 药品缴费";
        return "";

    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
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

    public Examination getExamination_id() {
        return examination_id;
    }

    public void setExamination_id(Examination examination_id) {
        this.examination_id = examination_id;
    }

    public Prescript getPrescript_id() {
        return prescript_id;
    }

    public void setPrescript_id(Prescript prescript_id) {
        this.prescript_id = prescript_id;
    }

    public String getTimeString() {
        return TimeUtils.timestamp2String(getTimestamp());
    }

}
