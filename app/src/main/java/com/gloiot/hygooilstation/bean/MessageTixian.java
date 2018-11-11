package com.gloiot.hygooilstation.bean;

/**
 * Created by dlt on 2016/12/2.
 * 提现消息
 */

public class MessageTixian {

    private String type;        //类型
    private String bank;        //银行
    private String tixianTime;  //提现时间
    private String daozhangTime; //到账时间
    private String money;       //金额
    private String extra;       //备注
    private String title;       //提现状态：到账/失败

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getTixianTime() {
        return tixianTime;
    }

    public void setTixianTime(String tixianTime) {
        this.tixianTime = tixianTime;
    }

    public String getDaozhangTime() {
        return daozhangTime;
    }

    public void setDaozhangTime(String daozhangTime) {
        this.daozhangTime = daozhangTime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
