package com.construction.app.cpms.miscellaneous.bean;

public class ChatRoomMainItem {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(String deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }



    private String name;
    private String timeStamp;
    private String deliverStatus;
    private String role;
    private String lastMessage;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String uid;

    public ChatRoomMainItem(String name, String timeStamp, String deliverStatus, String role, String lastMessage) {
        this.name = name;
        this.timeStamp = timeStamp;
        this.deliverStatus = deliverStatus;
        this.role = role;
        this.lastMessage = lastMessage;
    }
}
