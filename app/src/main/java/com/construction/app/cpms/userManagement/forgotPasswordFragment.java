package com.construction.app.cpms.userManagement;


import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.construction.app.cpms.Navigation;
import com.construction.app.cpms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class forgotPasswordFragment extends Fragment {

    private static final String TAG = "forgotPasswordFragment";
    private TextInputEditText emailEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        //Grabbing references to UI elements.
        TextView forgotPw = view.findViewById(R.id.forgotPassword);
        TextView resetPwSub = view.findViewById(R.id.resetYourPassword);

        TextInputLayout emailTIL = view.findViewById(R.id.email_inputLayout);
        emailEditText = view.findViewById(R.id.email_editText);

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
                //((Navigation) getActivity() ).naviagateTo(new forgotPasswordResetFragment(),true);
                String emailAdd = processEmailString(emailEditText.getText().toString().trim());
                sendResetEmail(emailAdd);
            }
        });






        return view;
    }


    //refered to firebase documentation
    public void sendResetEmail(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();

       if(email != null) {
           auth.sendPasswordResetEmail(email)
                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()) {
                               Log.d(TAG, "Email sent.");

                               //region AlertDialog

                               AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                               builder.setMessage("Password reset email has been sent. " +
                                       "Please check your email to reset the password." +
                                       "Hint -: Your SPAM filter might remove email from inbox.");
                               builder.setCancelable(true);

                               builder.setPositiveButton(
                                       "Ok",
                                       new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int id) {
                                               dialog.cancel();
                                               //navigate back to login.
                                               ((Navigation) getActivity() ).naviagateTo(new loginFragment(),false);
                                           }
                                       });

                               AlertDialog alert = builder.create();
                               alert.show();

                               //endregion

                           }
                       }
                   });
       }else {

           AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
           builder.setMessage("Something went wrong, contact system administrator.");
           builder.setCancelable(true);

           builder.setPositiveButton(
                   "Ok",
                   new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           dialog.cancel();
                       }
                   });

           AlertDialog alert = builder.create();
           alert.show();
           }
    }

    private String processEmailString(String email){

        if(email != null){
            email = email.trim();
            if(email.length() > 0){
                return email;
            }else {
                emailEditText.setError("Enter your email");
                return null;
            }
        }else {
            emailEditText.setError("Enter your email");
            return null;
        }

    }

}
