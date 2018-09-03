package com.construction.app.cpms.expenses;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.R;

public class Expense_Add extends AppCompatActivity {

    //variable initialization
    private TextInputEditText description = null;
    private TextInputEditText category = null;
    private TextInputEditText amount = null;
    private Button submit = null;

    //Database
    private RequestQueue requestQueue;
    private String insertUrl = "http://projectcpms99.000webhostapp.com/scripts/insertUser.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense__add);

        description = findViewById(R.id.expense_description);
        category = findViewById(R.id.expense_category);
        amount = findViewById(R.id.expense_amount);
        submit = findViewById(R.id.expense_submit_button);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        boolean isFormValid = true;


    }
}
