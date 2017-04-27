package com.spring.controller.api;

import com.spring.bean.DraperEntity;
import com.spring.service.DraperService;
import com.spring.utils.ComPortutils;
import com.spring.utils.Draper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/3/20.
 */
@Controller()
@RequestMapping(value = "/v1/draper")
public class DraperController {

    @Resource(name = "draperService")
    public DraperService service;

    /**
     * 获取电机
     * @return
     */
    @RequestMapping(value = "/drapers",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public List<Integer> getDrapers(){
        return service.getDrapers();
    }

    /**
     * 电机操作
     * @param draperEntity
     */
    @RequestMapping(value = "/operation",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void operation(DraperEntity draperEntity){
        service.operation(draperEntity);
    }
}
