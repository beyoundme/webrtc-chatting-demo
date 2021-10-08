package com.beyoundme.controller;

import com.alibaba.fastjson.JSONObject;
import com.beyoundme.ServerDemo;
import com.beyoundme.bean.Screen;
import com.beyoundme.bean.TreeOptions;
import com.beyoundme.service.UserService;
import com.beyoundme.utils.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author:lx
 * @Date:2021/4/6
 * @Description: 组织机构相关操作api
 **/
@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class BussinessController {

    @Autowired
    UserService userService;


    @Autowired
    ServerDemo serverDemo;

    /**
     * 登陆
     * @param user
     * @return
     */
    @RequestMapping("/get_login")
    public AjaxResponse checkLogin(String user,String pass){
        AjaxResponse response = new AjaxResponse();
        if(userService.checkPass(user,pass)){
            response.setCode(200);
            response.setMsg("登录成功!!!");
            revert(user);
        }else {
            response.setCode(201);
            response.setMsg("账号或密码有误!!!");
        }
        return response;
    }

    /**
     *
     * @param screen
     * @return
     */
    @RequestMapping("/changescreen")
    public AjaxResponse changeScreen(String screen,String user){
        AjaxResponse response = new AjaxResponse();
        List<Screen> screens = null;
        try {
            screens = serverDemo.findOwnerScreen(user);
            int index = Integer.valueOf(screen);
            int nums = index*index;
            screens.stream().forEach(bean ->{
                if(bean.getIndex()<nums+1){
                    bean.setDiable(false);
                }else {
                    bean.setDiable(true);
                }
            });
            serverDemo.addScreens(screens);
            response.setCode(200);
            response.setMsg("操作成功!!!");
            screens.clear();
        }catch (Exception e){
            response.setCode(201);
            response.setMsg("服务器响应失败，请稍后重试或重新登录!!!");
        }finally {
            if(!screens.isEmpty()){
                screens.clear();
            }
        }
        return response;
    }

    /**
     * @return
     */
    @RequestMapping("/getscreen")
    public AjaxResponse getScreen(String user){
        AjaxResponse response = new AjaxResponse();
        List<Screen> screens = null;
        try {
            screens = serverDemo.findOwnerScreen(user);
            if(!screens.isEmpty()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("screens",screens);
                jsonObject.put("nums",screens.stream().filter(bean ->!bean.getDiable()).count());
                response.setData(jsonObject);
                response.setCode(200);
                response.setMsg("获取屏幕信息成功!!!");
                screens.clear();
            }
        }catch (Exception e){
            response.setCode(201);
            response.setMsg("服务器响应失败，请稍后重试或重新登录!!!");
        }finally {
            if(!screens.isEmpty()){
                screens.clear();
            }
        }
        return response;
    }

    /**
     * @return
     */
    @RequestMapping("/getRelated")
    public AjaxResponse getRelated(String user){
        AjaxResponse response = new AjaxResponse();
        try {
            response.setData(serverDemo.getRelatedNums(user));
            response.setCode(200);
            response.setMsg("获取屏幕信息成功!!!");
        }catch (Exception e){
            response.setCode(201);
            response.setMsg("服务器响应失败，请稍后重试或重新登录!!!");
        }
        return response;
    }

    @RequestMapping("/getUrl")
    public AjaxResponse getUrl(String user){
        AjaxResponse response = new AjaxResponse();
        try {
            response.setData(userService.findUrlByDeviceName(user));
            response.setCode(200);
            response.setMsg("获取屏幕信息成功!!!");
        }catch (Exception e){
            response.setCode(201);
            response.setMsg("服务器响应失败，请稍后重试或重新登录!!!");
        }
        return response;
    }

    /**
     * @return
     */
    @RequestMapping("/play")
    public AjaxResponse play(String user,int index,String target,String oper,String type){
        AjaxResponse response = new AjaxResponse();
        List<Screen> screensUser = null;
        List<Screen> screensTarget = null;
        JSONObject data = new JSONObject();
        try {
            screensUser = serverDemo.findOwnerScreen(user);
            Collections.sort(screensUser, new Comparator<Screen>() {
                @Override
                public int compare(Screen o1, Screen o2) {
                    //String类型进行排序
                    return o1.getIndex()-o2.getIndex();
                }
            });
            if(index != -1){
                if(index>16){
                    response.setCode(201);
                    response.setMsg("己方已无可用屏幕，请取消业务后重试!!!");
                    return response;
                }
                Screen screen = screensUser.get(index-1);
                screen.setOper(oper);
                screen.setSipId(target);
                screen.setDiable(false);
                serverDemo.addScreen(screen);
                data.put("index",screen.getIndex());
                response.setData(data);
                response.setCode(200);
                response.setMsg("操作成功!!!");
                return response;
            }
            Optional<Screen> first = screensUser.stream().filter(bean -> user.equals(bean.getOnwer()) && target.equals(bean.getSipId())).findFirst();
            screensTarget = serverDemo.findOwnerScreen(target);
            Collections.sort(screensTarget, new Comparator<Screen>() {
                @Override
                public int compare(Screen o1, Screen o2) {
                    //String类型进行排序
                    return o1.getIndex()-o2.getIndex();
                }
            });
            if(first.isPresent()){
                Screen local = first.get();
                if(oper.equals(local.getOper())){
                    data.put("index",local.getIndex());
                    response.setData(data);
                    response.setCode(200);
                    response.setMsg("操作成功!!!");
                }else if("call".equals(oper)){
                    Optional<Screen> tar = screensTarget.stream().filter(bean -> StringUtils.isEmpty(bean.getSipId())).findFirst();
                    if(!tar.isPresent()){
                        response.setCode(201);
                        response.setMsg("对方已无可用屏幕，无法接听您的呼叫!!!");
                        return response;
                    }
                    local.setOper(oper);
                    local.setSipId(target);
                    local.setDiable(false);
                    serverDemo.addScreen(local);
                    data.put("index",local.getIndex());
                    response.setData(data);
                    response.setCode(200);
                    //占用目标一个窗口
                    tar.get().setOper(oper);
                    tar.get().setSipId(user);
                    tar.get().setDiable(false);
                    serverDemo.addScreen(tar.get());
                }else {
                    response.setCode(201);
                    response.setMsg("点播用户已被呼叫!!!");
                }
            }else {
                Optional<Screen> first1 = screensUser.stream().filter(bean -> StringUtils.isEmpty(bean.getSipId())).findFirst();
                if(!first1.isPresent()){
                    response.setCode(201);
                    response.setMsg("己方已无可用屏幕，请取消业务后重试!!!");
                    return response;
                }
                if("call".equals(oper)){
                    Optional<Screen> first2 = screensTarget.stream().filter(bean -> StringUtils.isEmpty(bean.getSipId())).findFirst();
                    if(!first2.isPresent()){
                        response.setCode(201);
                        response.setMsg("对方已无可用屏幕，无法接听您的呼叫!!!");
                        return response;
                    }
                    first2.get().setOper(oper);
                    first2.get().setSipId(user);
                    first2.get().setDiable(false);
                    if(first2.get().getIndex() == 2 || first2.get().getIndex() == 5 || first2.get().getIndex() == 10){
                        serverDemo.setScreenDiableFalse(first2.get().getOnwer(),first2.get().getIndex());
                    }
                    serverDemo.addScreen(first2.get());
                }
                first1.get().setOper(oper);
                first1.get().setSipId(target);
                first1.get().setDiable(false);
                serverDemo.addScreen(first1.get());
                data.put("index",first1.get().getIndex());
                response.setData(data);
                response.setCode(200);
                response.setMsg("操作成功!!!");
            }

        }catch (Exception e){
            response.setCode(201);
            response.setMsg("服务器响应失败，请稍后重试或重新登录!!!");
        }finally {
            if(!screensUser.isEmpty()){
                screensUser.clear();
            }
            if(!screensTarget.isEmpty()){
                screensTarget.clear();
            }
        }
        return response;
    }


    /**
     * @return
     */
    @RequestMapping("/stopPlay")
    public AjaxResponse stopPlay(String user,int index){
        AjaxResponse response = new AjaxResponse();
        List<Screen> screens = null;
        try {
            screens = serverDemo.findOwnerScreen(user);
            if(!screens.isEmpty()){
                Collections.sort(screens, new Comparator<Screen>() {
                    @Override
                    public int compare(Screen o1, Screen o2) {
                        //String类型进行排序
                        return o1.getIndex()-o2.getIndex();
                    }
                });
                if(index == -1){
                    screens.stream().forEach(beansl ->{
                        if("call".equals(beansl.getOper())){
                            try {
                                Screen screen1 = serverDemo.findCallScreen(beansl,user);
                                screen1.setSipId("");
                                screen1.setOper("");
                                serverDemo.addScreen(screen1);
                            }catch (Exception e){}

                        }
                        serverDemo.sendMessage(beansl.getSipId(),"stop"+beansl.getOper()+ ":"+user);
                        beansl.setOper("");
                        beansl.setSipId("");
                        serverDemo.addScreen(beansl);

                    });
                }else {
                    Screen screen = screens.get(index-1);
                    if("call".equals(screen.getOper())){
                        try {
                            Screen screen1 = serverDemo.findCallScreen(screen,user);
                            screen1.setSipId("");
                            screen1.setOper("");
                            serverDemo.addScreen(screen1);
                        }catch (Exception e){}
                    }
                    serverDemo.sendMessage(screen.getSipId(),"stop"+screen.getOper()+ ":"+user);
                    response.setData(screen.getOper());
                    screen.setOper("");
                    screen.setSipId("");
                    serverDemo.addScreen(screen);
                }
                response.setCode(200);
                response.setMsg("停止业务成功!!!");
            }
        }catch (Exception e){
            response.setCode(201);
            response.setMsg("服务器响应失败，请稍后重试或重新登录!!!");
        }finally {
            if(!screens.isEmpty()){
                screens.clear();
            }
        }
        return response;
    }

    /**
     * 获取组织机构列表
     * @return
     */
    @RequestMapping("/isOnline")
    public AjaxResponse isOnline(String sipId){
        AjaxResponse response = new AjaxResponse();
        Boolean online = serverDemo.getClinets().contains(sipId);
        if(online){
            response.setCode(200);
        }else {
            response.setCode(500);
        }
        return response;
    }

    /**
     * 获取组织机构列表
     * @return
     */
    @RequestMapping("/getOnlines")
    public AjaxResponse getOnline(){
        AjaxResponse response = new AjaxResponse();
        List<String> onlineUserIds = new ArrayList<>(serverDemo.getClinets());
        response.setCode(200);
        response.setData(onlineUserIds);
        return response;
    }


    /**
     * 获取组织机构列表
     * @return
     */
    @RequestMapping("/getResources")
    public AjaxResponse getResData(String dataType,String userName){
        AjaxResponse response = new AjaxResponse();
        try{
            JSONObject jsonObject = new JSONObject();
            List<TreeOptions> allResouces = userService.getAllResouces(dataType,userName);
            jsonObject.put("resources",allResouces);
            response.setData(jsonObject);
            response.setCode(200);
        }catch (Exception e){
            e.printStackTrace();
            response.setCode(201);
        }
        return response;
    }

    private void revert(String user) {
        Set<Screen> screens = new HashSet<>();
        for(int i = 1;i<17;i++){
            Screen sc = new Screen(i);
            sc.setOnwer(user);
            if(i<5){
                sc.setDiable(false);
            }
            screens.add(sc);
        }
        serverDemo.addScreens(screens);
    }


}
