package com.construction.app.cpms.expenses;

public class Expense {
    private String expenseID;
    private String description;
    private String category;
    private double amount;

    public Expense(String expenseID, String description, String category, double amount) {
        this.expenseID = expenseID;
        this.description = description;
        this.category = category;
        this.amount = amount;
    }

    public void setExpenseID(String expenseID){this.expenseID = expenseID;    }

    public String getExpenseID(){return expenseID;}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
