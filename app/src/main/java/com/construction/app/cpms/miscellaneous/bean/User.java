package com.construction.app.cpms.miscellaneous.bean;

public class User {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    private String userId;
    private String firebaseId;
    private String fName;
    private String lName;
    private String email;
    private String picUrl; //profile pic url from firebase
    private String type;

    public User(String userId, String firebaseId, String fName, String lName, String email, String picUrl, String type) {
        this.userId = userId;
        this.firebaseId = firebaseId;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.picUrl = picUrl;
        this.type = type;
    }
}
