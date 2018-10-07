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


public class ViewProject extends AppCompatActivity {

    private RecyclerView recyclerView2;
    private GridLayoutManager gridLayoutManager2;
    private ProjectViewAdapter pAdapter;
    private List<My_Data> dataList;
    private static String URL_PHP_SCRIPT = "https://projectcpms99.000webhostapp.com/scripts/Harshan/fetchProjects.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    private static final String TAG = "viewProject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_project);

        Log.d(TAG, "onCreate: Started");
        requestQueue = Volley.newRequestQueue(this);

        //new stuff, just in case delete everything below
        recyclerView2 = (RecyclerView) findViewById(R.id.recycler_view_project);
        dataList = new ArrayList<>();
        load_from_server(0);


        gridLayoutManager2 = new GridLayoutManager(this, 2);
        recyclerView2.setLayoutManager(gridLayoutManager2);

        pAdapter = new ProjectViewAdapter(this, dataList);
        recyclerView2.setAdapter(pAdapter);


    }

    private void load_from_server(final int PID) {
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
                                My_Data data = new My_Data(object.getInt("PID"), object.getString("Name"),object.getString("Description"));
                                dataList.add(data); //all objects are added to the arrayList
                            }
                            //notifies the adapter about updates to arrayList.
                            pAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CharSequence message4 = "Error. No Internet Access!";
                        Toast.makeText(ViewProject.this, message4, Toast.LENGTH_LONG).show();
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
                pAdapter.notifyDataSetChanged();
            }
        };
        task.execute();

    }
}
