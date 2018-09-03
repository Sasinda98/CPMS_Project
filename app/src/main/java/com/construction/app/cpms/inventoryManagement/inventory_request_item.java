package com.construction.app.cpms.inventoryManagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import com.construction.app.cpms.R;

import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class inventory_request_item extends AppCompatActivity {

    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    private String currentTime;
    private String itemName;
    private int itemID;
    private TextView reqItemTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_request_item);

        //Getting category object from category grid activity.
        //We get the image id and the category name for the query from this
        Intent intent = getIntent();
        itemName = intent.getStringExtra("itemName");
        itemID = intent.getIntExtra("itemID", 0);

        reqItemTextView = (TextView) findViewById(R.id.request_item_name);
        reqItemTextView.setText(itemName);

        //Getting current time to pass to script
        currentTime = now();
        //Just for testing in Logcat
        System.out.println(currentTime);




    }
// Used as tutorial: http://www.java-samples.com/showtutorial.php?tutorialid=682
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
}
