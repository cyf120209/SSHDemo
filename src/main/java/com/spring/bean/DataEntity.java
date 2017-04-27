package com.spring.bean;

/**
 * Created by lenovo on 2017/3/15.
 */
public class DataEntity {

    private String data;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public DataEntity(String data) {
        this.data = data;
    }

    public DataEntity() {
    }
}
