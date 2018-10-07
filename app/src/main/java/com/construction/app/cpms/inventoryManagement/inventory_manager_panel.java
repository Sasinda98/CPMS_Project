package com.construction.app.cpms.inventoryManagement;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import com.construction.app.cpms.R;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class inventory_manager_panel extends AppCompatActivity {

    private CardView editItemsCard;
    private CardView requestsCard;
    private CardView requestHistoryCard;
    private CardView stockLowCard;
    private FloatingActionButton addItemFAB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_manager_panel);

        editItemsCard = (CardView) findViewById(R.id.editInventoryCard);
        requestsCard = (CardView) findViewById(R.id.requestsCard);
        requestHistoryCard = (CardView) findViewById(R.id.requestHistoryCard);
        stockLowCard = (CardView) findViewById(R.id.stockLowCard);


        editItemsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(inventory_manager_panel.this, inventory_edit_list.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                startActivity(intent);
            }
        });

        requestsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(inventory_manager_panel.this, inventory_incoming_requests.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                startActivity(intent);
            }
        });

        requestHistoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(inventory_manager_panel.this, inventory_request_history.class);
                startActivity(intent);
            }
        });

        stockLowCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(inventory_manager_panel.this, inventory_stock_low.class);
                startActivity(intent);
            }
        });


        addItemFAB = (FloatingActionButton) findViewById(R.id.add_item_fab);
        addItemFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(inventory_manager_panel.this, inventory_add_item.class);
                startActivity(intent);
            }
        });

    }
}
