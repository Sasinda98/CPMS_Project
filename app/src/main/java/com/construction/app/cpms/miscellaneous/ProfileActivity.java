package com.construction.app.cpms.miscellaneous;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Binder;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.construction.app.cpms.MainActivity;
import com.construction.app.cpms.R;
import com.construction.app.cpms.miscellaneous.firebaseModels.FirebaseUserDetails;
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
import com.google.firebase.database.Query;
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
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private ValueEventListener profilePictureListener;

    private final static String TAG = "ProfileActivity";
    private final int requestCode = 44;     //picked random number to be 44, which will be the code to identify the intent

    public CircleImageView imageView;
    private ImageButton signOutBtn;
    private EditText roleET;
    private EditText nameET;
    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        MaterialButton materialButton = findViewById(R.id.buttonBadluck);
        imageView = findViewById(R.id.actprofile_profile_pic);
        signOutBtn = findViewById(R.id.signOutButton);
        roleET = findViewById(R.id.role_editText);
        nameET = findViewById(R.id.fname_editText);
        updateBtn = findViewById(R.id.updateBtn);

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

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteLoginData();  //log out the user.
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserDetails();    //firebase method
            }
        });



        populateUserDetails();


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
           profilePictureListener = reference.child("users").child(firebaseUser.getUid()).child("photoUrl").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                   if(dataSnapshot.exists()) {
                       //getting download url of prof pic
                       String url = (String) dataSnapshot.getValue();

                       url = url.trim();
                       if ((url != null) && (url != "")) {
                           //passing url and loading it in to circle image view.
                           Glide.with(getApplicationContext()).asBitmap().load(url)
                                   .into(imageView);
                       }

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

    //removing the set listeners
    @Override
    public void onDestroy() {
        super.onDestroy();
        //removing the listeners set on firebase once destroyed..
        if( profilePictureListener != null ) {
            DatabaseReference databaseReference = firebaseDatabase.getReference().getRoot();
            databaseReference.child("users").child(firebaseUser.getUid()).child("photoUrl").removeEventListener(profilePictureListener);
        }
    }

    //Sign Out method,.
    public void deleteLoginData(){
        /*Stackoverflow used as reference for use of sharepref in fragment*/
        SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

        this.firebaseAuth.signOut();
        //Toast.makeText(this,"SHARED PREF CLEARED,Restart APP", Toast.LENGTH_LONG).show();
        System.out.println("===========DELETE SHARED PREF==========");
    }

    private void populateUserDetails(){
        //region SET UserDetails like PIC, Name, Type using users node in firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = firebaseDatabase.getReference("users");
        usersReference.keepSynced(true);

        Query query = usersReference.orderByChild("UID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());        //to get relevant details, userID should match the one inside arraylist.

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

                        nameET.setText(user.getName());
                        roleET.setText(user.getType());

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

    private void updateUserDetails(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = firebaseDatabase.getReference("users");
       usersReference = usersReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        usersReference.child("name").setValue(nameET.getText().toString());
        usersReference.child("type").setValue(roleET.getText().toString());


    }

}
