package com.construction.app.cpms.inventoryManagement;

// Icons made by Freepik from www.flaticon.com (tick and cross)

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.R;
import com.construction.app.cpms.inventoryManagement.adapters.request_history_adapter;
import com.construction.app.cpms.inventoryManagement.beans.request_history_bean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class inventory_request_history extends AppCompatActivity {

    /*Database Variables*/
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private String URL_PHP_SCRIPT = "https://projectcpms99.000webhostapp.com/scripts/chandula/fetchRequestHistory.php";
    private Toolbar toolbar;

    private static ArrayList<request_history_bean> requestHistoryArrayList;  // Forum class is a bean.

    request_history_adapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_request_history);

        requestQueue = Volley.newRequestQueue(inventory_request_history.this);
        requestHistoryArrayList = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Requests History");



        fetchdata();

//        for(inventory_item_Bean itemB : itemArrayList) {
//            System.out.println(itemB.getItemName());
//        }

        listView = (ListView) findViewById(R.id.request_history_listView);
        adapter = new request_history_adapter(this, requestHistoryArrayList);
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
                                String valDate = object.getString("reqValiDate");
                                String reqMessage = object.getString("reqMessage");
                                String itemName = object.getString("itemName");
                                String fName = object.getString("fName");
                                String lName = object.getString("lName");
                                String reqStat = object.getString("reqStatus");
                                String itemCategory = object.getString("itemCategory");
                                String itemUnit = object.getString("itemUnit");






                                request_history_bean request_history = new request_history_bean(reqID,subConID,itemID,reqQty,reqDate, valDate, reqMessage,itemName,fName,lName,itemCategory,itemUnit, reqStat);



                                //populate arraylist
                                requestHistoryArrayList.add(request_history);
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
                Toast toast = Toast.makeText(inventory_request_history.this, "Loading Items ", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        asyncTask.execute();
    }
}
