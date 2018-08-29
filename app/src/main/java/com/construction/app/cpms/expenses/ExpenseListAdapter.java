package com.construction.app.cpms.expenses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.construction.app.cpms.R;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListAdapter extends ArrayAdapter<Expense> {

    private static final String TAG = "ExpenseListAdapter";

    private Context mContext;
    int mResource;


    //constructor for context, resource, objects
    public ExpenseListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Expense> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    //get expense information
        String description = getItem(position).getDescription();
        String category = getItem(position).getCategory();
        double amount = getItem(position).getAmount();
        String amountAsString = Double.toString(amount);

        //creating expense object with details

        Expense exp = new Expense(description, category, amount);


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
        TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);
        TextView textView3 = (TextView) convertView.findViewById(R.id.textView3);

        textView1.setText(description);
        textView2.setText(category);
        textView3.setText(amountAsString);

        return convertView;



    }
}
