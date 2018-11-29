package com.example.qihang.bpm_hw3.network.model;

import com.example.qihang.bpm_hw3.utils.TimeUtils;

/**
 * Created by qihang on 2018/11/21.
 */

public class DrugResult {
    public String id;
    public String detail;
    public long timestamp;

    public Patient patient_id_1;
    public Pharmacy pharmacy_id;
    public Prescript prescript_id;

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

    public Patient getPatient_id_1() {
        return patient_id_1;
    }

    public void setPatient_id_1(Patient patient_id_1) {
        this.patient_id_1 = patient_id_1;
    }

    public Pharmacy getPharmacy_id() {
        return pharmacy_id;
    }

    public void setPharmacy_id(Pharmacy pharmacy_id) {
        this.pharmacy_id = pharmacy_id;
    }

    public String getTimeString() {
        return TimeUtils.timestamp2String(getTimestamp());
    }

    public Prescript getPrescript_id() {
        return prescript_id;
    }

    public void setPrescript_id(Prescript prescript_id) {
        this.prescript_id = prescript_id;
    }
}
