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

    //private MediatorLiveData<List<CategoryTotal>> mCategoryTotalsList;

    public CategoryTotalViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance(application);
        mCurrWeeksCategoryTotalList = mRepository.getCurrWeeksCategoryTotalList();
        mCurrMonthsCategoryTotalList = mRepository.getCurrMonthsCategoryTotalList();
        mCurrYearsCategoryTotalList = mRepository.getCurrYearsCategoryTotalList();

        mCurrWeeksCategoryTotal = mRepository.getCurrWeeksCategoryTotal();
        mCurrMonthsCategoryTotal = mRepository.getCurrMonthsCategoryTotal();
        mCurrYearsCategoryTotal = mRepository.getCurrYearsCategoryTotal();

        /*//Set the category list to an empty list until we get data from the database
        mCategoryTotalsList.setValue(new ArrayList<CategoryTotal>());
        //Fetch the LiveData list from the database
        LiveData<List<CategoryTotal>> categoryTotals = mRepository.getAllCategoryTotals();

        mCategoryTotalsList.addSource(categoryTotals, new Observer<List<CategoryTotal>>() {
            @Override
            public void onChanged(@Nullable List<CategoryTotal> categoryTotals) {
                mCategoryTotalsList.setValue(categoryTotals);
            }
        });*/
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
