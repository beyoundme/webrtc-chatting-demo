package com.beyoundme.service;


import com.beyoundme.bean.TreeOptions;

import java.util.List;

/**
 * @Author:lx
 * @Date:2021/4/6
 * @Description: 用户相关操作service
 **/
public interface UserService {

    /**
     * checkout if the pass is right
     * @return
     */
    boolean checkPass(String user,String pass);

    String findUrlByDeviceName(String user);


    List<TreeOptions> getAllResouces(String dataType, String userName);

}
