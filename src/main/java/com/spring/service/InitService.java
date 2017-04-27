package com.spring.service;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.spring.model.DraperSubItem;
import com.spring.model.DraperSubList;
import com.spring.utils.Draper;
import com.spring.utils.MyLocalDevice;
import com.spring.utils.RxBus;
import rx.Subscription;
import rx.functions.Action1;

import java.util.*;


/**
 * Created by lenovo on 2017/3/20.
 */
public class InitService {

    private LocalDevice localDevice;

    public boolean init(String port){
        boolean isSuccess=false;
        localDevice = MyLocalDevice.getInstance(port);
        if (localDevice!=null){
            isSuccess=true;
        }
        return isSuccess;
    }

}
