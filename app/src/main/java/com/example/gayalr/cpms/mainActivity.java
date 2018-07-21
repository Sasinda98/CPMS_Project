package com.example.gayalr.cpms;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gayalr.cpms.userManagement.loginFragment;
import com.example.gayalr.cpms.userManagement.signupFragment;

public class mainActivity extends AppCompatActivity implements Navigation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){ //Checking for savedinstances, to prevent creation of more than one fragment.
            loginFragment loginFragment = new loginFragment();  //instantiation of login fragment.
            getSupportFragmentManager().beginTransaction().add(R.id.container, loginFragment).commit(); //adding loginFragment to container
        }


    }

    @Override
    public void naviagateTo(Fragment fragmentToNavigateTo, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragmentToNavigateTo);

        fragmentTransaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        //if true allow user to go back using back button
        if(addToBackStack){
            fragmentTransaction.addToBackStack(null);
        }



        fragmentTransaction.commit();
    }
}
