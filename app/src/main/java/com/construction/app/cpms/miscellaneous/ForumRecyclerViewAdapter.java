package com.construction.app.cpms.miscellaneous;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.bean.ForumPost;

import java.util.ArrayList;
import java.util.List;

//Used a lot of tutorials as reference to put this code together...

public class ForumRecyclerViewAdapter extends RecyclerView.Adapter<ForumRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ForumPost>  forumPostArrayList = new ArrayList<ForumPost>();

    public ForumRecyclerViewAdapter(Context context, ArrayList<ForumPost> forumPostArrayList) {
        this.context = context;
        this.forumPostArrayList = forumPostArrayList;
    }

    @NonNull
    @Override   //reference the layout
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_forum_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override   //manipulation of elements in the card
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //extracting the values from the arraylist and setting individual card view's ui components to match...
        viewHolder.forumTitle.setText(forumPostArrayList.get(i).getTitle());
        viewHolder.postedBy.setText(forumPostArrayList.get(i).getPostedBy());
        viewHolder.body.setText(forumPostArrayList.get(i).getBody());

    }

    @Override   //return item count in arraylist
    public int getItemCount() {
        return this.forumPostArrayList.size();
    }

    //search func specific method
    public void setFilterList(List<ForumPost> list){
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //mapping the ui elements, dynamic
            forumTitle = itemView.findViewById(R.id.postTitle);
            postedBy = itemView.findViewById(R.id.postedBy);
            body = itemView.findViewById(R.id.body);
            timestamp = itemView.findViewById(R.id.timeStamp);

            //bottom clickable, static.
            track = itemView.findViewById(R.id.track);
            viewMore = itemView.findViewById(R.id.viewMore);
            parentCardlayout = itemView.findViewById(R.id.forumCardParentLayout);


        }
    }

}
