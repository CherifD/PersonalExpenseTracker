package com.cherifcodes.personalexpensetracker.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.cherifcodes.personalexpensetracker.backend.Expense;

public class EditExpenseViewModel extends ViewModel {

    private MutableLiveData<Expense> mMutableLiveExpense;

    public EditExpenseViewModel() {
        mMutableLiveExpense = new MutableLiveData<>();
    }

    public void setLiveExpense(Expense expense) {
        mMutableLiveExpense.setValue(expense);
    }

    public LiveData<Expense> getLiveExpense() {
        return mMutableLiveExpense;
    }
}
