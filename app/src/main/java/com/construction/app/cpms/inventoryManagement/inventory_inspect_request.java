package com.construction.app.cpms.inventoryManagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.construction.app.cpms.R;


public class inventory_inspect_request extends AppCompatActivity {

    private TextView subConName, itemName, itemCat, itemQty, reqQty, reqMessage, inventoryBalance;
    private String iName, itemCategory, requestMessage, sCName;
    private int subConID, itemID;
    private double rQty, iQty, bal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_inspect_request);

        //Get all the values from previous screen
        Intent intent = getIntent();
        iName = intent.getStringExtra("itemName");
        itemCategory = intent.getStringExtra("category");
        requestMessage = intent.getStringExtra("message");
        sCName = intent.getStringExtra("subConName");
        itemID = intent.getIntExtra("itemID", 0);
        subConID = intent.getIntExtra("subConID", 0);
        Bundle b = getIntent().getExtras();
        iQty = b.getDouble("itemQty");
        rQty = b.getDouble("reqQty");

        bal = getBal(iQty, rQty);

        //Access all the text view
        subConName = (TextView) findViewById(R.id.incoming_request_reqName);
        itemName = (TextView) findViewById(R.id.incoming_request_itemName);
        itemCat = (TextView) findViewById(R.id.incoming_request_category);
        itemQty = (TextView) findViewById(R.id.incoming_request_itemQty);
        reqQty = (TextView) findViewById(R.id.incoming_request_reqQty);
        reqMessage = (TextView) findViewById(R.id.incoming_request_message);
        inventoryBalance = (TextView) findViewById(R.id.incoming_request_balance);

        //Set all the values for the textViews
        subConName.setText(sCName.toString());
        itemName.setText(iName.toString());
        itemCat.setText(itemCategory.toString());
        itemQty.setText(String.valueOf(iQty));
        reqQty.setText(String.valueOf(rQty));
        reqMessage.setText(requestMessage.toString());
        inventoryBalance.setText(String.valueOf(bal));



    }

    //Function written to get the amount of stock of item left if request is approved
    public double getBal(double iQty, double rQty){
        double bal;
        bal = iQty - rQty;
        return bal;
    }
}
