package com.cherifcodes.personalexpensetracker.backend;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class Repository {

    private static Repository sInstance;
    private AppDatabase mDb;

    private Repository(Application application) {
        mDb = AppDatabase.getInstance(application);
    }

    public static Repository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (Repository.class) {
                if (sInstance == null) {
                    sInstance = new Repository(application);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<Expense>> getThisMonthsExpenses(String category) {
        return mDb.expenseDao().getThisMonthExpenses(category);
    }

    public LiveData<List<CategoryTotal>> getAllCategoryTotals() {
        return mDb.expenseDao().getAllCategoryTotals();
    }

    public void insertExpense(Expense expense) {
       new InsertAsyncTask(mDb).execute(expense);
   }

    public LiveData<List<Expense>> getThisWeeksExpenses(String category) {
        return mDb.expenseDao().getThisWeekExpenses(category);
    }

    public LiveData<List<Expense>> getThisYearsExpenses(String category) {
        return mDb.expenseDao().getThisYearExpenses(category);
    }

    public LiveData<Double> getThisCategoryTotalForThisWeek(String category) {
        return mDb.expenseDao().getThisCategoryTotalForThisWeek(category);
    }

    public LiveData<Double> getThisCategoryTotalForThisMonth(String category) {
        return mDb.expenseDao().getThisCategoryTotalForThisMonth(category);
    }

    public LiveData<Double> getThiCategoryTotalForThisYear(String category) {
        return mDb.expenseDao().getThisCategoryTotalForThisYear(category);
    }

    public LiveData<List<CategoryTotal>> getCurrWeeksCategoryTotalList() {
        return mDb.expenseDao().getCurrWeeksCategoryTotalList();
    }

    public LiveData<List<CategoryTotal>> getCurrMonthsCategoryTotalList() {
        return mDb.expenseDao().getCurrMonthsCategoryTotalList();
    }

    public LiveData<List<CategoryTotal>> getCurrYearsCategoryTotalList() {
        return mDb.expenseDao().getCurrYearsCategoryTotalList();
    }

    public LiveData<Double> getCurrWeeksCategoryTotal() {
        return mDb.expenseDao().getCurrWeeksCategoryTotal();
    }

    public LiveData<Double> getCurrMonthsCategoryTotal() {
        return mDb.expenseDao().getCurrMonthsCategoryTotal();
    }

    public LiveData<Double> getCurrYearsCategoryTotal() {
        return mDb.expenseDao().getCurrYearsCategoryTotal();
    }

    /**
     * AsyncTask class for inserting Expense objects into database
     */
    private static class InsertAsyncTask extends AsyncTask<Expense, Void, Void> {
        private AppDatabase mAsyncTaskDb;

        InsertAsyncTask(AppDatabase db) {
            mAsyncTaskDb = db;
        }

        @Override
        protected Void doInBackground(final Expense... params) {
            mAsyncTaskDb.expenseDao().insertExpense(params[0]);
            return null;
        }
    }
}
