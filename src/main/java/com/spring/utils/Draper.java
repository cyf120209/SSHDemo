package com.spring.utils;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.ConfirmedPrivateTransferAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedPrivateTransferRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedPrivateTransferRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.*;
import com.spring.model.DraperInformation;
import com.spring.model.DraperInformationItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class Draper {
    public static final int DRAPER_CMD_STOP=1;
    public static final int DRAPER_CMD_RETRACTED=3;
    public static final int DRAPER_CMD_EXTENDED=4;
    public static final int DRAPER_VENDOR_ID=900;
    public static final int CMD_UNCONF_SERNUM=1;
    public static final int HAVEFRAME_UNCONF_SERNUM=2;
    public static final int GETFRAME_CONF_SERSUM=3;
    public static final int FRAME_BLOCK_UNCONF_SEERNUM=4;
    public static final int SUBSCRIPTION_GROUP=5;
    public static final int ANNOUNCE_GROUP=6;
    public static final int ANNOUNCE_DRAPER_INFORMATION=8;

    public static final String GET_FRIMEBLOCK_PRODUCT_MODE_NAME="Product Model Name";
    public static final String GET_FRIMEBLOCK_START_OFFSET="Start Offset";
    public static final String GET_FRIMEBLOCK_BLOCK_SZIE="Block Size";

    public static final UnsignedInteger cmdSer=new UnsignedInteger(CMD_UNCONF_SERNUM);
    public static UnsignedInteger vendorID=new UnsignedInteger(DRAPER_VENDOR_ID);
    public static UnsignedInteger haveFrame=new UnsignedInteger(HAVEFRAME_UNCONF_SERNUM);
    public static UnsignedInteger brocastFram=new UnsignedInteger(FRAME_BLOCK_UNCONF_SEERNUM);
    public static UnsignedInteger subscription=new UnsignedInteger(SUBSCRIPTION_GROUP);
    public static UnsignedInteger announce=new UnsignedInteger(ANNOUNCE_GROUP);
    public static UnsignedInteger announceDraperInformation=new UnsignedInteger(ANNOUNCE_DRAPER_INFORMATION);
    public static ObjectIdentifier deviceid=new ObjectIdentifier(ObjectType.device,900900);

    private static LocalDevice dev=MyLocalDevice.getInstance();

    private static int calcDataCRC(int dataValue, int crcValue) {
        int crcLow = (crcValue & 0xff) ^ dataValue; /* XOR C7..C0 with D7..D0 */
        /* Exclusive OR the terms in the table (top down) */
        int crc = (crcValue >> 8) ^ (crcLow << 8) ^ (crcLow << 3) ^ (crcLow << 12) ^ (crcLow >> 4) ^ (crcLow & 0x0f)
                ^ ((crcLow & 0x0f) << 7);
        return crc & 0xffff;
    }

    /**
     * 固件升级-单台
     * @param _type 固件类型
     * @param peer 远程设备
     * @param major 主版本号
     * @param minor 副版本号
     * @param patch 补丁版本号
     * @param type  文件类型
     * @param fileTemp 文件二进制流
     * @throws Exception
     */
    public static void  sendIHaveFrameToOne(String _type, RemoteDevice peer, int major, int minor, int patch, int type, byte[] fileTemp) throws Exception {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        byte[] buffer=fileTemp.clone();
//        byte[] buffer = new byte[(int) file.length()];
//        FileInputStream fi = new FileInputStream(file);
        int crc =0;
        listParam.add(new CharacterString(_type));
        listParam.add(new Unsigned16(major));
        listParam.add(new Unsigned16(minor));
        listParam.add(new Unsigned16(patch));
        listParam.add(new Enumerated(type));
        listParam.add(new UnsignedInteger(buffer.length));
        int offset = 0;
        int numRead = 0;
//        while (offset < buffer.length
//                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
//            offset += numRead;
//        }
        // 确保所有数据均被读取
//        if (offset != buffer.length) {
//            throw new IOException("Could not completely read file "
//                    + file.getName());
//        }else {
            for (byte tmp : buffer)
                crc = calcDataCRC(tmp & 0xff, crc);
//        }
//        fi.close();
        Unsigned16 pramCrc=new Unsigned16(crc);
        listParam.add(pramCrc);
        dev.sendUnconfirmed(peer.getAddress(),peer.getLinkService(),new UnconfirmedPrivateTransferRequest(vendorID,haveFrame,listParam));
    }
    public static void sendFrameWareToOne(String type, RemoteDevice peer, int major, int minor, int patch, int filesize, int startoffset, byte[] data) throws BACnetException {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        // listParam.add(deviceid);
        listParam.add(new CharacterString(type));
        listParam.add(new Unsigned16(major));
        listParam.add(new Unsigned16(minor));
        listParam.add(new Unsigned16(patch));
        listParam.add(new UnsignedInteger(filesize));
        listParam.add(new UnsignedInteger(startoffset));
        listParam.add(new OctetString(data));
        dev.sendUnconfirmed(peer.getAddress(),peer.getLinkService(),new UnconfirmedPrivateTransferRequest(vendorID,brocastFram,listParam));
    }

    /**
     * 固件升级
     * @param _type 固件类型
     * @param major 主版本号
     * @param minor 副版本号
     * @param patch 补丁版本号
     * @param type 文件类型
     * @param fileTemp 文件二进制流
     * @throws Exception
     */
    public static void  sendIHaveFrame( String _type, int major, int minor, int patch, int type, byte[] fileTemp) throws Exception {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        byte[] buffer=fileTemp.clone();
//        byte[] buffer = new byte[(int) file.length()];
//        FileInputStream fi = new FileInputStream(file);
        int crc =0;
        listParam.add(new CharacterString(_type));
        listParam.add(new Unsigned16(major));
        listParam.add(new Unsigned16(minor));
        listParam.add(new Unsigned16(patch));
        listParam.add(new Enumerated(type));
        listParam.add(new UnsignedInteger(buffer.length));
        int offset = 0;
        int numRead = 0;
//        while (offset < buffer.length
//                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
//            offset += numRead;
//        }
        // 确保所有数据均被读取
//        if (offset != buffer.length) {
//            throw new IOException("Could not completely read file "
//                    + file.getName());
//        }else {
            for (byte tmp : buffer)
                crc = calcDataCRC(tmp & 0xff, crc);
//        }
//        fi.close();
        Unsigned16 pramCrc=new Unsigned16(crc);
        listParam.add(pramCrc);
        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,haveFrame,listParam));

    }

    //6.1	SHADE COMMAND
    public static  void sendCmd( int cmd) throws Exception {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
        listParam.add(new Unsigned8(0));
        listParam.add(new UnsignedInteger(cmd));
        listParam.add(new Unsigned8(7));
        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,cmdSer,listParam));
    }

    //6.1	SHADE COMMAND
    public static  void sendCmd( RemoteDevice remoteDevice, int cmd) throws Exception {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
        listParam.add(new Unsigned8(0));
        listParam.add(new UnsignedInteger(cmd));
        listParam.add(new Unsigned8(7));
        System.out.println(cmd);
        dev.sendUnconfirmed(remoteDevice.getAddress(),remoteDevice.getLinkService(),new UnconfirmedPrivateTransferRequest(vendorID,cmdSer,listParam));
    }

    //6.1	SHADE COMMAND
    public static  void sendCmd( int deviceID, int groupID, int cmd) throws Exception {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
        listParam.add(new Unsigned8(0));
        listParam.add(new UnsignedInteger(cmd));
        listParam.add(new Unsigned8(7));
        listParam.add(new ObjectIdentifier(ObjectType.device,deviceID));
        listParam.add(new Unsigned16(groupID));
        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,cmdSer,listParam));

    }

    public static  void sendCmd( Address addr, int cmd) throws Exception {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
        listParam.add(new UnsignedInteger(cmd));
        listParam.add(new Unsigned8(7));
        dev.sendUnconfirmed(addr,new UnconfirmedPrivateTransferRequest(vendorID,cmdSer,listParam));

    }

    public static void sendFrameWare( String type, int major, int minor, int patch, int filesize, int startoffset, byte[] data) throws BACnetException {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
       // listParam.add(deviceid);
        listParam.add(new CharacterString(type));
        listParam.add(new Unsigned16(major));
        listParam.add(new Unsigned16(minor));
        listParam.add(new Unsigned16(patch));
        listParam.add(new UnsignedInteger(filesize));
        listParam.add(new UnsignedInteger(startoffset));
        listParam.add(new OctetString(data));
        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,brocastFram,listParam));
    }

    //6.2	GROUP SUBSCRIPTION
    public static void sendGroupSubscription( boolean remove, int deviceID, int GroupID) throws BACnetException {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
        listParam.add(new Unsigned8(0));
        listParam.add(new ObjectIdentifier(ObjectType.device,deviceID));
        listParam.add(new Unsigned16(GroupID));
        listParam.add(new Boolean(remove));
        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,subscription,listParam));

    }
    public static synchronized void sendGroupSubscriptionToSelect( RemoteDevice peer, boolean remove, int deviceID, int GroupID) throws BACnetException {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
        listParam.add(new Unsigned8(0));
        listParam.add(new ObjectIdentifier(ObjectType.device,deviceID));
        listParam.add(new Unsigned16(GroupID));
        listParam.add(new Boolean(remove));
        dev.sendUnconfirmed(peer.getAddress(),peer.getLinkService(),new UnconfirmedPrivateTransferRequest(vendorID,subscription,listParam));

    }
    public static void sendAnnounce() throws BACnetException {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,announce,listParam));
    }

    public static void sendAnnounceDraperInformation( RemoteDevice peer) throws BACnetException {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
//        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,announce,listParam));
//        dev.send(peer,new ConfirmedPrivateTransferRequest(vendorID, announceDraperInformation, listParam));
        AcknowledgementService send = dev.send(peer,new ConfirmedPrivateTransferRequest(vendorID, announceDraperInformation, listParam));
        ConfirmedPrivateTransferAck result= (ConfirmedPrivateTransferAck) send;
        Sequence resultBlock = (Sequence)result.getResultBlock();
        Map<String, Encodable> values = resultBlock.getValues();
        DraperInformationItem encodable = (DraperInformationItem) values.get("DraperInformation");
        Boolean isReverse = encodable.getIsReverse();
        SignedInteger curPos = encodable.getCurPos();
        SignedInteger upLimit = encodable.getUpLimit();
        SignedInteger lowLimit = encodable.getLowLimit();
        List<SignedInteger> persetStopList = encodable.getPersetStop();
        DraperInformation draperInformation = new DraperInformation(isReverse, curPos, upLimit, lowLimit, persetStopList);
        RxBus.getDefault().post(draperInformation);
    }
}