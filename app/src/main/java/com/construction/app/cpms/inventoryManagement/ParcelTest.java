package com.construction.app.cpms.inventoryManagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.construction.app.cpms.R;


public class ParcelTest extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_test);

        tv = (TextView) findViewById(R.id.parcelTV);

        Intent intent = getIntent();
        inventory_category_Bean catBean = intent.getParcelableExtra("catObj");

        String catName = catBean.getName();
        int imgID = catBean.getImageID();

        tv.setText(catName);



    }
}
