package com.construction.app.cpms.miscellaneous;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.construction.app.cpms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    //Refer Documentation at -: https://firebase.google.com/docs/auth/android/manage-users#get_a_users_profile

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    private final static String TAG = "ProfileActivity";
    private final int requestCode = 44;     //picked random number to be 44, which will be the code to identify the intent

    private CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        MaterialButton materialButton = findViewById(R.id.buttonBadluck);
        imageView = findViewById(R.id.actprofile_profile_pic);

        //handles everything related to circular imageview used to show profpic.
        displayProfilePicInImageView(imageView, this.firebaseUser, this.firebaseDatabase);

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setProfPic("https://firebasestorage.googleapis.com/v0/b/cpms-4780c.appspot.com/o/user%2FJw405DV177dkOg2nBWAjsAERs8j1%2FprofilePic%2Funnamed.jpg?alt=media&token=3597b2d3-6a6c-4fb2-8449-0dbe8ca095bb");
                //setProfPicFireDB("https://firebasestorage.googleapis.com/v0/b/cpms-4780c.appspot.com/o/user%2FJw405DV177dkOg2nBWAjsAERs8j1%2FprofilePic%2Funnamed.jpg?alt=media&token=3597b2d3-6a6c-4fb2-8449-0dbe8ca095bb");

                //Making user pick the profile pic out of the gallery
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");      //restricting choice to only images.
                startActivityForResult(intent,requestCode);      // link -: https://developer.android.com/training/basics/intents/result
            }
        });
    }


    //This method deals with the image selected by user, method invoked after user selects imagge
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //matching the requestcode of intent to one we set to filter it out from all the other intents if there are others that is..
        //also if the result is fine, go ahead...
        if( ( requestCode == this.requestCode ) && ( resultCode == RESULT_OK ) ){
            Uri uri = data.getData();   //get returned result, data...
            setProfilePicFirebaseStorage(this.firebaseUser, this.firebaseStorage, uri);
            displayProfilePicInImageView(imageView, this.firebaseUser, this.firebaseDatabase);
        }
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
    /*Adds entry to firebase db*/
    public void setProfPicFireDB(String url) {
        DatabaseReference reference = firebaseDatabase.getReference().getRoot();
        reference.child("users").child(firebaseUser.getUid()).child("photoUrl").setValue(url);
    }

    public void displayProfilePicInImageView(final CircleImageView circleImageView, FirebaseUser firebaseUser, FirebaseDatabase firebaseDatabase) {

        if (firebaseUser != null) {

            circleImageView.setImageResource(R.drawable.ic_prof_pic_dark);       //default profile pic, until one load from firebase.

            DatabaseReference reference = firebaseDatabase.getReference().getRoot();

            //listener to set the circle imageview, updates it when value of child photoUrl changes.
            //which is the whole point of the listener used here.
            reference.child("users").child(firebaseUser.getUid()).child("photoUrl").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //getting download url of prof pic
                    String url = (String) dataSnapshot.getValue();

                    if ( ( url != null ) && ( url != "") ) {
                        //passing url and loading it in to circle image view.
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

    /*adds photo to firebase Storage*/
    public void setProfilePicFirebaseStorage(FirebaseUser loggedInAs, FirebaseStorage storage, Uri uri){
        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
       final StorageReference filepath = mStorage.child("users").child(loggedInAs.getUid()).child("profilePicture");      //setting path, where to save, users/{UID}/profilePicture/


        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //getting download link to the uploaded image
                //Reference link -: https://stackoverflow.com/questions/43641941/how-to-get-file-url-after-uploading-them-to-firebase
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri imageDwnlUrl = uri;
                        setProfPicFireDB(imageDwnlUrl.toString());
                        //System.out.println("URL============== " + imageDwnlUrl.toString());
                    }
                });


                 //setProfPicFireDB(downloadUrl.toString());
                Toast.makeText(getApplicationContext(), "Profile Picture Change Successful", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //notify user about failure
                Toast.makeText(getApplicationContext(), "Profile Picture Change Failed ", Toast.LENGTH_LONG).show();
                System.out.println(e.toString());
                e.printStackTrace();


            }
        });
    }
}
