package com.construction.app.cpms.Plan;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.construction.app.cpms.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class displayPlan extends AppCompatActivity {

    String Name, Image, Description, name, Status;
    Button updateBn, deleteBn;
    RequestQueue requestQueue;
    String showURL = "https://projectcpms99.000webhostapp.com/scripts/Harshan/fetchSinglePlan.php";
    String updateURL = "https://projectcpms99.000webhostapp.com/scripts/Harshan/updatePlans.php";
    String deleteURL = "https://projectcpms99.000webhostapp.com/scripts/Harshan/deletePlans.php";
    int planId;
    TextView result;
    EditText planName, planDescription,planStatus;
    ImageView planImage;
    int iD;


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
        planStatus = (EditText)findViewById(R.id.d_status);
        planImage = (ImageView) findViewById(R.id.d_image);
        updateBn = (Button) findViewById(R.id.vUpdate);
        deleteBn = (Button) findViewById(R.id.vDelete);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        planName = (EditText) findViewById(R.id.d_name);
        planDescription = (EditText) findViewById(R.id.d_description);
        planImage = (ImageView) findViewById(R.id.d_image);

        updateBn = findViewById(R.id.vUpdate);

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

        //navigate back to ViewPlans
        ImageButton imageButton = (ImageButton)findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(displayPlan.this, MainPlan.class);
                startActivity(intent);
                }
        });

        //editText cursor visibility handling
        final EditText editText = (EditText)findViewById(R.id.d_name);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(view.getId() == editText.getId()){
                    editText.setCursorVisible(true);
                }
                return false;
            }
        });
        final EditText editText2 =(EditText)findViewById(R.id.d_description);
        editText2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(view.getId() == editText2.getId()){
                    editText2.setCursorVisible(true);
                }
                return false;
            }
        });
        final EditText editText3 =(EditText)findViewById(R.id.d_status);
        editText3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(view.getId() == editText3.getId()){
                    editText3.setCursorVisible(true);
                }
                return false;
            }
        });





      /*  //onCreate
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                editText.setCursorVisible(false);
                if(keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(editText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
        //onCreate
       editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                editText2.setCursorVisible(false);
                if(keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(editText2.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        }); */

    }
    private void getIncomingIntent() {
        if (getIntent().hasExtra("name") && getIntent().hasExtra("image") && getIntent().hasExtra("description") && getIntent().hasExtra("status")) {
            Name = getIntent().getStringExtra("name");
            Image = getIntent().getStringExtra("image");
            Description = getIntent().getStringExtra("description");
            planId = getIntent().getIntExtra("pID", 0);
            Status = getIntent().getStringExtra("status");
            setImage(Name, Image, Description, Status);
        }
    }

    private void setImage(String Name, String Image, String description, String status) {
        EditText name = findViewById(R.id.d_name);
        name.setText(Name);

        EditText des = findViewById(R.id.d_description);
        des.setText(description);

        EditText st = findViewById(R.id.d_status);
        st.setText(status);

        ImageView image = findViewById(R.id.d_image);
        Glide.with(this)
                .asBitmap()
                .load(Image)
                .into(image);
    }

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
                CharSequence message4 = "Error. No Internet Access!";
                Toast.makeText(displayPlan.this, message4, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> postParams = new HashMap<String, String>();
                postParams.put("pID", String.valueOf(planId));

                return postParams;
            }
        };
        requestQueue.add(jsonObjRequest);
    }

    public void update() {
        final String name = planName.getText().toString();
        final String description = planDescription.getText().toString();
        final String status = planStatus.getText().toString();

        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                updateURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        //fetchData();
                        finish();
                        //Toast.makeText(getApplicationContext(),"Plan Successfully Updated",Toast.LENGTH_SHORT);
                        Toast.makeText(getBaseContext(), "Plan Successfully Updated", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
                System.out.print("Error: "+error.getMessage());
                CharSequence message4 = "Error. No Internet Access!";
                Toast.makeText(displayPlan.this, message4, Toast.LENGTH_LONG).show();
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
                postParam.put("description", description);
                postParam.put("pID", String.valueOf(planId));
                postParam.put("status", status);

                System.out.print(name + description + status);

                return postParam;
            }

        };

        requestQueue.add(jsonObjRequest);

    }

}































      /* getIncomingIntent();
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
        }); */

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

   /* public void delete() {
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


