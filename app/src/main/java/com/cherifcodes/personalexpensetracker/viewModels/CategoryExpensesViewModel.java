package com.cherifcodes.personalexpensetracker.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cherifcodes.personalexpensetracker.appConstants.PeriodConstants;
import com.cherifcodes.personalexpensetracker.backend.Expense;
import com.cherifcodes.personalexpensetracker.backend.Repository;

import java.util.List;

public class CategoryExpensesViewModel extends AndroidViewModel {

    private Repository mRepository;
    private LiveData<List<Expense>> mThisMonthsExpenseList;
    private MutableLiveData<String> mCategory;

    public CategoryExpensesViewModel(@NonNull Application application, String category) {
        super(application);
        mRepository = Repository.getInstance(application);
        mCategory = new MutableLiveData<>();
        mCategory.setValue(category);

        mThisMonthsExpenseList = mRepository.getThisMonthExpenses(mCategory.getValue());
    }

    public void setCategory(String category) {
        mCategory.setValue(category);
    }

    public LiveData<List<Expense>> getThisMonthExpenseList() {
        return mThisMonthsExpenseList;
    }

    public void updateThisMonthExpenseList() {
        mThisMonthsExpenseList = mRepository.getThisMonthExpenses(mCategory.getValue());
    }

    /*public LiveData<List<Expense>> getExpenses(String period) {

        if (TextUtils.isEmpty(period))
            throw new IllegalArgumentException("Invalid period.");

        if (period.equals(PeriodConstants.THIS_MONTH))
            mThisMonthsExpenseList = mRepository.getThisMonthExpenses(mCategory);
        else if (period.equals(PeriodConstants.THIS_YEAR)) {
            //mThisMonthsExpenseList = mRepository.getThisMonthExpenses(mCategory);
        } else {
            //mThisMonthsExpenseList = mRepository.getThisMonthExpenses(mCategory);
        }

        return mThisMonthsExpenseList;
    }*/


    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application mApplication;
        private final String mCategory;

        public Factory(@NonNull Application application, String category) {
            mApplication = application;
            mCategory = category;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CategoryExpensesViewModel(mApplication, mCategory);
        }
    }
}
