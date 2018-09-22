package com.construction.app.cpms.glideModule;


import android.support.annotation.NonNull;

import com.bumptech.glide.load.Key;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.Timestamp;
//for cache invalidation-: https://github.com/bumptech/glide/wiki/Caching-and-Cache-Invalidation
public class CacheSignature implements Key {

    private Timestamp time;

    public CacheSignature(Timestamp currentTime){
        this.time = currentTime;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ByteBuffer.allocate(Integer.SIZE).putInt(1).array());
    }
}
