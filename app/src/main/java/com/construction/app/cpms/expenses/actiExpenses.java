package com.construction.app.cpms.expenses;

import android.graphics.Color;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.construction.app.cpms.R;


import java.util.ArrayList;


public class actiExpenses extends AppCompatActivity {

    private static final String TAG = "ExpensesActivity";

    ListView LV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_expenses);
        Log.d(TAG, "onCreate: Started");
        ListView mListView = (ListView) findViewById(R.id.listView);

        //create sample expenses
        Expense exp1 = new Expense("Labour Charges", "Direct", 50000);
        Expense exp2 = new Expense("Supplier Payment", "Direct", 312000);
        Expense exp3 = new Expense("Architect Fee", "Consultation", 50000);
        Expense exp4 = new Expense("Transport", "Miscellaneous", 22000);
        Expense exp5 = new Expense("Site Utilities", "Overheads", 18600);
        Expense exp6 = new Expense("Labour Charges 2", "Direct", 60000);
        Expense exp7 = new Expense("Labour Charges 3", "Direct", 100000);

        //Adding objects to ArrayList
        ArrayList<Expense> theList = new ArrayList<>();
        theList.add(exp1);
        theList.add(exp2);
        theList.add(exp3);
        theList.add(exp4);
        theList.add(exp5);
        theList.add(exp6);
        theList.add(exp7);

        ExpenseListAdapter adapter = new ExpenseListAdapter(this, R.layout.expenses_adapter_view_layout, theList);
        mListView.setAdapter(adapter);

        LV = (ListView) findViewById(R.id.listView);
        LV.setBackgroundColor(Color.WHITE);


    }

}
