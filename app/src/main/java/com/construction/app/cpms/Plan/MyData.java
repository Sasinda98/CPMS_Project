package com.construction.app.cpms.Plan;

public class MyData {
    private int pid;
    private String name, image_link;


    public MyData(int pid, String name, String image_link) {
        this.pid = pid;
        this.name = name;
        this.image_link = image_link;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }
}
