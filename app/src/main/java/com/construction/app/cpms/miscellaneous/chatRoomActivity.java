package com.construction.app.cpms.miscellaneous;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.construction.app.cpms.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatRoomActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private AppCompatActivity appCompatActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


        setUpToolbar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*stackof-: https://stackoverflow.com/questions/28512662/slide-out-animation-not-working-on-back-press-button/28513065*/
        overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
    }

    public void setUpToolbar(){

        toolbar = findViewById(R.id.cr_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Name Of Recipient");
        getSupportActionBar().setSubtitle("Subtitle");

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.btm_navbar_item_notchecked));
        appCompatActivity = this;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //used stackoverflow forum to find this..
                appCompatActivity.onBackPressed();
            }
        });
    }
}
