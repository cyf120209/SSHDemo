package com.spring.listener;

import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.spring.service.UpgradeService;
import com.spring.utils.MyLocalDevice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2017/4/5.
 */
public class UpgradeListener extends DeviceEventAdapter {

    private UpgradeService upgradeService;

    public UpgradeListener(UpgradeService upgradeService) {
        this.upgradeService = upgradeService;
    }

    @Override
    public void iAmReceived(RemoteDevice d) {
        upgradeService.iAmReceived(d);
    }

    @Override
    public AcknowledgementService privateTransferReceivedComplex(UnsignedInteger vendorId, UnsignedInteger serviceNumber, Encodable serviceParameters, Address address) {
        return upgradeService.privateTransferReceivedComplex(vendorId, serviceNumber, serviceParameters, address);
    }
}
