package com.construction.app.cpms.miscellaneous.firebaseModels;

public class FirebaseUserDetails {
    public FirebaseUserDetails(String name, String type, String photoUrl, String UID) {
        this.name = name;
        this.type = type;
        this.photoUrl = photoUrl;
        this.UID = UID;
    }

    public FirebaseUserDetails() {
        //firebase requirement to have this
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

    private String name;
    private String type;
    private String photoUrl;

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    private String UID;
}
