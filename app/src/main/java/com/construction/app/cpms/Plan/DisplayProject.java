package com.construction.app.cpms.Plan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.construction.app.cpms.R;

public class DisplayProject extends AppCompatActivity {

    String Name, Description;
    int projectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_project);

        getIncomIntent();

        //navigate back to ViewProjects
        ImageButton imageButton = (ImageButton)findViewById(R.id.projectBackButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayProject.this, ViewProject.class);
                startActivity(intent);
                }
        });


    }

    private void getIncomIntent() {
        if (getIntent().hasExtra("name") && getIntent().hasExtra("description")) {
            Name = getIntent().getStringExtra("name");
            Description = getIntent().getStringExtra("description");
            projectID = getIntent().getIntExtra("PID", 0);
            setImage(Name, Description);
        }
    }

    private void setImage(String Name, String description) {
        EditText nam = findViewById(R.id.project_d_name);
        nam.setText(Name);

        EditText desc = findViewById(R.id.project_d_description);
        desc.setText(description);

    }



}

