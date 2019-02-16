package com.cherifcodes.personalexpensetracker.backend;

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

    @Query("SELECT * FROM Expense WHERE category LIKE :category")
    List<Expense> getExpensesByCategory(String category);

    @Query("SELECT category as categoryName, SUM(amount) as categoryTotal from Expense GROUP BY category")
    List<ExpenseCategoryTotal> getEntireCategoryTotals();

    @Query("SELECT * FROM Expense ORDER BY id ASC")
    List<Expense> getAllExpenses();

    @Query("SELECT COUNT(*) FROM Expense")
    int getNumberOfRecords();

    @Query("SELECT SUM(amount) from Expense WHERE date BETWEEN datetime('now', 'start of year') " +
            "AND datetime('now', 'start of year', '1 year')")
    double getThisYearTotal();

    @Query("SELECT category as categoryName, SUM(amount) as categoryTotal from Expense " +
            "WHERE date BETWEEN datetime('now', 'start of year') " +
            "AND datetime('now', 'start of year', '1 year') " +
            "GROUP BY category")
    List<ExpenseCategoryTotal> getCurrYearCategoryTotals();


    @Query("SELECT SUM(amount) from Expense WHERE date BETWEEN datetime('now', 'start of month') " +
            "AND datetime('now', 'start of month', '1 month')")
    double getThisMonthTotal();

    @Query("SELECT category as categoryName, SUM(amount) as categoryTotal from Expense " +
            "WHERE date BETWEEN datetime('now', 'start of month') " +
            "AND datetime('now', 'start of month', '1 month') " +
            "GROUP BY category")
    List<ExpenseCategoryTotal> getCurrMonthCategoryTotals();


    @Query("SELECT SUM(amount) from Expense WHERE strftime('%W', date) = strftime('%W', 'now')")
    double getThisWeekTotal();

    @Query("SELECT category as categoryName, SUM(amount) as categoryTotal from Expense " +
            "WHERE strftime('%W', date) = strftime('%W', 'now') " +
            "AND strftime('%Y', date) = strftime('%Y', 'now') " +
            "GROUP BY category")
    List<ExpenseCategoryTotal> getCurrWeekTotals();

}
