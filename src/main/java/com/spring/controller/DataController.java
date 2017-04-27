package com.spring.controller;

import com.spring.bean.DataEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2017/3/15.
 */
@Controller
@EnableWebMvc
@RequestMapping(value = "/data/")
public class DataController {

    private Map<Integer,DataEntity> map=new HashMap<>();
    private DataEntity dataEntity;

    @RequestMapping(value = "data/{id}",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    public @ResponseBody DataEntity getData(@PathVariable int id){
//        DataEntity dataEntity = map.get(new Integer(id));
        return map.get(new Integer(id));
    }

    @RequestMapping(value = "data",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    public @ResponseBody DataEntity addData(@ModelAttribute DataEntity data){
        map.put(new Integer(map.size()),data);
        dataEntity = map.get(new Integer(0));
        return dataEntity;
    }

    @RequestMapping(value = "data/edit/{id}",method = RequestMethod.PUT,produces = {"application/json;charset=UTF-8"})
    public @ResponseBody DataEntity updateData(@PathVariable("id") int id,@ModelAttribute DataEntity data){
        map.put(new Integer(id),new DataEntity("update"));
        return map.get(new Integer(id));
    }

    @RequestMapping(value = "data/edit",method = RequestMethod.PUT,produces = {"application/json;charset=UTF-8"})
    public @ResponseBody DataEntity updateData(HttpServletRequest request, HttpServletResponse response,@ModelAttribute DataEntity data){
//        map.put(new Integer(id),new DataEntity("update"));
        return map.get(new Integer(0));
    }


//    @RequestMapping(value = "data/edit/{id}",method = RequestMethod.PUT,produces = {"application/json;charset=UTF-8"})
//    public @ResponseBody DataEntity updateData(@PathVariable("id") int id){
//        map.put(new Integer(id),new DataEntity("update"));
//        return map.get(new Integer(id));
//    }

    @RequestMapping(value = "data/delete/{id}",method = RequestMethod.DELETE,produces = {"application/json;charset=UTF-8"})
    public @ResponseBody DataEntity deleteData(@PathVariable("id") int id){
        map.put(new Integer(id),new DataEntity("delete"));
        return map.get(new Integer(id));
    }

//    @RequestMapping(value = "user",method = RequestMethod.POST,headers = "content-type=application/x-www-form-urlencoded",produces = {"application/json;charset=UTF-8"})
//    @ResponseBody
//    public String addUser(@ModelAttribute UserEntity user){
////        String name = entity.getData();
//        return "{name:"+user.getName()+"}";
//    }

//    @RequestMapping(value = "data",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
//    @ResponseBody
//    public String addData(@ModelAttribute DataEntity data){
////        String name = entity.getData();
//        return "{name:"+data.getData()+"}";
//    }


}
