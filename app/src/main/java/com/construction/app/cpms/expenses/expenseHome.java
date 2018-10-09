package com.construction.app.cpms.expenses;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class expenseHome extends AppCompatActivity {

    private static final String TAG = "ExpensesActivity";
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private String fetchAllURL = "https://projectcpms99.000webhostapp.com/scripts/ayyoob/fetchAll.php"; //script to retrieve data
    private static ArrayList<Expense> expenseArrayList;//arrayList to store retrieved data
    private static ArrayList<Expense> recentExpenses;//duplicate array to store only 10 newest expenses
    private ExpenseListAdapter adapter;
    private String projectID; //project ID from shared preferences
    private String userType;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_expenses);

        //getting project ID from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("projSwitch", Context.MODE_PRIVATE);
        projectID = sharedPreferences.getString("projSwitchID", "");

        //getting userType from shared preferences
        SharedPreferences preferences = getSharedPreferences("jobSwitch", Context.MODE_PRIVATE);
        userType = String.valueOf(preferences.getString("jobRole", "")) ;

        requestQueue = Volley.newRequestQueue(expenseHome.this);
        expenseArrayList = new ArrayList<>();
        recentExpenses = new ArrayList<>();


        fetchdata();

        Log.d(TAG, "onCreate: Started");
        ListView mListView = findViewById(R.id.listView);



        adapter = new ExpenseListAdapter(this, R.layout.expenses_adapter_view_layout, recentExpenses);
        mListView.setAdapter(adapter);


        mListView.setBackgroundColor(Color.WHITE);


        //assigning buttons to category list page
        ImageButton directButt = findViewById(R.id.directButt);
        buttonPress(directButt, "Direct");

        ImageButton mButt = findViewById(R.id.miscellaneousButt);
        buttonPress(mButt, "Miscell");

        ImageButton cButt = findViewById(R.id.consultationButt);
        buttonPress(cButt, "Consult");

        ImageButton overButt = findViewById(R.id.overButt);
        buttonPress(overButt, "Overheads");

        //add expense button
        FloatingActionButton add = findViewById(R.id.addExpenses);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userType.equals("ProjectManager")) {
                    Intent intent = new Intent(expenseHome.this, Expense_Add.class);

                    startActivity(intent);

                }else{

                    Toast.makeText(expenseHome.this, "Contact Project Manager", Toast.LENGTH_LONG).show();

                }
            }
        });

        //report button
        Button button = findViewById(R.id.report);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                Intent intent = new Intent(expenseHome.this, expenseReport.class);

                startActivity(intent);



            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }



    private void buttonPress(ImageButton img, final String catg){

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(expenseHome.this, expenseCategoryList.class);
                intent.putExtra("expCategory", catg);

                startActivity(intent);
            }

        });
    }

    private void fetchdata(){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                stringRequest = new StringRequest(Request.Method.POST, fetchAllURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
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
                            addRecents(expenseArrayList);
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
                        params.put("pID", projectID);
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
                Toast.makeText(expenseHome.this, msg, Toast.LENGTH_LONG).show();
            }
        };

        asyncTask.execute();
    }

    private void addRecents(ArrayList<Expense> arrIn){
        if (arrIn.size() >= 10) // Make sure you really have 10 elements
        {
            recentExpenses.add(arrIn.get(arrIn.size()-1));
            recentExpenses.add(arrIn.get(arrIn.size()-2));
            recentExpenses.add(arrIn.get(arrIn.size()-3));
            recentExpenses.add(arrIn.get(arrIn.size()-4));
            recentExpenses.add(arrIn.get(arrIn.size()-5));
            recentExpenses.add(arrIn.get(arrIn.size()-6));
            recentExpenses.add(arrIn.get(arrIn.size()-7));
            recentExpenses.add(arrIn.get(arrIn.size()-8));
            recentExpenses.add(arrIn.get(arrIn.size()-9));
            recentExpenses.add(arrIn.get(arrIn.size()-10)); // The last


        }
    }



}