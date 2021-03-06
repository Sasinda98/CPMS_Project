package com.construction.app.cpms.inventoryManagement;
// Used as tutorial -->  https://www.mkyong.com/android/android-radio-buttons-example/
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
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class inventory_add_item extends AppCompatActivity {

    private RadioGroup radioCatGroup = null;
    private RadioButton radioCatButton = null;
    private Button btnConfirm = null;
    private Button btnCancel = null;
    private TextInputEditText itemName = null;
    private TextInputEditText itemQty = null;
    private TextInputEditText lowTh = null;
    private Spinner spinner = null;
    private String projectID;


    private RequestQueue requestQueue;
    private String insertUrl = "https://projectcpms99.000webhostapp.com/scripts/chandula/insertInventoryItem.php";
    private StringRequest stringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_add_item);

        itemName = findViewById(R.id.add_item_name_editText);
        itemQty = findViewById(R.id.add_item_qty_editText);
        lowTh = findViewById(R.id.add_item_lowTh_editText);
        radioCatGroup = (RadioGroup) findViewById(R.id.add_items_radio_group);
        btnConfirm = (Button) findViewById(R.id.add_item_confirm_btn);
        btnCancel = (Button) findViewById(R.id.add_item_cancel_btn);
        spinner = (Spinner) findViewById(R.id.units_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.inventory_units_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);



        //Temporary projectID
        SharedPreferences pref = getSharedPreferences("projSwitch", Context.MODE_PRIVATE);
        projectID = pref.getString("projSwitchID", "");

//What happens when confirm button is clicked
        addListenerOnConfirm();
        addListenerOnCancel();




    }

    private void addListenerOnCancel() {


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearTextViews();


                CharSequence msg = "Cancelled";
                Toast.makeText(inventory_add_item.this, msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(inventory_add_item.this, inventory_manager_panel.class);
                startActivity(intent);
            }
        });

    }

    private void addListenerOnConfirm() {


        requestQueue = Volley.newRequestQueue(this.getApplicationContext());


        //Linking the views on xml file to java file


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Variables Required for Validation checks
                Boolean itemNameCheckResult;
                Boolean itemQtyCheckResult;
                Boolean itemThCheckResult;

                itemNameCheckResult = editTextEmptyCheck(itemName);
                itemQtyCheckResult = editTextEmptyCheck(itemQty);
                itemThCheckResult = editTextEmptyCheck(lowTh);

                //Validation checks and associated error Messages
                if(itemNameCheckResult == true){

                    itemName.setError("Item Name is required");

                }else if(itemQtyCheckResult == true) {

                    itemQty.setError("Item Quantity is required");

                }else if(itemThCheckResult == true){

                    lowTh.setError("Low Threshold is required");
                }
                //All checks have been passed
                else{


                    //Get the ID of selected radio button
                    int radioButtonID = radioCatGroup.getCheckedRadioButtonId();
                    //use the id to find the button
                    radioCatButton = (RadioButton) findViewById(radioButtonID);
                    //getting selected spinner value
                    final String spinnerText = (String) spinner.getSelectedItem();
                    //getting value of selected radio button
                    final String radioText = (String) radioCatButton.getText();




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
                            params.put("iName", itemName.getText().toString());
                            params.put("iCat", radioText);
                            params.put("iQty", itemQty.getText().toString());
                            params.put("iUnit", spinnerText);
                            params.put("iLTh", lowTh.getText().toString());
                            params.put("pID", projectID);
                            return params;
                        }
                    };

                    requestQueue.add(request);

                    CharSequence msg = "Item Added";
                    Toast.makeText(inventory_add_item.this, msg, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(inventory_add_item.this, inventory_manager_panel.class);
                    startActivity(intent);

                }

            }
        });
    }

    //Currently unused
    private void clearTextViews(){
        itemName.getText().clear();
        itemQty.getText().clear();

    }

    //Method to check if the given editText is blank or empty
    // Got the concept from https://www.codebrainer.com/blogs/registration_form_in_android_check_email_is_valid_is_empty
    private boolean editTextEmptyCheck(EditText editText){
        CharSequence string = editText.getText().toString();
        if(TextUtils.isEmpty(string)){
            return true;
        }
        else{
            return  false;
        }
    }


}
