package com.construction.app.cpms.miscellaneous.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.bean.ChatRoomIDGenerator;
import com.construction.app.cpms.miscellaneous.bean.User;
import com.construction.app.cpms.miscellaneous.chatRoomActivity;
import com.construction.app.cpms.miscellaneous.firebaseModels.ChatRoom;
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
public class MembersRecyclerViewAdapter extends RecyclerView.Adapter<MembersRecyclerViewAdapter.ViewHolder> {

    private ArrayList<User> userDetailsArrayList = new ArrayList<User>();
    private  FirebaseUser loggedInAs;
    private Context context;
    private String projectId;
    private AppCompatActivity activity; //to call onBackPressed() need this.

    public static final String TAG = "MessageRecyViewAdapt";
    public static final  int MAX_CHARS_LATEST_MSG = 30;

    //Constructor
    public MembersRecyclerViewAdapter( AppCompatActivity activity, ArrayList<User> userDetailsArrayList, FirebaseUser loggedInAs, String projectId) {
        this.userDetailsArrayList = userDetailsArrayList;
        this.context = activity.getApplicationContext();
        this.loggedInAs = loggedInAs;
        this.projectId = projectId;
        this.activity = activity;

    }

    @NonNull
    @Override       //recyclable
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // the relevant layout for single item which is single chatroom main item                      //not a popup menu so, no need to attach
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_member_detail_card, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override   //everytime a new item gets added/created to the view, this method gets called.
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        //onclick listener for when user selects the chatroom to go in to it
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "The i Value passed in to addChatroom = " + i );
                addChatRoom(projectId, loggedInAs.getUid(), userDetailsArrayList.get(i).getFirebaseId());
                activity.onBackPressed();   //Go back!

            }
        });


        //_____________________________________________________________________ Firebase Queries to set views  ______________________________________\\

        //region SET UserDetails like PIC, Name, Type using users node in firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = firebaseDatabase.getReference("users");
        usersReference.keepSynced(true);

        Query query = usersReference.orderByChild("UID").equalTo(userDetailsArrayList.get(i).getFirebaseId());        //to get relevant details, userID should match the one inside arraylist.

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


    }


    //region ADD CHATROOM method, Firebase
    // creates chatroom for user to engage.
    public void addChatRoom(String projectId, String loggedInUID, String receiverUID){
        Log.d(TAG, "addChatRoom(String projectId) CALLED");
        projectId = "Project-P" + projectId;       //database/./

        String chatroomID = ChatRoomIDGenerator.getChatRoomID(loggedInUID, receiverUID);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Rooms");        //reference to node Rooms (Where chatrooms are there)

        FirebaseUserRoom user1 = new FirebaseUserRoom(loggedInUID,"");
        FirebaseUserRoom user2 = new FirebaseUserRoom(receiverUID,"");
        ChatRoom chatRoom = new ChatRoom(user1,user2, null);
        //                      Project-P{number}/{chatroomID}/{chatRoom Object}
        databaseReference.child(projectId).child(chatroomID).setValue(chatRoom);
    }
    //endregion


    @Override   //return size of arraylist passed in....
    public int getItemCount() {
        return userDetailsArrayList.size();
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //getting references to widgets/layout to manupulate them using adapter.
            linearLayout = itemView.findViewById(R.id.parentMemberDetailCardLayout);

            profilePic = itemView.findViewById(R.id.md_row_image);
            name = itemView.findViewById(R.id.md_name);
            role = itemView.findViewById(R.id.md_role);
            latestMessage = itemView.findViewById(R.id.md_latestMessge);
            timeStamp = itemView.findViewById(R.id.md_timeStamp);
            deliverStatus = itemView.findViewById(R.id.md_deliverStatus);

        }
    }

}
