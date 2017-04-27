package com.spring.service;

import com.spring.dao.UserDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by lenovo on 2017/3/14.
 */
//@Service("userService")
public class UserService {

//    @Resource
    private UserDAO userDao;

//    @Transactional
    public int userCount(){
        return userDao.getAllUser().size();
    }

    public UserDAO getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

}
