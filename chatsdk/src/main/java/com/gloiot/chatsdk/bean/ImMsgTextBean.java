package com.gloiot.chatsdk.bean;

/**
 * Created by jinzlin on 17/2/27.
 * 文字消息
 */

public class ImMsgTextBean extends ImMsgBean {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public ImMsgTextBean typeCast(ImMsgBean imMsgBean) {
        ImMsgTextBean imMsgTextBean = new ImMsgTextBean();
        imMsgTextBean.setId(imMsgBean.getId());
        imMsgTextBean.setMsgid(imMsgBean.getMsgid());
        imMsgTextBean.setSendid(imMsgBean.getSendid());
        imMsgTextBean.setReceiveid(imMsgBean.getReceiveid());
        imMsgTextBean.setSessiontype(imMsgBean.getSessiontype());
        imMsgTextBean.setMsgtype(imMsgBean.getMsgtype());
        imMsgTextBean.setText(imMsgBean.getMessage());
        imMsgTextBean.setPushdata(imMsgBean.getPushdata());
        imMsgTextBean.setSendTime(imMsgBean.getSendTime());
        imMsgTextBean.setMessageState(imMsgBean.getMessageState());
        imMsgTextBean.setExtra(imMsgBean.getExtra());

        return imMsgTextBean;
    }
}
