package com.beyoundme.bean;


/**
 * @Author:lx
 * @Date:2021/4/6
 * @Description: 前端选择器的封装属性
 **/
public class Options {

    String value;
    String label;

    public Options(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
