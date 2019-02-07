package com.cherifcodes.personalexpensetracker.backend;

import android.app.Application;
import android.text.style.TtsSpan;

public class Repository {

    private AppDatabase db;
    private ExpenseDao expenseDao;

    Repository(Application application) {
        db = AppDatabase.getInstance(application);
        expenseDao = db.expenseDao();
    }


}
