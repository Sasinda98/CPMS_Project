package com.construction.app.cpms;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class projectSwitcher extends Activity {

    Button proj1, proj2;
    TextView pstext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_switcher);

        proj1 = (Button) findViewById(R.id.projSwitch1);
        proj2 = (Button) findViewById(R.id.projSwitch2);
        pstext = (TextView) findViewById(R.id.projectSwitcherText);
        SharedPreferences preferences = getSharedPreferences("projSwitch", Context.MODE_PRIVATE);
        System.out.println("===========Project ID=========="+ preferences.getString("projSwitchID", ""));
        pstext.setText("Current Project: "+ preferences.getString("projSwitchID", ""));
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
                pstext.setText("Current Project: "+ preferences.getString("projSwitchID", ""));
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
                pstext.setText("Current Project: "+ preferences.getString("projSwitchID", ""));
                System.out.println("===========Project ID=========="+ preferences.getString("projSwitchID", ""));

            }
        });
    }

}
