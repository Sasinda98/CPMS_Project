package com.construction.app.cpms.expenses;

import android.content.Intent;
import android.graphics.Color;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import com.construction.app.cpms.R;


import java.util.ArrayList;


public class actiExpenses extends AppCompatActivity {

    private static final String TAG = "ExpensesActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_expenses);

        
        Log.d(TAG, "onCreate: Started");
        ListView mListView = findViewById(R.id.listView);

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


        mListView.setBackgroundColor(Color.WHITE);

        ImageButton directButt = findViewById(R.id.directButt);
        directButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(actiExpenses.this, Expense_Category_List.class);

                startActivity(intent);
            }
        });

        FloatingActionButton add = findViewById(R.id.addExpenses);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(actiExpenses.this, Expense_Add.class);

                startActivity(intent);
            }
        });






    }

}
