package com.spring.controller.api;

import com.serotonin.bacnet4j.LocalDevice;
import com.spring.service.InitService;
import com.spring.utils.ComPortutils;
import com.spring.utils.MyLocalDevice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/3/20.
 */
@Controller
@RequestMapping(value = "/v1/init")
public class InitController {

    @Resource(name = "initService")
    private InitService service;

    /**
     * 获取端口
     * @return
     */
    @RequestMapping(value = "/ports",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public List<String> getPorts(){
        return ComPortutils.listPort();
    }

    /**
     * 初始化bacnet/ip
     * @param port 端口
     * @return
     */
    @RequestMapping(value = "/init",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public boolean init(String port){
        return service.init(port);
    }

}
