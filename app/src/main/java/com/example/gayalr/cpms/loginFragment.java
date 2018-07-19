package com.example.gayalr.cpms;



import android.graphics.Typeface;
import android.os.Bundle;

// instead of this import android.app.Fragment;
//use this
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import android.support.design.widget.TextInputLayout;


public class loginFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //getting references to respective textfiels on xml(UI)
        final TextView signUpHeaderTV = (TextView) view.findViewById(R.id.signUpHeader);
        final TextView signUpSubHeaderTV = (TextView) view.findViewById(R.id.signUpSubHeader);



        //Refered to the following links to see how to add custom fonts -:
        // #1 https://stackoverflow.com/questions/26140094/custom-fonts-in-android-api-below-16
        // #2 https://stackoverflow.com/questions/43350183/cannot-resolve-method-getassets-while-adding-custom-font
         Typeface robotoLightFont  = Typeface.createFromAsset(signUpHeaderTV.getContext().getAssets(), "fonts/Roboto-Light.ttf");

         signUpHeaderTV.setTypeface(robotoLightFont);
         signUpSubHeaderTV.setTypeface(robotoLightFont);

        return view;
    }

}
