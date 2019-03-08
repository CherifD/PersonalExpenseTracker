package com.cherifcodes.personalexpensetracker.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.cherifcodes.personalexpensetracker.appConstants.PeriodConstants;

public class SharedPeriodViewModel extends ViewModel {

    private MutableLiveData<String> mLivePeriod;

    public SharedPeriodViewModel() {
        mLivePeriod = new MutableLiveData<>();
        mLivePeriod.setValue(PeriodConstants.THIS_WEEK);
    }

    public void setLivePeriod(String period) {
        mLivePeriod.setValue(period);
    }

    public LiveData<String> getLivePeriod() {
        return mLivePeriod;
    }
}
