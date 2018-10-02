package com.construction.app.cpms.inventoryManagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.R;
import com.construction.app.cpms.inventoryManagement.adapters.stockLowAdapter;
import com.construction.app.cpms.inventoryManagement.beans.inventory_item_Bean;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class inventory_stock_low extends AppCompatActivity {


    /*Database Variables*/
    private  StringRequest stringRequest;
    private RequestQueue requestQueue;
    private String URL_PHP_SCRIPT = "https://projectcpms99.000webhostapp.com/scripts/chandula/fetchAllStockLow.php";
    private String catName;
    private int imgID;
    private static ArrayList<inventory_item_Bean> stockLowArrayList;  // Forum class is a bean.
    private android.support.v7.widget.Toolbar toolbar;

    stockLowAdapter adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_stock_low);



        requestQueue = Volley.newRequestQueue(inventory_stock_low.this);
        stockLowArrayList = new ArrayList<inventory_item_Bean>();

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Stock Low");


        fetchdata();

//        for(inventory_item_Bean itemB : itemArrayList) {
//            System.out.println(itemB.getItemName());
//        }

        listView = (ListView) findViewById(R.id.items_stockLow_listView);
        adapter = new stockLowAdapter(this, stockLowArrayList);
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
                                int itemID = Integer.valueOf(object.getString("itemID"));
                                String itemName = object.getString("itemName");
                                String itemCat = object.getString("itemCategory");
                                double itemQty = Double.valueOf(object.getString("itemQty"));
                                String itemUnit = object.getString("itemUnit");
                                //image id is recieved through the intent from categories activity

                                inventory_item_Bean inventoryItem = new inventory_item_Bean(itemID, itemName, itemQty, itemCat, itemUnit, R.drawable.gloves);

                                System.out.println("ITEM NAME= " + inventoryItem.getItemName()+"ITEM Qty= " + itemQty+"ITEM Cat= " + itemCat+"ITEM Unit= " + itemUnit);

                                //populate arraylist
                                stockLowArrayList.add(inventoryItem);
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
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        HashMap<String, String> params = new HashMap<>();
//                        params.put("iCat", catName);
//                        return params;
//                    }

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
                Toast toast = Toast.makeText(inventory_stock_low.this, "Loading Items", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        asyncTask.execute();
    }
}
