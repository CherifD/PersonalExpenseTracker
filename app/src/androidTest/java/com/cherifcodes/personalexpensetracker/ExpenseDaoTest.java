package com.cherifcodes.personalexpensetracker;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cherifcodes.personalexpensetracker.backend.AppDatabase;
import com.cherifcodes.personalexpensetracker.backend.Expense;
import com.cherifcodes.personalexpensetracker.backend.ExpenseDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ExpenseDaoTest {

    private ExpenseDao expenseDao;
    private AppDatabase mDb;

    @Before
    public void createDb() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(appContext, AppDatabase.class).build();
        expenseDao = mDb.expenseDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void test() throws Exception {

        Expense [] expenseArray = {
                new Expense("Gas", "Shell", 20.00,
                        LocalDateTime.of(2019, 2, 13, 2, 34)),
                new Expense("Gas", "BP", 25.00,
                        LocalDateTime.of(2019, 2, 9, 21, 3)),
                new Expense("Education", "DataCamp", 45.00,
                        LocalDateTime.of(2019, 3, 13, 2, 11)),
                new Expense("Food", "Wal-Mart", 50.00,
                        LocalDateTime.of(2020, 3, 13, 2, 56)),
        };

        // Insert all expenses into database
        for (Expense exp : expenseArray) {
            expenseDao.insertExpense(exp);
        }

        //Test getNumberOfRecords
        int numberOfRecords = expenseDao.getNumberOfRecords();
        assertEquals(numberOfRecords, expenseArray.length);

        //Test getThisYearTotal
        double thisYearTotal = expenseDao.getThisYearTotal();
        System.out.println("This year's total is: " + thisYearTotal);
        assertTrue(thisYearTotal == 90.00);

        //Test getThisMonthTotal
        double thisMonthTotal = expenseDao.getThisMonthTotal();
        assertTrue(thisMonthTotal == 45.0);
        System.out.println("This month's total is: " + thisMonthTotal);

        //Test getThisWeekTotal
        double thisWeekTotal = expenseDao.getThisWeekTotal();
        System.out.println("This week's total is: " + thisWeekTotal);
        assertTrue(thisWeekTotal == 25.0);

    }
}
