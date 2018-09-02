package com.construction.app.cpms.miscellaneous;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.bean.ForumPost;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumsFragment extends Fragment {

    /*Database stuff*/
    private  StringRequest stringRequest;
    private  RequestQueue requestQueue;
    private String URL_PHP_SCRIPT = "http://projectcpms99.000webhostapp.com/scripts/fetchForumPosts.php";

    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private ArrayList<ForumPost> postArrayList;  // Forum class is a bean.
    private ForumRecyclerViewAdapter forumRecyclerViewAdapter;


    public ForumsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forums, container, false);
       // View view = inflater.inflate(R.layout.layout_forum_card, container, false);

        requestQueue = Volley.newRequestQueue(getContext());

        recyclerView = view.findViewById(R.id.recyclerView);
        postArrayList = new ArrayList<ForumPost>();


        fetchdata();

        System.out.println("On create executing");

        //setting layout manager for recycler view.
        gridLayoutManager = new GridLayoutManager(getContext(),1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        forumRecyclerViewAdapter = new ForumRecyclerViewAdapter(getContext(),postArrayList);    //create adaoter
        recyclerView.setAdapter(forumRecyclerViewAdapter);  //set adapter

        //top bar setting..
        setUpTopBar(view);


        return view;
    }

    private void fetchdata(){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                    System.out.println("Do backgorund func");
                stringRequest = new StringRequest(Request.Method.POST, URL_PHP_SCRIPT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ON RESPONSE");

                        try {
                             JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i<jsonArray.length(); i++){ //loop through jsonarray(stores objects in each index) and put data to arraylist.
                                System.out.println("FOR LOOP");
                                JSONObject object = jsonArray.getJSONObject(i);     //get the JSON object at index i

                                ForumPost forumPost = new ForumPost(object.getString("forumId"), object.getString("title"),
                                        object.getString("postedBy"), object.getString("body"));
                                System.out.println(object.getString("title"));
                                //populate arraylist
                                postArrayList.add(forumPost);
                            }
                            forumRecyclerViewAdapter.notifyDataSetChanged();    //if you dont notify adapter about updates to arraylist so recycler view can load them up.

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){

                };
                requestQueue.add(stringRequest);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                forumRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
        };

        asyncTask.execute();
    }




    //Used mdc codelabs as reference
    private void setUpTopBar(View view){
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if(activity != null){
            activity.setSupportActionBar(toolbar);
        }
    }

}
