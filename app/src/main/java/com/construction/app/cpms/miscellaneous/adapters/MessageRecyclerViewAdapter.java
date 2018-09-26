package com.construction.app.cpms.miscellaneous.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.bean.ChatRoomIDGenerator;
import com.construction.app.cpms.miscellaneous.chatRoomActivity;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseMessage;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseUserDetails;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseUserRoom;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//Handles message cards
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder> {
    public static final String TAG = "MessageRecyViewAdapt";
    public static final String KEY_INTENT_ProjectId = "MessageRecycler-projectId";
    public static final String KEY_INTENT_UID = "MessageRecyvler-uid";

    private static final  int MAX_CHARS_LATEST_MSG = 30;

    private ArrayList<FirebaseUserRoom> chatRoomItems = new ArrayList<FirebaseUserRoom>();
    private  FirebaseUser loggedInAs;
    private Context context;
    private String projectId;
    private String firebaseProjectId;

    //Constructor
    public MessageRecyclerViewAdapter(ArrayList<FirebaseUserRoom> chatRoomItems, Context context, FirebaseUser loggedInAs, String projectId) {
        this.chatRoomItems = chatRoomItems;
        this.context = context;
        this.loggedInAs = loggedInAs;
        this.projectId = projectId;

        firebaseProjectId = "Project-P" + projectId;        //project nodes are named with prefix Project-P# so to go along, this is done.

    }

    @NonNull
    @Override       //recyclable
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // the relevant layout for single item which is single chatroom main item                      //not a popup menu so, no need to attach
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_message_card, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override   //everytime a new item gets added/created to the view, this method gets called.
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.deliverStatus.setText("Delivered");
        //_____________________________________________________________________ Firebase Queries to set views  ______________________________________\\

        //region SET UserDetails like PIC, Name, Type using users node in firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = firebaseDatabase.getReference("users");
        usersReference.keepSynced(true);

        Query query = usersReference.orderByChild("UID").equalTo(chatRoomItems.get(i).getUID());        //to get relevant details, userID should match the one inside arraylist.

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

                        //setting profile picture
                        Glide.with(context).asBitmap().load(user.getPhotoUrl())
                                .into(viewHolder.profilePic);
                        //setting user's name
                        viewHolder.name.setText(user.getName());
                        //setting the role of user.
                        viewHolder.role.setText(user.getType());
                    }
                }else{
                    //if user given user doesnt exist

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //endregion

        //region SETTING MESSAGE DETAILS LIKE LATEST MESSAGE IN CHATROOM AND TIMESTAMP

        final DatabaseReference chatroomRef = firebaseDatabase.getReference("ChatLogs")
                .child(firebaseProjectId)
                .child(ChatRoomIDGenerator.getChatRoomID(loggedInAs.getUid(), chatRoomItems.get(i).getUID()));  //ref to chatroom

        Query q =  chatroomRef.limitToLast(1);         //critical in fetching latest, [Modifier needed here]...
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot = " + dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //get to the message id node by calling getChildren  {loops through message objects}
                        FirebaseMessage message = snapshot.getValue(FirebaseMessage.class);      //populate details
                        Log.d(TAG, "Send by UID = " + message.getSentBy());
                        Log.d(TAG, "Body = " + message.getBody());
                        Log.d(TAG, "Time stamp = " + message.getTimeStamp());


                        viewHolder.latestMessage.setText(getFormattedBody(message));
                       /* viewHolder.timeStamp.setText(message.getTimeStamp());*/
                    }
                }else {
                    //what to do if messages dont exist for the chat. or chatroom unavail....       //FOR NOW DONT DO ANYTHING, LEAVE BLANK
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //setting timestamp
        viewHolder.timeStamp.setText(chatRoomItems.get(i).getLastRead());

        //endregion



        //onclick listener for when user selects the chatroom to go in to it
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //BE WARNED, values of the parcelable is set inside the firebase, so if datasnapshot goes missing, null pointer excp may occur

                Intent intent = new Intent(context, chatRoomActivity.class);
                intent.putExtra(KEY_INTENT_ProjectId, projectId);
                intent.putExtra(KEY_INTENT_UID, chatRoomItems.get(i).getUID());
                context.startActivity(intent);
            }
        });

    }


    @Override   //return size of arraylist passed in....
    public int getItemCount() {
        return chatRoomItems.size();
    }

    //HELPER METHODS TO PROCESS THE STRINGS IN THE onBindViewHolder() METHOD and makes it acceptable for UI render.
    public String getFormattedBody(FirebaseMessage message){

        String latestMsgBody = message.getBody();

        if(message.getSentBy().equals(loggedInAs.getUid())){    //if message is sent by you
            //append latest message view with  "You:" for the ui element.
            latestMsgBody = "You : " + latestMsgBody;
        }else {
            //if not sent by you Dont append anything...
        }

        if(latestMsgBody.length() > MAX_CHARS_LATEST_MSG) {       //if length greater than allowed, cut it down, append with three dots.
            latestMsgBody = latestMsgBody.substring(0, MAX_CHARS_LATEST_MSG) + "...";
        }else {
          //if message length is within the given limit, dont append anything...
        }
        return latestMsgBody;
    }


    public class  ViewHolder extends RecyclerView.ViewHolder{
        //widgets in one single item which is refered to as a card here.
        LinearLayout linearLayout;  //parentlayout that holds all the widgets listed below
        CircleImageView profilePic;
        TextView name;
        TextView role;
        TextView latestMessage;
        TextView timeStamp;
        TextView deliverStatus;

        String UID; //Stores UID of the displayed Viewholder

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //getting references to widgets/layout to manupulate them using adapter.
            linearLayout = itemView.findViewById(R.id.parentMessageCardLayout);

            profilePic = itemView.findViewById(R.id.lm_row_image);
            name = itemView.findViewById(R.id.lm_name);
            role = itemView.findViewById(R.id.lm_role);
            latestMessage = itemView.findViewById(R.id.lm_latestMessge);
            timeStamp = itemView.findViewById(R.id.lm_timeStamp);
            deliverStatus = itemView.findViewById(R.id.lm_deliverStatus);
            UID = "";
        }
    }

}
