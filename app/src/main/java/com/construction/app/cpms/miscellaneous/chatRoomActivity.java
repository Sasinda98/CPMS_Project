package com.construction.app.cpms.miscellaneous;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.construction.app.cpms.R;

public class chatRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*stackof-: https://stackoverflow.com/questions/28512662/slide-out-animation-not-working-on-back-press-button/28513065*/
        overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
    }
}
