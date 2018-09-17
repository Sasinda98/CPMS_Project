package com.construction.app.cpms.miscellaneous;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.Navigation;
import com.construction.app.cpms.R;

import com.construction.app.cpms.SecondaryActivity;
import com.construction.app.cpms.miscellaneous.bean.ChatRoomMainItem;
import com.construction.app.cpms.miscellaneous.bean.ForumPost;
import com.construction.app.cpms.miscellaneous.bean.User;
import com.construction.app.cpms.userManagement.forgotPasswordFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {
    /*STATIC fields are because we need to access them inside async task etc*/
    private static String projectId = "1";   //to be passed in from Harshan's createPlans and Harshan's user_plan
    private String userId;      //logged in user's userid.

    private static ArrayList<ChatRoomMainItem> chatRoomMainArrayList = new ArrayList<ChatRoomMainItem>();
    private static ArrayList<User> userArrayList = new ArrayList<User>();
    private static MessageRecyclerViewAdapter messageRecyclerViewAdapter;

    /*Database stuff*/
    private  static StringRequest stringRequest;
    private  static RequestQueue requestQueue;
    private  static String URL_PHP_SCRIPT = "http://projectcpms99.000webhostapp.com/scripts/gayal/fetchUserDetails.php";

    /*Firbase*/
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //firebase
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();

    /*Dialog to provide user some clues as to whats going onj*/
    private static AlertDialog.Builder builder;
    private static ProgressDialog progressDialog;

    //task1-: get the list of users relevant to current project from database, depends on Harshan's assigning users to relevant project func.
    //tasl2-: if the project doesnt have a project entry under messaging in firebase(Query to find out) add it
    // by creating relevant chatrooms for available users, if not don't. set it as root and continue to task3.
    //task3-: get timestamp, get latestmessage using query and also image of user using firebase (if task 2 had to get done, disregard non mandatory fields).
    //task4-: fill up an arraylist pass to adapter and display!



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_messages, null, false);

        requestQueue = Volley.newRequestQueue(getContext());

        //setting up progress dialog
        progressDialog = new ProgressDialog(getActivity());

       // firebaseMagic();
        progressDialog.setMessage("Getting Project Data...");
        initializeProjectChatNode(projectId);   //if project node doesnt exist, create it in firebase...


        //Setting up the alertbox to show if, query to remote db on users for a given project id comes up empty
        builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Project has 0 users assigned, Therefore Messaging is currently disabled.");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        ((Navigation)getActivity()).naviagateTo(new DashboardFragment(), false);

                    }
                });


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_messaging);

        messageRecyclerViewAdapter = new MessageRecyclerViewAdapter( chatRoomMainArrayList, getContext());

        recyclerView.setAdapter(messageRecyclerViewAdapter);
        GridLayoutManager  gridLayoutManager = new GridLayoutManager(getContext(),1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        setUpTopBar(view);
        Toolbar toolbar = view.findViewById(R.id.toolbar);


        return view;
    }

    //Used mdc codelabs as reference, helper method
    private void setUpTopBar(View view){
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if(activity != null){
            activity.setSupportActionBar(toolbar);
        }
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

                                //users involved in the project
                                User user_Project = new User(object.getString("userId"), object.getString("firebaseId"),
                                        object.getString("fName"), object.getString("lName"), object.getString("email"), object.getString("picUrl"), object.getString("type"));

                                System.out.println("First Name Message Main = " + object.getString("fName").toString() + "Firebase ID = " + object.getString("firebaseId"));

                                //populate arraylist
                                userArrayList.add(user_Project);

                                ChatRoomMainItem chatRoomMain1 = new ChatRoomMainItem( object.getString("fName"),"12:30pm", "Delivered",object.getString("type"),"Hello World" );
                                chatRoomMainArrayList.add(chatRoomMain1);

                            }
                            messageRecyclerViewAdapter.notifyDataSetChanged();    //if you dont notify adapter about updates to arraylist so recycler view can load them up.
                        } catch (JSONException e) {
                           // e.printStackTrace();
                            //occurs when no response comes up empty, meaning no users in the project you are searching for


                    /*        builder.setMessage("Project has 0 users assigned, Therefore Messaging is currently disabled.");
                            builder.setCancelable(true);

                            builder.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            ((Navigation)getActivity()).naviagateTo(new forgotPasswordFragment(), true);
                                        }
                                    });*/
                            AlertDialog alert = builder.create();
                            alert.show();


                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> hashMap = new HashMap<String, String>();
                        hashMap.put("projectId", projectId);        //sending project id to get the relevant user list
                        return hashMap;
                    }
                };
                requestQueue.add(stringRequest);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
               // forumRecyclerViewAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            protected void onPreExecute() {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
        };

        asyncTask.execute();
    }

    private void firebaseMagic(){
        //traditional query is bad according to -: https://stackoverflow.com/questions/47893328/checking-if-a-particular-value-exists-in-the-firebase-database?rq=1
        //therefore going with the method reccommended by Alex Mamo
        //doing this way prevents looping through entire tree

        final DatabaseReference project = root.child("Messages").child("Project-P1");


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if no such object in databse, 'Project-P1'
                if(!dataSnapshot.exists()){
                    //create it
                    DatabaseReference project1 = root.child("Messages");
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Project-P1", "");
                    project1.updateChildren(hashMap);


                }else {
                    System.out.println("Firebase HAS IT!!!!!!!!!!!!!!!!!!!!!");
                    DatabaseReference project1 = root.child("Messages").child("Project-P1");
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Chatroom", "");
                    project1.updateChildren(hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        project.addListenerForSingleValueEvent(eventListener);

    }


    private void initializeProjectChatNode(final String projectId){
        //traditional query is bad according to -: https://stackoverflow.com/questions/47893328/checking-if-a-particular-value-exists-in-the-firebase-database?rq=1
        //therefore going with the method recommended by Alex Mamo
        //doing this way prevents looping through entire tree

        //prefix "Project-P" is used before specifying the project id.

        final DatabaseReference project = root.child("Messages").child("Project-P" + projectId);

        ValueEventListener eventListener = new ValueEventListener() {

            String projectId_node = "Project-P" + projectId;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if no such object in databse, 'Project-P1'
                if(!dataSnapshot.exists()){
                    //create it
                    DatabaseReference project1 = root.child("Messages");
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(projectId_node, "");
                    project1.updateChildren(hashMap);


                }else {

                    //CHAT ROOMS ARE CREATED/LOADED ON DEMAND OF USER WHEN THEY TAP ON THE PERSON TO Send MEssages.


                   /* System.out.println("Firebase HAS IT!!!!!!!!!!!!!!!!!!!!!");
                    DatabaseReference project1 = root.child("Messages").child(projectId_node);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Chatroom-Success", "");
                    project1.updateChildren(hashMap);*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        project.addListenerForSingleValueEvent(eventListener);

    }

    public  void getUserChatroomCombinations(){
        HashMap<String,String> hashMap = new HashMap<>();
    }



    @Override
    public void onStart() {
        super.onStart();
        chatRoomMainArrayList.clear();
        fetchdata();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser==null){
            System.out.println("FIREBASE LOGIN FAILED... NO USER -%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        }else{
            System.out.println( "FireBAse = " + currentUser.getUid());
        }

        // updateUI(currentUser);
    }





}
