package com.construction.app.cpms.miscellaneous;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.bean.ChatRoomMain;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {

    private String projectId;   //to be passed in from Harshan's createPlans and Harshan's user_plan
    private String userId;      //logged in user's userid.

    private ArrayList<ChatRoomMain> chatRoomMainArrayList = new ArrayList<>();

    //task1-: get the list of users relevant to current project from database, depends on Harshan's assigning users to relevant project func.
    //tasl2-: if the project doesnt have a project entry under messaging in firebase(Query to find out) add it
    // by creating relevant chatrooms for available users, if not don't. set it as root and continue to task3.
    //task3-: get timestamp, get latestmessage using query and also image of user using firebase (if task 2 had to get done, disregard non mandatory fields).
    //task4-: fill up an arraylist pass to adapter and display!



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_messages, null, false);

        //test data
        ChatRoomMain chatRoomMain1 = new ChatRoomMain("Gayal Sasinda","12:30pm", "Delivered","Project Manager","Hello World" );
        ChatRoomMain chatRoomMain2 = new ChatRoomMain("Harshan Sebastian","2:30pm", "Delivered","Plan Manager","Hello World1" );
        ChatRoomMain chatRoomMain3 = new ChatRoomMain("Ayyoob Rifdhi","1:30pm", "Delivered","Cons Manager","ayy World" );

        ChatRoomMain chatRoomMain4 = new ChatRoomMain("Gayal Sasinda","12:30pm", "Delivered","Project Manager","Hello World" );
        ChatRoomMain chatRoomMain5 = new ChatRoomMain("Harshan Sebastian","2:30pm", "Delivered","Plan Manager","Hello World1" );
        ChatRoomMain chatRoomMain6 = new ChatRoomMain("Ayyoob Rifdhi","1:30pm", "Delivered","Cons Manager","ayy World" );

        chatRoomMainArrayList.add(chatRoomMain1);
        chatRoomMainArrayList.add(chatRoomMain2);
        chatRoomMainArrayList.add(chatRoomMain3);
        chatRoomMainArrayList.add(chatRoomMain4);
        chatRoomMainArrayList.add(chatRoomMain5);
        chatRoomMainArrayList.add(chatRoomMain6);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_messaging);
        MessageRecyclerViewAdapter messageRecyclerViewAdapter = new MessageRecyclerViewAdapter( chatRoomMainArrayList, getContext());
        recyclerView.setAdapter(messageRecyclerViewAdapter);
        GridLayoutManager  gridLayoutManager = new GridLayoutManager(getContext(),1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        setUpTopBar(view);
        Toolbar toolbar = view.findViewById(R.id.toolbar);

        return view;
    }

    //Used mdc codelabs as reference, helper method
    private void setUpTopBar(View view){
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if(activity != null){
            activity.setSupportActionBar(toolbar);
        }
    }

}
