package com.construction.app.cpms;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class projectSwitcher extends Activity {

    Button proj1, proj2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_switcher);

        proj1 = (Button) findViewById(R.id.projSwitch1);
        proj2 = (Button) findViewById(R.id.projSwitch2);
        SharedPreferences preferences = getSharedPreferences("projSwitch", Context.MODE_PRIVATE);
        System.out.println("===========Project ID=========="+ preferences.getString("projSwitchID", ""));

        //Use this line if you want to get a demo project ID. ~Chandula
        String projectID = preferences.getString("projSwitchID", "");

        proj1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = getSharedPreferences("projSwitch", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.putString("projSwitchID","1");
                editor.commit();

                System.out.println("===========Project ID=========="+ preferences.getString("projSwitchID", ""));

            }
        });


        proj2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = getSharedPreferences("projSwitch", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.putString("projSwitchID","2");
                editor.commit();

                System.out.println("===========Project ID=========="+ preferences.getString("projSwitchID", ""));

            }
        });
    }

}
