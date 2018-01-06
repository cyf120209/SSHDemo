package com.spring.utils;

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
