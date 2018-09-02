package com.construction.app.cpms.expenses;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.construction.app.cpms.R;

import java.util.ArrayList;

public class Expense_Category_List extends AppCompatActivity {

    private static final String TAG = "ExpensesActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense__category__list);

        Log.d(TAG, "onCreate: Started");
        ListView mListView = findViewById(R.id.exp_listView);

        //create sample expenses
        Expense exp1 = new Expense("Labour Charges", "Direct", 50000);
        Expense exp2 = new Expense("Supplier Payment", "Direct", 312000);
        Expense exp3 = new Expense("Tile Purchase", "Direct", 50000);
        Expense exp4 = new Expense("Supplier Payment 01", "Direct", 500000);
        Expense exp5 = new Expense("Raw Material Purchase", "Direct", 845000);
        Expense exp6 = new Expense("Labour Charges 2", "Direct", 60000);
        Expense exp7 = new Expense("Labour Charges 3", "Direct", 100000);
        Expense exp8 = new Expense("Labour Charges 4", "Direct", 100000);
        Expense exp9 = new Expense("Labour Charges 5", "Direct", 100000);
        Expense exp10 = new Expense("Labour Charges 6", "Direct", 100000);
        Expense exp11 = new Expense("Labour Charges 7", "Direct", 100000);
        Expense exp12 = new Expense("Labour Charges 8", "Direct", 100000);
        Expense exp13 = new Expense("Labour Charges 9", "Direct", 100000);
        Expense exp14 = new Expense("Labour Charges 10", "Direct", 100000);

        //Adding objects to ArrayList
        ArrayList<Expense> theList = new ArrayList<>();
        theList.add(exp1);
        theList.add(exp2);
        theList.add(exp3);
        theList.add(exp4);
        theList.add(exp5);
        theList.add(exp6);
        theList.add(exp7);
        theList.add(exp8);
        theList.add(exp9);
        theList.add(exp10);
        theList.add(exp11);
        theList.add(exp12);
        theList.add(exp13);
        theList.add(exp14);



        ExpenseListAdapter adapter = new ExpenseListAdapter(this, R.layout.expenses_adapter_view_layout, theList);
        mListView.setAdapter(adapter);





    }
}
