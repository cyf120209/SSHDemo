package com.spring.controller;

import com.spring.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by lenovo on 2017/3/13.
 */
@Controller
public class ViewController {

    @Resource(name = "userService")
    private UserService service;

    @RequestMapping(value = "login",method = RequestMethod.GET)
    public ModelAndView test(){
        return new ModelAndView("login");
    }

    @RequestMapping(value = "view",method = RequestMethod.GET)
    public ModelAndView view(){
        ModelAndView modelAndView = new ModelAndView("pages/index");
        modelAndView.addObject("message","cyf");
        modelAndView.addObject("user","sdfg");
        String message="asdf";
        ModelAndView view = new ModelAndView("pages/index", "message", message);
        return view;
    }

    @RequestMapping(value = "addUser1")
    public ModelAndView getValue(String userName, String password){
        ModelAndView modelAndView = new ModelAndView("pages/index");
        modelAndView.addObject("message",userName);
        modelAndView.addObject("user",password);
        return  new ModelAndView("pages/index","message",userName);
    }

    @RequestMapping(value = "count")
    public ModelAndView getCount(){
        int count=service.userCount();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message",count);
        modelAndView.setViewName("pages/index");
        return modelAndView;
    }
}
