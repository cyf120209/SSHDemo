package com.spring.listener;

import antlr.collections.impl.*;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.spring.model.DraperSubItem;
import com.spring.model.DraperSubList;
import com.spring.service.InitService;
import com.spring.utils.MyLocalDevice;
import com.spring.utils.RxBus;
import com.spring.utils.STExecutor;

import javax.annotation.Resource;
import java.util.*;
import java.util.Vector;


/**
 * Created by lenovo on 2017/1/19.
 */
public class Listener extends DeviceEventAdapter {

    public LocalDevice localDevice;

    public Listener() {
        this.localDevice = MyLocalDevice.getInstance();
    }

    @Override
    public void iAmReceived(RemoteDevice d) {
//        super.iAmReceived(d);
        System.out.println("iHaveReceived"+d);
        boolean exist = MyLocalDevice.isExist(d);
        if(exist){
            return;
        }
//        RxBus.getDefault().post(d);
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                MyLocalDevice.addRemoteDevice(d);
            }
        };
        STExecutor.submit(runnable);

    }

    @Override
    public void iHaveReceived(final RemoteDevice d, final RemoteObject o) {

    }
}