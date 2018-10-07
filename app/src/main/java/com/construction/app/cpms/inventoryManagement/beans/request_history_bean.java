package com.construction.app.cpms.inventoryManagement.beans;

public class request_history_bean {

    private int reqID;
    private int subConID;
    private int itemID;
    private double reqQty;
    private String reqDate;
    private String valDate;
    private String message;
    private String itemName;
    private String subConFName;
    private String subConLname;
    private String itemUnit;
    private String reqStatus;

    public request_history_bean(int reqID, int subConID, int itemID, double reqQty, String reqDate, String valDate, String message, String itemName, String subConFName, String subConLname, String itemUnit, String reqStatus) {
        this.reqID = reqID;
        this.subConID = subConID;
        this.itemID = itemID;
        this.reqQty = reqQty;
        this.reqDate = reqDate;
        this.valDate = valDate;
        this.message = message;
        this.itemName = itemName;
        this.subConFName = subConFName;
        this.subConLname = subConLname;
        this.reqStatus = reqStatus;
        this.itemUnit = itemUnit;
    }



    public int getReqID() {
        return reqID;
    }

    public void setReqID(int reqID) {
        this.reqID = reqID;
    }

    public int getSubConID() {
        return subConID;
    }

    public void setSubConID(int subConID) {
        this.subConID = subConID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public double getReqQty() {
        return reqQty;
    }

    public void setReqQty(double reqQty) {
        this.reqQty = reqQty;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getValDate() {
        return valDate;
    }

    public void setValDate(String valDate) {
        this.valDate = valDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSubConFName() {
        return subConFName;
    }

    public void setSubConFName(String subConFName) {
        this.subConFName = subConFName;
    }

    public String getSubConLname() {
        return subConLname;
    }

    public void setSubConLname(String subConLname) {
        this.subConLname = subConLname;
    }


    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }


}
