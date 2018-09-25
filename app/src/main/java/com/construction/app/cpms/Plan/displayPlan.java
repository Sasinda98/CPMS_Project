package com.construction.app.cpms.Plan;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.construction.app.cpms.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class displayPlan extends AppCompatActivity {

    String Name, Image, Description, name;
    Button updateBn, deleteBn;
    RequestQueue requestQueue;
    String showURL =   "https://projectcpms99.000webhostapp.com/scripts/Harshan/fetchPlans.php";
    String updateURL = "https://projectcpms99.000webhostapp.com/scripts/Harshan/updatePlans.php";
    String deleteURL = "https://projectcpms99.000webhostapp.com/scripts/Harshan/deletePlans.php";
    int planId;
    TextView result;
    EditText planName, planDescription;
    ImageView planImage;
    int iD;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_plan);

        getIncomingIntent();
        System.out.println("======================================================================================================");

        System.out.println("======================================================================================================"+planId);
        //text fields and buttons
        planName = (EditText) findViewById(R.id.d_name);
        planDescription = (EditText) findViewById(R.id.d_description);
        planImage = (ImageView) findViewById(R.id.d_image);
        updateBn = (Button) findViewById(R.id.vUpdate);
        deleteBn = (Button) findViewById(R.id.vDelete);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        planId = getIntent().getIntExtra("pID", 0);
        System.out.println("\n"+planId+"\n");


        deleteBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
        updateBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        /*fetchData();*/

        /*delete stuff begins here*/
        /*deleteBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest jsonObjRequest = new StringRequest(Request.Method.POST, deleteURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        finish();
                        Toast.makeText(getBaseContext(), "Plan Successfully Deleted", Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getStackTrace();
                        System.out.print("Error: " + error.getMessage());
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> postParams = new HashMap<String, String>();
                        postParams.put("id", String.valueOf(planId));

                        return postParams;
                    }
                };
                requestQueue.add(jsonObjRequest);
            }
        });*/
    }
  /*  public void fetchData(){
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                showURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.print(response);
                        try {
                            JSONArray plan = new JSONArray(response);

                            for (int i = 0; i < plan.length(); i++) {
                                JSONObject pl = plan.getJSONObject(i);

                                Integer id = pl.getInt("pID");
                                String name = pl.getString("Name");
                                String image = pl.getString("Image");
                                String description = pl.getString("Description");

                                planName.setText(name);
                                planDescription.setText(description);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
                System.out.print("Error: " + error.getMessage());
            }}) {
            @Override
            public String getBodyContentType(){
                return "application/x-www-form-urlencoded; charset=UTF-8";
                }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postParams = new HashMap<String, String>();
                postParams.put("id",String.valueOf(planId));
                return postParams;
            }
        };

        requestQueue.add(jsonObjRequest);
    } */

    public void delete() {
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST, deleteURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //the finish() here return the activity to the view plans
                finish();
                Toast.makeText(getBaseContext(), "Plan Successfully Deleted", Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
                System.out.print("Error: " + error.getMessage());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> postParams = new HashMap<String, String>();
                postParams.put("id", String.valueOf(planId));

                return postParams;
            }
        };
        requestQueue.add(jsonObjRequest);
    }

    public void update() {
        final String name = planName.getText().toString();
        final String description = planDescription.getText().toString();

        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                updateURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);


                        //fetchData();
                        finish();
                        Toast.makeText(getApplicationContext(),"Plan Successfully Updated",Toast.LENGTH_SHORT);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
                System.out.print("Error: "+error.getMessage());
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> postParam = new HashMap<String, String>();


                postParam.put("name", name);
                postParam.put("desc", description);

                System.out.print(name + description);

                return postParam;
            }

        };

        requestQueue.add(jsonObjRequest);

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("name") && getIntent().hasExtra("image") && getIntent().hasExtra("description")) {
            Name = getIntent().getStringExtra("name");
            Image = getIntent().getStringExtra("image");
            Description = getIntent().getStringExtra("description");
            planId = getIntent().getIntExtra("pID", 0);
            setImage(Name, Image, Description);
        }
    }

    private void setImage(String Name, String Image, String description) {
        EditText name = findViewById(R.id.d_name);
        name.setText(Name);

        EditText des = findViewById(R.id.d_description);
        des.setText(description);

        ImageView image = findViewById(R.id.d_image);
        Glide.with(this)
                .asBitmap()
                .load(Image)
                .into(image);
    }
}


/*Note
 *In display plans changing the textView to editText makes the editing of description possible
 *But try to find out a way to edit the textView
 *According to research, textView has limitations, as in, textView can be changed to editable, but the existing data might not exactly change
 */

/*private void addListenerOnUpdate(){
        requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        updateBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest request = new StringRequest(Request.Method.POST, updateURL, new Response.Listener<String>() {
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
                        params.put("name", String.valueOf(Name));
                        params.put("description", String.valueOf(Description));
                        return params;
                    }
                };

                requestQueue.add(request);

                CharSequence msg = "Update Successful";
                Toast.makeText(displayPlan.this, msg, Toast.LENGTH_LONG).show();
                Intent newIntent = new Intent(displayPlan.this, newMainPlan.class);
                startActivity(newIntent);
            }
        });
    }; */


