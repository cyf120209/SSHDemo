package com.spring.controller.api;

import com.spring.bean.ShadeEntity;
import com.spring.pagehelper.PageInfo;
import com.spring.service.ShadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/3/20.
 */
@Controller()
@RequestMapping(value = "/v1/shades")
public class ShadeController {


//@Autowired
    @Resource(name = "shadeService")
    public ShadeService service;

    /**
     * 获取电机
     * @return
     */
    @RequestMapping(value = "",produces = {"application/json;charset=UTF-8"})
    public String getShades(Model model, HttpServletRequest request,ShadeEntity shade,
                            @RequestParam(required = false) Integer pageNum){
        if(shade.getAttrCount()>0){
            model.addAttribute("attr",shade);
        }
        List<ShadeEntity> shadeList = service.getShadeList();
        PageInfo pageInfo = new PageInfo(shadeList,10);
        pageInfo.setPageNum((null==pageNum)?1:pageNum);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("shadeList",pageInfo.getList());
        return "pages/main/index";
    }
}
