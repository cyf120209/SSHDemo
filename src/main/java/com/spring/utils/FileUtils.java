package com.spring.utils;

import com.spring.bean.FirmWareEntity;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2017/4/7.
 */
public class FileUtils {

    FirmWareEntity firmWareEntity;

    /**
     * 文件二进制流
     */
    private byte[] fileTmp;

    public FirmWareEntity uploadFile(HttpServletRequest request) throws IOException {
        String upload="";
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if(multipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iterator = multiRequest.getFileNames();
            while (iterator.hasNext()){
                MultipartFile file = multiRequest.getFile(iterator.next().toString());
                if (file!=null){
//                    upload = request.getSession().getServletContext().getRealPath("upload");
//                    File dir = new File(upload);
//                    if(!dir.exists()){
//                        dir.mkdirs();
//                    }
//                    String path = upload + "/" + file.getOriginalFilename();
//                    File f = new File(path);
//                    file.transferTo(f);
                    CommonsMultipartFile cf = (CommonsMultipartFile) file;
                    DiskFileItem fi = (DiskFileItem)cf.getFileItem();
                    File updateFile = fi.getStoreLocation();
                    firmWareEntity = readFileTobuff(updateFile);
                    firmWareEntity.setTypeNum(checkFileType(updateFile));
                }
            }
            return firmWareEntity;
        }
        return firmWareEntity;
    }

    /**
     * 检测文件类型
     * @param file
     * @return
     */
    private int checkFileType(File file){
        int updateType=-1;
        try {
            FileInputStream fi=new FileInputStream(file);
            // fi.skip(0);
            byte[] tmp=new byte[8];
            fi.read(tmp,0,8);
//            int startAddress=(tmp[6]&0xffffff)*256+tmp[5]&0xffffff;
            int startAddress=(int)(tmp[6]&0xff)*256+(int)(tmp[5]&0xff);

            if ( startAddress<0x0080){
                updateType=0;
            }else if(startAddress<0x0240){
                updateType=1;
            }else{
                updateType=2;
            }
            fi.close();
//            if (tmp[1]==122&&tmp[3]==0&&tmp[4]==32){
//                return  true;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateType;
    }

    /**
     * 获取固件的type，版本号，及固件是否损坏
     * @param framefile
     * @return
     */
    private FirmWareEntity readFileTobuff(File framefile){
        try {
            fileTmp = new byte[(int) framefile.length()];
            FileInputStream fi = new FileInputStream(framefile);
            int offset=0,numRead=0;
            while (offset < fileTmp.length
                    && (numRead = fi.read(fileTmp, offset, fileTmp.length - offset)) >= 0) {
                offset += numRead;
            }
            fi.close();
            String s = new String(fileTmp);
            boolean right=false;
            String name = getString(s, "--\\{DARPER\\sFIREWARE\\}--");
//            String type = getString(s, "\\[[a-zA-Z]{2}-[a-zA-Z]{2}-[0-9]{2}\\]");
            String type = getString(s, "--\\[[A-Za-z0-9-/]*\\]--");
            type=type.substring(3,type.length()-3);
            String version = getString(s, "--\\*v[0-9]+.[0-9]+.[0-9]+\\*--");
            version=version.substring(4,version.length()-3);
            String[] split = version.split("\\.");
            if("--{DARPER FIREWARE}--".equals(name) && !"".equals(type) && !"".equals(version)){
                right=true;
            }
            if(right){
                FirmWareEntity.Builder builder = new FirmWareEntity.Builder();
                builder.setCorrupt(true)
                        .setType(type)
                        .setMajorNum(Integer.valueOf(split[0]))
                        .setMinorNum(Integer.valueOf(split[1]))
                        .setPatchNum(Integer.valueOf(split[2]));
                return builder.create();
//                mUpdateView.updateVersionAndType(type,split[0],split[1].trim(),split[2]);
            }
            return new FirmWareEntity.Builder().setCorrupt(false).create();
        } catch (Exception e1) {
//            mUpdateView.showError("固件损坏");
            e1.printStackTrace();
            return new FirmWareEntity.Builder().setCorrupt(false).create();
        }
    }

    /**
     * 匹配正则表达式
     * @param str
     * @param regx
     * @return
     */
    private String getString(String str, String regx) {
        //1.将正在表达式封装成对象Patten 类来实现
        Pattern pattern = Pattern.compile(regx);
        //2.将字符串和正则表达式相关联
        Matcher matcher = pattern.matcher(str);
        //3.String 对象中的matches 方法就是通过这个Matcher和pattern来实现的。
        System.out.println(matcher.matches());
        String group="";
        //查找符合规则的子串
        while (matcher.find()) {
            //获取 字符串
            group = matcher.group();
            //获取的字符串的首位置和末位置
//            System.out.println(matcher.start() + "--" + matcher.end());
        }
        return group;
    }

    public byte[] getFileTmp() {
        return fileTmp;
    }
}
