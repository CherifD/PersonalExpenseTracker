package com.cherifcodes.personalexpensetracker.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cherifcodes.personalexpensetracker.backend.CategoryTotal;
import com.cherifcodes.personalexpensetracker.backend.Repository;

import java.util.ArrayList;
import java.util.List;

public class CategoryTotalViewModel extends AndroidViewModel {

    private Repository mRepository;
    //private LiveData<List<CategoryTotal>> mCategoryTotalsList;
    private MediatorLiveData<List<CategoryTotal>> mCategoryTotalsList;

    public CategoryTotalViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance(application);
        mCategoryTotalsList = new MediatorLiveData<>();
        //Set the category list to an empty list until we get data from the database
        mCategoryTotalsList.setValue(new ArrayList<CategoryTotal>());
        //Fetch the LiveData list from the database
        LiveData<List<CategoryTotal>> categoryTotals = mRepository.getAllCategoryTotals();

        mCategoryTotalsList.addSource(categoryTotals, new Observer<List<CategoryTotal>>() {
            @Override
            public void onChanged(@Nullable List<CategoryTotal> categoryTotals) {
                mCategoryTotalsList.setValue(categoryTotals);
            }
        });
    }

    public LiveData<List<CategoryTotal>> getAllCategoryTotals() {
        return mCategoryTotalsList;
    }
}
