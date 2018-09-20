package com.construction.app.cpms.miscellaneous;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.Navigation;
import com.construction.app.cpms.R;

import com.construction.app.cpms.miscellaneous.bean.ChatRoomID;
import com.construction.app.cpms.miscellaneous.bean.ChatRoomMainItem;
import com.construction.app.cpms.miscellaneous.bean.User;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

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
    private FirebaseUser fireBaseCurrentUser = mAuth.getCurrentUser();


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
        //initializeProjectChatNode(projectId);   //if project node doesnt exist, create it in firebase...
        //chatRoomInitializeFirebaseDB();
        //chatRoomInit();


        test();

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
        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if(activity != null){
            activity.setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.messages_main_toolbar, menu);   //inflate the toolbar options

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cm_composeMessage :
                Toast toast = Toast.makeText(getContext(), "Compose Message", Toast.LENGTH_SHORT);
                toast.show();
                break;

        }

        return super.onOptionsItemSelected(item);
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

        //checking for project node, if it doesnt exist create it......
        final DatabaseReference project = root.child("Messages").child("Project-P" + projectId);
        ValueEventListener projectNodeListener = new ValueEventListener() {

            String projectId_node = "Project-P" + projectId;        // example  = Project-P1 and so on..

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


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        project.addListenerForSingleValueEvent(projectNodeListener);

    }

    /*chatRoomInitializeFirebase() Attributes*/
    boolean isCombination1_Avail;
    boolean isCombination2_Avail;
    public static int flag;

    public void chatRoomInitializeFirebaseDB(){
        DatabaseReference reference = root;  //the main root of the database
       final String senderUID = "senderUID";
                        //  dir = Messages/Project-P{ID}/{CHATROOM ID}
                        //  dir = Messages/Project-P{ID}/{SenderUID-ReceiverUID}
/*        reference.child("Messages").child( "Project-P" + projectId).child(mAuth.getCurrentUser().getUid() + "-" + senderUID).setValue("");*/

        String chatRoomIDCombination1 = fireBaseCurrentUser.getUid() + "-" + senderUID; //1st possible chatroom id combination

        //Reference for combination 1
        final DatabaseReference chatroomComb1_REF = root.child( "Messages" ).child( "Project-P" + projectId ).child(chatRoomIDCombination1);

        //Checking for first combination
        ValueEventListener chatroomListener_COMB1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){ //if combination1 of chatroom id exists
                    //combination1 of chatroom id EXISTS!
                    isCombination1_Avail = true;
                    System.out.println("1111111111111111111111111111111111111111111111111111");

                }else {
                    //if combination1 doesnt exist
                    isCombination1_Avail = false;

                    final String chatRoomIDCombination2 = senderUID + "-" + fireBaseCurrentUser.getUid(); //2nd possible chatroom id combination

                    //Reference for combination 2
                    final DatabaseReference chatroomComb2_REF = root.child( "Messages" ).child( "Project-P" + projectId ).child(chatRoomIDCombination2);

                    //Checking for second combination
                    ValueEventListener chatRoomListener_COMB2 = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){ //if combination2 of chatroom id doesnt exist
                                //combination2 of chatroom id EXISTS!
                                isCombination2_Avail = true;
                                System.out.println("2222222222222222222222222222222222222");
                            }else {
                                isCombination2_Avail = false;
                                //if it comes here, means chatroom combinations 1,2 don't exist
                                //So create a room...
                                HashMap<String,Object> hashMap = new HashMap<>();
                                hashMap.put("Message","");
                                chatroomComb2_REF.updateChildren(hashMap);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };

                    chatroomComb2_REF.addListenerForSingleValueEvent(chatRoomListener_COMB2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };



        chatroomComb1_REF.addListenerForSingleValueEvent(chatroomListener_COMB1);


        System.out.println("==============================isAvailCOMB 1 = " + isCombination1_Avail);
        System.out.println("==============================isAvailCOMB 2 = " + isCombination2_Avail);

        /*
        if(isCombination1_Avail){
            Toast.makeText(getContext(), "COMBINATION 1 AVAILABLE", Toast.LENGTH_LONG).show();
        }

        if(isCombination2_Avail){
            Toast.makeText(getContext(), "COMBINATION 2 AVAILABLE", Toast.LENGTH_LONG).show();
        }*/
    }

    public void chatRoomInit(){
        DatabaseReference reference = root;  //the main root of the database
        final String senderUID = "senderUID";
        //  dir = Messages/Project-P{ID}/{CHATROOM ID}
        //  dir = Messages/Project-P{ID}/{SenderUID-ReceiverUID}
        /*        reference.child("Messages").child( "Project-P" + projectId).child(mAuth.getCurrentUser().getUid() + "-" + senderUID).setValue("");*/

        String chatRoomIDCombination1 = fireBaseCurrentUser.getUid() + "-" + senderUID; //1st possible chatroom id combination
        String chatRoomIDCombination2 = senderUID + "-" + fireBaseCurrentUser.getUid(); //2nd possible chatroom id combination


        //Reference for combination 1
        DatabaseReference chatroomComb1_REF = root.child( "Messages" ).child( "Project-P" + projectId ).child(chatRoomIDCombination1);
        //Reference for combination 2
        DatabaseReference chatroomComb2_REF = root.child( "Messages" ).child( "Project-P" + projectId ).child(chatRoomIDCombination2);


        chatroomComb2_REF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    isCombination2_Avail = true;
                    System.out.println("2222222222222222222222222222222222222");

                }else {
                    isCombination2_Avail = false;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

/*
        //Checking for first combination
        ValueEventListener chatroomListener_COMB1 = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){ //if combination1 of chatroom id exists
                    //combination1 of chatroom id EXISTS!
                    isCombination1_Avail = true;
                    System.out.println("1111111111111111111111111111111111111111111111111111");
                }else {

                    // combination1 doesnt exist
                    isCombination1_Avail = false;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };


        //Checking for second combination
        ValueEventListener chatRoomListener_COMB2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){ //if combination2 of chatroom id doesnt exist

                    //combination2 of chatroom id EXISTS!
                    isCombination2_Avail = true;
                    System.out.println("2222222222222222222222222222222222222");
                }else {

                    //combination2 doesnt exist.
                    isCombination2_Avail = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };




        chatroomComb1_REF.addListenerForSingleValueEvent(chatroomListener_COMB1);
        chatroomComb2_REF.addListenerForSingleValueEvent(chatRoomListener_COMB2);*/



        System.out.println("==============================isAvailCOMB 1 = " + isCombination1_Avail);
        System.out.println("==============================isAvailCOMB 2 = " + isCombination2_Avail);


  /*      if(isCombination1_Avail){
            Toast.makeText(getContext(), "COMBINATION 1 AVAILABLE", Toast.LENGTH_LONG).show();
        }

        if(isCombination2_Avail){
            Toast.makeText(getContext(), "COMBINATION 2 AVAILABLE", Toast.LENGTH_LONG).show();
        }*/
    }

    public void test(){

        DatabaseReference reference = root;  //the main root of the database
        final String receiverUID = "senderUID";
        final String loggedInUID = fireBaseCurrentUser.getUid();

        //  dir = Messages/Project-P{ID}/{CHATROOM ID}
        //  dir = Messages/Project-P{ID}/{SenderUID-ReceiverUID}
        /*        reference.child("Messages").child( "Project-P" + projectId).child(mAuth.getCurrentUser().getUid() + "-" + senderUID).setValue("");*/

        //Reference for combination 1
        DatabaseReference chatroomComb_REF = root.child( "Messages" ).child( "Project-P" + projectId );

        chatroomComb_REF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //readnodes...
                Iterator iterator = dataSnapshot.getChildren().iterator();

                String receivedCombination = ((DataSnapshot) iterator.next()).getKey();

                if(ChatRoomID.isCombinationHavingIndividualIDs(receivedCombination, receiverUID, loggedInUID)){
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Chatroom Found = " + receivedCombination);
                }
                else {
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Chatroom NOT FOUND");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
