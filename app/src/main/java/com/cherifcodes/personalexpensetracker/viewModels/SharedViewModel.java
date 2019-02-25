package com.cherifcodes.personalexpensetracker.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<String> mCategory = new MutableLiveData<>();

    public void setCategory(String category) {
        if (TextUtils.isEmpty(category))
            throw new IllegalArgumentException("Invalid category in SharedViewModel");
        mCategory.setValue(category);
    }

    public LiveData<String> getCategory() {
        return mCategory;
    }

}
