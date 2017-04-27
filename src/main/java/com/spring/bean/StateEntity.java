package com.spring.bean;

/**
 * Created by lenovo on 2017/3/24.
 */
public class StateEntity {

    private boolean isSuccess;

    private String data;

    public StateEntity() {
    }

    public StateEntity(boolean isSuccess, String data) {
        this.isSuccess = isSuccess;
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
