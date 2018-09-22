package com.construction.app.cpms.glideModule;

//Firebase UI and glide usage -: https://github.com/firebase/FirebaseUI-Android/tree/master/storage
//followed documentation in the above link to make this class.


/*this allows storage ref to pass in to glide instead of querying the firebase databse*/

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public void registerComponents( Context context, Glide glide, Registry registry) {
        registry.append(StorageReference.class, InputStream.class,
                new FirebaseImageLoader.Factory());
    }
}
