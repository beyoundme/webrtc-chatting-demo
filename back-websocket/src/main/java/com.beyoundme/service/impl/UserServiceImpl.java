package com.beyoundme.service.impl;

import com.beyoundme.ServerDemo;
import com.beyoundme.bean.Depart;
import com.beyoundme.bean.TreeOptions;
import com.beyoundme.service.UserService;
import com.beyoundme.utils.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author:lx
 * @Date:2021/4/6
 * @Description: 用户相关操作impl
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ServerDemo serverDemo;

    private static Map<String,String> users = new ConcurrentHashMap<>();

    private static Map<String,String> devices = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        System.out.println("-------------------LoadProperties start-------------------");
        Properties userProp = new Properties();
        Properties deviceProp = new Properties();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("users.txt")), StandardCharsets.UTF_8);
            userProp.load(inputStreamReader);
            userProp.stringPropertyNames().stream().forEach(bean ->{
                users.put(bean,userProp.getProperty(bean));
            });

            inputStreamReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("devices.txt")), StandardCharsets.UTF_8);
            deviceProp.load(inputStreamReader);
            deviceProp.stringPropertyNames().stream().forEach(bean ->{
                devices.put(bean,deviceProp.getProperty(bean));
            });

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println("-------------------LoadProperties end-------------------");
    }

    @Override
    public boolean checkPass(String user,String pass){
        if(users.containsKey(user) && pass.equals(users.get(user))){
            return true;
        }
        return false;
    }

    @Override
    public String findUrlByDeviceName(String user) {
        return devices.get(user);
    }

    @Override
    public List<TreeOptions> getAllResouces(String dataType, String userName) {
        if("resources".equals(dataType)){
            List<Depart> departInfos = new ArrayList<Depart>();
            departInfos.add(new Depart("111","资源",""));
            List<TreeOptions> orgsOptions = TreeUtil.getOrgsTree(departInfos);
            setChild(orgsOptions);
            return orgsOptions;
        }
        return null;
    }

    private void setChild(List<TreeOptions> orgsOptions) {
        for(TreeOptions bean:orgsOptions){

            List<TreeOptions> collectUser = users.keySet().stream().map(user ->{
                TreeOptions node = new TreeOptions();
                node.setCurenetId(user);
                node.setId(-1);
                node.setType("user");
                node.setParentId(bean.getCurenetId());
                node.setLabel(user);
                if(serverDemo.isOnline(user)){
                    node.setStatus("1");
                }
                return node;
            }).collect(Collectors.toList());
            collectUser.stream().forEach(user->{
                bean.add(user);
            });

            List<TreeOptions> collectDevice = devices.keySet().stream().map(user ->{
                TreeOptions node = new TreeOptions();
                node.setCurenetId(user);
                node.setId(-1);
                node.setType("device");
                node.setParentId(bean.getCurenetId());
                node.setLabel(user);
                node.setStatus("1");
                return node;
            }).collect(Collectors.toList());
            collectDevice.stream().forEach(user->{
                bean.add(user);
            });
        }
    }

}
