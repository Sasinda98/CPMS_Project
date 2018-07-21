package com.example.gayalr.cpms;

import android.support.v4.app.Fragment;

public interface Navigation {
    //Refered to -: https://codelabs.developers.google.com/codelabs/mdc-101-java/#2
    void naviagateTo(Fragment fragment, boolean addToBackStack);

}
