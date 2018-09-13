
// Used as tutorial: https://www.sitepoint.com/transfer-data-between-activities-with-android-parcelable/

package com.construction.app.cpms.inventoryManagement.beans;

import android.os.Parcel;
import android.os.Parcelable;
//Implements Parcelable to allow passing the object using intents
public class inventory_category_Bean implements Parcelable{
    private String name;
    private int imageID;

    public inventory_category_Bean(String name, int imageID) {
        this.name = name;
        this.imageID = imageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }







    public inventory_category_Bean(Parcel in) {
        name = in.readString();
        imageID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(imageID);
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    public static final Creator<inventory_category_Bean> CREATOR = new Creator<inventory_category_Bean>() {
        @Override
        public inventory_category_Bean createFromParcel(Parcel in) {
            return new inventory_category_Bean(in);
        }

        @Override
        public inventory_category_Bean[] newArray(int size) {
            return new inventory_category_Bean[size];
        }
    };



}
