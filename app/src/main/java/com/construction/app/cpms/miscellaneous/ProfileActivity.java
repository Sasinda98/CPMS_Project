package com.construction.app.cpms.miscellaneous;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.construction.app.cpms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends AppCompatActivity {

    //Refer Documentation at -: https://firebase.google.com/docs/auth/android/manage-users#get_a_users_profile

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private final static String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        MaterialButton materialButton = findViewById(R.id.buttonBadluck);

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProfPic("https://firebasestorage.googleapis.com/v0/b/cpms-4780c.appspot.com/o/user%2FJw405DV177dkOg2nBWAjsAERs8j1%2FprofilePic%2Funnamed.jpg?alt=media&token=3597b2d3-6a6c-4fb2-8449-0dbe8ca095bb");
            }
        });
}

    public void setProfPic(String url){
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


}
