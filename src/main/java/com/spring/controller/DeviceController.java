package com.spring.controller;

import com.spring.bean.DeviceEntity;
import com.spring.bean.ShadeEntity;
import com.spring.service.DeviceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/v1/device")
public class DeviceController{

    @Resource(name = "deviceService")
    protected DeviceService deviceService;

    @RequestMapping(value = "/devices",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    public String getDeviceList(Model model, HttpServletRequest request){
        List<DeviceEntity> deviceList = deviceService.getDeviceList();
        model.addAttribute("deviceList",deviceList);
        return "pages/main/index";
    }

}
