package com.construction.app.cpms.userManagement;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class signupFragment extends Fragment {

    private TextView signUpHeaderTV = null;
    private TextView signUpSubHeadingTV = null;
    private Button continueBtn = null;

    /*Text inputs*/
    private TextInputEditText fNameEntry = null;
    private TextInputEditText lNameEntry = null;
    private TextInputEditText mobileNumberEntry = null;
    private TextInputEditText emailEntry = null;
    private TextInputEditText passwordEntry = null;


    //Database
    private RequestQueue requestQueue;
    private String insertUrl = "http://projectcpms99.000webhostapp.com/scripts/insertUser.php";
    private StringRequest stringRequest;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        signUpHeaderTV = view.findViewById(R.id.signUpHeader);
        signUpSubHeadingTV = view.findViewById(R.id.signUpSubHeader);


        /*Initialization of text input*/
        fNameEntry = view.findViewById(R.id.fname_editText);
        lNameEntry = view.findViewById(R.id.lname_editText);
        mobileNumberEntry = view.findViewById(R.id.mobile_editText);
        emailEntry = view.findViewById(R.id.fname_editText);
        passwordEntry = view.findViewById(R.id.password_editText);

        continueBtn = view.findViewById(R.id.ContinueBtn);

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

                StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                        return params;
                    }
                };

                requestQueue.add(request);


            }//end onclick
        });


        return view;
    }

}
