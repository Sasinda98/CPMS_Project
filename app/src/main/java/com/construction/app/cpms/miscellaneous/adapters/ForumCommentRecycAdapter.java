package com.construction.app.cpms.miscellaneous.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseComment;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseUserDetails;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ForumCommentRecycAdapter extends RecyclerView.Adapter<ForumCommentRecycAdapter.ViewHolder> {
    private static final String TAG = "ForumCommnetRecycAdapte";

    private ArrayList<FirebaseComment> commentArrayList = new ArrayList<>();
    private Context context;
    private String projectID;
/*    private FirebaseUser loggedInAs;*/

    public ForumCommentRecycAdapter(Context context, ArrayList<FirebaseComment> commentArrayList, String projectID) {
        this.commentArrayList = commentArrayList;
        this.context = context;
        this.projectID = projectID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_comment_bubble, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.postedByTV.setText("TESTING.");
        viewHolder.commentTV.setText(commentArrayList.get(i).getComment());
        viewHolder.timeStamp.setText(getTimeOnly(commentArrayList.get(i).getTimeStamp()));

        viewHolder.deleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference postRef = database.getReference().getRoot()     //references relevant post.
                        .child("ForumsComments")            //Make modular!
                        .child("Project-P" + projectID )  //project ID is given prefix Project-P, Naming convention in the database.
                        .child(commentArrayList.get(i).getPostID())
                        .child(commentArrayList.get(i).getCommentID());

                postRef.removeValue();
            }
        });


        //region SET UserDetails like PIC, Name, Type using users node in firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = firebaseDatabase.getReference("users");
        usersReference.keepSynced(true);

        Query query = usersReference.orderByChild("UID").equalTo(commentArrayList.get(i).getGetPostedByUID());        //to get relevant details, userID should match the one inside arraylist.

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

                        //setting user's name
                        viewHolder.postedByTV.setText(user.getName());

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

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    //returns time only when given string with time in format mentioned inside the method.
    public String getTimeOnly(String dateTime) {
        //Time format Suppiorted = "dd:MM:yyyy hh:mm a"
        return dateTime.substring(11, dateTime.length() );
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView postedByTV;    //name of poster
        TextView commentTV;
        TextView timeStamp;
        TextView deleteTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postedByTV = itemView.findViewById(R.id.comment_postedBy);
            commentTV = itemView.findViewById(R.id.comment_body);
            timeStamp = itemView.findViewById(R.id.comment_timeStamp);
            deleteTV = itemView.findViewById(R.id.comment_delete);
        }
    }
}



