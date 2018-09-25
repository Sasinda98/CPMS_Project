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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    private String userId;  //needed
    private String firebaseId;  //needed
    //private String fName;
   // private String lName;
   // private String email;
    //private String picUrl; //profile pic url from firebase
    private String type;    //needed

    public User(String userId, String firebaseId, String type) {
        this.userId = userId;
        this.firebaseId = firebaseId;
        this.type = type;
    }
}
