package com.beyoundme.bean;

import java.util.Objects;

public class Screen {

    /**
     * user
     */
    String onwer="";
    /**
     * 1-16
     */
    int index = -1;

    /**
     * 当前屏幕用户对象
     */
    String sipId = "";

    /**
     * call / play
     */
    String oper = "";

    Boolean diable = true;


    public String getOnwer() {
        return onwer;
    }

    public void setOnwer(String onwer) {
        this.onwer = onwer;
    }

    public Screen(int index) {
        this.index = index;
    }


    public Boolean getDiable() {
        return diable;
    }

    public void setDiable(Boolean diable) {
        this.diable = diable;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSipId() {
        return sipId;
    }

    public void setSipId(String sipId) {
        this.sipId = sipId;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Screen screen = (Screen) o;
        return index == screen.index &&
                Objects.equals(onwer, screen.onwer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(onwer, index);
    }

    @Override
    public Screen clone(){
        Screen screen = new Screen(index);
        screen.setOper(oper);
        screen.setSipId(sipId);
        screen.setDiable(diable);
        screen.setOnwer(onwer);
        return screen;
    }
}
