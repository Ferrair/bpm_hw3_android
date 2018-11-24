package com.example.qihang.bpm_hw3.network.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qihang on 2018/11/21.
 */

public class Patient implements MapBuilder {
    public String name;
    public String id;

    public Patient(String name) {
        this.name = name;
    }

    @Override
    public Map<String, Object> build() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", this.name);
        return map;
    }

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
}
