package com.construction.app.cpms.miscellaneous;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.construction.app.cpms.Navigation;
import com.construction.app.cpms.Plan.MainPlan;
import com.construction.app.cpms.Plan.newMainPlan;
import com.construction.app.cpms.R;
import com.construction.app.cpms.SecondaryActivity;
import com.construction.app.cpms.expenses.actiExpenses;

import com.construction.app.cpms.inventoryManagement.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    private CardView plansTile;
    private CardView inventoryTile;
    private CardView financesTile;
    private CardView forumsTile;
    private CardView milestonesTile;
    private CardView projectTile;


    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private ValueEventListener profilePictureListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView() Called");
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setUpTopBar(view);

        /*Setting Dashboard as selected in the bottom nav bar, needed because line 85 Message fragment, alert navigates to this frag*/
        BottomNavigationView navigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        navigationView.getMenu().getItem(0).setChecked(true);

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        //Getting references to the tiles available to select from.
        plansTile = (CardView) view.findViewById(R.id.plansTile);
        inventoryTile = (CardView) view.findViewById(R.id.inventoryTile);
        financesTile = (CardView) view.findViewById(R.id.financesTile);
        forumsTile = (CardView) view.findViewById(R.id.forumsTile);
        milestonesTile = (CardView) view.findViewById(R.id.milestonesTile);
        projectTile =  (CardView) view.findViewById(R.id.projectTile);

        getLoginCredentials();

        //Click listeners for the tiles, navigate to the relevant activity you made using intents.
        plansTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast toast = Toast.makeText(getContext(), "Plans Tile Clicked", Toast.LENGTH_SHORT);
                toast.show();*/
                Intent intent = new Intent(getActivity(), newMainPlan.class);
                startActivity(intent, null);
            }
        });

        inventoryTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast toast = Toast.makeText(getContext(), "Inventory Tile Clicked", Toast.LENGTH_SHORT);
//                toast.show();
                Intent intent = new Intent(getActivity(), inventory_category_grid.class);
                startActivity(intent, null);

            }
        });

        financesTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast toast = Toast.makeText(getContext(), "Finances Tile Clicked", Toast.LENGTH_SHORT);
                toast.show();*/

                Intent intent = new Intent(getActivity(), actiExpenses.class);
                startActivity(intent, null);

            }
        });

        forumsTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Navigation)getActivity()).naviagateTo(new ForumsFragment(), true);
            }
        });

        milestonesTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast toast = Toast.makeText(getContext(), "Milestones Tile Clicked", Toast.LENGTH_SHORT);
                //toast.show();

                getLoginCredentials();
            }
        });

        projectTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //have your stuff here..... intent...
            }
        });


        /*Profile pic change related stuff*/

        //Reference-: https://stackoverflow.com/questions/45366125/how-to-store-google-authenticated-user-profile-picture-in-firebase-android
         final CircleImageView circleImageView = view.findViewById(R.id.db_profile_image);
     /*   Glide.with(getContext()).load("https://firebasestorage.googleapis.com/v0/b/cpms-4780c.appspot.com/o/user%2FJw405DV177dkOg2nBWAjsAERs8j1%2FprofilePic%2Funnamed.jpg?alt=media&token=3597b2d3-6a6c-4fb2-8449-0dbe8ca095bb")
                .asBitmap().into(circleImageView);*/
        if(firebaseUser != null){
            Log.d(TAG,"Firebase User != Null");

            circleImageView.setImageResource(R.drawable.ic_prof_pic);       //default profile pic, until one load from firebase.

            DatabaseReference reference = firebaseDatabase.getReference().getRoot();

            //listener to set the circle imageview, updates it when value of child photoUrl changes.
            //which is the whole point of the listener used here.
            ValueEventListener profilePictureListener = reference.child("users").child(firebaseUser.getUid()).child("photoUrl").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()) {

                            Log.d(TAG, "DATASNAPSHOT EXISTS = " + dataSnapshot.toString());
                            String url = (String) dataSnapshot.getValue();
                            url = url.trim();
                            Log.d(TAG, "URL = " + url.toString());

                            if ((url != null) && (url != "") && getContext() != null) {/*
                            Glide.with(getContext()).asBitmap().load(url)
                                    .into(circleImageView);*/
                                //caches stuff better
                                Log.d(TAG, "URL = " + url.toString());
                                Glide.with(getContext()).asBitmap().load(url).into(circleImageView);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG,"onCancelled");
                    }
                });
             //   circleImageView.setImageURI(firebaseUser.getPhotoUrl());
            }



        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    //Used mdc codelabs as reference
    private void setUpTopBar(View view){
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if(activity != null){
            activity.setSupportActionBar(toolbar);
        }
    }

    public void getLoginCredentials(){
        /*Stackoverflow used as reference for use of sharepref in fragment*/
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String email = preferences.getString("email","");
        String password = preferences.getString("password","");
        String userId = preferences.getString("userId","");

     //   Toast.makeText(getContext(),"Details em " + email + " " + password, Toast.LENGTH_LONG).show();
        System.out.println("==============GET CREDENTIAL EXECUTED DASHBOARD=====================");
    }

}
