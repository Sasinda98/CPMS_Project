package com.construction.app.cpms.Plan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.construction.app.cpms.R;

public class newMainPlan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main_plan);

        //Navigate from view plan card to viewPlan dashboard
        CardView cardView1 = (CardView)findViewById(R.id.card_view);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent1 = new Intent(newMainPlan.this, MainPlan.class);
                startActivity(myIntent1);
            }
        });

        //Navigate from uploadPlan card to addPlan dashboard
        CardView cardView2 = (CardView)findViewById(R.id.card_upload);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent2 = new Intent(newMainPlan.this, addPlan.class);
                startActivity(myIntent2);
            }
        });
    }
}
