package com.construction.app.cpms.Plan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.construction.app.cpms.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.MainActivity;
import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.bean.ForumPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanReport extends AppCompatActivity {

    private RecyclerView recyclerView3;
    private GridLayoutManager gridLayoutManager3;
    private ReportViewAdapter rAdapter;
    private List<ReportData> report_Data;
    private static String URL_PHP_SCRIPT = "https://projectcpms99.000webhostapp.com/scripts/Harshan/fetchPlans.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    private static final String TAG = "viewReport";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_report);

        Log.d(TAG, "onCreate: Started");
        requestQueue = Volley.newRequestQueue(this);

        //new stuff, just in case delete everything below
        recyclerView3 = (RecyclerView) findViewById(R.id.recycler_view_plan_report);
        report_Data = new ArrayList<>();
        load_server(0);


        gridLayoutManager3 = new GridLayoutManager(this, 1);
        recyclerView3.setLayoutManager(gridLayoutManager3);

        rAdapter = new ReportViewAdapter(this, report_Data);
        recyclerView3.setAdapter(rAdapter);

    }

    //database part
    private void load_server(final int pID) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... integers) {
                stringRequest = new StringRequest(com.android.volley.Request.Method.POST, URL_PHP_SCRIPT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                System.out.println("FOR LOOP");
                                JSONObject object = jsonArray.getJSONObject(i);
                                ReportData data = new ReportData(object.getInt("pID"), object.getString("Name"), object.getString("Image"), object.getString("Status"));
                                report_Data.add(data); //all objects are added to the arrayList
                            }
                            //notifies the adapter about updates to arrayList.
                            rAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CharSequence message4 = "Error. No Internet Access!";
                        Toast.makeText(PlanReport.this, message4, Toast.LENGTH_LONG).show();
                    }
                }) {
                    //nothing to end since all plans are displayed
                    /*
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("name", "Lex Luthor Mansion");
                        return hashMap;
                    } */
                };
                requestQueue.add(stringRequest);
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                rAdapter.notifyDataSetChanged();
            }
        };
        task.execute();
    }

}
