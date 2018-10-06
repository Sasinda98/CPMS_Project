package com.construction.app.cpms.miscellaneous.firebaseModels;

public class ChatRoom {
    public FirebaseUserRoom getUser1() {
        return user1;
    }

    public void setUser1(FirebaseUserRoom user1) {
        this.user1 = user1;
    }

    public FirebaseUserRoom getUser2() {
        return user2;
    }

    public void setUser2(FirebaseUserRoom user2) {
        this.user2 = user2;
    }

    public ChatRoom(FirebaseUserRoom user1, FirebaseUserRoom user2, FirebaseLastRead lastRead) {
        this.user1 = user1;
        this.user2 = user2;
        this.lastRead = lastRead;
    }

    public ChatRoom() {

    }

    private FirebaseUserRoom user1;
    private FirebaseUserRoom user2;

    public FirebaseLastRead getLastRead() {
        return lastRead;
    }

    public void setLastRead(FirebaseLastRead lastRead) {
        this.lastRead = lastRead;
    }

    private FirebaseLastRead lastRead;




}
