package com.construction.app.cpms.expenses;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class expenseCategoryList extends AppCompatActivity {


    /*Database Variables*/
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private String catFetchURL = "https://projectcpms99.000webhostapp.com/scripts/ayyoob/fetchingExpenses.php"; //script to retrieve data
    private String deleteURL = "https://projectcpms99.000webhostapp.com/scripts/ayyoob/deleteExpense.php";//script to delete data
   //Global Strings
    private String expCategory;//variable to retrieve selected category from intent
    private String projectID;
    private String userType;

    private static ArrayList<Expense> expenseArrayList;//arrayList to store retrieved data
    private ExpenseListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense__category__list);

        //getting project ID from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("projSwitch", Context.MODE_PRIVATE);
        projectID = sharedPreferences.getString("projSwitchID", "");

        SharedPreferences pref = getSharedPreferences("jobSwitch", Context.MODE_PRIVATE);
        userType = String.valueOf(pref.getString("jobRole", "")) ;

        //getting category from intent
        Intent intent = getIntent();
        expCategory = intent.getStringExtra("expCategory");

        //setting apptitle based on category from intent
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        String category = null;
        switch(expCategory) {
            case "Direct":
                category = "Direct Costs";
                break;
            case "Miscell":
                category = "Miscellaneous Costs";
                break;
            case "Consult":
                category = "Consultation Costs";
                break;
            case "Overheads":
                category = "Overhead Costs";
                break;
            default:
                break;
        }
        toolbar.setTitle(category);

        requestQueue = Volley.newRequestQueue(expenseCategoryList.this);
        expenseArrayList = new ArrayList<Expense>();

        //database access
        fetchdata();

        ListView listView = findViewById(R.id.exp_listView);
        adapter = new ExpenseListAdapter(this, R.layout.expenses_adapter_view_layout, expenseArrayList);
        listView.setAdapter(adapter);

        //making contextmenu available for listview
        registerForContextMenu(listView);

        //if user is not project manager cannot edit or delete any expenses
        if(!userType.equals("ProjectManager")){
            listView.setLongClickable(false);
        }else{
            listView.setLongClickable(true);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exp_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.deleteExp:
                int index = info.position;
                System.out.println("===================================================================================================================================================="+index);
                String expID = expenseArrayList.get(index).getExpenseID();
                System.out.println("Expense ID ================================================================================"+expID);
                expenseArrayList.remove(info.position);
                deleteExpense(expID);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.editExp:
                index = info.position;
                expID = expenseArrayList.get(index).getExpenseID();

                Intent intent = new Intent(expenseCategoryList.this, editExpense.class);
                intent.putExtra("expId", expID);

                startActivity(intent);

                default:
                    return super.onContextItemSelected(item);


        }

        //return super.onContextItemSelected(item);
    }


    private void deleteExpense(final String expId){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                stringRequest = new StringRequest(Request.Method.POST, deleteURL, new Response.Listener<String>() {
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

                                System.out.println("Description= " + expense.getDescription()+"Category of Expense= " + category +"Amount= " + amount);

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
                        HashMap<String, String> params = new HashMap<>();
                        params.put("expId", expId);
                        return params;
                    }

                };
                requestQueue.add(stringRequest);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                CharSequence msg = "Successfully Deleted Expense" + expId;
                Toast.makeText(expenseCategoryList.this, msg, Toast.LENGTH_LONG).show();

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
        };

        asyncTask.execute();
    }




    private void fetchdata(){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                stringRequest = new StringRequest(Request.Method.POST, catFetchURL, new Response.Listener<String>() {
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

                                System.out.println("Description= " + expense.getDescription()+"Category of Expense= " + category +"Amount= " + amount);

                                //populate arraylist
                                expenseArrayList.add(expense);
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

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("pID", projectID);
                        params.put("category", expCategory);
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
                Toast.makeText(expenseCategoryList.this, msg, Toast.LENGTH_LONG).show();
            }
        };

        asyncTask.execute();
    }


}
