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
import android.widget.TextView;

import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.adapters.ForumRecyclerViewAdapter;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseForumPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class viewMoreForumPostActivity extends AppCompatActivity {

    private static final String TAG = "viewMoreForum";

    private Toolbar toolbar;
    private AppCompatActivity appCompatActivity;

    //region TRANSFERED VARS
    private String projectId = "1";     //depends on another member's function, the value should come from that function which is not yet implemented. hardcoded to 1.

    /*FIREBASE vars*/
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser loggedInAs = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference;

    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private ArrayList<FirebaseForumPost> postArrayList;  // Forum class is a bean.
    private ForumRecyclerViewAdapter forumRecyclerViewAdapter;

    private NestedScrollView nestedScrollView;

    //endregion
    private String postTitle;
    private String postBody;
    private String postPostedBy;
    private String postTimeStamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more_forum_post);
        setUpToolbar();

        nestedScrollView = findViewById(R.id.nestedScrollView);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
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
            postArrayList = new ArrayList<FirebaseForumPost>();


            gridLayoutManager = new GridLayoutManager(this,1, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);

            forumRecyclerViewAdapter = new ForumRecyclerViewAdapter(this,postArrayList, loggedInAs, projectId);    //create adaoter
            recyclerView.setAdapter(forumRecyclerViewAdapter);  //set adapter


            populateView(projectId);

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

    private void populateView(String projectId){
        databaseReference = database.getReference().getRoot()
                .child("ForumPosts")            /*Make modular!*/
                .child("Project-P" + projectId );

        databaseReference.keepSynced(true);     //offline capabilities

        listenerMain =  new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot = " + dataSnapshot.toString());

                if(dataSnapshot.exists()){
                    postArrayList.clear();
                 /*   backupSearchList.clear(); */          //refer onQueryTextChange() method... relevant for searchfunc.
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        //Log.d(TAG, "Datasnapshot for loop " + ds.toString());

                        FirebaseForumPost post = ds.getValue(FirebaseForumPost.class);

                        Log.d(TAG, "posted by = " + post.getPostedByUID());
                        Log.d(TAG, "posted title = " + post.getTitle());

                        postArrayList.add(post);
                    }
                    forumRecyclerViewAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(listenerMain);

    }
}
