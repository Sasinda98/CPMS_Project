package com.construction.app.cpms.inventoryManagement;

public class inventory_item_Bean {

    private String itemName;
    private String itemQuantity;
    private String category;
    private String unit;
    private int imageID;



    public inventory_item_Bean(String itemName, String itemQuantity, String category, String unit, int imageID) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.category = category;
        this.unit = unit;
        this.imageID = imageID;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}

