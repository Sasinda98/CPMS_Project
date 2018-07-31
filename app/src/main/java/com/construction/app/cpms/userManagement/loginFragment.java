package com.construction.app.cpms.userManagement;



import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

// instead of this import android.app.Fragment;
//use this
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.construction.app.cpms.Navigation;
import com.construction.app.cpms.R;


public class loginFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //getting references to respective textfiels on xml(UI)
        final TextView signUpHeaderTV = view.findViewById(R.id.signInHeader);
        final TextView signUpSubHeaderTV = view.findViewById(R.id.signInSubHeader);

        final TextView forgotPasswordTV = view.findViewById(R.id.forgotPassword);
        final TextView dontHaveAccountTV = view.findViewById(R.id.dontHaveAccount);


        //Refered to the following links to see how to add custom fonts -:
        // #1 https://stackoverflow.com/questions/26140094/custom-fonts-in-android-api-below-16
        // #2 https://stackoverflow.com/questions/43350183/cannot-resolve-method-getassets-while-adding-custom-font
         Typeface robotoLightFont  = Typeface.createFromAsset(signUpHeaderTV.getContext().getAssets(), "fonts/Roboto-Light.ttf");

         signUpHeaderTV.setTypeface(robotoLightFont);
         signUpSubHeaderTV.setTypeface(robotoLightFont);
         forgotPasswordTV.setTypeface(robotoLightFont);
         dontHaveAccountTV.setTypeface(robotoLightFont);

         //Setting onclick listeners to relevant elements on sign in page.
        //Refered to -: https://codelabs.developers.google.com/codelabs/mdc-101-java/#2 (all the pages in this tutorial)

        dontHaveAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to sign up fragment.
                ((Navigation)getActivity()).naviagateTo(new signupFragment(), true);
            }
        });

         forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 //navigate to forgot password fragment.
                 ((Navigation)getActivity()).naviagateTo(new forgotPasswordFragment(), true);

             }
         });

        return view;
    }

}
