package com.spring.service;

import com.googlecode.ehcache.annotations.Cacheable;
import com.spring.bean.ShadeEntity;
import com.spring.service.manager.RetrofitManager;
import io.reactivex.functions.Consumer;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lenovo on 2017/3/21.
 */
@Service("shadeService")
public class ShadeService {

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
    @Cacheable(cacheName = "myCache")
    public List<ShadeEntity> getShadeList() {
        List<ShadeEntity> shadeEntityList=new ArrayList<>();
        RetrofitManager.Builder()
                .getShadeList()
                .subscribe(new Consumer<List<ShadeEntity>>() {
                    @Override
                    public void accept(List<ShadeEntity> shadeEntities) throws Exception {

                        shadeEntityList.addAll(shadeEntities);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        return shadeEntityList;
    }

    /**
     * 获取电机列表
     * @return
     */
    public List<ShadeEntity> getShadeList(Integer pageStartIndex,Integer pageSize) {
        List<ShadeEntity> shadeEntityList=new ArrayList<>();
        RetrofitManager.Builder()
                .getShadeList(pageStartIndex,pageSize)
                .subscribe(new Consumer<List<ShadeEntity>>() {
                    @Override
                    public void accept(List<ShadeEntity> shadeEntities) throws Exception {
                        shadeEntityList.addAll(shadeEntities);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        return shadeEntityList;
    }
}
