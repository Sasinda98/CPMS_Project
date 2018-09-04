package com.construction.app.cpms.miscellaneous.bean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.ForumsFragment;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//form represents class in java, that could hold a single forum database record.
public class ForumPost {

    private String forumId;
    private String title;
    private String postedBy;
    private String body;


    public ForumPost(String forumId, String title, String postedBy, String body) {
        this.forumId = forumId;
        this.title = title;
        this.postedBy = postedBy;
        this.body = body;
    }


    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    /*Database stuff here*/
    private static RequestQueue requestQueue;
    private static String URL_PHP_SCRIPT="http://projectcpms99.000webhostapp.com/scripts/gayal/deleteForumPost.php";
    private static ForumPost forumPost_p;
    private static String loggedInUID_p;

    public static void deletePost(Context context, final String loggedInUID, ForumPost forumPost){
        System.out.println(" DELETE func");
        /*Database stuff*/
        requestQueue = Volley.newRequestQueue(context);

        forumPost_p = forumPost;
        loggedInUID_p = loggedInUID;

        System.out.println("FORUM ID =========== " + forumPost_p.getForumId());

        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                System.out.println("Do backgorund func");
               StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PHP_SCRIPT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ON RESPONSE DELETE======================");

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){

                   @Override
                   protected Map<String, String> getParams() throws AuthFailureError {
                       HashMap<String,String> hashMap = new HashMap<>();
                       hashMap.put("forumId", forumPost_p.getForumId());
                       hashMap.put("userId", loggedInUID_p);
                       return hashMap;
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


    //Inserts post to db
    public static void insertPost(final Context context, ForumPost forumPost, String loggedInUID){

        URL_PHP_SCRIPT="http://projectcpms99.000webhostapp.com/scripts/gayal/insertForumPost.php";

        requestQueue = Volley.newRequestQueue(context);

        forumPost_p = forumPost;
        loggedInUID_p = loggedInUID;

        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PHP_SCRIPT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ON RESPONSE");


                        try {
                            JSONObject jsonArray = new JSONObject(response);
                           

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
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("title", forumPost_p.getTitle());
                        hashMap.put("postedBy", loggedInUID_p);
                        hashMap.put("body", forumPost_p.getBody());
                        return hashMap;
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

    //Updates post db
    public static void updatePost(Context context, ForumPost forumPost){

        URL_PHP_SCRIPT="http://projectcpms99.000webhostapp.com/scripts/gayal/updateForumPost.php";

        requestQueue = Volley.newRequestQueue(context);

        forumPost_p = forumPost;

        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PHP_SCRIPT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ON RESPONSE UPDATE POST========================");


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("forumId", forumPost_p.getForumId());
                        hashMap.put("title", forumPost_p.getTitle());
                        hashMap.put("body", forumPost_p.getBody());
                        return hashMap;
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




}


