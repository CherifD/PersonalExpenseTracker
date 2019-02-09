package com.cherifcodes.personalexpensetracker.backend;

import android.app.Application;
import android.os.AsyncTask;
import android.text.style.TtsSpan;

import java.util.List;

public class Repository {

    private AppDatabase db;
    private ExpenseDao expenseDao;

    private List<Expense> expenseList;

    Repository(Application application) {
        db = AppDatabase.getInstance(application);
        expenseDao = db.expenseDao();
        expenseList = expenseDao.getAllExpenses();
    }

    public List<Expense> getExpenseList() {
        return expenseList;
    }

    public void insertExpense(final Expense expense) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                expenseDao.insertExpense(expense);
                return null;
            }
        };
    }
}
