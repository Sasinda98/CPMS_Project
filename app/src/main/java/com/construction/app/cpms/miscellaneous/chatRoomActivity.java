package com.construction.app.cpms.miscellaneous;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.adapters.MessageRecyclerViewAdapter;
import com.construction.app.cpms.miscellaneous.bean.ChatRoomIDGenerator;
import com.construction.app.cpms.miscellaneous.firebaseModels.ChatRoom;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseMessage;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseUserDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    private DatabaseReference chatnodeRef;
    private FirebaseUser loggedInAs = FirebaseAuth.getInstance().getCurrentUser();

    private RecyclerView chatLogRecyc;
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

        chatLogRecyc = findViewById(R.id.cr_chatLogRecycView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        chatLogRecyc.setHasFixedSize(true);
        chatLogRecyc.setLayoutManager(linearLayoutManager);



        Log.d(TAG, "Intent passed in, receiver uid = " + RECEIVER_UID + "Project Id = " + PROJECT_ID);
        //endregion

        //regionFIREBASE INITIALIZATION
        chatnodeRef = FirebaseDatabase.getInstance().getReference("ChatLogs").child("Project-P1").child(ChatRoomIDGenerator.getChatRoomID(loggedInAs.getUid(), RECEIVER_UID));
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
        //                      Project-P{number}/{chatroomID}/{FirebaseGeneratedVal}/{messageObject}
        databaseReference.child(projectId).child(chatroomID).push().setValue(messageBean);
    }

    public FirebaseMessage processMessageForSending(String senderUID){
        Log.d(TAG, "processMessageForSending(String senderUID)  CALLED");
        //take the user input
        String msg_body = messageWritePad.getText().toString().trim();  //remove white spaces either end.       /

        FirebaseMessage messageBean = new FirebaseMessage(msg_body,"9:03pm", senderUID);

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

/*        FirebaseRecyclerAdapter<FirebaseMessage,MessageViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<FirebaseMessage, MessageViewHolder>(FirebaseMessage.class, ) {
            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull FirebaseMessage model) {

            }

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return null;
            }
        };*/

    }

  /*  //test method for listening for chatrooms.
    public void listenToProjectNode(String projectId){

        Log.d(TAG, "listenToProjectNode(String projectId)  CALLED!");                       // /Rooms/Project-P1

        reference = FirebaseDatabase.getInstance().getReference("ChatLogs").child("Project-P1").child("UID1-UID2");
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
                }
                setVisibilityOfTextView();  //refer to method, it shows user message if no chatrooms are avail..

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addValueEventListener(valueEventListener);            *//*previously - : reference.addValueEventListener(valueEventListener);     *//*
    }*/


    //endregion

}
