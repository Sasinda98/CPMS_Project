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
import com.construction.app.cpms.inventoryManagement.beans.incoming_requests_bean;

import java.util.ArrayList;

public class incoming_requests_adapter extends BaseAdapter {

    Context c;
    ArrayList<incoming_requests_bean>incomingRequests;

    public incoming_requests_adapter(Context c, ArrayList<incoming_requests_bean> incomingRequests) {
        this.c = c;
        this.incomingRequests = incomingRequests;
    }

    @Override
    public int getCount() {
        return incomingRequests.size();
    }

    @Override
    public Object getItem(int i) {
        return incomingRequests.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view= LayoutInflater.from(c).inflate(R.layout.inventory_incoming_request_row,viewGroup,false);
        }

        final incoming_requests_bean item= (incoming_requests_bean) this.getItem(i);


        TextView subConName= (TextView) view.findViewById(R.id.req_row_con_name);
        TextView itemName= (TextView) view.findViewById(R.id.req_row_item_name);

        String conName = item.getSubConFName() + " " + item.getSubConLname();

        subConName.setText(conName);
        itemName.setText(item.getItemName());

        return view;

    }
}
