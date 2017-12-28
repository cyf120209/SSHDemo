package com.spring.controller.api;

import com.spring.bean.ShadeEntity;
import com.spring.pagehelper.PageInfo;
import com.spring.service.ShadeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by lenovo on 2017/3/20.
 */
@Controller()
@RequestMapping(value = "/v1/shades")
public class ShadeController {

    @Resource(name = "shadeService")
    public ShadeService service;

    /**
     * 获取电机
     * @return
     */
    @RequestMapping(value = "",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    public String getShades(Model model, HttpServletRequest request,
                            @RequestParam(required = false) Integer pageStartIndex){
        List<ShadeEntity> shadeList = service.getShadeList();
        PageInfo pageInfo = new PageInfo(shadeList);
        int pageNum = (pageStartIndex == null) ? 1 : pageStartIndex;
        pageInfo.setPageNum(pageNum);
        int endIndex = pageNum * pageInfo.getPageSize();
        if(endIndex>shadeList.size()){
            endIndex=shadeList.size()-1;
        }
        shadeList=shadeList.subList((pageNum-1)*pageInfo.getPageSize(), endIndex);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("shadeList",shadeList);
        return "pages/main/index";
    }
}
