package com.construction.app.cpms.miscellaneous;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.construction.app.cpms.miscellaneous.bean.ForumPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class editForumPost extends AppCompatActivity {

    private Toolbar toolbar;
    private AppCompatActivity appCompatActivity;


    /*Database stuff*/
    private  static StringRequest stringRequest;
    private  static RequestQueue requestQueue;
    private  static String URL_PHP_SCRIPT = "http://projectcpms99.000webhostapp.com/scripts/gayal/fetchSingleForumPost.php";
    private static boolean isItOkay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_forum_post);

        requestQueue = Volley.newRequestQueue(this);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            String forumId = extras.getString("forumId"); // retrieve the data using keyName
            System.out.println("FORUM ID TO EDIT=========================================" +forumId);
            Toast.makeText(this,forumId,Toast.LENGTH_LONG).show();
            fetchdata(forumId); //populate edit activity with ui elements with their respective text values
        }


        setUpToolbar();
    }

    public void setUpToolbar(){

        toolbar = findViewById(R.id.toolbar);
        appCompatActivity = this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Forum Post");

        toolbar.setNavigationIcon(R.drawable.ic_forum_down_arrow);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.btm_navbar_item_notchecked));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //used stackoverflow forum to find this..
                appCompatActivity.onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.forum_edit_post_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ep_editPost :
                if(isItOkay==true){
                    /*New values*/
                     EditText newTitle = findViewById(R.id.ep_forumTitle_editText);
                     EditText newDescription = findViewById(R.id.ep_forumDescription_editText);

                    ForumPost updated =new ForumPost(forumPost.getForumId().toString(),newTitle.getText().toString(),forumPost.getPostedBy().toString(), newDescription.getText().toString());
                    ForumPost.updatePost(this, updated);
                    Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    appCompatActivity.onBackPressed();
                    finish();//kill activity
                }else{
                    Toast.makeText(this, "Please Wait..", Toast.LENGTH_LONG).show();
                }

        }

        return super.onOptionsItemSelected(item);
    }

    ForumPost forumPost;
    //pulling datafrom db and filling the ui components
    private void fetchdata(final String forumId){


        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                System.out.println("Do backgorund func");
                stringRequest = new StringRequest(Request.Method.POST, URL_PHP_SCRIPT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ON RESPONSE");

                       final EditText title = findViewById(R.id.ep_forumTitle_editText);
                       final EditText description = findViewById(R.id.ep_forumDescription_editText);
                        try {
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i<jsonArray.length(); i++){ //loop through jsonarray(stores objects in each index) and put data to arraylist.
                                System.out.println("FOR LOOP");
                                JSONObject object = jsonArray.getJSONObject(i);     //get the JSON object at index i

                                 forumPost = new ForumPost(object.getString("forumId"), object.getString("title"),
                                        object.getString("postedBy"), object.getString("body"));
                                 title.setText(forumPost.getTitle().toString());
                                 description.setText(forumPost.getBody().toString());
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
                        set.put("forumId", forumId );
                        return set;
                    }
                };
                requestQueue.add(stringRequest);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                isItOkay = true;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
        };

        asyncTask.execute();
    }
}
