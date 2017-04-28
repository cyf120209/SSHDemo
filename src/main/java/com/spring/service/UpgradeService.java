package com.spring.service;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.mstp.Frame;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.ConfirmedPrivateTransferAck;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Primitive;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.spring.bean.FirmWareEntity;
import com.spring.bean.StateEntity;
import com.spring.listener.UpgradeListener;
import com.spring.socket.SpringWebSocketHandler;
import com.spring.utils.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.socket.TextMessage;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2017/3/24.
 */
public class UpgradeService {

    /**
     * 升级进度
     */
    private int percent;

    /**
     * 固件总帧数
     */
    private int mTotalSize;

    /**
     * 文件二进制流
     */
    private byte[] fileTmp;

    FirmWareEntity firmWareEntity;

    private Object lock = new Object();

    /**
     * 固件升级监听
     */
    private UpgradeListener upgradeListener;

    /**
     * 远程设备列表
     */
    private Map<Integer, RemoteDevice> mRemoteDevice = new HashMap<>();

    /**
     * 过滤日志列表
     */
    private String modelName;

    public SpringWebSocketHandler infoHandler() {
        return new SpringWebSocketHandler();
    }

    /**
     * -1 代表升级所有
     */
    private int draperID;

    /**
     * 升级日志显示 1.代表原始 2.代表升级前 3.代表升级后
     */
    private int flag = 1;

    /**
     * 电机的状态 与{@link "draperPreStatue"} 比较，值相等则代表电机全都就绪
     */
    private List<String> draperStatue=new ArrayList<>();

    /**
     * 电机升级前的状态 与{@link "draperStatue"} 比较，值相等则代表电机全都就绪
     */
    private List<String> draperPreStatue=new ArrayList<>();


    /**
     * 上传文件
     *
     * @param request
     * @return
     * @throws IOException
     */
    public FirmWareEntity uploadFile(HttpServletRequest request) throws IOException {
        FileUtils fileUtils = new FileUtils();
        firmWareEntity = fileUtils.uploadFile(request);
        fileTmp = fileUtils.getFileTmp();
        modelName = firmWareEntity.getType();
        return firmWareEntity;
    }

    /**
     * 固件升级
     *
     * @param draperID draperID -1 代表升级所有
     * @return
     */
    public boolean upgradeDraper(int draperID) {
        this.draperID = draperID;
        percent = 0;
        mRemoteDevice.clear();
        mRemoteDevice.putAll(MyLocalDevice.getRemoteDeviceMap());
        if(upgradeListener==null){
            upgradeListener = new UpgradeListener(this);
            MyLocalDevice.getInstance().getEventHandler().addListener(upgradeListener);
        }
        if (draperID == -1) {
            return upgrade();
        } else {
            return upgrade(draperID);
        }
    }

    /**
     * 指定升级
     *
     * @param draperID
     * @return
     */
    public boolean upgrade(int draperID) {
        Map<Integer, RemoteDevice> remoteDeviceMap = MyLocalDevice.getRemoteDeviceMap();
        RemoteDevice remoteDevice = remoteDeviceMap.get(draperID);
        String item = remoteDevice.getInstanceNumber()+"-"+readVersion(remoteDevice);
        infoHandler().sendMessageToUser("cyf", new TextMessage("origin," + item));
        new RunUpdateToOne(remoteDevice).start();
        return false;
    }

    /**
     * 所有升级
     *
     * @return
     */
    public boolean upgrade() {
        flag = 2;
        init();
        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new RunUpdate().start();
        return false;
    }

    private void init() {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                List<RemoteDevice> remoteDeviceList = MyLocalDevice.getRemoteDeviceList();
                for (RemoteDevice remoteDevice:remoteDeviceList){
                    String version = readVersion(remoteDevice);
                    String rm = remoteDevice.getInstanceNumber()+"-" + version;
                    System.out.println("-------------init"+rm);
                    infoHandler().sendMessageToUser("cyf", new TextMessage("origin," + rm));
                    draperStatue.add(rm);
                }
                synchronized (lock){
                    lock.notify();
                }
            }
        };
        STExecutor.submit(runnable);
    }

    /**
     * 读取版本号
     * @param remoteDevice
     * @return
     */
    public String readVersion(RemoteDevice remoteDevice){
        try {
            ReadPropertyAck ack = (ReadPropertyAck) MyLocalDevice.getInstance().send(remoteDevice, new ReadPropertyRequest(remoteDevice.getObjectIdentifier(), PropertyIdentifier.firmwareRevision));
            return ack.getValue().toString();
        } catch (BACnetException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取版本号
     *
     * @param draperID
     * @return
     */
    public List<String> readVersion(int draperID) {
        ArrayList<String> list = new ArrayList<>();
        synchronized (lock) {
            try {
                Observable.just(draperID)
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                try {
                                    Map<Integer, RemoteDevice> remoteDeviceMap = MyLocalDevice.getRemoteDeviceMap();
                                    RemoteDevice remoteDevice = remoteDeviceMap.get(draperID);
                                    ReadPropertyAck ack = (ReadPropertyAck) MyLocalDevice.getInstance().send(remoteDevice, new ReadPropertyRequest(remoteDevice.getObjectIdentifier(), PropertyIdentifier.firmwareRevision));
                                    list.add(ack.getValue().toString());
                                } catch (BACnetException e) {
                                    e.printStackTrace();
                                } finally {
                                    synchronized (lock) {
                                        lock.notify();
                                    }
                                }
                            }
                        });
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 更新单台电机
     */
    public class RunUpdateToOne extends Thread {

        RemoteDevice peer;

        RunUpdateToOne(RemoteDevice peer) {
            this.peer = peer;
        }

        @Override
        public void run() {
            try {
                Draper.sendIHaveFrameToOne(firmWareEntity.getType(), peer, firmWareEntity.getMajorNum(), firmWareEntity.getMinorNum(), firmWareEntity.getPatchNum(), firmWareEntity.getTypeNum(), fileTmp);
                Thread.sleep(6000);
                String item = peer.getInstanceNumber()+"-"+readVersion(peer);
                infoHandler().sendMessageToUser("cyf", new TextMessage("upgradeBefore," + item));
                flag = 3;
//                FileInputStream fi = new FileInputStream(updateFile);
//                byte[] buffer=new byte[(int)updateFile.length()];
//                int offset = 0;
//                int numRead = 0;
//                while (offset < buffer.length
//                        && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
//                    offset += numRead;
//                }
                // 确保所有数据均被读取
//                if (offset != buffer.length) {
//                    throw new IOException("Could not completely read file "
//                            + updateFile.getName());
//                }else{
                int sent = 0;
                while (sent < fileTmp.length) {
                    sent = sentBuffer(fileTmp, sent);
                }
//                }
//                fi.close();

                //更新进度条的UI
                List<Frame> frameToSend = MyLocalDevice.getFrameToSend();
                mTotalSize = frameToSend.size();
                new UpdatePercent().run();

            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {

            }
        }

        private synchronized int sentBuffer(byte[] buffer, int sent) throws BACnetException, InterruptedException {
            if (buffer.length - sent > 256) {
                byte[] data = new byte[256];
                System.arraycopy(buffer, sent, data, 0, 256);
                Draper.sendFrameWareToOne(firmWareEntity.getType(), peer, firmWareEntity.getMajorNum(), firmWareEntity.getMinorNum(), firmWareEntity.getPatchNum(), buffer.length, sent, data);
                sent = sent + 256;
            } else {
                byte[] data1 = new byte[buffer.length - sent];
                System.arraycopy(buffer, sent, data1, 0, buffer.length - sent);
                Draper.sendFrameWareToOne(firmWareEntity.getType(), peer, firmWareEntity.getMajorNum(), firmWareEntity.getMinorNum(), firmWareEntity.getPatchNum(), buffer.length, sent, data1);
                sent = buffer.length;
            }
//            Thread.sleep(200);
//            percent = sent * 100 / buffer.length;
            return sent;
        }
    }

    /**
     * 更新所有
     */
    public class RunUpdate extends Thread {

        @Override
        public void run() {
            try {
                Draper.sendIHaveFrame(firmWareEntity.getType(), firmWareEntity.getMajorNum(), firmWareEntity.getMinorNum(), firmWareEntity.getPatchNum(), firmWareEntity.getTypeNum(), fileTmp);
                try {
                    //差不多需要6秒的准备时间
                    Thread.sleep(6000);
                    MyLocalDevice.getInstance().sendGlobalBroadcast(new WhoIsRequest());
                } catch (BACnetException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock) {
                    lock.wait();
                }
//                Thread.sleep(15000);
                flag = 3;
//                FileInputStream fi = new FileInputStream(updateFile);
//                byte[] buffer=new byte[(int)updateFile.length()];
//                int offset = 0;
//                int numRead = 0;
//                while (offset < buffer.length
//                        && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
//                    offset += numRead;
//                }
//
//                // 确保所有数据均被读取
//                if (offset != buffer.length) {
//                    throw new IOException("Could not completely read file "
//                            + updateFile.getName());
//                }else{
                int sent = 0;
                while (sent < fileTmp.length) {
                    sent = sentBuffer(fileTmp, sent);
                }
//                }
//                fi.close();

                //更新进度条的UI
                List<Frame> frameToSend = MyLocalDevice.getFrameToSend();
                mTotalSize = frameToSend.size();
                new UpdatePercent().run();
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {

            }
        }

        private synchronized int sentBuffer(byte[] buffer, int sent) throws BACnetException, InterruptedException {
            if (buffer.length - sent > 256) {
                byte[] data = new byte[256];
                System.arraycopy(buffer, sent, data, 0, 256);
                Draper.sendFrameWare(firmWareEntity.getType(), firmWareEntity.getMajorNum(), firmWareEntity.getMinorNum(), firmWareEntity.getPatchNum(), buffer.length, sent, data);
                sent = sent + 256;
            } else {
                byte[] data1 = new byte[buffer.length - sent];
                System.arraycopy(buffer, sent, data1, 0, buffer.length - sent);
                Draper.sendFrameWare(firmWareEntity.getType(), firmWareEntity.getMajorNum(), firmWareEntity.getMinorNum(), firmWareEntity.getPatchNum(), buffer.length, sent, data1);
                sent = buffer.length;
            }
//            Thread.sleep(200);
//            percent = sent * 100 / buffer.length;
            return sent;
        }
    }

    public class UpdatePercent implements Runnable {

        public UpdatePercent() {
        }

        @Override
        public void run() {
            Timer time = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    List<Frame> frameToSend = MyLocalDevice.getFrameToSend();
                    System.out.println("UpdatePresenterImpl frameToSend.size" + frameToSend.size());
                    percent = 100 - frameToSend.size() * 100 / mTotalSize;
                    infoHandler().sendMessageToUser("cyf", new TextMessage("percent," + percent));
                    Timer t = (Timer) e.getSource();
                    // 如果进度条达到最大值重新开发计数
                    if (percent == 100) {
                        t.stop();
                    }
                }
            });
            time.start();
        }
    }


    public void iAmReceived(RemoteDevice d) {
        if (flag == 2) {
            Runnable readModelName = new Runnable() {
                @Override
                public void run() {
                    if (!RemoteUtils.readModelName(d).equals(modelName)){
                        return;
                    }
                    String name = readVersion(d);
                    System.out.println("--------------modelName-----------" + name);
                    if (name == null) {
                        STExecutor.submit(this);
                    } else {
                        if (draperID == -1) {
                            String item = d.getInstanceNumber() + "-" + name;
                            System.out.println("--------------pre" + item);
                            draperPreStatue.add(item);
                            infoHandler().sendMessageToUser("cyf", new TextMessage("pre," + draperPreStatue.size()+"/"+draperStatue.size()));
                            infoHandler().sendMessageToUser("cyf", new TextMessage("upgradeBefore," + item));
                            if (draperStatue.equals(draperPreStatue)) {
                                draperStatue.clear();
                                draperPreStatue.clear();
                                synchronized (lock) {
                                    lock.notify();
                                }
                            }
                        } else {

                        }
                    }
                }
            };
            STExecutor.submit(readModelName);
        }else if (flag == 3) {
            Runnable readModelName = new Runnable() {
                @Override
                public void run() {
                    if (!RemoteUtils.readModelName(d).equals(modelName)){
                        return;
                    }
                        String name = readVersion(d);
                        System.out.println("--------------version-----------" + name);
                        String item = d.getInstanceNumber() + "-" + name;
                        infoHandler().sendMessageToUser("cyf", new TextMessage("upgradeAfter," + item));
                }
            };
            STExecutor.submit(readModelName);
        }
    }

//    /**
//     * 读取modelName
//     *
//     * @param remoteDevice
//     * @return
//     */
//    public synchronized String readModelName(RemoteDevice remoteDevice) {
//        try {
//            LocalDevice localDevice = MyLocalDevice.getInstance();
//            ReadPropertyAck ack = (ReadPropertyAck) localDevice.send(remoteDevice, new ReadPropertyRequest(remoteDevice.getObjectIdentifier(), PropertyIdentifier.modelName));
//            return ack.getValue().toString();
//        } catch (Exception e1) {
//            e1.printStackTrace();
//            return null;
//        }
//    }

    public AcknowledgementService privateTransferReceivedComplex(UnsignedInteger vendorId, UnsignedInteger serviceNumber, Encodable serviceParameters, Address address) {
        if (vendorId.intValue() != 900) {
            return null;
        }
        Sequence parms = (Sequence) serviceParameters;
        int blockSize = ((UnsignedInteger) parms.getValues().get(Draper.GET_FRIMEBLOCK_BLOCK_SZIE)).intValue();
        int startOffset = ((UnsignedInteger) parms.getValues().get(Draper.GET_FRIMEBLOCK_START_OFFSET)).intValue();
        switch (serviceNumber.intValue()) {
            case Draper.GETFRAME_CONF_SERSUM:
                SequenceOf<Primitive> list = new SequenceOf<>();
                list.add(new UnsignedInteger(startOffset));
                byte[] buffer = new byte[blockSize];
                if (startOffset + blockSize > fileTmp.length) {
                    System.arraycopy(fileTmp, startOffset, buffer, 0, fileTmp.length - startOffset);
                } else {
                    System.arraycopy(fileTmp, startOffset, buffer, 0, blockSize);
                }

                list.add(new OctetString(buffer));
                System.out.println("---------------ConfirmedPrivateTransferAck");
                return new ConfirmedPrivateTransferAck(vendorId, serviceNumber, list);
        }
        return null;
    }

    /**
     * 升级进度
     *
     * @return
     */
    public int getPercent() {
        return percent;
    }
}
