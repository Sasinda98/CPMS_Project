package com.example.gayalr.cpms.userManagement;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gayalr.cpms.Navigation;
import com.example.gayalr.cpms.R;

import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;


public class forgotPasswordFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        //Grabbing references to UI elements.
        TextView forgotPw = view.findViewById(R.id.forgotPassword);
        TextView resetPwSub = view.findViewById(R.id.resetYourPassword);

        TextInputLayout emailTIL = view.findViewById(R.id.email_inputLayout);
        TextInputEditText emailEditText = view.findViewById(R.id.email_editText);

        Button continueBtn = view.findViewById(R.id.ContinueBtn);

        //setting fonts
        Typeface robotoLightFont  = Typeface.createFromAsset(forgotPw.getContext().getAssets(), "fonts/Roboto-Light.ttf");

        forgotPw.setTypeface(robotoLightFont);
        resetPwSub.setTypeface(robotoLightFont);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do the necessary checks to ensure that an account exists. (send verification email)
                //To do Search google for account verification via email.
                //Navigation shouldn't happen unless user is verified.
                ((Navigation) getActivity() ).naviagateTo(new forgotPasswordResetFragment(),true);
            }
        });



        return view;
    }

}
