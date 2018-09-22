package com.construction.app.cpms.inventoryManagement.adapters;
// http://camposha.info/source/android-custom-cardview-listview-source
//above used as examples
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.construction.app.cpms.R;
import com.construction.app.cpms.inventoryManagement.beans.inventory_item_Bean;

import java.util.ArrayList;

public class inventory_item_row_adapter extends BaseAdapter {

    Context c;
    ArrayList<inventory_item_Bean> inventoryItems;

    public inventory_item_row_adapter(Context c, ArrayList<inventory_item_Bean> inventoryItems) {
        this.c = c;
        this.inventoryItems = inventoryItems;
    }

    @Override
    public int getCount() {
        return inventoryItems.size();
    }

    @Override
    public Object getItem(int i) {
        return inventoryItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view= LayoutInflater.from(c).inflate(R.layout.inventory_items_list_row,viewGroup,false);
        }

        final inventory_item_Bean item= (inventory_item_Bean) this.getItem(i);

        String cat;
        ImageView image= (ImageView) view.findViewById(R.id.item_row_image);
        TextView name= (TextView) view.findViewById(R.id.item_row_name);
        TextView qty= (TextView) view.findViewById(R.id.item_row_qty);
        TextView unit= (TextView) view.findViewById(R.id.item_row_unit);

        cat = item.getCategory();
        name.setText(item.getItemName());
        qty.setText (String.valueOf(item.getItemQuantity()));
        unit.setText(item.getUnit());



        if(cat.equalsIgnoreCase("Common")){
            image.setImageResource(R.drawable.toolbox);
        }else if(cat.equalsIgnoreCase("Masonry")){
            image.setImageResource(R.drawable.brickwall);
        }else if(cat.equalsIgnoreCase("Carpentry")){
            image.setImageResource(R.drawable.saw);
        }else if(cat.equalsIgnoreCase("Plumbing")){
            image.setImageResource(R.drawable.plumbing);
        }else if(cat.equalsIgnoreCase("Flooring")){
            image.setImageResource(R.drawable.floor);
        }else if(cat.equalsIgnoreCase("Electrical")){
            image.setImageResource(R.drawable.light_bulb);
        }else if(cat.equalsIgnoreCase("Steel")){
            image.setImageResource(R.drawable.crane);
        }else if(cat.equalsIgnoreCase("Roofing")){
            image.setImageResource(R.drawable.roof);
        }


//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(c, item.getItemName(), Toast.LENGTH_SHORT).show();
//            }
//        });

        return view;
    }
}
