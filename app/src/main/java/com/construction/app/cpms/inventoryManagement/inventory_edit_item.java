package com.construction.app.cpms.inventoryManagement;

import android.content.Intent;
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

    private RequestQueue requestQueue;
    private String insertUrl = "https://projectcpms99.000webhostapp.com/scripts/chandula/editItemQty.php";
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

                    CharSequence msg = "Item Edited";
                    Toast.makeText(inventory_edit_item.this, msg, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(inventory_edit_item.this, inventory_edit_list.class);
                    startActivity(intent);


                }








        });
    }

    private void addListenerOnCancel(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(inventory_edit_item.this, inventory_edit_list.class);
                startActivity(intent);
            }
        });
    }


}
