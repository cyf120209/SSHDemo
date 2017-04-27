package com.spring.controller.api;

import com.spring.bean.FirmWareEntity;
import com.spring.bean.StateEntity;
import com.spring.service.UpgradeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lenovo on 2017/3/24.
 */
@Controller
@RequestMapping(value = "/v1/upgrade")
public class UpgradeController {

    @Resource(name = "upgradeService")
    public UpgradeService upgradeService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    public @ResponseBody
    FirmWareEntity uploadFile(HttpServletRequest request) throws IOException {
        return upgradeService.uploadFile(request);
    }

    /**
     * 固件升级
     * @param draperID -1 代表升级所有
     * @return
     */
    @RequestMapping(value = "/upgrade",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public boolean upgrade(int draperID){
        return upgradeService.upgradeDraper(draperID);
    }

    /**
     * 获取版本号
     * @param draperID
     * @return
     */
    @RequestMapping(value = "/version/{draperID}" ,method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<String> readVersion(@PathVariable("draperID") int draperID){
        return upgradeService.readVersion(draperID);
    }

    @RequestMapping(value = "/percent",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public @ResponseBody
    Integer getPercent(){
        return upgradeService.getPercent();
    }
}
