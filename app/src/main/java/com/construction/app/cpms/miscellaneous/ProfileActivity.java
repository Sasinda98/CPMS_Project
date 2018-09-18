package com.construction.app.cpms.miscellaneous;

import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.construction.app.cpms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    //Refer Documentation at -: https://firebase.google.com/docs/auth/android/manage-users#get_a_users_profile

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final static String TAG = "ProfileActivity";

    private CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        MaterialButton materialButton = findViewById(R.id.buttonBadluck);
        imageView = findViewById(R.id.actprofile_profile_pic);

        displayProfilePicInImageView(imageView, firebaseUser, firebaseDatabase);

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setProfPic("https://firebasestorage.googleapis.com/v0/b/cpms-4780c.appspot.com/o/user%2FJw405DV177dkOg2nBWAjsAERs8j1%2FprofilePic%2Funnamed.jpg?alt=media&token=3597b2d3-6a6c-4fb2-8449-0dbe8ca095bb");
                setProfPicFireDB("https://firebasestorage.googleapis.com/v0/b/cpms-4780c.appspot.com/o/user%2FJw405DV177dkOg2nBWAjsAERs8j1%2FprofilePic%2Funnamed.jpg?alt=media&token=3597b2d3-6a6c-4fb2-8449-0dbe8ca095bb");
            }
        });
    }

    /*Do not use*/
    public void setProfPic(String url) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(url))
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });

    }

    /* sets the photoUrl of profile pic in firebase databsae, root/users/$UID/photoUrl */
    public void setProfPicFireDB(String url) {
        DatabaseReference reference = firebaseDatabase.getReference().getRoot();
        reference.child("users").child(firebaseUser.getUid()).child("photoUrl").setValue(url);
    }

    public void displayProfilePicInImageView(final CircleImageView circleImageView, FirebaseUser firebaseUser, FirebaseDatabase firebaseDatabase) {

        if (firebaseUser != null) {

            circleImageView.setImageResource(R.drawable.ic_prof_pic_dark);       //default profile pic, until one load from firebase.

            DatabaseReference reference = firebaseDatabase.getReference().getRoot();

            reference.child("users").child(firebaseUser.getUid()).child("photoUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String url = (String) dataSnapshot.getValue();

                    if (url != null || url != "") {
                        Glide.with(getApplicationContext()).load(url)
                                .asBitmap().into(circleImageView);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }
}
