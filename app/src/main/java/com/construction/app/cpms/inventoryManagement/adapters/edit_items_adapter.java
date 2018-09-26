package com.construction.app.cpms.inventoryManagement.adapters;


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


public class edit_items_adapter extends BaseAdapter{
    Context c;
    ArrayList<inventory_item_Bean> editItems;

    public edit_items_adapter(Context c, ArrayList<inventory_item_Bean> editItems) {
        this.c = c;
        this.editItems = editItems;
    }

    @Override
    public int getCount() {
        return editItems.size();
    }

    @Override
    public Object getItem(int i) {
        return editItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view= LayoutInflater.from(c).inflate(R.layout.inventory_edit_list_row,viewGroup,false);
        }

        final inventory_item_Bean item= (inventory_item_Bean) this.getItem(i);

        String cat;
        ImageView image= (ImageView) view.findViewById(R.id.edit_item_row_image);
        TextView name= (TextView) view.findViewById(R.id.edit_item_row_name);


        cat = item.getCategory();
        name.setText(item.getItemName());

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


        return view;
    }
}
