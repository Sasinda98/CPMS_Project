package com.construction.app.cpms.miscellaneous;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.construction.app.cpms.R;

import com.construction.app.cpms.miscellaneous.adapters.MembersRecyclerViewAdapter;
import com.construction.app.cpms.miscellaneous.bean.ChatRoomIDGenerator;
import com.construction.app.cpms.miscellaneous.bean.ChatRoomMainItem;
import com.construction.app.cpms.miscellaneous.bean.User;
import com.construction.app.cpms.miscellaneous.firebaseModels.ChatRoom;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseMessage;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseUserDetails;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseUserRoom;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ComposeChatRoomActivity extends AppCompatActivity {
    private static final String TAG = "ComposeChatRoomAct";

    private static String projectId= "1";         //harshan's func should set the value (project creation)

    //Vars required to setup Top Toolbar
    private Toolbar toolbar;
    private AppCompatActivity appCompatActivity;

    /*Remote Database stuff*/
    private  static StringRequest stringRequest;
    private  static RequestQueue requestQueue;
    private  static String URL_PHP_SCRIPT = "http://projectcpms99.000webhostapp.com/scripts/gayal/fetchUserDetails.php";

    /*RecyclerView related stuff*/
    private ArrayList<User> userArrayList;
    private RecyclerView memberRecycView;
    private MembersRecyclerViewAdapter adapter;

    /*Dialog to provide user some clues as to whats going onj*/
    private static AlertDialog.Builder builder;

    /*Firbase*/
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //firebase
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser fireBaseCurrentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_chat_room);
        userArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        Bundle extras = getIntent().getExtras();
        projectId = extras.getString(MessagesFragment.KEY_INTENT);  //get passed in project id from Messages Fragment;


        setUpToolbar();

        memberRecycView = findViewById(R.id.ccr_memberRecycView);   //reference to recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        memberRecycView.setLayoutManager(layoutManager);

        adapter = new MembersRecyclerViewAdapter(this, userArrayList,fireBaseCurrentUser,projectId);
        memberRecycView.setAdapter(adapter);

    }

    //region TEST METHODS, IMPORTANT TO NOT DELETE EVEN THOUGH THEY ARE NOT USED HERE, but elsewhere
    // creates chatroom for user to engage.
    public void addChatRoom(String projectId, String loggedInUID, String receiverUID){
        Log.d(TAG, "addChatRoom(String projectId) CALLED");
        projectId = "Project-P" + projectId;       //database/./

        String chatroomID = ChatRoomIDGenerator.getChatRoomID(loggedInUID, receiverUID);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Rooms");        //reference to node Rooms (Where chatrooms are there)

        FirebaseUserRoom user1 = new FirebaseUserRoom(loggedInUID,"");
        FirebaseUserRoom user2 = new FirebaseUserRoom(receiverUID,"");
        ChatRoom chatRoom = new ChatRoom(user1,user2,null);
        //                      Project-P{number}/{chatroomID}/{chatRoom Object}
        databaseReference.child(projectId).child(chatroomID).setValue(chatRoom);
    }



    //For use with the chatroom itself,
    public void addMessage(String projectId, String loggedInUID, String receiverUID){
        Log.d(TAG, "addMessage(String projectId) CALLED");
        projectId = "Project-P" + projectId;       //database/./

        String chatroomID = ChatRoomIDGenerator.getChatRoomID(loggedInUID,receiverUID);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("ChatLogs");        //reference to ChatLogs node on firebase (Where chat messages go to)

        FirebaseMessage messageBean = new FirebaseMessage("This is body","12:40pm",loggedInUID);
        //                      Project-P{number}/{chatroomID}/{FirebaseGeneratedVal}/{messageObject}
        databaseReference.child(projectId).child(chatroomID).push().setValue(messageBean);
    }
    //endregion

    public void setUpToolbar(){
        toolbar = findViewById(R.id.ccr_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select A Member");

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


    public void testing(){
        Log.d(TAG, "testing() CALLED");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = firebaseDatabase.getReference("users");

        Query query = usersReference.orderByChild("UID").equalTo("Jw405DV177dkOg2nBWAjsAERs8j1");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot = " + dataSnapshot.toString());

                for(DataSnapshot usernode : dataSnapshot.getChildren()) {//get to the user node that has user details as the value
                    FirebaseUserDetails user = usernode.getValue(FirebaseUserDetails.class);

                    Log.d(TAG, "Name = " + user.getName());
                    Log.d(TAG, "UID = " + user.getUID());
                    Log.d(TAG, "PhotoURl = " + user.getPhotoUrl());
                    Log.d(TAG, "type = " + user.getType());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void testing2(){

        Log.d(TAG, "testing() CALLED");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //region Handling part where latest message and time is fetched
        DatabaseReference chatroomRef = firebaseDatabase.getReference("ChatLogs")
                .child("Project-P1")
                .child(ChatRoomIDGenerator.getChatRoomID("Jw405DV177dkOg2nBWAjsAERs8j1", "4kE5XKKcm6VOBb7yISPfqKmW6Li2"));  //ref to chatroom

           Query q =  chatroomRef.limitToLast(1);
           q.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   Log.d(TAG, "DataSnapshot = " + dataSnapshot.toString());

                   for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //get to the message id node by calling getChildren
                       FirebaseMessage message = snapshot.getValue(FirebaseMessage.class);      //populate details
                       Log.d(TAG, "Send by UID = " + message.getSentBy());
                       Log.d(TAG, "Body = " + message.getBody());
                       Log.d(TAG, "Time stamp = " + message.getTimeStamp());


                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userArrayList.clear();
        fetchdata();


    }

    private void fetchdata(){

        Log.d(TAG, "fetchDataCalled");
        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                System.out.println("Do backgorund func");
                stringRequest = new StringRequest(Request.Method.POST, URL_PHP_SCRIPT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       Log.d(TAG, "ON RESPONSE");
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i<jsonArray.length(); i++){ //loop through jsonarray(stores objects in each index) and put data to arraylist.
                                System.out.println("FOR LOOP");
                                JSONObject object = jsonArray.getJSONObject(i);     //get the JSON object at index i

                                //users involved in the project
                                User user_Project = new User(object.getString("userId"), object.getString("firebaseId"),
                                     object.getString("type"));

                                Log.d(TAG , "Remote db User id = " + object.getString("userId") + "Firebase ID = "
                                        + object.getString("firebaseId") + "Type = " +  object.getString("type") );

                                FirebaseUser loggedInAs = FirebaseAuth.getInstance().getCurrentUser();

                                //if logged in user, dont add to arraylist
                                if(!loggedInAs.getUid().equals(user_Project.getFirebaseId())){

                                    //populate arraylist
                                    userArrayList.add(user_Project);

                                }

                            }
                            adapter.notifyDataSetChanged();    //if you dont notify adapter about updates to arraylist so recycler view can load them up.

                        } catch (JSONException e) {
                            e.printStackTrace();

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
             /*   progressDialog.dismiss();*/
            }

            @Override
            protected void onPreExecute() {
            /*    progressDialog.setMessage("Loading...");
                progressDialog.show();*/
            }
        };

        asyncTask.execute();
    }



}
