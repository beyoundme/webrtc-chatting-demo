package com.beyoundme;

import com.alibaba.fastjson.JSONObject;
import com.beyoundme.bean.Screen;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author 99514925@qq.com
 * @Create 2020/2/3 23:03
 **/
@ServerEndpoint("/ws")
@Component
public class ServerDemo {

    private  static Map<String, Session> clients = new ConcurrentHashMap<String, Session>();
    private  Set<Screen> screenSet = new HashSet<>();
    public   Set<String> getClinets() {
        return clients.keySet();
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("new connection ==="+session.getQueryString());
        clients.put(session.getQueryString(),session);
        String msg = "online:"+session.getQueryString();
        clients.values().forEach(bean->{
            try {
                bean.getBasicRemote().sendText(msg);
            }catch (Exception e){}

        });

    }

    @OnClose
    public void onClose(Session session) {
        String value = session.getQueryString();
        clients.remove(value);
        screenSet.stream().forEach(bean ->{
            if(value.equals(bean.getSipId())||value.equals(bean.getOnwer())){
                bean.setSipId("");
                bean.setOper("");
            }
        });
        String msg = "offline:"+value;
        clients.values().forEach(bean->{
            try {
                bean.getBasicRemote().sendText(msg);
            }catch (Exception e){}
        });
        System.out.println("close connection ===" + value);
    }

    @OnMessage
    public void OnMessage(Session session,String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        Session toIp  = null;
        try {
            toIp  = clients.get(jsonObject.getString("toIp"));
        }catch (Exception e){
        }
        if(toIp==null){
            System.out.println("目标不存在");
        }else {
            jsonObject.getJSONObject("data").put("fromIp",jsonObject.getString("fromIp"));
            try {
                toIp.getBasicRemote().sendText(jsonObject.getString("data"));
            }catch (Exception e){}

        }
        System.out.println(jsonObject.toJSONString());
    }

    @OnError
    public void OnError(Session session,Throwable  error) {
       error.printStackTrace();
    }

    public boolean isOnline(String user){
        if( clients.containsKey(user)){
            return true;
        }
        return false;
    }


    public void sendMessage(String user,String meesage){
        Session toIp  = null;
        try {
            toIp  = clients.get(user);
        }catch (Exception e){
        }
        if(toIp==null){
            System.out.println("目标不存在");
        }else {
            try {
                toIp.getBasicRemote().sendText(meesage);
            }catch (Exception e){}
        }
    }

    public Screen findCallScreen(Screen screen,String user){
        try {
            return screenSet.stream().filter(bean -> screen.getSipId().equals(bean.getOnwer()) && user.equals(bean.getSipId()) && "call".equals(bean.getOper())).findFirst().get().clone();
        }catch (Exception e){

        }
        return null;
    }

    public List<Screen> findOwnerScreen(String user){
        try {
            List<Screen> screens = screenSet.stream().filter(bean ->user.equals(bean.getOnwer())).collect(Collectors.toList());
            List<Screen> tempList = new ArrayList<>();
            screens.forEach(bean ->{
                tempList.add(bean.clone());
            });
            return tempList;
        }catch (Exception e){

        }
        return null;
    }

    public void addScreens(Collection<Screen> screens){
        try {
            screens.forEach(bean ->addScreen(bean));
        }catch (Exception e){

        }
    }

    public void addScreen(Screen screen){
        try {
            if(screenSet.contains(screen)){
                screenSet.remove(screen);
            }
            screenSet.add(screen);
        }catch (Exception e){
        }
    }

    public static void main(String[] args) {
        Screen screen = new Screen(1);
        screen.setOnwer("user");
        screen.setSipId("1111");
        Set<Screen> screens = new HashSet<>();
        screens.add(screen);
        Screen screen1 = new Screen(1);
        screen1.setOnwer("user");
        screen1.setSipId("222");
        if(screens.contains(screen1)){
            screens.remove(screen1);
            screens.add(screen1);
        }
        System.out.println("");
    }

    public long getRelatedNums(String user){
        try {
            return screenSet.stream().filter(bean ->user.equals(bean.getOnwer())&&!bean.getSipId().equals("")).count();
        }catch (Exception e){

        }
        return 0;
    }


    public void setScreenDiableFalse(String onwer, int index) {
        int nums = 16;
        switch (index){
            case 2:
                nums = 4;
                break;
            case 5:
                nums = 9;
                break;
            case 10:
                nums = 16;
                break;
            default:
                break;
        }
        final int num = nums;
        screenSet.stream().forEach(bean ->{
            if(bean.getOnwer().equals(onwer) && bean.getIndex()<=num){
                bean.setDiable(false);
            }
        });
    }
}
