package com.cherifcodes.personalexpensetracker.backend;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertExpense(Expense expense);

    @Update
    int updateExpense(Expense expense);

    @Delete
    int deleteExpense(Expense expense);

    @Query("SELECT * FROM Expense WHERE category = :category")
    List<Expense> getExpensesByCategory(String category);

    @Query("SELECT category as categoryName, SUM(amount) as categoryTotal from Expense GROUP BY category")
    public LiveData<List<CategoryTotal>> getAllCategoryTotals();

    @Query("SELECT * FROM Expense WHERE strftime('%Y', date) = strftime('%Y', 'now', 'localtime') " +
            "AND category = :category " +
            "ORDER BY date DESC")
    LiveData<List<Expense>> getThisYearExpenses(String category);

    @Query("SELECT * FROM Expense WHERE strftime('%m', date) = strftime('%m', 'now', 'localtime') " +
            "AND strftime('%Y', date) = strftime('%Y', 'now', 'localtime') " +
            "AND category = :category " +
            "ORDER BY date DESC")
    LiveData<List<Expense>> getThisMonthExpenses(String category);

    @Query("SELECT * FROM Expense WHERE strftime('%W', date) = strftime('%W', 'now', 'localtime') " +
            "AND strftime('%Y', date) = strftime('%Y', 'now', 'localtime') " +
            "AND category = :category " +
            "ORDER BY date DESC")
    LiveData<List<Expense>> getThisWeekExpenses(String category);


    // Below are the queries for category totals lists retrieval
    @Query("SELECT category as categoryName, SUM(amount) as categoryTotal from Expense " +
            "WHERE strftime('%Y', date) = strftime('%Y', 'now', 'localtime') " +
            "GROUP BY category")
    LiveData<List<CategoryTotal>> getCurrYearsCategoryTotalList();

    @Query("SELECT category as categoryName, SUM(amount) as categoryTotal from Expense " +
            " WHERE strftime('%Y', date) = strftime('%Y', 'now', 'localtime') " +
            " AND strftime('%m', date) = strftime('%m', 'now', 'localtime')" +
            " Group By category")
    LiveData<List<CategoryTotal>> getCurrMonthsCategoryTotalList();

    @Query("SELECT category as categoryName, SUM(amount) as categoryTotal from Expense " +
            " WHERE strftime('%Y', date) = strftime('%Y', 'now', 'localtime') " +
            " AND strftime('%W', date) = strftime('%W', 'now', 'localtime')" +
            " Group By category")
    LiveData<List<CategoryTotal>> getCurrWeeksCategoryTotalList();
    // End of category totals lists queries


    @Query("SELECT SUM(amount) FROM Expense WHERE category = :category " +
            "AND strftime('%W', date) = strftime('%W', 'now', 'localtime') " +
            "AND strftime('%Y', date) = strftime('%Y', 'now', 'localtime')")
    LiveData<Double> getThisCategoryTotalForThisWeek(String category);

    @Query("SELECT SUM(amount) FROM Expense WHERE category = :category " +
            "AND strftime('%m', date) = strftime('%m', 'now', 'localtime') " +
            "AND strftime('%Y', date) = strftime('%Y', 'now', 'localtime')" )
    LiveData<Double> getThisCategoryTotalForThisMonth(String category);

    @Query("SELECT SUM(amount) FROM Expense WHERE category = :category " +
            "AND strftime('%Y', date) = strftime('%Y', 'now', 'localtime')" )
    LiveData<Double> getThisCategoryTotalForThisYear(String category);

    @Query("SELECT SUM(amount) from Expense WHERE " +
            "strftime('%W', date) == strftime('%W', 'now', 'localtime') " +
            "AND strftime('%Y', date) == strftime('%Y', 'now', 'localtime')")
    LiveData<Double> getCurrWeeksCategoryTotal();

    @Query("SELECT SUM(amount) from Expense WHERE strftime('%Y', date) == strftime('%Y', 'now', 'localtime') " +
            "AND strftime('%m', date) == strftime('%m', 'now', 'localtime')" )
    LiveData<Double> getCurrMonthsCategoryTotal();

    @Query("SELECT SUM(amount) from Expense WHERE strftime('%Y', date) == strftime('%Y', 'now', 'localtime')" )
    LiveData<Double> getCurrYearsCategoryTotal();

    @Query("SELECT SUM(amount) from Expense WHERE strftime('%Y', date) == strftime('%Y', 'now', 'localtime') " +
            "AND strftime('%m', date) == strftime('%m', 'now', 'localtime')" )
    double getCurrMonthsCategoryTotal_forWidget();
}
