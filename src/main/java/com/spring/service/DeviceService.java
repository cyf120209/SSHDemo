package com.spring.service;

import com.spring.bean.DeviceEntity;
import com.spring.bean.ShadeEntity;
import com.spring.dao.IDeviceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

public class DeviceService implements IDeviceService {


    @Autowired
    private IDeviceDao deviceDao;

    @Override
    public List<DeviceEntity> getDeviceList() {
        List<DeviceEntity> deviceEntityList = deviceDao.queryAll();
        return deviceEntityList;
    }
}
