package com.construction.app.cpms.miscellaneous;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseForumPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class editForumPost extends AppCompatActivity {

    private static final String TAG = "editForumPost";

    private Toolbar toolbar;
    private AppCompatActivity appCompatActivity;

    /*FIREBASE vars*/
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser loggedInAs = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference;

    /*vars passed from intent*/
    private String postId;
    private String projectId;

    /*UI ELEMENTS*/
    private EditText titleET;
    private EditText bodyET;


    /*vars for checking whether entries actually changed.*/
    private String title_database;
    private String body_database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_forum_post);

        titleET = findViewById(R.id.ep_forumTitle_editText);
        bodyET = findViewById(R.id.ep_forumDescription_editText);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            postId = extras.getString("postId");             // retrieve the data using keyName
            projectId = extras.getString("projectId");

            Log.d(TAG, "Received from bundle, postID = " + postId + " projectId = " + projectId ) ;    //project Id should be integer string. no letters.


            //USES FIREBASE
            populateView(postId, projectId); //populate edit activity with ui elements with their respective text values
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
            case R.id.ep_editPost : //confirmation button on the ui to top right corner, which the user uses to say that they are done editing.

                //USES FIREBASE
                editPost(postId, projectId);

                //go back to mainpage..
               // onBackPressed();      coded in to the editPost method itself, which is executed above,.
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void populateView(final String postId, String projectId){

        DatabaseReference referencePost = database.getReference().getRoot()     //references relevant post.
                .child("ForumPosts")            /*Make modular!*/
                .child("Project-P" + projectId )  //project ID is given prefix Project-P, Naming convention in the database.
                .child(postId);

                referencePost.keepSynced(true);

                referencePost.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            //Log.d(TAG, "DATA SNAPSHOT = " + dataSnapshot);

                            FirebaseForumPost post = dataSnapshot.getValue(FirebaseForumPost.class);
                            Log.d(TAG, "Title = " + post.getTitle());
                            Log.d(TAG, "Body = " + post.getBody());

                            title_database = post.getTitle();
                            body_database = post.getBody();

                            titleET.setText(post.getTitle());
                            bodyET.setText(post.getBody());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void editPost(String postId, String projectId){

        DatabaseReference postRef = database.getReference().getRoot()     //references relevant post.
                .child("ForumPosts")            /*Make modular!*/
                .child("Project-P" + projectId )  //project ID is given prefix Project-P, Naming convention in the database.
                .child(postId);

        //getting input from the edit texts.
        String titleEdited = titleET.getText().toString().trim();
        String bodyEdited = bodyET.getText().toString().trim();

        boolean isDatabaseUpdated = false;

        //checking whether user made changes to the input form, if so go ahead with the update.
        if(!body_database.equalsIgnoreCase(bodyEdited)){
            Log.d(TAG, "Body has been edited, changing value in firebase database now.");

            if(bodyEdited.length() > 0){    //mesaning there is some text, input not empty.
                isDatabaseUpdated = true;
                postRef.child("body").setValue(bodyEdited); //firebase
                onBackPressed();        /*CHECK HERE IF IRREGULARITIES ARE PRESENT WHEN EDITING BOTH TITLE AND BODY*/
            }else{
                bodyET.setError("You Can't Leave The Description Empty!");
            }
        }

        if(!title_database.equalsIgnoreCase(titleEdited)){
            Log.d(TAG, "Title has been edited, changing value in firebase database now.");

            if(bodyEdited.length() > 0){    //mesaning there is some text, input not empty.
                isDatabaseUpdated = true;
                postRef.child("title").setValue(titleEdited); //firebase
                onBackPressed();    /*CHECK HERE IF IRREGULARITIES ARE PRESENT WHEN EDITING BOTH TITLE AND BODY*/
            }else{
                titleET.setError("You Can't Leave The Title Empty!");
            }
        }

        //updating the dateTime of the post to the date and time the post was edited.
        if(isDatabaseUpdated == true){
            postRef.child("dateTime").setValue(getCurrentDateTime()); //firebase
        }

    }


    //this method returns current date and time.
    public String getCurrentDateTime(){
        Date date = new Date();
        String timeFormat = "dd:MM:yyyy hh:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        String timeStamp = simpleDateFormat.format(date);

        return timeStamp;
    }

}
