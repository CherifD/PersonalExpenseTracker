package com.cherifcodes.personalexpensetracker;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cherifcodes.personalexpensetracker.backend.AppDatabase;
import com.cherifcodes.personalexpensetracker.backend.CategoryTotal;
import com.cherifcodes.personalexpensetracker.backend.Expense;
import com.cherifcodes.personalexpensetracker.backend.ExpenseDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public void testGetTotalByPeriod() throws Exception {

        Expense [] expenseArray = {
                new Expense("Gas", "Shell", 20.00,
                        LocalDateTime.of(2019, 2, 15, 2, 34)),
                new Expense("Gas", "BP", 25.00,
                        LocalDateTime.of(2019, 2, 15, 21, 3)),
                new Expense("Education", "DataCamp", 45.00,
                        LocalDateTime.of(2019, 3, 15, 2, 11)),
                new Expense("Food", "Wal-Mart", 50.00,
                        LocalDateTime.of(2020, 3, 15, 2, 56)),
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
        assertTrue(thisWeekTotal == 45.0);
    }

    @Test
    public void testGetExpensesByCategory() {
        Expense [] expenseArray = {
                new Expense("Food", "Wal-Mart", 20.00,
                        LocalDateTime.of(2019, 3, 13, 2, 56)),
                new Expense("Gas", "Shell", 20.00,
                        LocalDateTime.of(2019, 2, 23, 2, 34)),
                new Expense("Gas", "BP", 25.00,
                        LocalDateTime.of(2019, 2, 22, 21, 3)),
                new Expense("Education", "DataCamp", 45.00,
                        LocalDateTime.of(2019, 3, 15, 2, 11)),
                new Expense("Food", "Wal-Mart", 50.00,
                        LocalDateTime.of(2020, 2, 21, 2, 56)),
                new Expense("Food", "Harris Teeter", 60.00,
                        LocalDateTime.of(2019, 5, 15, 2, 56)),
        };

        // Insert all expenses into database
        for (Expense exp : expenseArray) {
            expenseDao.insertExpense(exp);
        }

        //Test getExpenses by category
        List<Expense> categoryExpenses = expenseDao.getExpensesByCategory("Food");

        //Make sure the list size is correct
        //assertTrue(categoryExpenses.size() == 3);

        // View the returned list of expenses
        for (Expense ex : categoryExpenses) {
            System.out.println(ex + "\n\n");
        }

        //Test to make sure that only Expenses having the correct category are returned.
        List<Expense> actualList = new ArrayList<>();
        actualList.add(new Expense("Food", "Wal-Mart", 20.00,
                LocalDateTime.of(2019, 3, 22, 2, 56)));
        actualList.add(new Expense("Food", "Wal-Mart", 50.00,
                LocalDateTime.of(2020, 2, 22, 2, 56)));
        actualList.add(new Expense("Food", "Harris Teeter", 60.00,
                LocalDateTime.of(2019, 5, 11, 2, 56)));

        for (int i = 0; i < actualList.size(); i++) {
            assertTrue(actualList.get(i).getCategory().equals(categoryExpenses.get(i)
            .getCategory()));
        }

        //Test getCurrYearCategoryTotals()
        LiveData<List<CategoryTotal>> liveCategoryTotals = expenseDao.getAllCategoryTotals(); //WORKS!!
        List<CategoryTotal> categoryTotalList = liveCategoryTotals.getValue();
        //List<CategoryTotal> categoryTotals = expenseDao.getCurrYearCategoryTotals(); //WORKS!!
        //List<CategoryTotal> categoryTotalList = expenseDao.getAllCategoryTotals();
        // View the returned categoryTotals list
        for (CategoryTotal cat : categoryTotalList) {
            System.out.println("Category!!!: " + cat + "\n\n");
        }
    }

    @Test
    public void testGetCurrentYearCategoryTotals() {
        //Expense list to insert into the database
        Expense [] expenseArray = {
                new Expense("Food", "Wal-Mart", 50.00,
                        LocalDateTime.of(2019, 3, 15, 2, 56)),
                new Expense("Gas", "Shell", 30.00,
                        LocalDateTime.of(2019, 2, 15, 2, 34)),
                new Expense("Gas", "BP", 20.00,
                        LocalDateTime.of(2019, 2, 18, 21, 3)),
                new Expense("Education", "DataCamp", 45.00,
                        LocalDateTime.of(2019, 3, 13, 2, 11)),
                new Expense("Education", "DataCamp", 45.00,
                        LocalDateTime.of(2020, 1, 1, 0, 00)),
                new Expense("Food", "Wal-Mart", 50.00,
                        LocalDateTime.of(2019, 12, 31, 11, 50)),
                new Expense("Food", "Harris Teeter", 60.00,
                        LocalDateTime.of(2023, 5, 11, 2, 56)),
                new Expense("Gas", "Exxon Mobile", 40.00,
                        LocalDateTime.of(2019, 11, 1, 12, 34))
        };

        // Insert all expenses into database
        for (Expense exp : expenseArray) {
            expenseDao.insertExpense(exp);
        }
        
        //Returned list of category totals
        List<CategoryTotal> categoryTotalList = expenseDao.getCurrYearCategoryTotals();
        assertTrue(categoryTotalList.size() == 3);

        for (CategoryTotal eCat : categoryTotalList) {
            if (eCat.getCategoryName().equals("Food"))
                assertTrue(eCat.getCategoryTotal() == 100.0);
            else if (eCat.getCategoryName().equals("Gas"))
                assertTrue(eCat.getCategoryTotal() == 90);
            else if (eCat.getCategoryName().equals("Education"))
                assertTrue(eCat.getCategoryTotal() == 45);
        }
    }

    @Test
    public void testGetCurrMonthCategoryTotals() {
        //Expense list to insert into the database
        Expense [] expenseArray = {
                new Expense("Food", "Wal-Mart", 30.00,
                        LocalDateTime.of(2019, 2, 15, 2, 56)),
                new Expense("Gas", "Shell", 30.00,
                        LocalDateTime.of(2019, 2, 25, 2, 34)),
                new Expense("Gas", "BP", 20.00,
                        LocalDateTime.of(2019, 2, 16, 21, 3)),
                new Expense("Education", "DataCamp", 45.00,
                        LocalDateTime.of(2019, 2, 28, 2, 11)),
                new Expense("Education", "DataCamp", 45.00,
                        LocalDateTime.of(2020, 1, 1, 0, 00)),
                new Expense("Food", "Wal-Mart", 50.00,
                        LocalDateTime.of(2019, 12, 31, 11, 50)),
                new Expense("Food", "Harris Teeter", 60.00,
                        LocalDateTime.of(2023, 2, 11, 2, 56)),
        };

        // Insert all expenses into database
        for (Expense exp : expenseArray) {
            expenseDao.insertExpense(exp);
        }


        //Returned list of expense categories
        List<CategoryTotal> categoryTotalList = expenseDao.getCurrMonthCategoryTotals();
        assertTrue(categoryTotalList.size() == 3);

        for (CategoryTotal eCat : categoryTotalList) {
            if (eCat.getCategoryName().equals("Food"))
                assertTrue(eCat.getCategoryTotal() == 30.0);
            else if (eCat.getCategoryName().equals("Gas"))
                assertTrue(eCat.getCategoryTotal() == 50);
            else if (eCat.getCategoryName().equals("Education"))
                assertTrue(eCat.getCategoryTotal() == 45);
        }
    }


    @Test
    public void testGetCurrWeekCategoryTotals() {
        //Expense list to insert into the database
        Expense [] expenseArray = {
                new Expense("Food", "Wal-Mart", 30.00,
                        LocalDateTime.of(2019, 2, 15, 2, 56)),
                new Expense("Gas", "Shell", 30.00,
                        LocalDateTime.of(2019, 2, 25, 2, 34)),
                new Expense("Gas", "BP", 20.00,
                        LocalDateTime.of(2019, 2, 16, 21, 3)),
                new Expense("Education", "DataCamp", 45.00,
                        LocalDateTime.of(2019, 2, 18, 2, 11)),
                new Expense("Education", "DataCamp", 70.00,
                        LocalDateTime.of(2020, 1, 2, 0, 00)),
                new Expense("Food", "Wal-Mart", 50.00,
                        LocalDateTime.of(2019, 12, 31, 11, 50)),
                new Expense("Food", "Harris Teeter", 60.00,
                        LocalDateTime.of(2023, 2, 11, 2, 56)),
                new Expense("Gas", "BP", 15.00,
                        LocalDateTime.of(2019, 2, 15, 21, 3))
        };

        // Insert all expenses into database
        for (Expense exp : expenseArray) {
            expenseDao.insertExpense(exp);
        }


        //Returned list of expense categories
        List<CategoryTotal> categoryTotalList = expenseDao.getCurrWeekTotals();
        assertTrue(categoryTotalList.size() == 2);

        for (CategoryTotal eCat : categoryTotalList) {
            if (eCat.getCategoryName().equals("Food"))
                assertTrue(eCat.getCategoryTotal() == 30.0);
            else if (eCat.getCategoryName().equals("Gas"))
                assertTrue(eCat.getCategoryTotal() == 35);
            else if (eCat.getCategoryName().equals("Education"))
                assertTrue(eCat.getCategoryTotal() == 45);
        }
    }
}
