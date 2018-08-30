package com.construction.app.cpms.userManagement;



import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

// instead of this import android.app.Fragment;
//use this
import android.support.annotation.NonNull;

import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class loginFragment extends Fragment {

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
    private String URL_PHP_SCRIPT = "http://projectcpms99.000webhostapp.com/scripts/signIn.php";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

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
                 //navigate to forgot password fragment.
                 //((Navigation)getActivity()).naviagateTo(new DashboardFragment(), true);

                // ((Navigation)getActivity()).naviagateTo(new testing(), true);

                 //once logged in, start secondary activity


                 //Intent i =  new Intent(getActivity(), SecondaryActivity.class);
                // startActivity(i,null);

                // getActivity().finish();    //Destroy this activity from backstack so itdoesnt go back with back button


                stringRequest = new StringRequest(Request.Method.POST, URL_PHP_SCRIPT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {       //Handling the response using json object...
                        try {
                            JSONObject jsonObject = new JSONObject(response);   //Response to jsonobject

                            //checking if the sent response has the relevant attributes.. if so continue with the sign in..
                            if(jsonObject.names().get(0).equals("isEmailMatched")
                                    && jsonObject.names().get(1).equals("isPasswordMatched")
                                        &&  jsonObject.names().get(2).equals("userId") )
                            {
                                String value = jsonObject.getString("userId");

                                Toast.makeText(getContext(), "Userid= " + value, Toast.LENGTH_LONG).show();
                                //Intent i =  new Intent(getActivity(), SecondaryActivity.class);
                                // startActivity(i,null);


                            } else{
                                Toast.makeText(getContext(), "Error Occurred, DB OR INT", Toast.LENGTH_LONG).show();    //When there are issues wit response, network down etc.
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
                    //send email and password to post...

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> params = new HashMap<>();
                        params.put("email", emailEntry.getText().toString());
                        params.put("password", passwordEntry.getText().toString());
                        return params;
                    }
                };

                requestQueue.add(stringRequest);

             }//end of onclick
         });


        return view;
    }

}
