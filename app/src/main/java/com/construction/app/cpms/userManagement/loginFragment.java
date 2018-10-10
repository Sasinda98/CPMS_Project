package com.construction.app.cpms.userManagement;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

// instead of this import android.app.Fragment;
//use this
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.Navigation;
import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.*;
import com.construction.app.cpms.SecondaryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//Used Tool->Firebase to assist in coding the email and password auth
public class loginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private static final String TAG = "loginFragment";

    private TextView signUpHeaderTV;
    private TextView signUpSubHeaderTV;
    private Button signInBtn;
    private TextView forgotPasswordTV;
    private TextView dontHaveAccountTV;
    private TextInputEditText emailEntry;
    private TextInputEditText passwordEntry;

    /*Database stuff*/
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private String URL_PHP_SCRIPT = "http://projectcpms99.000webhostapp.com/scripts/gayal/signIn.php";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance(); //initializing authinstance

        requestQueue = Volley.newRequestQueue(getContext());

        //getting references to respective textfiels on xml(UI)
        signUpHeaderTV = view.findViewById(R.id.signInHeader);
        signUpSubHeaderTV = view.findViewById(R.id.signInSubHeader);

        signInBtn = view.findViewById(R.id.signInButton);
        forgotPasswordTV = view.findViewById(R.id.forgotPassword);
        dontHaveAccountTV = view.findViewById(R.id.dontHaveAccount);

        //Textbox inputs
        emailEntry = view.findViewById(R.id.email_editText);
        passwordEntry = view.findViewById(R.id.password_editText);


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



         signInBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                //Checking if the form entered data is correct...
                 if(!FormValidator.isEmailValid(emailEntry.getText().toString()) && !FormValidator.isPasswordEntryCorrect(passwordEntry.getText().toString())){
                     Toast toast = Toast.makeText(getContext(), "Login Credentials Are Invalid", Toast.LENGTH_SHORT);
                     toast.show();
                 }
                 else {
                     //if form entered data is correct go ahead with database operations etc...
                     stringRequest = new StringRequest(Request.Method.POST, URL_PHP_SCRIPT, new Response.Listener<String>() {
                         @Override
                         public void onResponse(String response) {       //Handling the response using json object...
                             try {
                                 JSONObject jsonObject = new JSONObject(response);   //Response to jsonobject

                                 //checking if the sent response has the relevant attributes.. if so continue with the sign in..
                                 if (jsonObject.names().get(0).equals("isEmailMatched")
                                         && jsonObject.names().get(1).equals("isPasswordMatched")
                                         && jsonObject.names().get(2).equals("userId")) {
                                     String value = jsonObject.getString("userId");

                                     boolean isEmailMatched = Boolean.valueOf(jsonObject.getString("isEmailMatched"));
                                     boolean isPasswordMatched = Boolean.valueOf(jsonObject.getString("isPasswordMatched"));
                                     String userId = jsonObject.getString("userId"); //null if there is no a matching accnt.

                                     /*bug fix*/
                                     if (isEmailMatched && userId != null) { //means user credentials match an actual accnt in db, script returns null itheres no matcvh
                                         saveLoginCredentials(userId);

                                         mAuth.signInWithEmailAndPassword(emailEntry.getText().toString(), passwordEntry.getText().toString())
                                                 .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<AuthResult> task) {
                                                         if (task.isSuccessful()) {
                                                             // Sign in success, update UI with the signed-in user's information
                                                             Log.d(TAG, "signInWithEmail:success");
                                                             FirebaseUser user = mAuth.getCurrentUser();
                                                             Toast.makeText(getContext(), "Authentication Successful.", Toast.LENGTH_SHORT).show();
                                                             //  updateUI(user);

                                                             Intent i =  new Intent(getActivity(), SecondaryActivity.class);
                                                             startActivity(i,null);
                                                         } else {
                                                             // If sign in fails, display a message to the user.
                                                             Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                             Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                                             //updateUI(null);
                                                         }

                                                         // ...
                                                     }
                                                 });




/*
                                         Intent i =  new Intent(getActivity(), SecondaryActivity.class);
                                         startActivity(i,null);*/


                                     } else {
                                         //account doesnt exist. invalid credentials..
                                        // Toast.makeText(getContext(), "Userid= " + value + "Access=Denied", Toast.LENGTH_LONG).show();
                                         Toast.makeText(getContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                                     }


                                 } else {
                                     Toast.makeText(getContext(), "Error Occurred, DB OR INTERNET", Toast.LENGTH_LONG).show();    //When there are issues wit json response.
                                 }

                             } catch (JSONException e) {
                                 e.printStackTrace();
                             }
                         }
                     }, new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError error) {
                             Toast.makeText(getContext(), "Error Occurred, while connecting to internet", Toast.LENGTH_LONG).show();    //When there are issues wit response, network down etc.
                         }
                     }) {
                         //send email and password to post...
                         @Override
                         protected Map<String, String> getParams() throws AuthFailureError {
                             HashMap<String, String> params = new HashMap<>();
                             params.put("email", emailEntry.getText().toString());
                             params.put("password", passwordEntry.getText().toString());
                             return params;
                         }
                     };
                     requestQueue.add(stringRequest);
                 }
             }//end of onclick
         });


        return view;
    }

    public void saveLoginCredentials(String userId){
        /*Stackoverflow used as reference for use of sharepref in fragment*/
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString("email",emailEntry.getText().toString());
        editor.putString("password",passwordEntry.getText().toString());
        editor.putString("userId", userId);
        editor.commit();
       // Toast.makeText(getContext(),"Save login creds", Toast.LENGTH_LONG).show();
        System.out.println("===========SaveLOGIN SHARED PREF==========");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

      //  updateUI(currentUser);
    }


}
