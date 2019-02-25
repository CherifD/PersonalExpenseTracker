package com.cherifcodes.personalexpensetracker.backend;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {Expense.class}, version = 1)
@TypeConverters(DateTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    abstract public ExpenseDao expenseDao();
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            /////////////////
                            //AppDatabase.class, "Expense_database").allowMainThreadQueries().build();
                            ///////////////
                            AppDatabase.class, "Expense_database").build();
                    return INSTANCE;
                }
            }
        }
        return INSTANCE;
    }

}
