package com.spring.service;

import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.spring.bean.AddGroupEntity;
import com.spring.bean.GroupEntity;
import com.spring.listener.AnnounceListener;
import com.spring.model.DraperSubItem;
import com.spring.model.DraperSubList;
import com.spring.utils.Draper;
import com.spring.utils.MyLocalDevice;
import com.spring.utils.RxBus;
import rx.Subscription;
import rx.functions.Action1;

import java.util.*;

/**
 * Created by lenovo on 2017/3/23.
 */
public class GroupService {

    private Object lock=new Object();
    /**
     * announce类型 1：代表全部，2：代表组
     */
    private int mAnnounceType=1;

    /**
     * serviceParameters参数列表，主要防止重复添加
     */
    private List<Sequence> mSequence = new ArrayList<>();

    /**
     * 电机，设备和组的关系列表
     */
    private Map<Integer, Map<Integer, List<Integer>>> mRelativeList = new HashMap<>();

    /**
     * 设备ID列表
     */
    private List<Integer> mDevicesIDList = new ArrayList<>();

    private AnnounceListener listener;
    private List<Integer> remoteDeviceIDList;

    /**
     * 获取map关系列表
     * @return
     */
    public Map<Integer, Map<Integer, List<Integer>>> getMap(){
        try {
            mSequence.clear();
            mRelativeList.clear();
            mRelativeList.clear();
            remoteDeviceIDList = MyLocalDevice.mRemoteUtils.getRemoteDeviceIDList();
            if(listener==null){
                listener = new AnnounceListener(this);
            }
            MyLocalDevice.getInstance().getEventHandler().addListener(listener);
            Draper.sendAnnounce();
        } catch (BACnetException e) {
            e.printStackTrace();
        }
        Subscription subscribe = RxBus.getDefault().toObservable(Map.class)
                .subscribe(new Action1<Map>() {
                    @Override
                    public void call(Map m) {
//                        mRelativeList = m;
                        synchronized (lock) {
                            lock.notify();
                        }
                    }
                });
        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        MyLocalDevice.getInstance().getEventHandler().removeListener(listener);
        subscribe.unsubscribe();
        return mRelativeList;
    }

    /**
     * 组的基本操作
     * @param groupEntity 组操作的实体（设备ID，组ID，命令）
     */
    public void operation(GroupEntity groupEntity){
        try {
            Draper.sendCmd(groupEntity.getDeviceID(), groupEntity.getGroupID(), groupEntity.getCmd());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加组
     * @param groupID 组ID
     */
    public void add(int deviceID, int groupID,AddGroupEntity entity){
        List<Integer> draperList = entity.getDraperList();
        Map<Integer, RemoteDevice> remoteDeviceMap = MyLocalDevice.getRemoteDeviceMap();
        for (Integer integer: draperList) {
            RemoteDevice remoteDevice = remoteDeviceMap.get(integer);
            try {
                Draper.sendGroupSubscriptionToSelect(remoteDevice,false,deviceID,groupID);
            } catch (BACnetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新组
     * @param groupID 组ID
     * @param groupName 组名称
     */
    public void update(int groupID,String groupName){
        //TODO

    }

    /**
     * 删除设备下的某个组
     * @param deviceID 设备ID
     * @param groupID 组ID
     */
    public void delete(int deviceID,int groupID){
        try {
            Draper.sendGroupSubscription(true,deviceID,groupID);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 删除组下面的某个电机
     * @param deviceID 设备ID
     * @param groupID 组ID
     * @param draperID 电机ID
     */
    public void delete(int deviceID,int groupID,int draperID){
        try {
            Map<Integer, RemoteDevice> remoteDeviceMap = MyLocalDevice.getRemoteDeviceMap();
            Draper.sendGroupSubscriptionToSelect(remoteDeviceMap.get(draperID),true,deviceID,groupID);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public synchronized void paraseServiceParameter(UnsignedInteger serviceNumber, Sequence serviceParameters) {
        Sequence parms = serviceParameters;
        if (serviceNumber.intValue() == 7) {
            if (mAnnounceType == 1) {
                deviceAnnounce(parms);
            } else {
                groupAnnounce(parms);
            }
        }
    }

    private void groupAnnounce(Sequence parms) {
        Map<String, Encodable> values1 = parms.getValues();
        ObjectIdentifier draperID1 = (ObjectIdentifier) values1.get("draperID");
        int instanceNumber1 = draperID1.getInstanceNumber();
        List<Integer> list = new ArrayList<>();
        list.add(instanceNumber1);
    }

    private void deviceAnnounce(Sequence parms) {
        //parms 去重处理
        if (mSequence.contains(parms)) {
            return;
        }
        mSequence.add(parms);
        Map<String, Encodable> values1 = parms.getValues();
        //获取draperID
        ObjectIdentifier draperID1 = (ObjectIdentifier) values1.get("draperID");
        int instanceNumber1 = draperID1.getInstanceNumber();
        //该draperID下的设备-组关系
        DraperSubList deviceGroup1 = (DraperSubList) values1.get("DeviceGroup");
        for (DraperSubItem item1 : deviceGroup1.getList()) {
            Map<Integer, List<Integer>> CdevGrpInf = null;
            CdevGrpInf = mRelativeList.get(item1.getDevicID().getInstanceNumber());
            if (CdevGrpInf == null) {
                CdevGrpInf = new HashMap<Integer, List<Integer>>();
                mRelativeList.put(item1.getDevicID().getInstanceNumber(), CdevGrpInf);
            }
            if (!mDevicesIDList.contains(item1.getDevicID().getInstanceNumber())) {
                mDevicesIDList.add(item1.getDevicID().getInstanceNumber());
            }
            List<Integer> devList = null;
            devList = CdevGrpInf.get(item1.getGroupID().intValue());
            if (devList == null) {
                devList = new LinkedList<Integer>();
                CdevGrpInf.put(item1.getGroupID().intValue(), devList);
            }
            if (!devList.contains(instanceNumber1)) {
                devList.add(instanceNumber1);
            }
        }
        if(remoteDeviceIDList.contains(new Integer(instanceNumber1))){
            remoteDeviceIDList.remove(new Integer(instanceNumber1));
        }
        if(remoteDeviceIDList.size()==0){
            RxBus.getDefault().post(mRelativeList);
        }
        Vector vector = new Vector();
//        mGroupOperationView.updateDevice(mDevicesIDList.toArray());
    }
}
