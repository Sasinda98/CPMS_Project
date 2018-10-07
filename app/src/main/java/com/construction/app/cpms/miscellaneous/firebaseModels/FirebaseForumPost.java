package com.construction.app.cpms.miscellaneous.firebaseModels;

public class FirebaseForumPost {

    private String postId;
    private String title;
    private String body;
    private String postedByUID;
    private String dateTime;

    public FirebaseForumPost(String postId, String title, String body, String postedByUID, String dateTime) {
        this.postId = postId;
        this.title = title;
        this.body = body;
        this.postedByUID = postedByUID;
        this.dateTime = dateTime;
    }

    public FirebaseForumPost() {
        //firebase requirement
    }



    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public String getPostedByUID() {
        return postedByUID;
    }

    public void setPostedByUID(String postedByUID) {
        this.postedByUID = postedByUID;
    }



}
