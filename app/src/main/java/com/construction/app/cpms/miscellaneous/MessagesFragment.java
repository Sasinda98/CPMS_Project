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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import com.construction.app.cpms.miscellaneous.adapters.MessageRecyclerViewAdapter;
import com.construction.app.cpms.miscellaneous.bean.ChatRoomMainItem;
import com.construction.app.cpms.miscellaneous.bean.User;
import com.construction.app.cpms.miscellaneous.firebaseModels.ChatRoom;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseUserRoom;
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
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {

    public static final String TAG = "MessagesFragment";

    /*STATIC fields are because we need to access them inside async task etc*/
    private static String projectId = "1";   //to be passed in from Harshan's createPlans and Harshan's user_plan
    private String userId;      //logged in user's userid.

    private static ArrayList<ChatRoomMainItem> chatRoomMainArrayList = new ArrayList<ChatRoomMainItem>();
    private static MessageRecyclerViewAdapter messageRecyclerViewAdapter;

    /*Database stuff*/
    private  static StringRequest stringRequest;
    private  static RequestQueue requestQueue;
    private  static String URL_PHP_SCRIPT = "http://projectcpms99.000webhostapp.com/scripts/gayal/fetchUserDetails.php";

    /*Firbase*/
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //firebase
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser fireBaseCurrentUser = mAuth.getCurrentUser();
    private DatabaseReference reference;


    /*Dialog to provide user some clues as to whats going onj*/
    private static AlertDialog.Builder builder;
    private static ProgressDialog progressDialog;

    //store the stuff
    private ArrayList<FirebaseUserRoom> firebaseUserRooms = new ArrayList<>();


    //UI element
    private TextView messageTextView;
    private RecyclerView chatRoomRecycView;

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

        //region Initialization
        requestQueue = Volley.newRequestQueue(getContext());

        chatRoomRecycView = (RecyclerView) view.findViewById(R.id.recyclerView_messaging);  // apart from usage inside this method, refer to method -: setVisibilityOfTextView();
        messageTextView = view.findViewById(R.id.fm_emptyDataSetMessage);  //refer method -: setVisibilityOfTextView();

        //setting up progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Getting Project Data...");


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
    //endregion

                                                                                                                        //Careful when changing projectId
        messageRecyclerViewAdapter = new MessageRecyclerViewAdapter( firebaseUserRooms, getContext(), fireBaseCurrentUser, "Project-P1");

        chatRoomRecycView.setAdapter(messageRecyclerViewAdapter);
        GridLayoutManager  gridLayoutManager = new GridLayoutManager(getContext(),1, GridLayoutManager.VERTICAL, false);
        chatRoomRecycView.setLayoutManager(gridLayoutManager);

        setUpTopBar(view);
        Toolbar toolbar = view.findViewById(R.id.toolbar);


        return view;
    }

    //region Fragment Specific Methods
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
                Intent intent = new Intent(getActivity(), ComposeChatRoomActivity.class);
                intent.putExtra("projectId", projectId);
                Log.d(TAG, "added Extra key = projectId value = " + projectId);
                startActivity(intent);


                break;

        }

        return super.onOptionsItemSelected(item);
    }



    //endregion

    @Override
    public void onStart() {
        super.onStart();
        chatRoomMainArrayList.clear();
        firebaseUserRooms.clear();

        //listenToNode(projectId);

        listenToProjectNode("Project-P1");


        //region Firebase
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser==null){
            System.out.println("FIREBASE LOGIN FAILED... NO USER -%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        }else{
            System.out.println( "FireBAse = " + currentUser.getUid());
        }

        // updateUI(currentUser);
        //endregion
    }


    //test method for listening for chatrooms.
    public void listenToProjectNode(String projectId){

        Log.d(TAG, "listenToProjectNode(String projectId)  CALLED!");                       // /Rooms/Project-P1

        reference = FirebaseDatabase.getInstance().getReference().getRoot().child("Rooms").child(projectId);
        reference.keepSynced(true);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot = " + dataSnapshot.toString());

                firebaseUserRooms.clear();

                if(dataSnapshot.exists()){

                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Log.d(TAG, "DataSnapshot FOR LOOP = " + ds.toString());
                        ChatRoom c = ds.getValue(ChatRoom.class);
                        populateArrayList(firebaseUserRooms, c);

                        Log.d(TAG, "CustoClass user name 1 = " + c.getUser1().getUID() );
                    }

                    messageRecyclerViewAdapter.notifyDataSetChanged();
                    setVisibilityOfTextView();  //refer to method, it shows user message if no chatrooms are avail..
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addValueEventListener(valueEventListener);            /*previously - : reference.addValueEventListener(valueEventListener);     */
    }

    //Adds relevant user (based on user currently logged in) to arraylist used by recycler view
    public void populateArrayList(ArrayList<FirebaseUserRoom> firebaseUserRooms, ChatRoom chatRoom){

        FirebaseUser currentloggedin = fireBaseCurrentUser;

        FirebaseUserRoom user1 = chatRoom.getUser1();
        FirebaseUserRoom user2 = chatRoom.getUser2();

        //if userid of passed in object is same as lgged in, discard it, get user detals of other one.
        if(user1.getUID().equals(currentloggedin.getUid())){
            firebaseUserRooms.add(user2);   //addUser2 to the list.
            Log.d(TAG,"User2 data added to arraylist");
        }else if(user2.getUID().equals(currentloggedin.getUid())){
            firebaseUserRooms.add(user1);
            Log.d(TAG,"User1 data added to arraylist");
        }else{
            Log.d(TAG,"USER ID's of objects from firebase doesnt match any!");
            Log.d(TAG,"There is no chatroom available for this account right now.");

            //show user a alert dialog box


        }

    }

    //sets visibility of textview with a message to display when the array list is empty, meaning nothig to display
    //this happens when logged in user is not involved in any conversation with anyother user.
    //refered to -: https://stackoverflow.com/questions/47417645/empty-view-on-a-recyclerview
    public void setVisibilityOfTextView(){
        String message = "Press The + Button To Start A Conversation";
        messageTextView.setText(message);

        if( firebaseUserRooms.size() == 0 ){    //arraylist empty, meaning no chatroom to display for recyclerview
            messageTextView.setVisibility(View.VISIBLE);    //enable visibility textview to see message.
            chatRoomRecycView.setVisibility(View.GONE);     //visibily disable for recycler view as it has nothing to show
        }else{                                  //arraylist is having items
            messageTextView.setVisibility(View.GONE);    //disabe visibility textview to not see message.
            chatRoomRecycView.setVisibility(View.VISIBLE);     //visibily enable for recycler view as it has stuff to show
        }

    }







}
