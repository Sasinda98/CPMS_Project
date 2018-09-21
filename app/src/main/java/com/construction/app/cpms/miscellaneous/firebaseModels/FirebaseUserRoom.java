package com.construction.app.cpms.miscellaneous.firebaseModels;

//This is a model class representing  one of Firebase node structures, specifically room details based on user......
public class FirebaseUserRoom {

    private String UID;
    private String name;
    private String type;
    private String photoUrl;
    private String lastRead;

    public FirebaseUserRoom() { //For firebase specifically...

    }

    public FirebaseUserRoom(String name, String type, String photoUrl, String UID) {
        this.name = name;
        this.type = type;
        this.photoUrl = photoUrl;
        this.UID = UID;
    }

    public String getLastRead() {
        return lastRead;
    }

    public void setLastRead(String lastRead) {
        this.lastRead = lastRead;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }





}
