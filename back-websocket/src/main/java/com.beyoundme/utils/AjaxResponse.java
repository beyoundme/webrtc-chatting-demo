package com.beyoundme.utils;


/**
 * @Author:lx
 * @Date:2021/4/6
 * @Description: 封装response
 **/
public class AjaxResponse<T> {


    private int code;
    private T data;
    private String msg;

    public AjaxResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}


