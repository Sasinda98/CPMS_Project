package com.construction.app.cpms.inventoryManagement;

import android.support.v7.app.AppCompatActivity;
import com.construction.app.cpms.R;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;


public class inventory_items_list extends AppCompatActivity {

    inventory_item_row_adapter adapter;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items_list);

        lv = (ListView) findViewById(R.id.items_listView);
        adapter = new inventory_item_row_adapter(this, getData());
        lv.setAdapter(adapter);

    }

    private ArrayList getData(){
         ArrayList<inventory_item_Bean> itemBeanList = new ArrayList<>();

        inventory_item_Bean inventoryItemBean = new inventory_item_Bean("Cement", "100","Masonry" , "Bags", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("Bricks", "2500","Masonry" , "Nos.", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("Cinder Blocks", "2500","Masonry" , "Nos.", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("Sand", "50","Masonry" , "Cubes.", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("Rubble", "250","Masonry" , "tonnes.", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("Metal", "40", "Masonry", "tonnes", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("Lime", "250","Masonry" , "Cubes", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("Plywood", "2500","Masonry" , "Nos.", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("Shuttering", "2500","Masonry" , "Nos.", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("2X2 Timber", "2500","Masonry" , "Nos.", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("4X2 Timber", "2500","Masonry" , "Nos.", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("10mm Tor Steel", "2500","Masonry" , "Meters", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("12mm Tor Steel", "2500","Masonry" , "Meters", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("16mm Tor Steel", "2500","Masonry" , "Meters", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("08mm Tor Steel", "2500","Masonry" , "Meters", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("1.5' Wire Nails", "2500","Masonry" , "Nos.", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);

        inventoryItemBean = new inventory_item_Bean("3' Wire Nails", "2500","Masonry" , "Nos.", R.drawable.brickwall);
        itemBeanList.add(inventoryItemBean);
        return itemBeanList;
    }
}
