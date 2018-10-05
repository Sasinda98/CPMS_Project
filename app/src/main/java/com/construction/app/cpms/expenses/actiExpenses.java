package com.construction.app.cpms.expenses;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class actiExpenses extends AppCompatActivity {

    private static final String TAG = "ExpensesActivity";
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private String URL_PHP_SCRIPT = "https://projectcpms99.000webhostapp.com/scripts/ayyoob/fetchAll.php"; //script to retrieve data
    private static ArrayList<Expense> expenseArrayList;//arrayList to store retrieved data
    private static Expense sample;
    private ExpenseListAdapter adapter;
    private double sum;
    private String val = "1";
    TextView textView = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_expenses);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);
        }

        requestQueue = Volley.newRequestQueue(actiExpenses.this);
        expenseArrayList = new ArrayList<>();
        textView = findViewById(R.id.totalExp);

        fetchdata();

        Log.d(TAG, "onCreate: Started");
        ListView mListView = findViewById(R.id.listView);



        adapter = new ExpenseListAdapter(this, R.layout.expenses_adapter_view_layout, expenseArrayList);
        mListView.setAdapter(adapter);


        mListView.setBackgroundColor(Color.WHITE);



        ImageButton directButt = findViewById(R.id.directButt);
        buttonPress(directButt, "Direct");

        ImageButton mButt = findViewById(R.id.miscellaneousButt);
        buttonPress(mButt, "Miscell");

        ImageButton cButt = findViewById(R.id.consultationButt);
        buttonPress(cButt, "Consult");

        ImageButton overButt = findViewById(R.id.overButt);
        buttonPress(overButt, "Overheads");


        FloatingActionButton add = findViewById(R.id.addExpenses);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(actiExpenses.this, Expense_Add.class);

                startActivity(intent);
            }
        });


        Button button = findViewById(R.id.report);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                saveTextAsFile(expenseArrayList);
            }

        });
        //System.out.println("*******************************************************************************************" +sum);










    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void saveTextAsFile(ArrayList<Expense> expenseReport){

        String trial = "sample.txt";
        String testing = "Trying to write text to a file on Android";

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), trial);


        try {
            //FileOutputStream fos = new FileOutputStream(file);
            FileOutputStream fos = openFileOutput(trial, Context.MODE_APPEND);
            int size = expenseReport.size();
            for(int i = 0; i < size; i++){
                String str = expenseReport.get(i).toString();
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" +str);
                fos.write(str.getBytes());
                /*if (i < size - 1) {
                    fos.write("/n");
                }*/
            }
            fos.write(testing.getBytes());
            fos.close();




            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        }catch(FileNotFoundException e){
            e.printStackTrace();
            Toast.makeText(this, "File Not Found!", Toast.LENGTH_SHORT).show();
        }catch(IOException e){
            e.printStackTrace();
            Toast.makeText(this, "Error Saving File", Toast.LENGTH_SHORT).show();
        }



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1000:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void buttonPress(ImageButton img, final String catg){

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(actiExpenses.this, Expense_Category_List.class);
                intent.putExtra("expCategory", catg);

                startActivity(intent);
            }

        });
    }

    private void fetchdata(){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                stringRequest = new StringRequest(Request.Method.POST, URL_PHP_SCRIPT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            sum = 0;
                            for (int i = 0; i<jsonArray.length(); i++){ //loop through jsonarray(stores objects in each index) and put data to arraylist.
                                JSONObject object = jsonArray.getJSONObject(i);//get the JSON object at index i

                                //Getting all the attributes of the bean from the JSON object
                                String expenseID = object.getString("expenseID");
                                String description = object.getString("description");
                                String category = object.getString("category");
                                double amount = Double.valueOf(object.getString("Amount"));


                                Expense expense = new Expense(expenseID, description, category, amount);
                                //sum = sum + expense.getAmount();
                                //System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+sum);
                                //System.out.println("Description= " + expense.getDescription()+"Category of Expense= " + category +"Amount= " + amount);

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
                Toast.makeText(actiExpenses.this, msg, Toast.LENGTH_LONG).show();
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
