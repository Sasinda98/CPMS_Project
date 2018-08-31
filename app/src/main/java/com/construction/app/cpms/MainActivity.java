package com.construction.app.cpms;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.construction.app.cpms.userManagement.loginFragment;
import com.construction.app.cpms.userManagement.signupFragment;

public class MainActivity extends AppCompatActivity implements Navigation {

    private boolean isLoggedIn = false ;  //for testing purposes value is hardcoded.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isLoggedIn){   //if logged in
            Intent i =  new Intent(this, SecondaryActivity.class);
            startActivity(i);
            finish();    //Destroy this activity from backstack so itdoesnt go back with back button
        }
        else {      //if not logged in
            if(savedInstanceState == null){ //Checking for savedinstances, to prevent creation of more than one fragment.
                loginFragment loginFragment = new loginFragment();  //instantiation of login fragment.
                getSupportFragmentManager().beginTransaction().add(R.id.container, loginFragment).commit(); //adding loginFragment to container
            }
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
