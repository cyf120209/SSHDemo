package com.spring.controller.api;

import com.spring.bean.AddGroupEntity;
import com.spring.bean.DraperEntity;
import com.spring.bean.GroupEntity;
import com.spring.service.GroupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(value = "/v1/group")
public class GroupController {

    @Resource(name = "groupService")
    public GroupService service;

    @RequestMapping(value = "/map",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Map<Integer, Map<Integer, List<Integer>>> map(){
        return service.getMap();
    }

    @RequestMapping(value = "/operation",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void operation(GroupEntity groupEntity){
        service.operation(groupEntity);
    }

    @RequestMapping(value = "/add/{deviceID}/{groupID}",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void add(@PathVariable("deviceID") int deviceID, @PathVariable("groupID") int groupID, AddGroupEntity entity){
        service.add(deviceID,groupID,entity);
    }

    @RequestMapping(value = "/update/{groupID}",method = RequestMethod.PUT,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void update(@PathVariable("groupID") int groupID,String groupName){
        service.update(groupID,groupName);
    }

    @RequestMapping(value = "/delete/{deviceID}/{groupID}",method = RequestMethod.DELETE,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void delete(@PathVariable("deviceID") int deviceID,@PathVariable("groupID") int groupID){
        service.delete(deviceID,groupID);
    }

    @RequestMapping(value = "/delete/{deviceID}/{groupID}/{draperID}",method = RequestMethod.DELETE,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void delete(@PathVariable("deviceID") int deviceID,@PathVariable("groupID") int groupID,@PathVariable("draperID") int draperID){
        service.delete(deviceID,groupID,draperID);
    }
}
