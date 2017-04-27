package com.spring.bean;

/**
 * Created by lenovo on 2017/3/20.
 * 组的基本操作的实体
 * 设备ID，组ID，命令
 */
public class GroupEntity {

    private int groupID;

    private int cmd;

    private int deviceID;

    public GroupEntity() {
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }
}
