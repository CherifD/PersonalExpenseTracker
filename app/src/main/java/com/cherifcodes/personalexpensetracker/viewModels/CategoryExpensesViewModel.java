package com.cherifcodes.personalexpensetracker.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.cherifcodes.personalexpensetracker.backend.Expense;
import com.cherifcodes.personalexpensetracker.backend.Repository;

import java.util.List;

public class CategoryExpensesViewModel extends AndroidViewModel {
    private Repository mRepository;
    private LiveData<List<Expense>> mThisMonthsExpenseList;
    private LiveData<List<Expense>> mThisWeeksExpenseList;
    private LiveData<List<Expense>> mThisYearsExpenseList;
    private MutableLiveData<String> mCategory;

    private LiveData<Double> mThisCategoryTotalForThisWeek;
    private LiveData<Double> mThisCategoryTotalForThisMonth;
    private LiveData<Double> mThisCategoryTotalForThisYear;

    public CategoryExpensesViewModel(@NonNull Application application, String category) {
        super(application);
        mRepository = Repository.getInstance(application);
        mCategory = new MutableLiveData<>();
        mCategory.setValue(category);

        updateExpenseList();
    }

    public void updateExpenseList() {
        mThisMonthsExpenseList = mRepository.getThisMonthsExpenses(mCategory.getValue());
        mThisWeeksExpenseList = mRepository.getThisWeeksExpenses(mCategory.getValue());
        mThisYearsExpenseList = mRepository.getThisYearsExpenses(mCategory.getValue());

        mThisCategoryTotalForThisWeek = mRepository.getThisCategoryTotalForThisWeek(mCategory.getValue());
        mThisCategoryTotalForThisMonth = mRepository.getThisCategoryTotalForThisMonth(mCategory.getValue());
        mThisCategoryTotalForThisYear = mRepository.getThiCategoryTotalForThisYear(mCategory.getValue());
    }

    public void setCategory(String category) {
        mCategory.setValue(category);
        updateExpenseList();
    }

    public LiveData<List<Expense>> getThisWeeksExpenseList() {
        return mThisWeeksExpenseList;
    }

    public LiveData<List<Expense>> getThisMonthsExpenseList() {
        return mThisMonthsExpenseList;
    }

    public LiveData<List<Expense>> getThisYearsExpenseList() {
        return mThisYearsExpenseList;
    }

    public LiveData<Double> getThisCategoryTotalForThisWeek() {
        return mThisCategoryTotalForThisWeek;
    }

    public LiveData<Double> getThisCategoryTotalForThisMonth() {
        return mThisCategoryTotalForThisMonth;
    }

    public LiveData<Double> getThisCategoryTotalForThisYear() {
        return mThisCategoryTotalForThisYear;
    }



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
