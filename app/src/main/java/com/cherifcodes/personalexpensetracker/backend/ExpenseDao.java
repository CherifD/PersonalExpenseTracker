package com.cherifcodes.personalexpensetracker.backend;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.time.LocalDateTime;
import java.util.List;

import static android.icu.text.MessagePattern.ArgType.SELECT;

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

    @Query("SELECT * FROM Expense ORDER BY id ASC")
    List<Expense> getAllExpenses();

    @Query("SELECT COUNT(*) FROM Expense")
    int getNumberOfRecords();

    @Query("SELECT SUM(amount) from Expense WHERE date BETWEEN datetime('now', 'start of year') " +
            "AND datetime('now', 'start of year', '1 year', '-1 day')")
    double getThisYearTotal();

    @Query("SELECT SUM(amount) from Expense WHERE date BETWEEN datetime('now', 'start of month') " +
            "AND datetime('now', 'start of month', '1 month', '-1 day')")
    double getThisMonthTotal();

    @Query("SELECT SUM(amount) from Expense WHERE strftime('%W', date) = strftime('%W', 'now')")
    double getThisWeekTotal();

}
