package com.construction.app.cpms.miscellaneous.firebaseModels;

//class used in ChatRoom class which is a firebase model...
public class FirebaseLastRead {
    public FirebaseLastRead(String lastRead) {
        this.lastRead = lastRead;
    }


    public FirebaseLastRead() {
        //firebase requirement
    }


    public String getLastRead() {
        return lastRead;
    }

    public void setLastRead(String lastRead) {
        this.lastRead = lastRead;
    }

    private String lastRead;


}
