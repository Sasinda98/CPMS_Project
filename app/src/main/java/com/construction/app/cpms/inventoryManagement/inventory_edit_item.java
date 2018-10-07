package com.construction.app.cpms.inventoryManagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.construction.app.cpms.R;
import com.construction.app.cpms.inventoryManagement.beans.inventory_item_Bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class inventory_edit_item extends AppCompatActivity {

    private String iName;
    private String iUnit;
    private int iID;
    private double iQty;

    private TextView itemName;
    private TextView itemUnit;
    private TextInputEditText itemQty = null;
    private Button btnConfirm = null;
    private Button btnCancel = null;
//    private Button btnDelete = null;

    private RequestQueue requestQueue;
    private String insertUrl = "https://projectcpms99.000webhostapp.com/scripts/chandula/editItemQty.php";
    private String URL_DELETE_SCRIPT = "https://projectcpms99.000webhostapp.com/scripts/chandula/deleteItem.php";
    private StringRequest stringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_edit_item);

        itemName = (TextView) findViewById(R.id.edit_item_name);
        itemUnit = (TextView) findViewById(R.id.edit_item_unit);
        itemQty =  findViewById(R.id.edit_item_qty);
        btnConfirm = (Button) findViewById(R.id.edit_item_confirm_btm);
        btnCancel = (Button) findViewById(R.id.edit_item_cancel_btn);
//        btnDelete = (Button) findViewById(R.id.edit_item_delete_btn);

        Intent intent = getIntent();
        iName = intent.getStringExtra("itemName");
        iUnit = intent.getStringExtra("itemUnit");
        iID = intent.getIntExtra("itemID", 0);
        Bundle b = getIntent().getExtras();
        iQty = b.getDouble("itemQty");

        itemName.setText(iName);
        itemUnit.setText(iUnit);
        itemQty.setText(String.valueOf(iQty));

        addListenerOnConfirm();
        addListenerOnCancel();
//        addListenerOnDelete();




    }
    private void deleteItem(final String iID){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                System.out.println("Do backgorund func");
                stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_SCRIPT, new Response.Listener<String>() {
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
                Intent intent = new Intent(inventory_edit_item.this, inventory_edit_list.class);
                startActivity(intent);

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast toast = Toast.makeText(inventory_edit_item.this, "Loading Items ", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        asyncTask.execute();

    }


    private void addListenerOnConfirm() {


        requestQueue = Volley.newRequestQueue(this.getApplicationContext());


        //Linking the views on xml file to java file


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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
                            params.put("iID", Integer.toString(iID));
                            params.put("iQty", itemQty.getText().toString());
                            return params;
                        }
                    };

                    requestQueue.add(request);

                    CharSequence msg = "Item Edited ";
                    Toast.makeText(inventory_edit_item.this, msg, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(inventory_edit_item.this, inventory_edit_list.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                    startActivity(intent);


                }








        });
    }

    private void addListenerOnCancel(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(inventory_edit_item.this, inventory_edit_list.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                startActivity(intent);
            }
        });
    }




//    private void addListenerOnDelete(){
//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                deleteItem(String.valueOf(iID));
//            }
//        });
//
//    }


}
