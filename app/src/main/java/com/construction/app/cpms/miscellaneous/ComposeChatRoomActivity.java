package com.construction.app.cpms.miscellaneous;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.construction.app.cpms.R;
import com.construction.app.cpms.glideModule.GlideApp;
import com.construction.app.cpms.miscellaneous.bean.ChatRoomIDGenerator;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseMessage;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseUserDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ComposeChatRoomActivity extends AppCompatActivity {
    private static final String TAG = "ComposeChatRoomAct";
    private CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_chat_room);

        circleImageView = findViewById(R.id.testProfilePic);
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a reference to a file from a Google Cloud Storage URI
        StorageReference gsReference = storage.getReferenceFromUrl("gs://cpms-4780c.appspot.com/users/Jw405DV177dkOg2nBWAjsAERs8j1/profilePicture");


        GlideApp.with(this )
                .load(gsReference)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(circleImageView);

    }


    public void invalidateCache(){

        //invlidate cache every 30mins.

        StorageReference gsReference = FirebaseStorage.getInstance().getReference();
        GlideApp.with(this )
                .load(gsReference)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(circleImageView);
    }



    @Override
    public void onBackPressed() {
        finish();
    }

    public void testing(){
        Log.d(TAG, "testing() CALLED");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = firebaseDatabase.getReference("users");

        Query query = usersReference.orderByChild("UID").equalTo("Jw405DV177dkOg2nBWAjsAERs8j1");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot = " + dataSnapshot.toString());

                for(DataSnapshot usernode : dataSnapshot.getChildren()) {//get to the user node that has user details as the value
                    FirebaseUserDetails user = usernode.getValue(FirebaseUserDetails.class);

                    Log.d(TAG, "Name = " + user.getName());
                    Log.d(TAG, "UID = " + user.getUID());
                    Log.d(TAG, "PhotoURl = " + user.getPhotoUrl());
                    Log.d(TAG, "type = " + user.getType());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void testing2(){

        Log.d(TAG, "testing() CALLED");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //region Handling part where latest message and time is fetched
        DatabaseReference chatroomRef = firebaseDatabase.getReference("ChatLogs")
                .child("Project-P1")
                .child(ChatRoomIDGenerator.getChatRoomID("Jw405DV177dkOg2nBWAjsAERs8j1", "4kE5XKKcm6VOBb7yISPfqKmW6Li2"));  //ref to chatroom

           Query q =  chatroomRef.limitToLast(1);
           q.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   Log.d(TAG, "DataSnapshot = " + dataSnapshot.toString());

                   for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //get to the message id node by calling getChildren
                       FirebaseMessage message = snapshot.getValue(FirebaseMessage.class);      //populate details
                       Log.d(TAG, "Send by UID = " + message.getSentBy());
                       Log.d(TAG, "Body = " + message.getBody());
                       Log.d(TAG, "Time stamp = " + message.getTimeStamp());


                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
    }

    @Override
    protected void onStart() {
        super.onStart();
        testing2();

    }
}
