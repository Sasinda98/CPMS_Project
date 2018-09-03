package com.construction.app.cpms.inventoryManagement;

import android.support.v7.app.AppCompatActivity;
import com.construction.app.cpms.R;

import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class inventory_request_item extends AppCompatActivity {

    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    private String currentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_request_item);
        //Getting current time to pass to script
        currentTime = now();


    }
// Used as tutorial: http://www.java-samples.com/showtutorial.php?tutorialid=682
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
}
