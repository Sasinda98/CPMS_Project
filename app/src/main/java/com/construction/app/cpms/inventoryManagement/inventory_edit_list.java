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
import com.construction.app.cpms.expenses.Expense_Category_List;
import com.construction.app.cpms.expenses.editExpense;
import com.construction.app.cpms.inventoryManagement.adapters.edit_items_adapter;
import com.construction.app.cpms.inventoryManagement.beans.inventory_item_Bean;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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


public class inventory_edit_list extends AppCompatActivity {

    /*Database Variables*/
    private  StringRequest stringRequest;
    private RequestQueue requestQueue;
    private String URL_PHP_SCRIPT = "https://projectcpms99.000webhostapp.com/scripts/chandula/fetchAllInventoryItems.php";
    private String URL_DELETE_SCRIPT = "https://projectcpms99.000webhostapp.com/scripts/chandula/deleteItem.php";


    private int imgID;
    private static ArrayList<inventory_item_Bean> editItemArrayList;  // Forum class is a bean.
    private android.support.v7.widget.Toolbar toolbar;

    edit_items_adapter adapter;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_edit_list);


        requestQueue = Volley.newRequestQueue(inventory_edit_list.this);
        editItemArrayList = new ArrayList<inventory_item_Bean>();



        fetchdata();



        listView = (ListView) findViewById(R.id.edit_items_listView);
        adapter = new edit_items_adapter(this, editItemArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(inventory_edit_list.this, inventory_edit_item.class);
                inventory_item_Bean itemBean = (inventory_item_Bean) listView.getItemAtPosition(i);
                String itemName = itemBean.getItemName();
                String itemUnit = itemBean.getUnit();
                int itemID = itemBean.getItemID();
                Double itemQty = itemBean.getItemQuantity();
                Bundle b = new Bundle();
                b.putDouble("itemQty", itemQty);

                intent.putExtra("itemName", itemName);
                intent.putExtra("itemUnit", itemUnit);
                intent.putExtra("itemID", itemID);
                intent.putExtras(b);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                startActivity(intent);
            }
        });

        registerForContextMenu(listView);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.inventory_edit_delete_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.edit_inventory_delete_menu_item:
                int index = info.position;
                System.out.println("====================================================================================================================================================  TEST");

                System.out.println("===================================================================================================================================================="+index);
                int itemID = editItemArrayList.get(index).getItemID();
                System.out.println("ITEM ID ================================================================================"+itemID);
                editItemArrayList.remove(info.position);
                deleteItem(String.valueOf(itemID));
                adapter.notifyDataSetChanged();
                return true;

            default:
                System.out.println("====================================================================================================================================================  DEFAULT");
                return super.onContextItemSelected(item);


        }

        //return super.onContextItemSelected(item);
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
                                editItemArrayList.add(inventoryItem);
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
//                    //send email and password to post...
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
                Toast toast = Toast.makeText(inventory_edit_list.this, "Loading Items", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        asyncTask.execute();
    }

    private void deleteItem(final String iID){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                System.out.println("Do backgorund func");
                stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_SCRIPT, new Response.Listener<String>() {
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
//                                editItemArrayList.add(inventoryItem);
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
//                    //send email and password to post...
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("iID", iID);
                        return params;
                    }

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
                Toast toast = Toast.makeText(inventory_edit_list.this, "Loading Items", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        asyncTask.execute();

    }
}
