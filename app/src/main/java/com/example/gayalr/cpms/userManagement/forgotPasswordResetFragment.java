package com.example.gayalr.cpms.userManagement;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gayalr.cpms.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class forgotPasswordResetFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_forgot_password_reset, container, false);

        //Grabbing references to the UI elements.
        TextView forgotPw = view.findViewById(R.id.forgotPassword);
        TextView resetPwSub = view.findViewById(R.id.resetYourPassword);

        TextInputEditText passwordEditText = view.findViewById(R.id.password_editText);
        TextInputEditText confPasswordEditText = view.findViewById(R.id.confPassword_editText);

        TextInputLayout passwordTIL = view.findViewById(R.id.password_inputLayout);
        TextInputLayout confPasswordTIL = view.findViewById(R.id.confPassword_inputLayout);

        Button changePasswordBtn = view.findViewById(R.id.changePasswordBtn);

        //setting fonts
        Typeface robotoLightFont  = Typeface.createFromAsset(forgotPw.getContext().getAssets(), "fonts/Roboto-Light.ttf");

        forgotPw.setTypeface(robotoLightFont);
        resetPwSub.setTypeface(robotoLightFont);

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Change the user's password after sending verification email.
            }
        });


        return view;
    }


}
