package com.construction.app.cpms.miscellaneous.firebaseModels;

public class FirebaseMessage {

    private String body;
    private String timeStamp;
    private String sentBy;

    public FirebaseMessage() {
        //em[pty constructor, Required by firebase
    }

    public FirebaseMessage(String body, String timeStamp, String sentBy) {
        this.body = body;
        this.timeStamp = timeStamp;
        this.sentBy = sentBy;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }


}
