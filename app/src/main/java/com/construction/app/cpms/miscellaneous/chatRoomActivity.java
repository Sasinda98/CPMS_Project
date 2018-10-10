package com.construction.app.cpms.miscellaneous;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.adapters.ChatBubbleRecyclerViewAdapter;
import com.construction.app.cpms.miscellaneous.adapters.MessageRecyclerViewAdapter;
import com.construction.app.cpms.miscellaneous.bean.ChatRoomIDGenerator;
import com.construction.app.cpms.miscellaneous.firebaseModels.ChatRoom;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseMessage;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseUserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class chatRoomActivity extends AppCompatActivity {
    private static String TAG = "chatRoomActivity";

    private Toolbar toolbar;
    private AppCompatActivity appCompatActivity;

   // intent set variables;
    private String PROJECT_ID;
    private String RECEIVER_UID;

    //UI ELEMENTS
    private EditText messageWritePad;
    private ImageButton sendBtn;

    //Firebase Related vars
    private DatabaseReference chatnodeRef = FirebaseDatabase.getInstance().getReference().getRoot();
    private FirebaseUser loggedInAs = FirebaseAuth.getInstance().getCurrentUser();

    private RecyclerView chatLogRecyc;
    private ArrayList<FirebaseMessage> messageArrayList = new ArrayList<>();
    private ChatBubbleRecyclerViewAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        //regionINITIALIZATION
        setUpToolbar();
        Bundle extras = getIntent().getExtras();
        RECEIVER_UID = extras.getString(MessageRecyclerViewAdapter.KEY_INTENT_UID);
        PROJECT_ID = extras.getString(MessageRecyclerViewAdapter.KEY_INTENT_ProjectId);
        setTitleBarName();

        Log.d(TAG, "Intent passed in, receiver uid = " + RECEIVER_UID + "Project Id = " + PROJECT_ID);
        //endregion

        //regionFIREBASE INITIALIZATION
        chatnodeRef = FirebaseDatabase.getInstance().getReference("ChatLogs").child("Project-P"+PROJECT_ID).child(ChatRoomIDGenerator.getChatRoomID(loggedInAs.getUid(), RECEIVER_UID));
        chatnodeRef.keepSynced(true);
        //endregion

        sendBtn = findViewById(R.id.cr_sendbtn);    //send button of the message, user clicks when they want to send message
        messageWritePad = findViewById(R.id.cr_cr_messageTextInputEditText);    //where user types their message in

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firebaseProjectNodeId = "Project-P" + PROJECT_ID;   //prefix Project-P has to be added.
                addMessage(firebaseProjectNodeId, loggedInAs.getUid(), RECEIVER_UID);
            }
        });


        //regionFIREBASE RECYCLERVIEW SETUP     HOW THE CHAT LOG IS DISPLAYED
        chatLogRecyc = findViewById(R.id.cr_chatLogRecycView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        chatLogRecyc.setLayoutManager(linearLayoutManager);

        chatAdapter = new ChatBubbleRecyclerViewAdapter(messageArrayList,this,loggedInAs,PROJECT_ID);

        chatLogRecyc.setAdapter(chatAdapter);

        listenToChatRoomNode(PROJECT_ID, loggedInAs.getUid(), RECEIVER_UID);

       //sets last read time of the logged in user for the relevant chatroom.
        setLastRead(PROJECT_ID, loggedInAs.getUid(),RECEIVER_UID);

        //endregion



    }

    //region Activity Related stuff
    @Override
    protected void onPause() {
        super.onPause();
        /*stackof-: https://stackoverflow.com/questions/28512662/slide-out-animation-not-working-on-back-press-button/28513065*/
        overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
    }

    public void setUpToolbar(){

        toolbar = findViewById(R.id.cr_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Loading...");
        getSupportActionBar().setSubtitle("Loading...");

        toolbar.setNavigationIcon(R.drawable.ic_back);
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

    //endregion

    //region FirebaseStuff
    //Inserts message to correct node in the Firebase database for given parameters.
    public void addMessage(String projectId, String loggedInUID, String receiverUID){
        Log.d(TAG, "addMessage(String projectId) CALLED");

        String chatroomID = ChatRoomIDGenerator.getChatRoomID(loggedInUID,receiverUID);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("ChatLogs");        //reference to ChatLogs node on firebase (Where chat messages go to)

        FirebaseMessage messageBean = processMessageForSending(loggedInUID);

        if(messageBean != null ){
            //                      Project-P{number}/{chatroomID}/{FirebaseGeneratedVal}/{messageObject}
            databaseReference.child(projectId).child(chatroomID).push().setValue(messageBean);
        }else {
            Log.d(TAG, "Message not getting added, because it is empty...");
        }
    }

    //this method returns current time, and also gets used inside processMessageForSending() method, line # 176
    public String getCurrentTime(){
        Date date = new Date();
        String timeFormat = "hh:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        String timeStamp = simpleDateFormat.format(date);

        Log.d(TAG, "TimeStamp = " + timeStamp);

        return timeStamp;

    }

    public FirebaseMessage processMessageForSending(String senderUID){
        Log.d(TAG, "processMessageForSending(String senderUID)  CALLED");
        //take the user input
        String msg_body = messageWritePad.getText().toString().trim();  //remove white spaces either end.       /

        if(msg_body.equals("")){
            return null;
        }

        FirebaseMessage messageBean = new FirebaseMessage(msg_body,getCurrentTime(), senderUID);

        messageWritePad.setText("");    //clear out the text..

        return messageBean;
    }


    public void setTitleBarName(){  //sets receiver's name in the title bar
        //region SET UserDetails like PIC, Name, Type using users node in firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = firebaseDatabase.getReference("users");
        usersReference.keepSynced(true);

        Query query = usersReference.orderByChild("UID").equalTo(RECEIVER_UID);        //to get relevant details, userID should match the one inside arraylist.

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot = " + dataSnapshot.toString());

                if(dataSnapshot.exists()) {     //means user is actually there in the database
                    for (DataSnapshot usernode : dataSnapshot.getChildren()) {//get to the user node that has user details as the value

                        Log.d(TAG,"Key = " + usernode.getKey());
                        FirebaseUserDetails user = usernode.getValue(FirebaseUserDetails.class);

                        Log.d(TAG, "Name = " + user.getName());
                        Log.d(TAG, "UID = " + user.getUID());
                        Log.d(TAG, "PhotoURl = " + user.getPhotoUrl());
                        Log.d(TAG, "type = " + user.getType());

                        toolbar.setTitle(user.getName());
                        toolbar.setSubtitle("");

                    }
                }else{
                    //if user given user doesnt exist

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //endregion

    //regionTesters

    //test method for listening for chatrooms.

    @Override
    protected void onStart() {
        super.onStart();



    }


    //test method for listening for chatrooms to get messages...
    public void listenToChatRoomNode(String projectId, String loggedInAs, String receiver){

        Log.d(TAG, "listenToProjectNode(String projectId)  CALLED!");                       // /Rooms/Project-P1

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference chatMessagesReference = firebaseDatabase.getReference("ChatLogs").child("Project-P" + projectId).child(ChatRoomIDGenerator.getChatRoomID(loggedInAs,receiver));
        chatMessagesReference.keepSynced(true);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot = " + dataSnapshot.toString());

                messageArrayList.clear();

                if(dataSnapshot.exists()){

                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Log.d(TAG, "DataSnapshot FOR LOOP = " + ds.toString());
                        FirebaseMessage message = ds.getValue(FirebaseMessage.class);

                        populateArrayList(message);
                        Log.d(TAG, "Message Body 1 = " + message.getBody());
                    }

                }
                chatAdapter.notifyDataSetChanged();
                chatLogRecyc.smoothScrollToPosition(chatLogRecyc.getAdapter().getItemCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        chatMessagesReference.addValueEventListener(valueEventListener);
    }


    public void populateArrayList(FirebaseMessage message){
        messageArrayList.add(message);
    }

    //endregion


    public void setLastRead(String projectID, String loggedInUID, String receiverUID){
        final String TAG = "setLastRead";

        DatabaseReference reference;

        reference = FirebaseDatabase.getInstance().getReference().getRoot().child("Rooms").child("Project-P" + projectID)
                .child(ChatRoomIDGenerator.getChatRoomID(loggedInUID, receiverUID)).child("lastRead")
                .child(loggedInUID);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("lastRead", getCurrentTime());        //      /Rooms/Project-P#/UID-UID/lastRead/UID/lastRead

        reference.updateChildren(hashMap);



    }


}
