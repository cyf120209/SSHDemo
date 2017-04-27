package com.spring.listener;

import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.spring.service.GroupService;

/**
 * Created by lenovo on 2017/4/5.
 */
public class AnnounceListener extends DeviceEventAdapter {

    private GroupService service;

    public AnnounceListener(GroupService service) {
        this.service = service;
    }

    @Override
    public void privateTransferReceived(UnsignedInteger vendorId, UnsignedInteger serviceNumber, Encodable serviceParameters) {
//        super.privateTransferReceived(vendorId, serviceNumber, serviceParameters);
        service.paraseServiceParameter(serviceNumber,(Sequence)serviceParameters);
    }
}
