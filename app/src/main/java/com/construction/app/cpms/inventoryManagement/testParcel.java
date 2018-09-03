package com.construction.app.cpms.inventoryManagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.construction.app.cpms.R;


public class testParcel extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_parcel);

        //Getting category object from category grid activity.
        //We get the image id and the category name for the query from this
        Intent intent = getIntent();
        inventory_category_Bean catBean = intent.getParcelableExtra("catObj");

        String catName = catBean.getName();
        int imgID = catBean.getImageID();
        ImageView imageView = findViewById(R.id.parcelImage);
        imageView.setImageResource(imgID);

        tv = (TextView) findViewById(R.id.parceltv);
        tv.setText(catName);
    }
}
