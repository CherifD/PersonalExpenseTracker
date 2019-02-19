package com.cherifcodes.personalexpensetracker.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.cherifcodes.personalexpensetracker.backend.ExpenseCategoryTotal;

public class ViewModelListSumCatExpense extends ViewModel {
    private MutableLiveData<ExpenseCategoryTotal> mExpenseCategoryTotal = new MutableLiveData<>();

    public void setExpenseCategoryTotal(ExpenseCategoryTotal expenseCategoryTotal) {
        mExpenseCategoryTotal.setValue(expenseCategoryTotal);
    }

    public LiveData<ExpenseCategoryTotal> getExpenseCategoryTotal() {
        return mExpenseCategoryTotal;
    }
}
