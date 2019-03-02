package com.cherifcodes.personalexpensetracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
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
import android.widget.TextView;

import com.cherifcodes.personalexpensetracker.adaptersAndListeners.CategoryTotalItemClickListener;
import com.cherifcodes.personalexpensetracker.adaptersAndListeners.CategoryTotalsAdapter;
import com.cherifcodes.personalexpensetracker.adaptersAndListeners.OnFragmentInteractionListener;
import com.cherifcodes.personalexpensetracker.appConstants.PeriodConstants;
import com.cherifcodes.personalexpensetracker.backend.CategoryTotal;
import com.cherifcodes.personalexpensetracker.viewModels.CategoryExpensesViewModel;
import com.cherifcodes.personalexpensetracker.viewModels.CategoryTotalViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays expenses totals grouped by category
 */
public class CategoryTotalsFragment extends Fragment implements CategoryTotalItemClickListener {

    private double mCurrWeeksCategoryTotal = 0.0;
    private double mCurrMonthsCategoryTotal;
    private double mCurrYearsCategoryTotal;

    private List<CategoryTotal> mCurrWeeksCategoryList = new ArrayList<>();
    private List<CategoryTotal> mCurrMonthsCategoryList = new ArrayList<>();
    private List<CategoryTotal> mCurrYearsCategoryList = new ArrayList<>();

    //Represents the currently selected period (This_Week, This_Year or This_Month)
    private String mSelectedPeriod = PeriodConstants.THIS_WEEK;

    private RecyclerView mRecyclerView;
    private CategoryTotalsAdapter mCategoryTotalsAdapter;

    private CategoryTotalViewModel mCategoryTotalViewModel;
    private CategoryExpensesViewModel mCategoryExpensesViewModel;

    private DecimalFormat mDf = new DecimalFormat("##.##");

    private TextView mCurrCategoryTotalLabel_tv;
    private TextView mCurrCategoryTotal_tv;

    private OnFragmentInteractionListener mOnFragmentInteractionListener;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView =  inflater.inflate(R.layout.fragment_category_totals, container, false);
        mRecyclerView = fragmentView.findViewById(R.id.reclView_category_totals);
        mCurrCategoryTotalLabel_tv = fragmentView.findViewById(R.id.tv_category_totals_label);
        mCurrCategoryTotal_tv = fragmentView.findViewById(R.id.tv_category_totals_total);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCategoryTotalsAdapter = new CategoryTotalsAdapter(mCurrWeeksCategoryList, this);

        mCategoryTotalViewModel = ViewModelProviders.of(getActivity()).get(CategoryTotalViewModel.class);

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
                        mCategoryTotalsAdapter.setCategoryTotalsList(categoryTotals);
                        mRecyclerView.setAdapter(mCategoryTotalsAdapter);
                    }
                });

        mCategoryTotalViewModel.getCurrMonthsCategoryTotalList().observe(getActivity(),
                new Observer<List<CategoryTotal>>() {
                    @Override
                    public void onChanged(@Nullable List<CategoryTotal> categoryTotals) {
                        mCurrMonthsCategoryList = categoryTotals;
                    }
                });

        mCategoryTotalViewModel.getCurrYearsCategoryTotalList().observe(getActivity(),
                new Observer<List<CategoryTotal>>() {
                    @Override
                    public void onChanged(@Nullable List<CategoryTotal> categoryTotals) {
                        mCurrYearsCategoryList = categoryTotals;
                    }
                });

        listenToCategoryTotals();
        return fragmentView;
    }

    private void listenToCategoryTotals() {
        mCategoryTotalViewModel.getCurrWeeksCategoryTotal().observe(
                this, new Observer<Double>() {
                    @Override
                    public void onChanged(@Nullable Double aDouble) {
                        if (aDouble != null) {
                            mCurrWeeksCategoryTotal = aDouble;
                        }
                        displayCombinedCategoryTotal(getString(R.string.this_weeks_total_label),
                                mCurrWeeksCategoryTotal);
                    }
                }
        );

        mCategoryTotalViewModel.getCurrMonthsCategoryTotal().observe(this,
                new Observer<Double>() {
                    @Override
                    public void onChanged(@Nullable Double aDouble) {
                        if (aDouble != null)
                            mCurrMonthsCategoryTotal = aDouble;
                    }
                });

        mCategoryTotalViewModel.getCurrYearsCategoryTotal().observe(this,
                new Observer<Double>() {
                    @Override
                    public void onChanged(@Nullable Double aDouble) {
                        if (aDouble != null)
                            mCurrYearsCategoryTotal = aDouble;
                    }
                });
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

    private void displayCombinedCategoryTotal(String period, double categoryTotal) {
        mCurrCategoryTotal_tv.setText(mDf.format(categoryTotal));
        mCurrCategoryTotalLabel_tv.setText(period);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_cat_expense_week) {
            mSelectedPeriod = PeriodConstants.THIS_WEEK;

            mCategoryTotalsAdapter.setCategoryTotalsList(mCurrWeeksCategoryList);
            mRecyclerView.setAdapter(mCategoryTotalsAdapter);

            displayCombinedCategoryTotal(getString(R.string.this_weeks_total_label), mCurrWeeksCategoryTotal);
        } else if (item.getItemId() == R.id.menu_cat_expense_month) {
            mSelectedPeriod = PeriodConstants.THIS_MONTH;

            mCategoryTotalsAdapter.setCategoryTotalsList(mCurrMonthsCategoryList);
            mRecyclerView.setAdapter(mCategoryTotalsAdapter);

            displayCombinedCategoryTotal(getString(R.string.this_months_total_label), mCurrMonthsCategoryTotal);
        } else {
            mSelectedPeriod = PeriodConstants.THIS_YEAR;

            mCategoryTotalsAdapter.setCategoryTotalsList(mCurrYearsCategoryList);
            mRecyclerView.setAdapter(mCategoryTotalsAdapter);

            displayCombinedCategoryTotal(getString(R.string.this_years_total_label), mCurrYearsCategoryTotal);
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
