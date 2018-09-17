package com.construction.app.cpms.miscellaneous;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.bean.ChatRoomMainItem;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//Handles message cards
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ChatRoomMainItem> chatRoomItems = new ArrayList<ChatRoomMainItem>();
    private Context context;

    //Constructor
    public MessageRecyclerViewAdapter(ArrayList<ChatRoomMainItem> chatRoomItems, Context context) {
        this.chatRoomItems = chatRoomItems;
        this.context = context;
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        //Setting the values of the widgets to match the ones passed in through the arraylist.
        viewHolder.name.setText(chatRoomItems.get(i).getName());
        viewHolder.role.setText(chatRoomItems.get(i).getRole());
        viewHolder.deliverStatus.setText(chatRoomItems.get(i).getDeliverStatus());
        viewHolder.latestMessage.setText(chatRoomItems.get(i).getLastMessage());
        viewHolder.timeStamp.setText(chatRoomItems.get(i).getTimeStamp());

        Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/cpms-4780c.appspot.com/o/user%2FJw405DV177dkOg2nBWAjsAERs8j1%2FprofilePic%2Funnamed.jpg?alt=media&token=3597b2d3-6a6c-4fb2-8449-0dbe8ca095bb")
        .asBitmap().into(viewHolder.profilePic);
        //onclick listener for when user selects the chatroom to go in to it
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You clicked on item", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override   //return size of arraylist passed in....
    public int getItemCount() {
        return chatRoomItems.size();
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
            linearLayout = itemView.findViewById(R.id.parentMessageCardLayout);

            profilePic = itemView.findViewById(R.id.lm_row_image);
            name = itemView.findViewById(R.id.lm_name);
            role = itemView.findViewById(R.id.lm_role);
            latestMessage = itemView.findViewById(R.id.lm_latestMessge);
            timeStamp = itemView.findViewById(R.id.lm_timeStamp);
            deliverStatus = itemView.findViewById(R.id.lm_deliverStatus);

        }
    }

}
