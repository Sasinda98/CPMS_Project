package com.example.gayalr.cpms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class mainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(savedInstanceState == null){ //Checking for savedinstances, to prevent creation of more than one fragment.
            loginFragment loginFragment = new loginFragment();  //instantiation of login fragment.
            getSupportFragmentManager().beginTransaction().add(R.id.container, loginFragment).commit(); //adding loginFragment to container
        }


    }
}
