package com.construction.app.cpms.userManagement;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.Navigation;
import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.addForumPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class signupFragment extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String TAG = "signupFragment";



    private TextView signUpHeaderTV = null;
    private TextView signUpSubHeadingTV = null;
    private Button continueBtn = null;


    /*Text inputs*/
    private TextInputEditText fNameEntry = null;
    private TextInputEditText lNameEntry = null;
    private TextInputEditText mobileNumberEntry = null;
    private TextInputEditText emailEntry = null;
    private TextInputEditText passwordEntry = null;
    private TextInputEditText confirmPasswordEntry = null;


    //Database
    private RequestQueue requestQueue;
    private String insertUrl = "http://projectcpms99.000webhostapp.com/scripts/gayal/insertUser.php";
    private StringRequest stringRequest;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);


        signUpHeaderTV = view.findViewById(R.id.signUpHeader);
        signUpSubHeadingTV = view.findViewById(R.id.signUpSubHeader);


        /*Initialization of text input*/
        fNameEntry = view.findViewById(R.id.fname_editText);
        lNameEntry = view.findViewById(R.id.lname_editText);
        mobileNumberEntry = view.findViewById(R.id.mobile_editText);
        emailEntry = view.findViewById(R.id.su_email_editText);
        passwordEntry = view.findViewById(R.id.su_password_editText);
        confirmPasswordEntry = view.findViewById(R.id.su_confPassword_editText);

        continueBtn = view.findViewById(R.id.su_ContinueBtn);

        //Refered to the following links to see how to add custom fonts -:
        // #1 https://stackoverflow.com/questions/26140094/custom-fonts-in-android-api-below-16
        // #2 https://stackoverflow.com/questions/43350183/cannot-resolve-method-getassets-while-adding-custom-font
        Typeface robotoLightFont  = Typeface.createFromAsset(signUpHeaderTV.getContext().getAssets(), "fonts/Roboto-Light.ttf");
        signUpHeaderTV.setTypeface(robotoLightFont);
        signUpSubHeadingTV.setTypeface(robotoLightFont);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("name", fNameEntry.getText() + " " + lNameEntry.getText());
                hashMap.put("type", "firebase-not-set");
                FirebaseDatabase.getInstance().getReference().getRoot().child("users").child("TESTING-UID").updateChildren(hashMap);*/



                boolean isFormValid = true;

                if(!FormValidator.isNameValid(fNameEntry.getText().toString())){
                   fNameEntry.setError("Enter your first name");
                   //return;
                    isFormValid = false;
                }


                if(!FormValidator.isNameValid(lNameEntry.getText().toString())){
                    lNameEntry.setError("Enter your last name");
                    isFormValid = false;
                }

                if(!FormValidator.isPhoneValid(mobileNumberEntry.getText().toString())){
                    mobileNumberEntry.setError("Check your mobile number");
                    isFormValid = false;
                }

                if(!FormValidator.isEmailValid(emailEntry.getText().toString())){
                    emailEntry.setError("Check your email address");
                    isFormValid = false;
                }

                //if length of password not enough display error if not, check if conf and pw fields match , if not display error
                if(!FormValidator.isPasswordEntryCorrect(passwordEntry.getText().toString())){
                    passwordEntry.setError("Password length > 5");
                    isFormValid = false;
                } else {
                    //if confirm pw input and pw dont match
                    if (!passwordEntry.getText().toString().equals(confirmPasswordEntry.getText().toString())) {
                        isFormValid = false;
                        passwordEntry.setError("Passwords don't match");
                        confirmPasswordEntry.setError("Passwords don't match");
                    }
                }



                //if form entered data checks out, go ahead with post requests to php script
                if(isFormValid == true){

                    //FIREBASE
                    //adding user to firebase to makeuse of realtime database in funcs
                    mAuth.createUserWithEmailAndPassword(emailEntry.getText().toString(), passwordEntry.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        //firebase uid passed in as parameter to store it in the remote db.
                                        insertRemoteDb(user.getUid());      //Add user record to remotedatabase, so others can also see this user.

                                        //Updating firebase Database with user info... (apart from Authentication part...)
                                        HashMap<String,Object> hashMap = new HashMap<>();
                                        hashMap.put("name", fNameEntry.getText() + " " + lNameEntry.getText());
                                        hashMap.put("type", "firebase-not-set");
                                        FirebaseDatabase.getInstance().getReference().getRoot().child("users").child(user.getUid()).updateChildren(hashMap);

                                        //Navigate to sign in page if sign up is successful.
                                        ((Navigation)getActivity()).naviagateTo(new loginFragment(), false);

                                        Toast.makeText(getContext(),"Sign Up Succesful",Toast.LENGTH_LONG).show();

                                        // updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                        //used stackobverflow as referemces to know which exceptions to handle. link - https://stackoverflow.com/questions/37859582/how-to-catch-a-firebase-auth-specific-exceptions/38676613
                                        String errorCode = ((FirebaseAuthException)task.getException()).getErrorCode();

                                        //add more validations later on if time permits use the link above to do so.. for now email verification and password is done.
                                        switch (errorCode){
                                            case "ERROR_EMAIL_ALREADY_IN_USE":
                                                Toast.makeText(getContext(), "The email address is already in use by another account.", Toast.LENGTH_LONG).show();
                                                emailEntry.setError("The email address is already in use by another account.");
                                               // emailEntry.requestFocus();
                                                break;
                                            case "ERROR_WEAK_PASSWORD":     //redundant case since check is done above before it comes here.
                                                Toast.makeText(getContext(), "The given password is invalid.", Toast.LENGTH_LONG).show();
                                                passwordEntry.setError("The password is invalid, it must 6 characters at least");
                                               // passwordEntry.requestFocus();
                                                break;

                                                default: Toast.makeText(getContext(),"Something Went Wrong", Toast.LENGTH_LONG).show();
                                        }


                                        Toast.makeText(getContext(), "Sign up failed!",
                                                Toast.LENGTH_SHORT).show();
                                        // updateUI(null);
                                    }

                                }
                            });

                }

            }//end onclick
        });


        return view;
    }

    public void insertRemoteDb(final String firebaseUserId ){
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    //checking if the sent response has the relevant attributes.. if so continue with the sign in..
                    if (jsonObject.names().get(0).equals("isEmailInDB") && jsonObject.names().get(1).equals("isUserAdded")) {

                        boolean isEmailInDB = Boolean.valueOf(jsonObject.getString("isEmailInDB"));
                        boolean isUserAddedToDB = Boolean.valueOf(jsonObject.getString("isUserAdded"));

                        if (isEmailInDB && isUserAddedToDB == false) { //returns true if user entered email already exist in db.
                            //handed over to firebase->         //emailEntry.setError("Email already registered");

                        } else {
                            //code to execute when user got added...

                        }


                    } else {
                        Toast.makeText(getContext(), "Error Occurred, Remote DB OR INTERNET", Toast.LENGTH_LONG).show();    //When there are issues wit json response.
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("fName", fNameEntry.getText().toString());
                params.put("lName", lNameEntry.getText().toString());
                params.put("contactNo", mobileNumberEntry.getText().toString());
                params.put("email", emailEntry.getText().toString());
                params.put("password", passwordEntry.getText().toString());
                params.put("firebaseId", firebaseUserId);
                return params;
            }
        };
         requestQueue.add(request);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
    }

}
