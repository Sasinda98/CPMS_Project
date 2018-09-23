package com.construction.app.cpms.miscellaneous.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
public class ChatBubbleRecyclerViewAdapter extends RecyclerView.Adapter<ChatBubbleRecyclerViewAdapter.ViewHolder> {
    public static final String TAG = "ChatBubbleRecyViewAdap";
    public static final String KEY_INTENT_ProjectId = "MessageRecycler-projectId";
    public static final String KEY_INTENT_UID = "MessageRecyvler-uid";

    private static final  int MAX_CHARS_LATEST_MSG = 30;

    private ArrayList<FirebaseMessage> messageArrayList = new ArrayList<>();
    private  FirebaseUser loggedInAs;
    private Context context;


    //Constructor
    public ChatBubbleRecyclerViewAdapter(ArrayList<FirebaseMessage> firbaseMessages, Context context, FirebaseUser loggedInAs, String projectId) {
        this.messageArrayList = firbaseMessages;
        this.context = context;
        this.loggedInAs = loggedInAs;
    }

    @NonNull
    @Override       //recyclable
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // the relevant layout for single item which is single chatroom main item                      //not a popup menu so, no need to attach
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_message_bubble, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override   //everytime a new item gets added/created to the view, this method gets called.
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.body.setText(messageArrayList.get(i).getBody());
        viewHolder.sentBy.setText(messageArrayList.get(i).getSentBy());
        viewHolder.timeStamp.setText(messageArrayList.get(i).getTimeStamp());
    }


    @Override   //return size of arraylist passed in....
    public int getItemCount() {
        return messageArrayList.size();
    }



    public class  ViewHolder extends RecyclerView.ViewHolder{
        //widgets in one single item which is refered to as a card here.
        RelativeLayout linearLayout;  //parentlayout that holds all the widgets listed below
        TextView body;
        TextView sentBy;
        TextView timeStamp;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //getting references to widgets/layout to manupulate them using adapter.
            linearLayout = itemView.findViewById(R.id.bubble_parentlayout);
            timeStamp = itemView.findViewById(R.id.mb_timeStamp);
            sentBy = itemView.findViewById(R.id.mb_senderName);
            body = itemView.findViewById(R.id.mb_messageBody);


        }
    }

}
