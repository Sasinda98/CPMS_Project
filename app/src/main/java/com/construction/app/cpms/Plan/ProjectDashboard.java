package com.construction.app.cpms.Plan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.construction.app.cpms.R;

public class ProjectDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_dashboard);

        //Navigate from view project card to view project dashboard
        CardView cardView = (CardView)findViewById(R.id.card_view_project);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectDashboard.this, ViewProject.class);
                startActivity(intent);
            }
        });

        //Navigate from uploadProject card to addProject dashboard
        CardView cardView1 = (CardView)findViewById(R.id.card_upload_project);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectDashboard.this, AddProject.class);
                startActivity(intent);
            }
        });




    }
}
