package com.cherifcodes.personalexpensetracker.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.cherifcodes.personalexpensetracker.backend.CategoryTotal;
import com.cherifcodes.personalexpensetracker.backend.Repository;

import java.util.List;

public class CategoryTotalViewModel extends AndroidViewModel {

    private Repository mRepository;
    private LiveData<List<CategoryTotal>> mCurrWeeksCategoryTotalList;
    private LiveData<List<CategoryTotal>> mCurrMonthsCategoryTotalList;
    private LiveData<List<CategoryTotal>> mCurrYearsCategoryTotalList;

    private LiveData<Double> mCurrWeeksCategoryTotal;
    private LiveData<Double> mCurrMonthsCategoryTotal;
    private LiveData<Double> mCurrYearsCategoryTotal;

    public CategoryTotalViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance(application);
        mCurrWeeksCategoryTotalList = mRepository.getCurrWeeksCategoryTotalList();
        mCurrMonthsCategoryTotalList = mRepository.getCurrMonthsCategoryTotalList();
        mCurrYearsCategoryTotalList = mRepository.getCurrYearsCategoryTotalList();

        mCurrWeeksCategoryTotal = mRepository.getCurrWeeksCategoryTotal();
        mCurrMonthsCategoryTotal = mRepository.getCurrMonthsCategoryTotal();
        mCurrYearsCategoryTotal = mRepository.getCurrYearsCategoryTotal();
    }

    public LiveData<Double> getCurrWeeksCategoryTotal() {
        return mCurrWeeksCategoryTotal;
    }

    public LiveData<Double> getCurrMonthsCategoryTotal() {
        return mCurrMonthsCategoryTotal;
    }

    public LiveData<Double> getCurrYearsCategoryTotal() {
        return mCurrYearsCategoryTotal;
    }

    public LiveData<List<CategoryTotal>> getCurrWeeksCategoryTotalList() {
        return mCurrWeeksCategoryTotalList;
    }

    public LiveData<List<CategoryTotal>> getCurrMonthsCategoryTotalList() {
        return mCurrMonthsCategoryTotalList;
    }

    public LiveData<List<CategoryTotal>> getCurrYearsCategoryTotalList() {
        return mCurrYearsCategoryTotalList;
    }

}
