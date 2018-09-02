package com.construction.app.cpms.inventoryManagement;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.construction.app.cpms.R;

import java.util.ArrayList;


public class inventory_category_grid extends AppCompatActivity {

    inventory_category_adapter adapter;

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_category_grid);

        gridView = (GridView) findViewById(R.id.inventory_category_grid);
        adapter = new inventory_category_adapter(this, getData());
        gridView.setAdapter(adapter);

    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(inventory_category_grid.this, inventory_items_list.class);
            startActivity(intent);

        }
    });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.categoryFloatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(inventory_category_grid.this, inventory_add_item.class);

                startActivity(intent);
            }
        });
    }



    private ArrayList getData() {
        ArrayList<inventory_category_Bean> categoryList = new ArrayList<>();

        inventory_category_Bean categoryBean = new inventory_category_Bean("MASONRY", R.drawable.brickwall);
        categoryList.add(categoryBean);

        categoryBean = new inventory_category_Bean("CARPENTRY", R.drawable.saw);
        categoryList.add(categoryBean);

        categoryBean = new inventory_category_Bean("PLUMBING", R.drawable.plumbing);
        categoryList.add(categoryBean);

        categoryBean = new inventory_category_Bean("FLOORING", R.drawable.floor);
        categoryList.add(categoryBean);

        categoryBean = new inventory_category_Bean("ELECTRICAL", R.drawable.light_bulb);
        categoryList.add(categoryBean);

        categoryBean = new inventory_category_Bean("STEEL", R.drawable.crane);
        categoryList.add(categoryBean);

        categoryBean = new inventory_category_Bean("ROOFING", R.drawable.roof);
        categoryList.add(categoryBean);

        return categoryList;

    }

}
