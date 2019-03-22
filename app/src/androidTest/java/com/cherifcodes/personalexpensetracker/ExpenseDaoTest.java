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
import com.cherifcodes.personalexpensetracker.backend.Repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.time.LocalDate;
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
    public void testGetCurrWeekCategoryTotalList() {
        //Expense list to insert into the database
        Expense [] expenseArray = {
                new Expense("Food", "Wal-Mart", 30.00,
                        LocalDate.of(2019, 3, 15)),
                new Expense("Gas", "Shell", 30.00,
                        LocalDate.of(2019, 3, 17)),
                new Expense("Gas", "BP", 20.00,
                        LocalDate.of(2019, 3, 18)),
                new Expense("Education", "DataCamp", 45.00,
                        LocalDate.of(2019, 3, 19)),
                new Expense("Education", "DataCamp", 70.00,
                        LocalDate.of(2020, 1, 2)),
                new Expense("Food", "Wal-Mart", 50.00,
                        LocalDate.of(2019, 12, 24)),
                new Expense("Food", "Wal-Mart", 50.00,
                        LocalDate.of(2019, 3, 24)),
                new Expense("Food", "Harris Teeter", 60.00,
                        LocalDate.of(2023, 3, 24)),
                new Expense("Gas", "BP", 15.00,
                        LocalDate.of(2019, 3, 25))
        };

        // Insert all expenses into database
        for (Expense exp : expenseArray) {
            expenseDao.insertExpense(exp);
        }


        //Returned list of expense categories
        List<CategoryTotal> categoryTotalList = mDb.expenseDao().getCurrWeeksCategoryTotalList().getValue();
        assertTrue(categoryTotalList.size() == 3);

        for (CategoryTotal eCat : categoryTotalList) {
            if (eCat.getCategoryName().equals("Food"))
                assertTrue(eCat.getCategoryTotal() == 50.0);
            else if (eCat.getCategoryName().equals("Gas"))
                assertTrue(eCat.getCategoryTotal() == 50);
            else if (eCat.getCategoryName().equals("Education"))
                assertTrue(eCat.getCategoryTotal() == 45);
        }
    }
}
