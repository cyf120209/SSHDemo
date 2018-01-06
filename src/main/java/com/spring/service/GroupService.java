package com.spring.service;

import com.spring.bean.GroupEntity;
import com.spring.bean.ShadeGroup;
import com.spring.dao.IGroupDao;
import com.spring.service.manager.RetrofitManager;
import io.reactivex.functions.Consumer;
import org.springframework.cache.annotation.Cacheable;

import java.util.*;

/**
 * Created by lenovo on 2017/3/23.
 */
public class GroupService {

    private Object lock=new Object();

    protected IGroupDao groupDao;

    /**
     * 获取组信息
     * @return
     */
    @Cacheable(value = "shadeGroupCache")
    public List<ShadeGroup> getGroups(){
        List<ShadeGroup> shadeGroupList = new ArrayList<>();
        RetrofitManager.Builder()
                .getShadeGroupList()
                .subscribe(new Consumer<List<ShadeGroup>>() {
                    @Override
                    public void accept(List<ShadeGroup> shadeEntities) throws Exception {
                        shadeGroupList.addAll(shadeEntities);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        return shadeGroupList;
    }

    /**
     * 组的基本操作
     * @param groupEntity 组操作的实体（设备ID，组ID，命令）
     */
    public void operation(GroupEntity groupEntity){
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
    }

}