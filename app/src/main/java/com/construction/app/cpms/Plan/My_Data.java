package com.construction.app.cpms.Plan;

public class My_Data {
    private int pid;
    private String name;
    private String decript;

    public My_Data(int pid, String name, String decript){
        this.pid = pid;
        this.name = name;
        this.decript = decript;
    }

    public int getPID() {
        return pid;
    }

    public void setPID(int pid) {
        this.pid = pid;
    }

    public String getNAME() {
        return name;
    }

    public void setNAME(String name) {
        this.name = name;
    }

    public String getDECRIPT() {
        return decript;
    }

    public void setDECRIPT(String decript) {
        this.decript = decript;
    }

}
