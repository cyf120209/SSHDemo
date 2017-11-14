package com.spring.utils;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.mstp.Frame;
import com.serotonin.bacnet4j.npdu.mstp.MasterNode;
import com.serotonin.bacnet4j.npdu.mstp.MstpNetwork;
import com.serotonin.bacnet4j.service.VendorServiceKey;
import com.serotonin.bacnet4j.service.acknowledgement.ConfirmedPrivateTransferAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedPrivateTransferRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedPrivateTransferRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.SequenceDefinition;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

import com.spring.common.Common;
import com.spring.listener.Listener;
import com.spring.model.DraperInformationItem;
import com.spring.model.DraperSubList;
import org.free.bacnet4j.util.SerialParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/2/5.
 */
public class MyLocalDevice {

    public static SerialParameters serialParams=new SerialParameters();

    private static LocalDevice localDevice;

//    /**
//     * 远程设备列表
//     */
//    private static List<RemoteDevice> mRemoteDeviceList=new ArrayList<>();
//
//    /**
//     * 远程设备列表
//     */
//    private static Map<Integer, RemoteDevice> mRemoteDevice = new HashMap<>();
//
//    /**
//     * 关系列表
//     */
//    private static Map<Integer, Map<Integer, List<Integer>>> mMap=new HashMap<Integer, Map<Integer, List<Integer>>>();


    private static MasterNode node;
    public static RemoteUtils mRemoteUtils;

    public synchronized static LocalDevice getInstance(){
        return localDevice;
    }

    public synchronized static LocalDevice getInstance(String prot){
        if(localDevice==null){
            serialParams.setCommPortId(prot);
            serialParams.setBaudRate(Common.BAUDRATE);
            node = new MasterNode(serialParams, (byte) 2,2);
            MstpNetwork network = new MstpNetwork(node);
            Transport transport = new Transport(network);
//            IpNetwork network = new IpNetwork("192.168.20.63");
//            Transport transport = new Transport(network);
//                    transport.setTimeout(15000);
//                    transport.setSegTimeout(15000);

            localDevice = new LocalDevice(900, 900900, transport);
            mRemoteUtils = new RemoteUtils();
            init();
        }
        return localDevice;
    }

    private static void init() {
        //电机，设备，组的关系
        List<SequenceDefinition.ElementSpecification> elements3 = new ArrayList<SequenceDefinition.ElementSpecification>();
        elements3.add(new SequenceDefinition.ElementSpecification("draperID", ObjectIdentifier.class, false, false));
        elements3.add(new SequenceDefinition.ElementSpecification("Motor Number", UnsignedInteger.class, false, false));
        elements3.add(new SequenceDefinition.ElementSpecification("DeviceGroup", DraperSubList.class, false, false));
        SequenceDefinition def3 = new SequenceDefinition(elements3);
        UnconfirmedPrivateTransferRequest.vendorServiceResolutions.put(new VendorServiceKey(new UnsignedInteger(900),
                new UnsignedInteger(7)), def3);
        //固件升级
        List<SequenceDefinition.ElementSpecification> elements4 = new ArrayList<SequenceDefinition.ElementSpecification>();
        elements4.add(new SequenceDefinition.ElementSpecification("DraperInformation", DraperInformationItem.class, false, false));
        SequenceDefinition def4 = new SequenceDefinition(elements4);
        ConfirmedPrivateTransferAck.vendorServiceResolutions.put(new VendorServiceKey(new UnsignedInteger(900),
                new UnsignedInteger(8)), def4);
        //固件升级失败后回调
        List<SequenceDefinition.ElementSpecification> elements2 = new ArrayList<SequenceDefinition.ElementSpecification>();
        elements2.add(new SequenceDefinition.ElementSpecification(Draper.GET_FRIMEBLOCK_PRODUCT_MODE_NAME, CharacterString.class, false, false));
        elements2.add(new SequenceDefinition.ElementSpecification(Draper.GET_FRIMEBLOCK_START_OFFSET, UnsignedInteger.class, false, false));
        elements2.add(new SequenceDefinition.ElementSpecification(Draper.GET_FRIMEBLOCK_BLOCK_SZIE, UnsignedInteger.class, false, false));
        SequenceDefinition def2 = new SequenceDefinition(elements2);
        ConfirmedPrivateTransferRequest.vendorServiceResolutions.put(new VendorServiceKey(new UnsignedInteger(900),
                new UnsignedInteger(Draper.GETFRAME_CONF_SERSUM)),def2);
        localDevice.getEventHandler().addListener(new Listener());
        try {
            localDevice.initialize();
            Thread.sleep(100);
            localDevice.sendGlobalBroadcast(localDevice.getIAm());
            Thread.sleep(100);
            localDevice.sendGlobalBroadcast(new WhoIsRequest());
        } catch (BACnetException e) {
            e.printStackTrace();
            if (localDevice != null) {
                localDevice.terminate();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            if (localDevice != null) {
                localDevice.terminate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (localDevice != null) {
                localDevice.terminate();
            }
        }
    }

    public static void stop(){
        if (localDevice != null) {
            localDevice.terminate();
            localDevice = null;
        }
    }

    /**
     * 判断设备是否已添加
     * @param remoteDevice
     * @return
     */
    public static boolean isExist(RemoteDevice remoteDevice){
        return mRemoteUtils.isExist(remoteDevice);
//        return mRemoteDeviceList.contains(remoteDevice);
    }

    /**
     * 添加远程设备
     * @param remoteDevice
     */
    public static void addRemoteDevice(RemoteDevice remoteDevice){
        mRemoteUtils.addRemoteDevice(remoteDevice);
//        mRemoteDeviceList.add(remoteDevice);
//        mRemoteDevice.put(remoteDevice.getInstanceNumber(),remoteDevice);
    }

    /**
     * 清空 mRemoteDeviceList,mRemoteDevice
     */
    public static void clearRemoteDevice(){
        mRemoteUtils.clearRemoteDevice();
//        mRemoteDeviceList.clear();
//        mRemoteDevice.clear();
    }

    public static Map<Integer, Map<Integer, List<Integer>>> getMap() {
        return mRemoteUtils.getMap();
//        return mMap;
    }

    public static void setMap(Map<Integer, Map<Integer, List<Integer>>> map) {
        mRemoteUtils.setMap(map);
//        mMap = map;
    }

    /**
     * 获取远程设备列表
     * @return
     */
    public static List<RemoteDevice> getRemoteDeviceList() {
        return mRemoteUtils.getRemoteDeviceList();
//        return mRemoteDeviceList;
    }

    /**
     * 获取deviceID，device的关系列表
     * @return
     */
    public static Map<Integer, RemoteDevice> getRemoteDeviceMap() {
        return mRemoteUtils.getRemoteDeviceMap();
//        return mRemoteDevice;
    }

    /**
     * 获取升级进度
     * @return
     */
    public static List<Frame> getFrameToSend(){
        return node.getFramesToSend();
    }

    /**
     * 获取所有设备的地址
     * @return
     */
    public static List<Byte> getAddressList(){
        return node.getAddressList();
    }
}
