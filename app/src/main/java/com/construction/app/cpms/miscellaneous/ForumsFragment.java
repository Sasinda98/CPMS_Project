package com.construction.app.cpms.miscellaneous;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private Toolbar toolbar;

    /*Database stuff*/
    private  static StringRequest stringRequest;
    private  static RequestQueue requestQueue;
    private  static String URL_PHP_SCRIPT = "http://projectcpms99.000webhostapp.com/scripts/gayal/fetchForumPosts.php";

    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private static ArrayList<ForumPost> postArrayList;  // Forum class is a bean.
    private static ForumRecyclerViewAdapter forumRecyclerViewAdapter;

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

        requestQueue = Volley.newRequestQueue(getContext());

        recyclerView = view.findViewById(R.id.recyclerView);
        postArrayList = new ArrayList<ForumPost>();


        //fetchdata();

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

    private static void fetchdata(){

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


    @Override
    public void onResume() {
        super.onResume();
        //fetchdata();
    }

    @Override
    public void onStart() {
        super.onStart();
        postArrayList.clear();
        fetchdata();

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

    }

    //called when user taps on option menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.searchPost :
                Toast toast = Toast.makeText(getContext(), "Search Selected", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.addPost :
             /*   Toast toast1 = Toast.makeText(getContext(), "Add Post Selected", Toast.LENGTH_SHORT);
                toast1.show();*/
                Intent intent = new Intent(getActivity(), addForumPost.class);
                startActivity(intent);

                break;
            case R.id.allPosts :
                Toast toast2 = Toast.makeText(getContext(), "AllPOSTS Submenu Selected", Toast.LENGTH_SHORT);
                toast2.show();
                filterByUserId(getLoggedInUserId(), false);    //false loads all posts
                break;
            case R.id.myPosts :
                Toast toast3 = Toast.makeText(getContext(), "MYPOSTS Submenu Selected", Toast.LENGTH_SHORT);
                toast3.show();
                filterByUserId(getLoggedInUserId(), true);     //true filters
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
    @Override
    public boolean onQueryTextChange(String input) {

        ArrayList<ForumPost> filtered = new ArrayList<>();

        for (ForumPost f:postArrayList) {
            if(f.getTitle().toLowerCase().contains(input.toLowerCase())){
                filtered.add(f);
            }
        }
        forumRecyclerViewAdapter.setFilterList(filtered);
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
    private void filterByUserId(String userId, boolean doFilter){
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        ArrayList<ForumPost> container = new ArrayList<>();
        ArrayList<ForumPost> backupList = new ArrayList<>();
        backupList = postArrayList;

        if(doFilter == true) {

            activity.getSupportActionBar().setSubtitle("My Posts");

            for (ForumPost f : postArrayList) {
                System.out.println(f.getPostedBy());

                if (Integer.parseInt(f.getPostedBy().toString()) == Integer.parseInt(userId)) {
                    System.out.println("Inner FOR LOOOP RUNS!");
                    container.add(f);
                }
            }
            System.out.println("CONTAINER SIZE==============================" + container.size());
            forumRecyclerViewAdapter.setFilterList(container);
        }
        else if(doFilter == false){

            activity.getSupportActionBar().setSubtitle("All Posts");
            //restore
            forumRecyclerViewAdapter.setFilterList(backupList);
        }

    }



}
