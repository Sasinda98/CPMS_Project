package com.construction.app.cpms.miscellaneous;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.bean.ForumPost;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseForumPost;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class addForumPost extends AppCompatActivity {

    private static final String TAG = "addForumPost";

  /*  private String projectId = "2";     //depends on another member's function, the value should come from that function which is not yet implemented. hardcoded to 1.
*/

    private String projectId = "1";

    /*FIREBASE vars*/
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser loggedInAs = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference;

    private Toolbar toolbar;
    private AppCompatActivity appCompatActivity;

    private EditText titleEditText;
    private EditText descriptionEditText; //body


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_forum_post);

        this.projectId = getProjectId();

        this.titleEditText = findViewById(R.id.ap_forumTitle_editText);
        this.descriptionEditText = findViewById(R.id.ap_forumDescription_editText);

        setUpToolbar(); //Toolbar init

    }

    public void setUpToolbar(){

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Post");

        toolbar.setNavigationIcon(R.drawable.ic_forum_down_arrow);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.btm_navbar_item_notchecked));
        appCompatActivity = this;
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
        //getActivity().getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        getMenuInflater().inflate(R.menu.forum_create_post_toolbar,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cp_createPost:

                createForumPostFirebase(projectId);         //method that adds post to firebase using user input;

       //         appCompatActivity.onBackPressed();  //return to main page
                break;
            }

        return super.onOptionsItemSelected(item);
    }

    public String getLoggedInUserId(){
        /*Stackoverflow used as reference for use of sharepref in fragment*/
        SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String email = preferences.getString("email","");
        String password = preferences.getString("password","");
        String userId = preferences.getString("userId","");

        //   Toast.makeText(getContext(),"Details em " + email + " " + password, Toast.LENGTH_LONG).show();
        System.out.println("==============GET CREDENTIAL EXECUTED DASHBOARD=====================");
        return userId;

    }

    public void createForumPostFirebase(String projectId){
        reference = database.getReference().getRoot()
                .child("ForumPosts")            /*Make modular!*/
                .child("Project-P" + projectId );

                String postId = reference.push().getKey();      //gets unique identifier from firebase.
                Log.d(TAG, "POST ID FIREBASE GEN = " + postId);

                FirebaseForumPost post = preparePost(postId);     //prepare post is a helper method for post object creation and user input handling.

                 if(post != null){       //if post object is non null, add it to firebase database
                    reference.child(postId).setValue(post);
                     appCompatActivity.onBackPressed();  //return to main page
                }else{
                    Toast.makeText(this, "Fill the relevant fields", Toast.LENGTH_SHORT);
                }

    }

    //returns post that need to be posted. if unsuccessful it will return null.
    //also Toast message will be shown to alert the user.
    public FirebaseForumPost preparePost(String postIdFirebase){
        Log.d(TAG, "processMessageForSending(String senderUID)  CALLED");

        //take the user input from EditTEXTS
        String postTitle = this.titleEditText.getText().toString().trim();  //remove white spaces either end.       /
        String postBody = this.descriptionEditText.getText().toString().trim();  //remove white spaces either end.       /

        if( ( postTitle.equals("") ) || ( postTitle == null )){
            this.titleEditText.setError("You need to type the title in.");
            return null;        //terminate
        }


        if( ( postBody.equals("") ) || ( postBody == null )){
            this.descriptionEditText.setError("You need to type the description in.");
            return null;        //terminate...
        }


        FirebaseForumPost forumPost = new FirebaseForumPost(postIdFirebase, postTitle, postBody, loggedInAs.getUid(), getCurrentDateTime());

        return forumPost;
    }



    //this method returns current date and time.
    public String getCurrentDateTime(){
        Date date = new Date();
        String timeFormat = "dd:MM:yyyy hh:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        String timeStamp = simpleDateFormat.format(date);

        return timeStamp;
    }

    //returns time only when given string with time in format mentioned inside the method.
    public String getTimeOnly(String dateTime) {
        //Time format Suppiorted = "dd:MM:yyyy hh:mm a"
        return dateTime.substring(11, dateTime.length() );
    }

    //returns date only when given string with time in format mentioned inside the method.
    public String getDateOnly(String dateTime) {
        //Time format Suppiorted = "dd:MM:yyyy hh:mm a"
        return dateTime.substring(0, 11);
    }


    //Grabs project Id from chandula's shared pref, temp function Switcher to demostrate change in project.
    public String getProjectId(){

        Log.d(TAG, "getProjectId() CALLED!");
        /*Stackoverflow used as reference for use of sharepref in fragment*/
        SharedPreferences preferences = getSharedPreferences("projSwitch", Context.MODE_PRIVATE);

        String projectID =  preferences.getString("projSwitchID", "1");


        projectID = projectID.trim();
        return projectID;
    }

}
