package com.example.gayalr.cpms.userManagement;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gayalr.cpms.R;

public class signupFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        final TextView signUpHeaderTV = view.findViewById(R.id.signUpHeader);
        final TextView signUpSubHeadingTV = view.findViewById(R.id.signUpSubHeader);
        final TextInputEditText fnameEntry = view.findViewById(R.id.fname_editText);

        //Refered to the following links to see how to add custom fonts -:
        // #1 https://stackoverflow.com/questions/26140094/custom-fonts-in-android-api-below-16
        // #2 https://stackoverflow.com/questions/43350183/cannot-resolve-method-getassets-while-adding-custom-font
        Typeface robotoLightFont  = Typeface.createFromAsset(signUpHeaderTV.getContext().getAssets(), "fonts/Roboto-Light.ttf");
        signUpHeaderTV.setTypeface(robotoLightFont);
        signUpSubHeadingTV.setTypeface(robotoLightFont);

        return view;
    }

}
