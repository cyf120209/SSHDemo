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


    /**
     * 文件二进制流
     */
    private byte[] fileTmp;

    public void uploadFile(HttpServletRequest request) throws IOException {
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
                }
            }
        }
    }

    public byte[] getFileTmp() {
        return fileTmp;
    }
}
