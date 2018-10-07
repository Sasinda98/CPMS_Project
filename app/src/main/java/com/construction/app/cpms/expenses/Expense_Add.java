package com.construction.app.cpms.expenses;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.construction.app.cpms.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Expense_Add extends AppCompatActivity implements Validator.ValidationListener {

    //variable initialization
    @NotEmpty
    @Length(min = 3, max = 20)
    private TextInputEditText description = null;

    @NotEmpty
    private TextInputEditText category = null;

    @NotEmpty
    private TextInputEditText amount = null;


    private Button submit = null;
    private String expCategory = null;
    private Validator validator;
    private boolean allokay = false;

    //Database
    private RequestQueue requestQueue;
    private String insertUrl = "http://projectcpms99.000webhostapp.com/scripts/ayyoob/addingExpense.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense__add);


        description = findViewById(R.id.expense_description);
        category = findViewById(R.id.expense_category);
        amount = findViewById(R.id.expense_amount);
        submit = findViewById(R.id.expense_submit_button);
        validator = new Validator(this);
        validator.setValidationListener(this);


        requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
                String category_V = category.getText().toString();

                 switch(category_V){
                    case "Direct":
                        allokay = true;
                        break;
                    case "Consult":
                        allokay = true;
                        break;
                    case "Miscell":
                        allokay = true;
                        break;
                    case "Overheads":
                        allokay = true;
                        break;
                    default:
                        category.setError("Category Does Not Exist!");
                        break;
                }



            }
        });






    }

    private void addData(){

        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("descr", description.getText().toString());
                params.put("catg", category.getText().toString());
                params.put("amt", amount.getText().toString());

                return params;
            }
        };
        expCategory = category.getText().toString();
        requestQueue.add(request);

        //CharSequence msg = "Successfully Added Expense";
        //Toast.makeText(Expense_Add.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this,"Validation Successful",Toast.LENGTH_LONG).show();
        addData();
        Intent intent = new Intent(Expense_Add.this, Expense_Category_List.class);
        intent.putExtra("expCategory", expCategory);
            startActivity(intent);

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for(ValidationError error : errors){
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if(view instanceof EditText){
                ((EditText) view).setError(message);
            }else{
                Toast.makeText(this,message,Toast.LENGTH_LONG).show();
            }
        }
    }
}
