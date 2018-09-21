package com.construction.app.cpms.miscellaneous.bean;

public class ChatRoomIDGenerator {

    //generates id only in oneway regardless of the order in which they are passed
    public static String getChatRoomID(String UID1, String UID2) {
        if (UID1.compareTo(UID2) > 0) {
            return UID1 + "-" + UID2;
        } else if (UID1.compareTo(UID2) < 0) {
            return UID2 + "-" + UID1;
        } else {
            return "Error Occured, Same UID, Same value cannot compare";
        }
    }

    //removes logged in UID from the UID Combination to reveal the user that's gonna receive the message
    public static String getReceiverUserUID(String receivedUIDCombination, String loggedInUID){

        receivedUIDCombination = receivedUIDCombination.replace(loggedInUID, "").replace("-", "");

        return receivedUIDCombination;
    }

}
