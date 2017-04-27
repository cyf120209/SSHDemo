package com.spring.service;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.unconfirmed.WhoHasRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.spring.bean.DraperEntity;
import com.spring.utils.Draper;
import com.spring.utils.MyLocalDevice;
import com.spring.utils.RxBus;
import rx.Subscription;
import rx.functions.Action1;

import java.util.*;

/**
 * Created by lenovo on 2017/3/21.
 */
public class DraperService {

    private Object lock=new Object();
    private Timer timer;
    /**
     * 当前时间
     */
    public long curTime;
    /**
     * 最后一个数据的时间
     */
    public long lastTime;
    /**
     * 传递数据的标志
     */
    private boolean isCall;

    /**
     * 获取电机列表
     * @return
     */
    public List<Integer> getDrapers(){
        List<Integer> draperList=new ArrayList<>();
        List<RemoteDevice> remoteDeviceList = MyLocalDevice.getRemoteDeviceList();
        for (RemoteDevice device:remoteDeviceList) {
            draperList.add(device.getInstanceNumber());
        }
        return draperList;
    }

    public void operation(DraperEntity draperEntity){
        Map<Integer, RemoteDevice> remoteDeviceMap = MyLocalDevice.getRemoteDeviceMap();
        RemoteDevice remoteDevice = remoteDeviceMap.get(draperEntity.getDraperID());
        try {
            Draper.sendCmd(remoteDevice,draperEntity.getCmd());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCountTime(){
        stopCountTime();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                curTime=System.currentTimeMillis();
                //20ms作为世界间隔，若20ms后没有数据进来，则数据传输完毕
                if (isCall && (curTime - lastTime) > 15) {
                    synchronized (lock) {
                        System.out.println("----------------------" + (curTime - lastTime));
                        lock.notify();
                        stopCountTime();
                    }
                }
            }
        },0,10);
    }

    private void stopCountTime(){
        if(timer!=null){
            timer.cancel();
        }
    }
}
