package com.construction.app.cpms.Plan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.R;

import java.util.HashMap;
import java.util.Map;

public class addPlan extends AppCompatActivity {

    EditText Name, Image, Description;
    ImageButton upload;
    RequestQueue requestQueue;
    String insertURL ="https://projectcpms99.000webhostapp.com/scripts/Harshan/addPlans.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        //cancel button
        ImageButton iBn = (ImageButton)findViewById(R.id.button);
        iBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myyIntent = new Intent(addPlan.this, newMainPlan.class);
                startActivity(myyIntent);
            }
        });

        //upload button stuff
        Name = (EditText) findViewById(R.id.editText6);
        Image = (EditText) findViewById(R.id.editText3);
        Description = (EditText) findViewById(R.id.editText_1);
        upload = (ImageButton) findViewById(R.id.button_2);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                     protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String,String> parameters = new HashMap<String, String>();
                        parameters.put("Name", Name.getText().toString());
                        parameters.put("Image", Image.getText().toString());
                        parameters.put("Descripion", Description.getText().toString());

                        return parameters;
                    }
                };
                requestQueue.add(request);
            }
            });



        /*//Navigate backwards from AddPlan to Plan
        ImageButton backBn = (ImageButton)findViewById(R.id.backButton);
        backBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myyIntent = new Intent(addPlan.this, MainPlan.class);
                startActivity(myyIntent);
            }
        });*/

    }
}
