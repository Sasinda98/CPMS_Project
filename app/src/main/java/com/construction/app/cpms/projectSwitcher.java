package com.construction.app.cpms;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class projectSwitcher extends Activity {

    Button proj1, proj2, pmBtn, masonBtn, carpenterBtn, plumberBtn, electricianBtn, floorerBtn, rooferBtn, architectBtn;
    TextView pstext, jrtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_switcher);

        proj1 = (Button) findViewById(R.id.projSwitch1);
        proj2 = (Button) findViewById(R.id.projSwitch2);
        pmBtn = (Button) findViewById(R.id.projectMngrBtn);
        architectBtn = (Button) findViewById(R.id.architectBtn);
        masonBtn = (Button) findViewById(R.id.masonBn);
        carpenterBtn = (Button) findViewById(R.id.carpenterBtn);
        plumberBtn = (Button) findViewById(R.id.plumberBtn);
        electricianBtn = (Button) findViewById(R.id.electricianBtn);
        floorerBtn = (Button) findViewById(R.id.floorerBtn);
        rooferBtn = (Button) findViewById(R.id.rooferBtn);



        pstext = (TextView) findViewById(R.id.projectSwitcherText);
        jrtext = (TextView) findViewById(R.id.jobRoleText);

        //Setting Shared preference for project ID
        SharedPreferences preferences = getSharedPreferences("projSwitch", Context.MODE_PRIVATE);
        System.out.println("===========Project ID=========="+ preferences.getString("projSwitchID", ""));
        pstext.setText("Current Project: "+ preferences.getString("projSwitchID", ""));
        //Copy and Use this line if you want to set a demo project ID. ~Chandula
        String projectID = preferences.getString("projSwitchID", "");

        //Setting shared preference for Job Role
        SharedPreferences pref = getSharedPreferences("jobSwitch", Context.MODE_PRIVATE);
        System.out.println("=========== User=========="+ preferences.getString("projSwitchID", ""));
        jrtext.setText("Current User: "+ pref.getString("jobRole", "Not Set"));
        //Copy and Use this line if you want to set a demo User Type. ~Chandula
        String jobRole = pref.getString("jobRole", "");




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


        pmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("jobSwitch", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.putString("jobRole","ProjectManager");
                editor.commit();
                jrtext.setText("Current User: "+ pref.getString("jobRole", ""));
                System.out.println("===========User =========="+ pref.getString("jobRole", ""));
            }
        });

        architectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("jobSwitch", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.putString("jobRole","Architect");
                editor.commit();
                jrtext.setText("Current User: "+ pref.getString("jobRole", ""));
                System.out.println("===========User =========="+ pref.getString("jobRole", ""));
            }
        });
        masonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("jobSwitch", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.putString("jobRole","Mason");
                editor.commit();
                jrtext.setText("Current User: "+ pref.getString("jobRole", ""));
                System.out.println("===========User =========="+ pref.getString("jobRole", ""));
            }
        });
        carpenterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("jobSwitch", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.putString("jobRole","Carpenter");
                editor.commit();
                jrtext.setText("Current User: "+ pref.getString("jobRole", ""));
                System.out.println("===========User =========="+ pref.getString("jobRole", ""));
            }
        });
        floorerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("jobSwitch", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.putString("jobRole","Floorer");
                editor.commit();
                jrtext.setText("Current User: "+ pref.getString("jobRole", ""));
                System.out.println("===========User =========="+ pref.getString("jobRole", ""));
            }
        });
        rooferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("jobSwitch", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.putString("jobRole","Roofer");
                editor.commit();
                jrtext.setText("Current User: "+ pref.getString("jobRole", ""));
                System.out.println("===========User =========="+ pref.getString("jobRole", ""));
            }
        });
        electricianBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("jobSwitch", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.putString("jobRole","Electrician");
                editor.commit();
                jrtext.setText("Current User: "+ pref.getString("jobRole", ""));
                System.out.println("===========User =========="+ pref.getString("jobRole", ""));
            }
        });
        plumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("jobSwitch", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.putString("jobRole","Plumber");
                editor.commit();
                jrtext.setText("Current User: "+ pref.getString("jobRole", ""));
                System.out.println("===========User =========="+ pref.getString("jobRole", ""));
            }
        });

    }

}
