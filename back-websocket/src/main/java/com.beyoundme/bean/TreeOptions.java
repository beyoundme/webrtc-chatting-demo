package com.beyoundme.bean;


import java.util.ArrayList;
import java.util.List;

/**
 * @Author:lx
 * @Date:2021/4/6
 * @Description: 树的封装属性
 **/
public class TreeOptions {

    int id;
    String uuid="";
    String curenetId;
    String parentId;
    String type = "org";
    String label;
    List<TreeOptions> children;
    String status = "0";

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void add(TreeOptions node) {
        if(children == null){
            children = new ArrayList<>();
        }
        children.add(node);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurenetId() {
        return curenetId;
    }

    public void setCurenetId(String curenetId) {
        this.curenetId = curenetId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TreeOptions> getChildren() {
        return children;
    }

    public void setChildren(List<TreeOptions> children) {
        this.children = children;
    }
}
