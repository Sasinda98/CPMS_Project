package com.construction.app.cpms.miscellaneous;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.construction.app.cpms.R;
import com.construction.app.cpms.glideModule.GlideApp;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    /*    gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {

                Glide.with(getApplicationContext()).load(uri)
                        .into(circleImageView);




            }
        });*/
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
}
