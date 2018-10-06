package com.construction.app.cpms.expenses;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class expenseReport extends AppCompatActivity {

    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private String URL_PHP_SCRIPT = "https://projectcpms99.000webhostapp.com/scripts/ayyoob/fetchAll.php";
    private static ArrayList<Expense> expenseArrayList;
    private ExpenseListAdapter adapter;
    private ListView listView;
    private String val = "1";
    private String totalExp;
    private TextView textView;
    private double sum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_report);


        textView = findViewById(R.id.reportTotalValue);



        requestQueue = Volley.newRequestQueue(expenseReport.this);
        expenseArrayList = new ArrayList<Expense>();

        fetchdata();
        ListView mListView = findViewById(R.id.exp_listView);

        adapter = new ExpenseListAdapter(this, R.layout.expenses_adapter_view_layout, expenseArrayList);
        mListView.setAdapter(adapter);





    }

    private void fetchdata(){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                stringRequest = new StringRequest(Request.Method.POST, URL_PHP_SCRIPT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            sum = 0;
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i<jsonArray.length(); i++){ //loop through jsonarray(stores objects in each index) and put data to arraylist.
                                JSONObject object = jsonArray.getJSONObject(i);//get the JSON object at index i

                                //Getting all the attributes of the bean from the JSON object
                                String expenseID = object.getString("expenseID");
                                String description = object.getString("description");
                                String category = object.getString("category");
                                double amount = Double.valueOf(object.getString("Amount"));


                                Expense expense = new Expense(expenseID, description, category, amount);

                                //populate arraylist
                                expenseArrayList.add(expense);

                            }
                            sum = totalExp(expenseArrayList);
                            textView.setText("LKR  " + Double.toString(sum));
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

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("val", val);
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
                CharSequence msg = "Loading...";
                Toast.makeText(expenseReport.this, msg, Toast.LENGTH_LONG).show();
            }
        };

        asyncTask.execute();
    }

    private double totalExp(ArrayList<Expense> newList) {
        double sum = 0;
        for (int i = 0; i < newList.size(); i++) {
            Expense expense = newList.get(i);
            sum += expense.getAmount();
        }
        return sum;
    }
}
