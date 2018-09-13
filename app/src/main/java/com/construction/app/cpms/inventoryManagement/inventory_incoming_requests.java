package com.construction.app.cpms.inventoryManagement;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.R;
import com.construction.app.cpms.inventoryManagement.adapters.incoming_requests_adapter;
import com.construction.app.cpms.inventoryManagement.beans.incoming_requests_bean;
import com.construction.app.cpms.inventoryManagement.beans.inventory_item_Bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class inventory_incoming_requests extends AppCompatActivity {

    /*Database Variables*/
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private String URL_PHP_SCRIPT = "https://projectcpms99.000webhostapp.com/scripts/chandula/fetchInventoryRequests.php";

    private static ArrayList<incoming_requests_bean> requestArrayList;  // Forum class is a bean.

    incoming_requests_adapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_requests);

        requestQueue = Volley.newRequestQueue(inventory_incoming_requests.this);
        requestArrayList = new ArrayList<incoming_requests_bean>();



        fetchdata();

//        for(inventory_item_Bean itemB : itemArrayList) {
//            System.out.println(itemB.getItemName());
//        }

        listView = (ListView) findViewById(R.id.requests_listView);
        adapter = new incoming_requests_adapter(this, requestArrayList);
        listView.setAdapter(adapter);
    }

    private void fetchdata(){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                System.out.println("Do backgorund func");
                stringRequest = new StringRequest(Request.Method.POST, URL_PHP_SCRIPT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ON RESPONSE");

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i<jsonArray.length(); i++){ //loop through jsonarray(stores objects in each index) and put data to arraylist.
                                System.out.println("FOR LOOP");
                                JSONObject object = jsonArray.getJSONObject(i);//get the JSON object at index i

                                //Getting all the attributes of the bean from the JSON object
                                int reqID = Integer.valueOf(object.getString("reqID"));
                                int subConID = Integer.valueOf(object.getString("subConID"));
                                int itemID = Integer.valueOf(object.getString("itemID"));
                                double reqQty = Double.valueOf(object.getString("reqQty"));
                                String reqDate = object.getString("reqDate");
                                String reqMessage = object.getString("reqMessage");
                                String itemName = object.getString("itemName");
                                String fName = object.getString("fName");
                                String lName = object.getString("lName");
                                double itemQty = Double.valueOf(object.getString("itemQty"));
                                String itemCategory = object.getString("itemCategory");
                                String itemUnit = object.getString("itemUnit");




                                incoming_requests_bean incoming_request = new incoming_requests_bean(reqID,subConID,itemID,reqQty,reqDate,reqMessage,itemName,fName,lName,itemQty,itemCategory,itemUnit);



                                //populate arraylist
                                requestArrayList.add(incoming_request);
                            }
                            adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){

                };
                requestQueue.add(stringRequest);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast toast = Toast.makeText(inventory_incoming_requests.this, "Loading Items", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        asyncTask.execute();
    }
}
