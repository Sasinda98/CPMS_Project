package com.construction.app.cpms.inventoryManagement;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.construction.app.cpms.MainActivity;
import com.construction.app.cpms.R;
import java.util.ArrayList;
public class inventory_category_adapter extends BaseAdapter {


    Context c;
    ArrayList<inventory_category_Bean> categoryItems;

    public inventory_category_adapter(Context c, ArrayList<inventory_category_Bean> categoryItems) {
        this.c = c;
        this.categoryItems = categoryItems;
    }

    @Override
    public int getCount() {
        return categoryItems.size();
    }

    @Override
    public Object getItem(int i) {
        return categoryItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view= LayoutInflater.from(c).inflate(R.layout.inventory_category_card,viewGroup,false);
        }

        final inventory_category_Bean item= (inventory_category_Bean) this.getItem(i);

        ImageView image= (ImageView) view.findViewById(R.id.inventory_category_card_image);
        final TextView name= (TextView) view.findViewById(R.id.inventory_category_text);


        name.setText(item.getName());

        image.setImageResource(item.getImageID());

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(c, item.getName(), Toast.LENGTH_SHORT).show();
//
//            }
//        })
//        ;

        return view;
    }

}
