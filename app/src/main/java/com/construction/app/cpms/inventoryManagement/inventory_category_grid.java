package com.construction.app.cpms.inventoryManagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.construction.app.cpms.R;
import com.construction.app.cpms.inventoryManagement.adapters.inventory_category_adapter;
import com.construction.app.cpms.inventoryManagement.beans.inventory_category_Bean;

import java.util.ArrayList;


public class inventory_category_grid extends AppCompatActivity {

    inventory_category_adapter adapter;

    GridView gridView;
    String userType;
    CardView managerCard;
    Button proj1;
    Button proj2;
    String projectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_category_grid);

        gridView = (GridView) findViewById(R.id.inventory_category_grid);
        adapter = new inventory_category_adapter(this, getData());
        gridView.setAdapter(adapter);
        managerCard = (CardView) findViewById(R.id.managerPanelCardView);

        //Temporary till user type implemented for user
        userType = "projectManager";

        //Temporary projectID
        SharedPreferences preferences = getSharedPreferences("projSwitch", Context.MODE_PRIVATE);
        projectID = preferences.getString("projSwitchID", "");


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            inventory_category_Bean catBean = (inventory_category_Bean) gridView.getItemAtPosition(i);
            String catName = catBean.getName();
            int imgID = catBean.getImageID();
            Intent intent = new Intent(inventory_category_grid.this, inventory_items_list.class);
            intent.putExtra("catName", catName);
            intent.putExtra("catImg",imgID);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
            startActivity(intent);

            }
        });

        //Button Leading to inventory management panel
        //Hide button if user is not the project manager
        if(!userType.equals("projectManager")){
            managerCard.setVisibility(View.GONE);
        }
        else {
            managerCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(inventory_category_grid.this, inventory_manager_panel.class);
                    startActivity(intent);
                }
            });
        }







    }



    private ArrayList getData() {
        ArrayList<inventory_category_Bean> categoryList = new ArrayList<>();

        inventory_category_Bean categoryBean = new inventory_category_Bean("COMMON", R.drawable.toolbox);
        categoryList.add(categoryBean);

        categoryBean = new inventory_category_Bean("MASONRY", R.drawable.brickwall);
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
