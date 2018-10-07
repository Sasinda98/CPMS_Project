package com.construction.app.cpms.Plan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class AddProject extends AppCompatActivity {

    EditText Name, Description;
    ImageButton upload;
    RequestQueue requestQueue;
    String insertURL ="https://projectcpms99.000webhostapp.com/scripts/Harshan/addProjects.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        //cancel button
        ImageButton imageB = (ImageButton)findViewById(R.id.pr_button);
        imageB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProject.this, ProjectDashboard.class);
                startActivity(intent);

                CharSequence message1 = "Project Upload Cancelled ";
                Toast.makeText(AddProject.this, message1, Toast.LENGTH_LONG).show();
            }
        });

        //getting values from editText fields in xml
        Name = (EditText) findViewById(R.id.project_name);
        Description = (EditText) findViewById(R.id.project_des);
        upload = (ImageButton) findViewById(R.id.pr_button_2);

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
                        CharSequence message3 = "Project Upload Unsuccessful. No Internet Access!";
                        Toast.makeText(AddProject.this, message3, Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameters = new HashMap<String, String>();
                        parameters.put("Name", Name.getText().toString());
                        parameters.put("Descripion", Description.getText().toString());

                        return parameters;
                    }
                };
                requestQueue.add(request);

                CharSequence message = "Project Uploaded Successfully ";
                Toast.makeText(AddProject.this, message, Toast.LENGTH_LONG).show();

                Intent newIntent = new Intent(AddProject.this, newMainPlan.class);
                startActivity(newIntent);
            }
        });


    }
}
