package com.example.qihang.bpm_hw3.network.model;

import com.example.qihang.bpm_hw3.utils.TimeUtils;

/**
 * Created by qihang on 2018/11/21.
 */

public class ExaminationResult {
    public String id;
    public String detail;
    public String need_re_examination;
    public long timestamp;

    public Patient patient_id_1;
    public MedicalDoctor medical_doctor_id;

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

    public String getNeed_re_examination() {
        return need_re_examination;
    }

    public void setNeed_re_examination(String need_re_examination) {
        this.need_re_examination = need_re_examination;
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

    public MedicalDoctor getMedical_doctor_id() {
        return medical_doctor_id;
    }

    public void setMedical_doctor_id(MedicalDoctor medical_doctor_id) {
        this.medical_doctor_id = medical_doctor_id;
    }

    public String getTimeString() {
        return TimeUtils.timestamp2String(getTimestamp());
    }

}
