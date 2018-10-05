package com.construction.app.cpms.miscellaneous.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.support.v7.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.bean.ForumPost;
import com.construction.app.cpms.miscellaneous.editForumPost;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseForumPost;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseLastRead;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseUserDetails;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//using filterable interface to support the search function to view posts.
public class ForumRecyclerViewAdapter extends RecyclerView.Adapter<ForumRecyclerViewAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "FORUMRECYC";

    private Context context;
    private FirebaseUser loggedInAs;
    private ArrayList<FirebaseForumPost>  forumPostArrayList = new ArrayList<FirebaseForumPost>();
    private ArrayList<FirebaseForumPost>  forumPostsArrayListCopy;
    private String projectId;



    public ForumRecyclerViewAdapter(Context context, ArrayList<FirebaseForumPost> forumPostArrayList, FirebaseUser loggedInAs, String projectId) {
        this.context = context;
        this.forumPostArrayList = forumPostArrayList;
        this.forumPostsArrayListCopy = new ArrayList<>(); //copy the contents of main list.
        this.forumPostsArrayListCopy.addAll(forumPostArrayList);
        this.loggedInAs = loggedInAs;
        this.projectId = projectId;
    }

    @NonNull
    @Override   //reference the layout
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_forum_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override   //manipulation of elements in the card
    public void onBindViewHolder( final ViewHolder viewHolder,final int i) {

        FirebaseForumPost forumPost = forumPostArrayList.get(i);

        Log.d(TAG, "Onbindviewholder called.");
        viewHolder.forumTitle.setText(forumPost.getTitle());
        //the name of user for posted by is queried and shown in the textview... look below for firebase query to find out more.
        viewHolder.body.setText(forumPost.getBody());
        viewHolder.timestamp.setText(getTimeOnly(forumPost.getDateTime()));


        //region POP UP MENU CODE

        //popup menu containing delete and edit must only be visible to logged in user's posts and nothing else. therefore this is coded as follows-:

        if(!loggedInAs.getUid().equals(forumPostArrayList.get(i).getPostedByUID())){        //if logged in UID != posted by UID (Not logged in user's post)
            //Make popup menu not visible.
            viewHolder.popup.setVisibility(View.GONE);
        }else {
            //means loggedInuser's UID == posted by, meaning the post is theirs. so enable pop up menu and provide necessary services

            viewHolder.popup.setVisibility(View.VISIBLE);

            viewHolder.popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    //inflate pop up                                //anchor point
                    PopupMenu popupMenu = new PopupMenu(context, viewHolder.popup);
                    popupMenu.inflate(R.menu.menu_forum_card_popup);


                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            String message = "Nothing selected";
                            switch (menuItem.getItemId()) {
                                case R.id.edit:
                                    message = "Edit selected";
                                    Intent intent = new Intent(view.getContext(), editForumPost.class);         //extras are important, helps firebase locate the node to edit.
                                    intent.putExtra("postId", forumPostArrayList.get(i).getPostId().toString());        /*passing the id of the post to editing activity*/
                                    intent.putExtra("projectId", projectId);                                            /*passing projectId to editing activity*/
                                    view.getContext().startActivity(intent);

                                    break;
                                case R.id.delete:
                                    // Log.d(TAG, "Delete the post with id = " +  forumPostArrayList.get(i).getForumId());
                                    //notifyDataSetChanged();

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                                    DatabaseReference postRef = database.getReference().getRoot()     //references relevant post.
                                            .child("ForumPosts")            //Make modular!
                                            .child("Project-P" + projectId )  //project ID is given prefix Project-P, Naming convention in the database.
                                            .child(forumPostArrayList.get(i).getPostId().toString());

                                    postRef.removeValue();
                                    
                                    break;
                            }

                            return false;
                        }
                    });
                    popupMenu.show();

                }
            });
        }
        //endregion

        //region SET UserDetails like PIC, Name, Type using users node in firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = firebaseDatabase.getReference("users");
        usersReference.keepSynced(true);

        Query query = usersReference.orderByChild("UID").equalTo(forumPostArrayList.get(i).getPostedByUID());        //to get relevant details, userID should match the one inside arraylist.

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

                        viewHolder.postedBy.setText(postedByStringProcessor(user.getName(), i));


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

        //region TAP FOR DATE AND TIME func
        //when user clicks this they can alternate between fullyspecified datetime and just time.
        viewHolder.timestamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.timestamp.setText(forumPostArrayList.get(i).getDateTime());          //shows fully specified date and time.
            }
        });

        //endregion

    }

    //region HELPER METHODS TO PROCESS STRINGS SHOWN IN THE UI.

    private String postedByStringProcessor(String name, int position){
        if(loggedInAs.getUid().equals(forumPostArrayList.get(position).getPostedByUID())) {        //if logged in UID == posted by UID (logged in user's post)
            //append with (YOU) for the name
           // return "Posted By : " + name + " (YOU)";
            return "Posted By : You";
        }
        return "Posted By : " + name;
    }


    public String getCurrentDateTime(){
        Date date = new Date();
        String timeFormat = "dd:MM:yyyy hh:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        String timeStamp = simpleDateFormat.format(date);

        return timeStamp;
    }

    //returns time only when given string with time in format mentioned inside the method.
    public String getTimeOnly(String dateTime) {
        //Time format Suppiorted = "dd:MM:yyyy hh:mm a"
        return dateTime.substring(11, dateTime.length() );
    }

    //returns date only when given string with time in format mentioned inside the method.
    public String getDateOnly(String dateTime) {
        //Time format Suppiorted = "dd:MM:yyyy hh:mm a"
        return dateTime.substring(0, 11);
    }

    //endregion

    @Override   //return item count in arraylist
    public int getItemCount() {
        return this.forumPostArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    //For the search function in searchview in the forum fragemnt
    //runs in background thread...
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<FirebaseForumPost> filteredPosts = new ArrayList<>();
            final String TAG = "FILTER";
            Log.d(TAG, "CharSeq = "  + charSequence.toString());
            Log.d(TAG, "arraylist size = "  + forumPostsArrayListCopy.size());

            if(charSequence == null || charSequence.length() == 0){ //if input is empty, meaning empty searchview
                filteredPosts.addAll(forumPostsArrayListCopy);   //insert all the elements of postarraylist on to the filtered post, because if nothing is in
                                                            //searchview field we have to display all the results.
            }else{
                String searchFor = charSequence.toString().toLowerCase().trim();    //remove whitespaces and tolowercase to avoid case sensitive search

                for (FirebaseForumPost post : forumPostsArrayListCopy){
                    if(post.getTitle().toLowerCase().contains(searchFor)){
                        filteredPosts.add(post);        //fill the filteredposts arraylist with relevant posts.
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredPosts;
            return results;
        }

        @Override       //ui thread, pass results
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            forumPostArrayList.clear();
            forumPostArrayList.addAll((ArrayList<FirebaseForumPost>)filterResults.values);
            notifyDataSetChanged();
        }
    };

    //search func specific method
    public void setFilterList(List<FirebaseForumPost> list){
        forumPostArrayList = new ArrayList<>();
        forumPostArrayList.addAll(list);
        notifyDataSetChanged();
    }

    //holds the widgets
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView forumTitle;
        TextView postedBy;
        TextView body;

        TextView track;
        TextView viewMore;
        TextView timestamp;

        LinearLayout parentCardlayout;

        ImageButton popup;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //mapping the ui elements, dynamic
            forumTitle = itemView.findViewById(R.id.postTitle);
            postedBy = itemView.findViewById(R.id.postedBy);
            body = itemView.findViewById(R.id.body);
            timestamp = itemView.findViewById(R.id.lm_timeStamp);

            //bottom clickable, static.
            track = itemView.findViewById(R.id.track);
            viewMore = itemView.findViewById(R.id.viewMore);
            parentCardlayout = itemView.findViewById(R.id.forumCardParentLayout);
            popup = itemView.findViewById(R.id.popMenu);


        }
    }

}
