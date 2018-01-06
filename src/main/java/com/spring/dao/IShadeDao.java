package com.spring.dao;

import com.spring.bean.ShadeEntity;

import java.util.List;

public interface IShadeDao {

    /**
     * 通过ID查询电机单台电机
     *
     * @param id
     * @return
     */
    ShadeEntity queryById(Integer id);

    /**
     * 查询所有
     *
     * @return
     */
    List<ShadeEntity> queryAll();
}
