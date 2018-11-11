package com.gloiot.hygooilstation.bean;

/**
 * Created by JinzLin on 2016/10/12.
 * 系统消息
 */

public class MessageSystem {

    private String type;        //类型
    private String text;        //文字
    private String extra;       //备注

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
