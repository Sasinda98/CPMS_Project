package com.construction.app.cpms.miscellaneous.bean;

public class Refresh {
    public static boolean isIsRefreshNeeded() {
        return isRefreshNeeded;
    }

    public static void setIsRefreshNeeded(boolean isRefreshNeeded) {
        Refresh.isRefreshNeeded = isRefreshNeeded;
    }

    private static boolean isRefreshNeeded;

}
