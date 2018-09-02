package com.construction.app.cpms.miscellaneous.bean;

//form represents class in java, that could hold a single forum database record.
public class ForumPost {

    private String forumId;
    private String title;
    private String postedBy;
    private String body;


    public ForumPost(String forumId, String title, String postedBy, String body) {
        this.forumId = forumId;
        this.title = title;
        this.postedBy = postedBy;
        this.body = body;
    }

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
