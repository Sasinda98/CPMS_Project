package com.construction.app.cpms.inventoryManagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.construction.app.cpms.R;
import com.construction.app.cpms.inventoryManagement.beans.request_history_bean;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class request_history_adapter extends BaseAdapter {

    Context c;
    ArrayList<request_history_bean> requestHistory;

    public request_history_adapter(Context c, ArrayList<request_history_bean> requestHistory) {
        this.c = c;
        this.requestHistory = requestHistory;
    }

    @Override
    public int getCount() {
        return requestHistory.size();
    }

    @Override
    public Object getItem(int i) {
        return requestHistory.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view= LayoutInflater.from(c).inflate(R.layout.inventory_request_history_row,viewGroup,false);
        }

        final request_history_bean item= (request_history_bean) this.getItem(i);

        ImageView image = (CircleImageView) view.findViewById(R.id.request_history_row_image);
        TextView subConName= (TextView) view.findViewById(R.id.req_history_row_con_name);
        TextView itemName= (TextView) view.findViewById(R.id.req_history_row_item_name);
        TextView dateTextView = (TextView) view.findViewById(R.id.req_history_row_date_display);

        String conName = item.getSubConFName() + " " + item.getSubConLname();
        String date = item.getValDate().substring(0,10);
        subConName.setText(conName);
        itemName.setText(item.getItemName());
        dateTextView.setText(date);
        String stat = item.getReqStatus();

        if(stat.equalsIgnoreCase("Approved")){
            image.setImageResource(R.drawable.inventory_check);
        }
        else {
            image.setImageResource(R.drawable.inventory_cancel);
        }


        return view;

    }
}








