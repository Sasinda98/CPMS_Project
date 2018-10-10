package com.construction.app.cpms.Plan;

public class ReportData {
    private int pid;
    private String name, image_link;
    private String status;

    public ReportData(int pid, String name, String image_link, String status) {
        this.pid = pid;
        this.name = name;
        this.image_link = image_link;
        this.status = status;
    }

    public int getPiD() {
        return pid;
    }

    public void setPiD(int pid) {
        this.pid = pid;
    }

    public String getNamE() {
        return name;
    }

    public void setNam(String name) {
        this.name = name;
    }

    public String getImage_linK() {
        return image_link;
    }

    public void setImage_linK(String image_link) {
        this.image_link = image_link;
    }

    public String getStatuS() {
        return status;
    }

    public void setStatuS(String status) {
        this.status = status;
    }
}
