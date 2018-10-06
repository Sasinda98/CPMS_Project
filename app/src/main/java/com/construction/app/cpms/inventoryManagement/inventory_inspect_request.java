package com.construction.app.cpms.inventoryManagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.construction.app.cpms.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class inventory_inspect_request extends AppCompatActivity {

    private TextView subConName, itemName, itemCat, itemQty, reqQty, reqMessage, inventoryBalance;
    private String iName, itemCategory, requestMessage, sCName;
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    private String currentTime;
    private int subConID, itemID, rID;
    private double rQty, iQty, bal;

    private Button approveBtn;
    private Button denyBtn;

    private RequestQueue requestQueue;
    private String insertUrl = "https://projectcpms99.000webhostapp.com/scripts/chandula/updateInventoryRequest.php";
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_inspect_request);

        //Get all the values from previous screen
        Intent intent = getIntent();
        iName = intent.getStringExtra("itemName");
        itemCategory = intent.getStringExtra("category");
        requestMessage = intent.getStringExtra("message");
        sCName = intent.getStringExtra("subConName");
        itemID = intent.getIntExtra("itemID", 0);
        subConID = intent.getIntExtra("subConID", 0);
        Bundle b = getIntent().getExtras();
        iQty = b.getDouble("itemQty");
        rQty = b.getDouble("reqQty");
        rID = b.getInt("reqID");
        //Getting current time to pass to script
        currentTime = now();
        //Quantity remaining if request accepted
        bal = getBal(iQty, rQty);
        //Access all the text view
        subConName = (TextView) findViewById(R.id.incoming_request_reqName);
        itemName = (TextView) findViewById(R.id.incoming_request_itemName);
        itemCat = (TextView) findViewById(R.id.incoming_request_category);
        itemQty = (TextView) findViewById(R.id.incoming_request_itemQty);
        reqQty = (TextView) findViewById(R.id.incoming_request_reqQty);
        reqMessage = (TextView) findViewById(R.id.incoming_request_message);
        inventoryBalance = (TextView) findViewById(R.id.incoming_request_balance);
        //Access Buttons
        approveBtn = (Button) findViewById(R.id.incoming_request_approve_btn);
        denyBtn = (Button) findViewById(R.id.incoming_request_deny_btn);
        //Set all the values for the textViews
        subConName.setText(sCName.toString());
        itemName.setText(iName.toString());
        itemCat.setText(itemCategory.toString());
        itemQty.setText(String.valueOf(iQty));
        reqQty.setText(String.valueOf(rQty));
        reqMessage.setText(requestMessage.toString());
        inventoryBalance.setText(String.valueOf(bal));


        addListenerOnApprove();
        addListenerOnDeny();


    }

    // Used as tutorial: http://www.java-samples.com/showtutorial.php?tutorialid=682
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());


    }

    //Function written to get the amount of stock of item left if request is approved
    public double getBal(double iQty, double rQty){
        double bal;
        bal = iQty - rQty;
        return bal;
    }

    private void addListenerOnApprove() {


        requestQueue = Volley.newRequestQueue(this.getApplicationContext());


        //Linking the views on xml file to java file


        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String stat = "Approved";

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
                            params.put("rID", String.valueOf(rID));
                            params.put("rDate", currentTime);
                            params.put("rStat", stat);
                            params.put("iID", String.valueOf(itemID));
                            params.put("rBal", String.valueOf(bal));

                            System.out.println("=========================================================================");
                            System.out.println("=============================="+String.valueOf(rID)+"=====================================");
                            System.out.println("===================================="+currentTime+"=====================================");
                            System.out.println("======================================"+ stat+"==================================");
                            System.out.println("=========================================================================");
                            return params;
                        }
                    };

                    requestQueue.add(request);

                    CharSequence msg = "Approved";
                    Toast.makeText(inventory_inspect_request.this, msg, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(inventory_inspect_request.this, inventory_incoming_requests.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                    startActivity(intent);










            }
        });
    }

    private void addListenerOnDeny() {


        requestQueue = Volley.newRequestQueue(this.getApplicationContext());


        //Linking the views on xml file to java file


        denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String stat = "Denied";

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
                        params.put("rID", String.valueOf(rID));
                        params.put("rDate", currentTime);
                        params.put("rStat", stat);

                        System.out.println("=========================================================================");
                        System.out.println("=============================="+String.valueOf(rID)+"=====================================");
                        System.out.println("===================================="+currentTime+"=====================================");
                        System.out.println("======================================"+ stat+"==================================");
                        System.out.println("=========================================================================");
                        return params;
                    }
                };

                requestQueue.add(request);

                CharSequence msg = "Denied";
                Toast.makeText(inventory_inspect_request.this, msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(inventory_inspect_request.this, inventory_incoming_requests.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                startActivity(intent);










            }
        });
    }
}
