package com.spring.utils;


import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedPrivateTransferRequest;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by lenovo on 2016/12/17.
 */
public class STExecutor {

    static ExecutorService singleThreadExecutor=Executors.newSingleThreadExecutor();
//    static ScheduledExecutorService scheduledExecutorService=Executors.newScheduledThreadPool(1);

    public static void submit(Runnable runnable){
        singleThreadExecutor.submit(runnable);
    }

    public static void shutdown(){
        singleThreadExecutor.shutdown();
    }

}
