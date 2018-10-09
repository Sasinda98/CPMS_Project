package com.construction.app.cpms.expenses;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class editExpense extends AppCompatActivity {

    private  static StringRequest stringRequest;
    private  static RequestQueue requestQueue;
    private  static String URL_PHP_SCRIPT = "http://projectcpms99.000webhostapp.com/scripts/ayyoob/fetchExpense.php"; //script for retrieving expense to be edited
    private  static String URL_PHP_SCRIPT_2 = "http://projectcpms99.000webhostapp.com/scripts/ayyoob/editExpense.php";//script to update the expense
    private TextInputEditText description = null;
    private TextInputEditText category = null;
    private TextInputEditText amount = null;
    private String expId = null;
    private String expCategory = null;
    private Button confirm = null;
    Expense expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        requestQueue = Volley.newRequestQueue(this);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            expId = extras.getString("expId"); // retrieve the data using keyName
            System.out.println("Expense ID TO EDIT=========================================" +expId);
            Toast.makeText(this,expId,Toast.LENGTH_LONG).show();
            fetchData(expId); //populate edit activity with ui elements with their respective text values
        }
        description = findViewById(R.id.expense_description_e);
        category = findViewById(R.id.expense_category_e);
        amount = findViewById(R.id.expense_amount_e);
        confirm = findViewById(R.id.expense_confirm_button);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateData(expId);
                Intent intent = new Intent(editExpense.this, expenseCategoryList.class);
                intent.putExtra("expCategory", expCategory);

                startActivity(intent);

            }
        });



    }

    private void fetchData(final String expId){


        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                System.out.println("Do backgorund func");
                stringRequest = new StringRequest(Request.Method.POST, URL_PHP_SCRIPT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ON RESPONSE");

                        final EditText description_e = findViewById(R.id.expense_description_e);
                        final EditText amount_e = findViewById(R.id.expense_amount_e);
                        final EditText category_e = findViewById(R.id.expense_category_e);
                        try {
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i<jsonArray.length(); i++){ //loop through jsonarray(stores objects in each index) and put data to arraylist.
                                System.out.println("FOR LOOP");
                                JSONObject object = jsonArray.getJSONObject(i);     //get the JSON object at index i

                                String expenseID = object.getString("expenseID");
                                String description = object.getString("description");
                                String category = object.getString("category");
                                double amount = Double.valueOf(object.getString("Amount"));

                                expense = new Expense(expenseID, description, category, amount);

                                description_e.setText(expense.getDescription().toString());
                                amount_e.setText(String.valueOf(expense.getAmount()).toString());
                                category_e.setText(expense.getCategory().toString());
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> set = new HashMap<>();
                        set.put("expId", expId );
                        return set;
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
            }
        };

        asyncTask.execute();
    }

    private void updateData(final String expId){

        StringRequest request = new StringRequest(Request.Method.POST, URL_PHP_SCRIPT_2, new Response.Listener<String>() {
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
                params.put("expId", expId);
                params.put("descr", description.getText().toString());
                params.put("catg", category.getText().toString());
                params.put("amt", amount.getText().toString());

                return params;
            }


        };

        expCategory = category.getText().toString();
        requestQueue.add(request);




    }

}


