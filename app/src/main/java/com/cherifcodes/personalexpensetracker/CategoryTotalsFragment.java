package com.cherifcodes.personalexpensetracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cherifcodes.personalexpensetracker.adaptersAndListeners.CategoryTotalItemClickListener;
import com.cherifcodes.personalexpensetracker.adaptersAndListeners.CategoryTotalsAdapter;
import com.cherifcodes.personalexpensetracker.adaptersAndListeners.OnFragmentInteractionListener;
import com.cherifcodes.personalexpensetracker.appConstants.PeriodConstants;
import com.cherifcodes.personalexpensetracker.backend.CategoryTotal;
import com.cherifcodes.personalexpensetracker.viewModels.CategoryExpensesViewModel;
import com.cherifcodes.personalexpensetracker.viewModels.CategoryTotalViewModel;
import com.cherifcodes.personalexpensetracker.viewModels.SharedPeriodViewModel;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays expenses totals grouped by category
 */
public class CategoryTotalsFragment extends Fragment implements CategoryTotalItemClickListener {
    public static final String TAG = "CategoryTotalsFragment";

    private PublisherAdView mPublisherAdView;

    private double mCurrWeeksCategoryTotal = 0.0;
    private double mCurrMonthsCategoryTotal;
    private double mCurrYearsCategoryTotal;
    private double mCurrUserSelectedCategoryTotal;

    private List<CategoryTotal> mCurrWeeksCategoryList = new ArrayList<>();
    private List<CategoryTotal> mCurrMonthsCategoryList = new ArrayList<>();
    private List<CategoryTotal> mCurrYearsCategoryList = new ArrayList<>();
    //Use the Week's category list as the default selected list
    private List<CategoryTotal> mCurrUserSelectedCategoryList = mCurrWeeksCategoryList;

    //Represents the currently selected period (This_Week, This_Year or This_Month)
    private String mSelectedPeriod = PeriodConstants.THIS_WEEK;
    private String mSelectedCategoryTotalLabel = "";

    private RecyclerView mRecyclerView;
    private CategoryTotalsAdapter mCategoryTotalsAdapter;

    private CategoryTotalViewModel mCategoryTotalViewModel;
    private SharedPeriodViewModel mSharedPeriodViewModel;
    private CategoryExpensesViewModel mCategoryExpensesViewModel;

    private DecimalFormat mDf = new DecimalFormat("##.##");

    private TextView mCurrCategoryTotalLabel_tv;
    private TextView mCurrCategoryTotal_tv;

    private OnFragmentInteractionListener mOnFragmentInteractionListener;
    private Context mContext;

    public CategoryTotalsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        String category = getResources().getStringArray(R.array.expense_categories_array)[0];
        CategoryExpensesViewModel.Factory factory = new CategoryExpensesViewModel.Factory(
                getActivity().getApplication(), category);
        mCategoryExpensesViewModel = ViewModelProviders.of(getActivity(), factory)
                .get(CategoryExpensesViewModel.class);
        mSharedPeriodViewModel = ViewModelProviders.of(getActivity()).get(SharedPeriodViewModel.class);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView =  inflater.inflate(R.layout.fragment_category_totals, container, false);
        mRecyclerView = fragmentView.findViewById(R.id.reclView_category_totals);
        mCurrCategoryTotalLabel_tv = fragmentView.findViewById(R.id.tv_category_totals_label);
        mCurrCategoryTotal_tv = fragmentView.findViewById(R.id.tv_category_totals_total);

        //Set up the ad
        mPublisherAdView = fragmentView.findViewById(R.id.cat_totals_publisherAdView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCategoryTotalsAdapter = new CategoryTotalsAdapter(mCurrWeeksCategoryList, this);

        mCategoryTotalViewModel = ViewModelProviders.of(getActivity()).get(CategoryTotalViewModel.class);

        mSelectedPeriod = mSharedPeriodViewModel.getLivePeriod().getValue();

        ////VIPT!!!! THE FIRST PARAMETER OF observe() MUST BE getActivity, not THIS!!!
        ///OTHERWISE THE RETURNED LIST WILL BE EMPTY OR NULL, Or the new Expense fragment will
        //throw an Exception
        //mCategoryTotalViewModel.getEntireCategoryTotalsList().observe(this, new Observer<List<CategoryTotal>>() {
       // mCategoryTotalsAdapter = new CategoryTotalsAdapter(mCategoryTotalsList, this);
        mCategoryTotalViewModel.getCurrWeeksCategoryTotalList().observe(getActivity(),
                new Observer<List<CategoryTotal>>() {
                    @Override
                    public void onChanged(@Nullable List<CategoryTotal> categoryTotals) {
                        mCurrWeeksCategoryList = categoryTotals;
                        updateCurrSelectedTotalAndList(mSelectedPeriod);
                    }
                });

        mCategoryTotalViewModel.getCurrMonthsCategoryTotalList().observe(getActivity(),
                new Observer<List<CategoryTotal>>() {
                    @Override
                    public void onChanged(@Nullable List<CategoryTotal> categoryTotals) {
                        mCurrMonthsCategoryList = categoryTotals;
                        updateCurrSelectedTotalAndList(mSelectedPeriod);
                    }
                });

        mCategoryTotalViewModel.getCurrYearsCategoryTotalList().observe(getActivity(),
                new Observer<List<CategoryTotal>>() {
                    @Override
                    public void onChanged(@Nullable List<CategoryTotal> categoryTotals) {
                        mCurrYearsCategoryList = categoryTotals;
                        updateCurrSelectedTotalAndList(mSelectedPeriod);
                    }
                });

        listenToCategoryTotals();

        //Get the live, shared period and use it to update the current selected category total
        //and category list
        mSharedPeriodViewModel.getLivePeriod().observe(getActivity(),
                new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        mSelectedPeriod = s;
                        updateCurrSelectedTotalAndList(mSelectedPeriod);
                    }
                });

        return fragmentView;
    }

    private void listenToCategoryTotals() {
        mCategoryTotalViewModel.getCurrWeeksCategoryTotal().observe(
                this, new Observer<Double>() {
                    @Override
                    public void onChanged(@Nullable Double aDouble) {
                        if (aDouble != null) {
                            mCurrWeeksCategoryTotal = aDouble;
                            updateCurrSelectedTotalAndList(mSelectedPeriod);
                        }
                    }
                }
        );

        mCategoryTotalViewModel.getCurrMonthsCategoryTotal().observe(this,
                new Observer<Double>() {
                    @Override
                    public void onChanged(@Nullable Double aDouble) {
                        if (aDouble != null) {
                            mCurrMonthsCategoryTotal = aDouble;
                            updateCurrSelectedTotalAndList(mSelectedPeriod);
                        }

                    }
                });

        mCategoryTotalViewModel.getCurrYearsCategoryTotal().observe(this,
                new Observer<Double>() {
                    @Override
                    public void onChanged(@Nullable Double aDouble) {
                        if (aDouble != null) {
                            mCurrYearsCategoryTotal = aDouble;
                            updateCurrSelectedTotalAndList(mSelectedPeriod);
                        }
                    }
                });
        Log.i(TAG, "Listened to Category Totals, which is: " + mCurrUserSelectedCategoryTotal);
    }

    @Override
    public void onCategoryTotalItemClicked(int itemPosition) {
        String clickedCategory;
        if (mSelectedPeriod.equals(PeriodConstants.THIS_WEEK)) {
            clickedCategory = mCurrWeeksCategoryList.get(itemPosition).getCategoryName();
        } else if (mSelectedPeriod.equals(PeriodConstants.THIS_MONTH)) {
            clickedCategory = mCurrMonthsCategoryList.get(itemPosition).getCategoryName();
        } else {
            clickedCategory = mCurrYearsCategoryList.get(itemPosition).getCategoryName();
        }
        mOnFragmentInteractionListener.navigateToCategoryExpensesFragment(clickedCategory);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.category_expenses_menu, menu);
    }

    private void updateRecyclerView(List<CategoryTotal> categoryTotalList) {
        mCategoryTotalsAdapter.setCategoryTotalsList(categoryTotalList);
        mRecyclerView.setAdapter(mCategoryTotalsAdapter);
    }

    /**
     * Updates the currently selected category total, category list and category total's label
     * based on the specified period
     * @param period the period currently saved in the SharedPeriodViewModel
     */
    private void updateCurrSelectedTotalAndList(String period) {
        if (period == null) {
            return;
        } else if(period.equals(PeriodConstants.THIS_WEEK)) {
            mCurrUserSelectedCategoryList = mCurrWeeksCategoryList;
            mCurrUserSelectedCategoryTotal = mCurrWeeksCategoryTotal;
            mSelectedCategoryTotalLabel = mContext.getString(R.string.this_weeks_total_label);
        } else if(period.equals(PeriodConstants.THIS_MONTH)) {
            mCurrUserSelectedCategoryList = mCurrMonthsCategoryList;
            mCurrUserSelectedCategoryTotal = mCurrMonthsCategoryTotal;
            mSelectedCategoryTotalLabel = mContext.getString(R.string.this_months_total_label);
        } else {
            mCurrUserSelectedCategoryList = mCurrYearsCategoryList;
            mCurrUserSelectedCategoryTotal = mCurrYearsCategoryTotal;
            mSelectedCategoryTotalLabel = mContext.getString(R.string.this_years_total_label);
        }

        displayCombinedCategoryTotal(mSelectedCategoryTotalLabel, mCurrUserSelectedCategoryTotal);
        updateRecyclerView(mCurrUserSelectedCategoryList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_cat_expense_week) {
            updateUiAndSharedPeriod(PeriodConstants.THIS_WEEK);
        } else if (item.getItemId() == R.id.menu_cat_expense_month) {
            updateUiAndSharedPeriod(PeriodConstants.THIS_MONTH);
        } else {
            updateUiAndSharedPeriod(PeriodConstants.THIS_YEAR);
        }
        return true;
    }

    private void updateUiAndSharedPeriod(String selectedPeriod) {
        mSharedPeriodViewModel.setLivePeriod(selectedPeriod);
        displayCombinedCategoryTotal(mSelectedCategoryTotalLabel, mCurrUserSelectedCategoryTotal);
        updateRecyclerView(mCurrUserSelectedCategoryList);
    }

    private void displayCombinedCategoryTotal(String period, double categoryTotal) {
        mCurrCategoryTotal_tv.setText(mDf.format(categoryTotal));
        mCurrCategoryTotalLabel_tv.setText(period);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mOnFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnFragmentInteractionListener = null;
    }
}
