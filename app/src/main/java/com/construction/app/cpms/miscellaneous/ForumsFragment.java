package com.construction.app.cpms.miscellaneous;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.adapters.ForumRecyclerViewAdapter;
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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumsFragment extends Fragment implements SearchView.OnQueryTextListener {

    final public String TAG = "ForumsFragment";
    private Toolbar toolbar;

    private String projectId = "1";     //depends on another member's function, the value should come from that function which is not yet implemented. hardcoded to 1.

    /*FIREBASE vars*/
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser loggedInAs = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference;


    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private ArrayList<FirebaseForumPost> postArrayList;  // Forum class is a bean.
    private ForumRecyclerViewAdapter forumRecyclerViewAdapter;

    private MenuItem menuItem;  //search func specific


    public ForumsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forums, container, false);
       // View view = inflater.inflate(R.layout.layout_forum_card, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);
        postArrayList = new ArrayList<FirebaseForumPost>();


        //fetchdata();

        System.out.println("On create executing");

        //setting layout manager for recycler view.
        gridLayoutManager = new GridLayoutManager(getContext(),1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        forumRecyclerViewAdapter = new ForumRecyclerViewAdapter(getContext(),postArrayList, loggedInAs);    //create adaoter
        recyclerView.setAdapter(forumRecyclerViewAdapter);  //set adapter

        //top bar setting..
        setUpTopBar(view);

        populateView(projectId);

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        //fetchdata();
    }

    @Override
    public void onStart() {
        super.onStart();
      //  postArrayList.clear();

    }

    //Used mdc codelabs as reference
    private void setUpTopBar(View view){
        setHasOptionsMenu(true);    //set to true, if not toolbar icons wont come up
        toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if(activity != null){
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setTitle("Forums");
            activity.getSupportActionBar().setSubtitle("All Posts");
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setSubtitleTextColor(getResources().getColor(R.color.btm_navbar_item_notchecked));

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //used stackoverflow forum to find this..
                    getFragmentManager().popBackStack();
                }
            });

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        //getActivity().getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        inflater.inflate(R.menu.menu_toolbar,menu);
        menuItem = menu.findItem(R.id.searchPost);      //getting reference
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(this);

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {

                Log.d(TAG,"Searchview close");
                populateView(projectId);    //firebase query to restore view.
                return true;
            }
        });


    }

    //called when user taps on option menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.searchPost :

                break;
            case R.id.addPost :
             /*   Toast toast1 = Toast.makeText(getContext(), "Add Post Selected", Toast.LENGTH_SHORT);
                toast1.show();*/
                Intent intent = new Intent(getActivity(), addForumPost.class);
                startActivity(intent);

                break;
            case R.id.allPosts :
                filterByUserId( false);    //false loads all posts
                break;
            case R.id.myPosts :
                filterByUserId( true);     //true filters
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //Search function specific method, corresponds to the user input when you tap on the search icon in toolbar
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    //Search function specific method, corresponds to the user input when you tap on the search icon in toolbar
    //whenever text in search box changes, it is passed in to 's' as param.
    private ArrayList<FirebaseForumPost> backupSearchList = new ArrayList<>();
    @Override
    public boolean onQueryTextChange(String input) {

        if(listenerMain != null)        //checking for null, just in case.
            databaseReference.removeEventListener(listenerMain);        //stop listening to changes... To avoid unexpected behavior.


        Log.d(TAG, "Backup size = " + backupSearchList.size());
        if(backupSearchList.size() == 0 ){
            backupSearchList.addAll(postArrayList);
        }



        ArrayList<FirebaseForumPost> filtered = new ArrayList<>();

        for (FirebaseForumPost f:backupSearchList) {
            if(f.getTitle().toLowerCase().contains(input.toLowerCase())){
                filtered.add(f);
            }
        }

        postArrayList.clear();
        postArrayList.addAll(filtered);
        forumRecyclerViewAdapter.notifyDataSetChanged();


        return true;
    }


    public String getLoggedInUserId(){
        /*Stackoverflow used as reference for use of sharepref in fragment*/
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String email = preferences.getString("email","");
        String password = preferences.getString("password","");
        String userId = preferences.getString("userId","");

        //   Toast.makeText(getContext(),"Details em " + email + " " + password, Toast.LENGTH_LONG).show();
        System.out.println("==============GET CREDENTIAL EXECUTED DASHBOARD=====================");
        return userId;

    }

    //filter out post by who posted the post.
    private void filterByUserId(boolean doFilter){
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        ArrayList<FirebaseForumPost> container = new ArrayList<>();
        ArrayList<FirebaseForumPost> backup = new ArrayList<>();

        backup.addAll(postArrayList);
        Log.d(TAG, "SIZE POST BACKUP = " + postArrayList.size());

        if(doFilter == true) {
            if(listenerMain != null)        //checking for null, just in case.
                databaseReference.removeEventListener(listenerMain);  //stop listening for changes for the posts node when you do client side filtering as doing so will interfere
                                                        //with the filter user is trying to do. (new post in to db while user filters the results will cause unexpected behavior)
                                                            //removing the listener prior to filtering the dataset retrieved solves this bug.

            activity.getSupportActionBar().setSubtitle("My Posts");

            postArrayList.clear();

            for (FirebaseForumPost post : backup) {

                if (post.getPostedByUID().equals(loggedInAs.getUid())) {    //if posted by logged in user populate container

                        //container.add(post);
                        postArrayList.add(post);
                }
            }

            forumRecyclerViewAdapter.notifyDataSetChanged();


        }
        else if(doFilter == false){

            activity.getSupportActionBar().setSubtitle("All Posts");
            //restore
            populateView(projectId);

        }

    }



    //region FIREBASE STUFF!!

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
                            backupSearchList.clear();           //refer onQueryTextChange() method... relevant for searchfunc.
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

    //endregion





}
