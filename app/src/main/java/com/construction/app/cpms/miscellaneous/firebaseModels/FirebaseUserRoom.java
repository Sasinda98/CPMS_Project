package com.construction.app.cpms.miscellaneous.firebaseModels;

//This is a model class representing  one of Firebase node structures, specifically room details based on user......
public class FirebaseUserRoom {

    private String UID;
    /*Type,name and photoUrl removed*/
    private String lastRead;

    public FirebaseUserRoom() { //For firebase specifically...

    }

    public FirebaseUserRoom(String UID) {
        this.UID = UID;
    }

    public String getLastRead() {
        return lastRead;
    }

    public void setLastRead(String lastRead) {
        this.lastRead = lastRead;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }





}
