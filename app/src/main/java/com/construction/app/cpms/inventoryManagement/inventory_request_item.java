package com.construction.app.cpms.inventoryManagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class inventory_request_item extends AppCompatActivity {

    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    private String currentTime;
    private String itemName;
    private String itemUnit;
    private String catName;
    private String projectID;
    private double iQty;
    private int itemID;
    private String userID;
    private TextView reqItemNameTextView;
    private TextView reqItemUnitTextView;
    private TextInputEditText reqMessage = null;
    private TextInputEditText reqQty = null;
    private Button confirmBtn;
    private Button denyBtn;


    private RequestQueue requestQueue;
    private String insertUrl = "https://projectcpms99.000webhostapp.com/scripts/chandula/insertInventoryRequest.php";
    private StringRequest stringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_request_item);

        //Getting UserID from sharedPreference
        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        userID = preferences.getString("userId","");

        //Temporary projectID
        SharedPreferences pref = getSharedPreferences("projSwitch", Context.MODE_PRIVATE);
        projectID = pref.getString("projSwitchID", "");

        System.out.println("UserID =========================================================================================== "+ userID);


        //Getting category object from category grid activity.
        //We get the image id and the category name for the query from this
        Intent intent = getIntent();
        itemName = intent.getStringExtra("itemName");
        itemUnit = intent.getStringExtra("itemUnit");
        catName = intent.getStringExtra("catName");
        itemID = intent.getIntExtra("itemID", 0);


        Bundle b = getIntent().getExtras();
        iQty = b.getDouble("itemQty");
        System.out.println("Testing =============================================================================================== "+ iQty);

        //Initialising all the stuff on the xml
        reqItemNameTextView = (TextView) findViewById(R.id.request_item_name);
        reqItemNameTextView.setText(itemName);
        reqItemUnitTextView = (TextView) findViewById(R.id.request_item_unit);
        reqItemUnitTextView.setText(itemUnit);
        confirmBtn = (Button) findViewById(R.id.request_item_confirm_btn);
        denyBtn = (Button) findViewById(R.id.request_item_cancel_btn);
        reqMessage = findViewById(R.id.inventory_request_message);
        reqQty = findViewById(R.id.inventory_request_qty);

        addListenerOnConfirm();
        addListenerOnDeny();


        //Getting current time to pass to script
        currentTime = now();
        //Just for testing in Logcat
        System.out.println("============================================================="+currentTime+"=============================================================");




    }
// Used as tutorial: http://www.java-samples.com/showtutorial.php?tutorialid=682
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());


    }


    private void addListenerOnConfirm() {


        requestQueue = Volley.newRequestQueue(this.getApplicationContext());


        //Linking the views on xml file to java file


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double rQty = Double.valueOf(reqQty.getText().toString());
                System.out.println("=============================================================================== rQty============================================================"+rQty);
                System.out.println("=============================================================================== iQty============================================================"+iQty);

                //Validating whether request qty greater than qty in stock
                if(iQty < rQty){
                    CharSequence msg = "Request Quantity Higher than inventory Quantity";
                    Toast.makeText(inventory_request_item.this, msg, Toast.LENGTH_LONG).show();

                }
                else{
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
                            params.put("iID", Integer.toString(itemID));
                            params.put("rQty", reqQty.getText().toString());
                            params.put("iName", itemName.toString());
                            params.put("iUnit", itemUnit.toString());
                            params.put("rMsg", reqMessage.getText().toString());
                            params.put("rDate", currentTime);
                            params.put("uID", userID.toString());
                            params.put("pID", projectID);
                            return params;
                        }
                    };

                    requestQueue.add(request);

                    CharSequence msg = "RequestSent";
                    Toast.makeText(inventory_request_item.this, msg, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(inventory_request_item.this, inventory_items_list.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                    intent.putExtra("catName", catName);
                    startActivity(intent);



                }







            }
        });
    }


    private void addListenerOnDeny() {

        denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence msg = "Cancelled";
                Toast.makeText(inventory_request_item.this, msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(inventory_request_item.this, inventory_items_list.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                intent.putExtra("catName", catName);
                startActivity(intent);
            }
        });


    }


}
