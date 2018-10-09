package com.construction.app.cpms.miscellaneous;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.adapters.ForumCommentRecycAdapter;
import com.construction.app.cpms.miscellaneous.adapters.ForumRecyclerViewAdapter;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseComment;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseForumPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class viewMoreForumPostActivity extends AppCompatActivity {

    private static final String TAG = "viewMoreForum";

    private Toolbar toolbar;
    private AppCompatActivity appCompatActivity;
    private EditText commentPadET;
    private ImageButton sendBtn;

    /*//region TRANSFERED VARS
    private String projectId = "1";     //depends on another member's function, the value should come from that function which is not yet implemented. hardcoded to 1.
*/
    /*FIREBASE vars*/
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser loggedInAs = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference;

    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private ArrayList<FirebaseComment> commentArraylist;  // Forum class is a bean.
    private ForumCommentRecycAdapter forumCommentRecycAdapter;

    private NestedScrollView nestedScrollView;

    //endregion
    private String postTitle;
    private String postBody;
    private String postPostedBy;
    private String postTimeStamp;

    //region TRANSFERED VARS
    private String projectId;     //depends on another member's function, the value should come from that function which is not yet implemented. hardcoded to 1.
    private String postID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more_forum_post);
        setUpToolbar();

        nestedScrollView = findViewById(R.id.nestedScrollView);
        commentPadET = findViewById(R.id.cr_cr_CommentForumTextInputEditText);
        sendBtn = findViewById(R.id.comment_sendbtn);    //send button of the message, user clicks when they want to send message

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {

            projectId = extras.getString("avmfp_projectID");
            postID = extras.getString("avmfp_postID");

            Log.d(TAG, "Project Id = " + projectId);
            Log.d(TAG, "Post Id = " + postID);


            postTitle = extras.getString("avmfp_postTitle");             // retrieve the data using keyName
            postBody = extras.getString("avmfp_postBody");
            postPostedBy = extras.getString("avmfp_postPostedBy");
            postTimeStamp = extras.getString("avmfp_postTimeStamp");

            Log.d(TAG, "Received from bundle, postTitle = " + postTitle + " postBody = " + postPostedBy);    //project Id should be integer string. no letters.

            TextView postTitleTV = findViewById(R.id.viewMore_postTitle);
            TextView postBodyTV = findViewById(R.id.viewMore_body);
            TextView postPostedByTV = findViewById(R.id.viewMore_postedBy);
            TextView postTimeStampTV = findViewById(R.id.viewMore_timeStamp);
            postTitleTV.setText(postTitle);
            postBodyTV.setText(postBody);
            postPostedByTV.setText(postPostedBy);
            postTimeStampTV.setText(postTimeStamp);

            recyclerView = findViewById(R.id.forumLogRecycView);
            commentArraylist = new ArrayList<FirebaseComment>();


            gridLayoutManager = new GridLayoutManager(this,1, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);

            forumCommentRecycAdapter = new ForumCommentRecycAdapter(this,commentArraylist, projectId);    //create adaoter
            recyclerView.setAdapter(forumCommentRecycAdapter);  //set adapter


          /*  FirebaseComment c1 = new FirebaseComment("asca", "asdasd", "09:10:2018 10:38 PM", "asdasd");


            FirebaseComment c2 = new FirebaseComment("asca", "asdasd", "09:10:2018 10:38 PM", "asdasd");


            FirebaseComment c3 = new FirebaseComment("asca", "asdasd", "09:10:2018 10:38 PM", "asdasd");

            commentArraylist.add(c1);
            commentArraylist.add(c2);
            commentArraylist.add(c3);
            forumCommentRecycAdapter.notifyDataSetChanged();
            */


            populateView(projectId, postID);


            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createForumCommentFirebase(projectId,postID);
                }
            });


        }


        }


    public void setUpToolbar(){

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forum-ViewMore");

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

    private ValueEventListener listenerMain;        //needed to remove the listener when performing filtering on the arraylist, to keep data consistent.

    private void populateView(String projectId,String ForumPostID){
        databaseReference = database.getReference().getRoot()
                .child("ForumsComments")            /*Make modular!*/
                .child("Project-P" + projectId )
                .child(ForumPostID);

        databaseReference.keepSynced(true);     //offline capabilities

        listenerMain =  new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot = " + dataSnapshot.toString());

                if(dataSnapshot.exists()){
                    commentArraylist.clear();
                 /*   backupSearchList.clear(); */          //refer onQueryTextChange() method... relevant for searchfunc.
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        //Log.d(TAG, "Datasnapshot for loop " + ds.toString());

                        FirebaseComment comment = ds.getValue(FirebaseComment.class);

                        Log.d(TAG, "comment = " + comment.getComment());
                        Log.d(TAG, "timestamp = " + comment.getTimeStamp());

                        commentArraylist.add(comment);
                    }
                    forumCommentRecycAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(listenerMain);

    }



    //region FIREBASE COMMENTING
    private DatabaseReference reference;

    public void createForumCommentFirebase(String projectId, String ForumPostID){
        reference = database.getReference().getRoot()
                .child("ForumsComments")            /*Make modular!*/
                .child("Project-P" + projectId )
                .child(ForumPostID);

        String commentID = reference.push().getKey();      //gets unique identifier from firebase.
        Log.d(TAG, "COMMENT ID FIREBASE GEN = " + commentID);

        FirebaseComment commentObj = prepareComment(commentID, postID);     //prepare post is a helper method for post object creation and user input handling.

        if(commentID != null){       //if post object is non null, add it to firebase database
            reference.child(commentID).setValue(commentObj);
        }else{
            Toast.makeText(this, "Type comment in", Toast.LENGTH_SHORT);
        }

    }

    //returns post that need to be posted. if unsuccessful it will return null.
    //also Toast message will be shown to alert the user.
    public FirebaseComment prepareComment(String commentIDFirebase, String postID){
        Log.d(TAG, "processMessageForSending(String senderUID)  CALLED");

        //take the user input from EditTEXTS
        String comment = this.commentPadET.getText().toString().trim();  //remove white spaces either end.       /


        if( ( comment.equals("") ) || ( comment == null )){
            this.commentPadET.setError("You need to type the comment in.");
            return null;        //terminate
        }

        FirebaseComment commentObj = new FirebaseComment( comment , loggedInAs.getUid(), getCurrentDateTime(), commentIDFirebase, postID);

        return commentObj;
    }



    //this method returns current date and time.
    public String getCurrentDateTime(){
        Date date = new Date();
        String timeFormat = "dd:MM:yyyy hh:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        String timeStamp = simpleDateFormat.format(date);

        return timeStamp;
    }
    //endregion FIREBASE COMMENTING


}
