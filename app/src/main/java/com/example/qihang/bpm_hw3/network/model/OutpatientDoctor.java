package com.example.qihang.bpm_hw3.network.model;

/**
 * Created by qihang on 2018/11/21.
 */

public class OutpatientDoctor {
    public String name;
    public String major;
    public String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
