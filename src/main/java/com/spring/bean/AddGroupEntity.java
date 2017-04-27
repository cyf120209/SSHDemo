package com.spring.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/3/23.
 */
public class AddGroupEntity {


    private List<Integer> draperList=new ArrayList<>();

    public List<Integer> getDraperList() {
        return draperList;
    }

    public void setDraperList(List<Integer> draperList) {
        this.draperList = draperList;
    }

}
