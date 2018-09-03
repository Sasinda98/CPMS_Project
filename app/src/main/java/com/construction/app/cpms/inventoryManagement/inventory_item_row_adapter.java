package com.construction.app.cpms.inventoryManagement;
// http://camposha.info/source/android-custom-cardview-listview-source
//above used as examples
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.construction.app.cpms.R;
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

        ImageView image= (ImageView) view.findViewById(R.id.item_row_image);
        TextView name= (TextView) view.findViewById(R.id.item_row_name);
        TextView qty= (TextView) view.findViewById(R.id.item_row_qty);
        TextView unit= (TextView) view.findViewById(R.id.item_row_unit);


        name.setText(item.getItemName());
        qty.setText (String.valueOf(item.getItemQuantity()));
        unit.setText(item.getUnit());

        image.setImageResource(item.getImageID());

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(c, item.getItemName(), Toast.LENGTH_SHORT).show();
//            }
//        });

        return view;
    }
}
