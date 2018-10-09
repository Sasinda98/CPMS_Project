package com.construction.app.cpms.miscellaneous.firebaseModels;

public class FirebaseComment {

    private String comment;
    private String getPostedByUID;
    private String timeStamp;
    private String commentID;
    private String postID;

    public FirebaseComment() {
        //firebase Requirement
    }

    public FirebaseComment(String comment, String getPostedByUID, String timeStamp, String commentID, String postID) {
        this.comment = comment;
        this.getPostedByUID = getPostedByUID;
        this.timeStamp = timeStamp;
        this.commentID = commentID;
        this.postID = postID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGetPostedByUID() {
        return getPostedByUID;
    }

    public void setGetPostedByUID(String getPostedByUID) {
        this.getPostedByUID = getPostedByUID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }
}
