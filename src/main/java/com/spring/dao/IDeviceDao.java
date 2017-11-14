package com.spring.dao;

import com.spring.bean.DeviceEntity;
import com.spring.bean.ShadeEntity;

import java.util.List;

public interface IDeviceDao {

    /**
     * 通过ID查询电机单台设备
     *
     * @param id
     * @return
     */
    DeviceEntity queryById(Integer id);

    /**
     * 查询所有
     *
     * @return
     */
    List<DeviceEntity> queryAll();
}
