package com.cherifcodes.personalexpensetracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cherifcodes.personalexpensetracker.adaptersAndListeners.OnFragmentInteractionListener;
import com.cherifcodes.personalexpensetracker.adaptersAndListeners.CategoryExpensesAdapter;
import com.cherifcodes.personalexpensetracker.adaptersAndListeners.ExpenseItemClickListener;
import com.cherifcodes.personalexpensetracker.appConstants.PeriodConstants;
import com.cherifcodes.personalexpensetracker.backend.Expense;
import com.cherifcodes.personalexpensetracker.viewModels.CategoryExpensesViewModel;
import com.cherifcodes.personalexpensetracker.viewModels.EditExpenseViewModel;
import com.cherifcodes.personalexpensetracker.viewModels.SharedPeriodViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;


/**
 * Displays list of expenses for a given category
 */
public class CategoryExpensesFragment extends Fragment implements ExpenseItemClickListener {

    private CategoryExpensesViewModel mCategoryExpensesViewModel;
    private EditExpenseViewModel mEditExpenseViewModel;
    private SharedPeriodViewModel mSharedPeriodViewModel;

    private List<Expense> mThisWeeksExpenseList = new ArrayList<>();
    private List<Expense> mThisMonthsExpenseList = new ArrayList<>();
    private List<Expense> mThisYearsExpenseList = new ArrayList<>();
    private List<Expense> mCurrUserSelectedExpenseList = mThisWeeksExpenseList;

    private double mThisWeeksExpenseTotal;
    private double mThisMonthsExpenseTotal;
    private double mThisYearsExpenseTotal;
    private double mCurrSelectedExpenseTotal;

    private TextView mExpenseTotal_tv;
    private TextView mExpenseTotalLabel_tv;
    private RecyclerView mRecyclerView;
    private CategoryExpensesAdapter mCategoryExpensesAdapter;

    private DecimalFormat mDf = new DecimalFormat("##.##");

    private OnFragmentInteractionListener mOnFragmentInteractionListener;
    private Context mContext;
    private String mSelectedPeriod;
    private String mSelectedExpenseTotalLabel;


    public CategoryExpensesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mEditExpenseViewModel = ViewModelProviders.of(getActivity()).get(EditExpenseViewModel.class);
        mSharedPeriodViewModel = ViewModelProviders.of(getActivity()).get(SharedPeriodViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.title_expenses_for_curr_category));
        // Inflate the layout for this fragment
        View categoryExpensesView = inflater.inflate(R.layout.fragment_category_expenses, container, false);
        mExpenseTotal_tv = categoryExpensesView.findViewById(R.id.tv_category_total);
        mExpenseTotalLabel_tv = categoryExpensesView.findViewById(R.id.tv_category_period);
        TextView categoryTotalUnit = categoryExpensesView.findViewById(R.id.tv_category_unit);

        categoryTotalUnit.setText(getString(R.string.us_dollars));
        mExpenseTotalLabel_tv.setText(getString(R.string.this_weeks_total_label));

        mRecyclerView = categoryExpensesView.findViewById(R.id.reclView_category_expenses);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCategoryExpensesAdapter = new CategoryExpensesAdapter(mThisMonthsExpenseList, this);
        mRecyclerView.setAdapter(mCategoryExpensesAdapter);

        mCategoryExpensesViewModel = mOnFragmentInteractionListener.getCatExpenseViewModel();

        mSelectedPeriod = mSharedPeriodViewModel.getLivePeriod().getValue();

        listenToCategoryTotals();

        //Listen to this week's expense list
        mCategoryExpensesViewModel.getThisWeeksExpenseList().observe(getActivity(),
                new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                mThisWeeksExpenseList = expenses;
                updateCurrSelectedTotalAndList(mSelectedPeriod);
            }
        });

        //Listen to this month's expense list
        mCategoryExpensesViewModel.getThisMonthsExpenseList().observe(getActivity(), new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                mThisMonthsExpenseList = expenses;
                updateCurrSelectedTotalAndList(mSelectedPeriod);
            }
        });

        //Listen to this Year's expense list
        mCategoryExpensesViewModel.getThisYearsExpenseList().observe(getActivity(), new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                mThisYearsExpenseList = expenses;
                updateCurrSelectedTotalAndList(mSelectedPeriod);
            }
        });

        FloatingActionButton fab = categoryExpensesView.findViewById(R.id.fab_cat_expenses);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.fragment)
                        .navigate(R.id.newExpense);
            }
        });

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

        return categoryExpensesView;
    }

    private void listenToCategoryTotals() {
        mCategoryExpensesViewModel.getThisCategoryTotalForThisWeek().observe(getActivity(),
                new Observer<Double>() {
                    @Override
                    public void onChanged(@Nullable Double aDouble) {
                        if (aDouble == null) {
                            mExpenseTotal_tv.setText("0.00");
                        } else {
                            mThisWeeksExpenseTotal = aDouble;
                            mExpenseTotal_tv.setText(mDf.format(mThisWeeksExpenseTotal));
                        }
                    }
                });

        mCategoryExpensesViewModel.getThisCategoryTotalForThisMonth().observe(getActivity(),
                new Observer<Double>() {
                    @Override
                    public void onChanged(@Nullable Double aDouble) {
                        if (aDouble == null) {
                            mExpenseTotal_tv.setText("0.00");
                        } else {
                            mThisMonthsExpenseTotal = aDouble;
                        }
                    }
                });

        mCategoryExpensesViewModel.getThisCategoryTotalForThisYear().observe(getActivity(),
                new Observer<Double>() {
                    @Override
                    public void onChanged(@Nullable Double aDouble) {
                        if (aDouble == null) {
                            mExpenseTotal_tv.setText("0.00");
                        } else {
                            mThisYearsExpenseTotal = aDouble;
                        }
                    }
                });
    }

    /**
     * Updates the currently selected category total, category list and category total's label
     * based on the specified period
     * @param period the period currently saved in the SharedPeriodViewModel
     */
    private void updateCurrSelectedTotalAndList(String period) {
        if (TextUtils.isEmpty(period)) {
            throw new IllegalArgumentException("Invalid period.");
        } else if(period.equals(PeriodConstants.THIS_WEEK)) {
            mCurrUserSelectedExpenseList = mThisWeeksExpenseList;
            mCurrSelectedExpenseTotal = mThisWeeksExpenseTotal;
            mSelectedExpenseTotalLabel = mContext.getString(R.string.this_weeks_total_label);
        } else if(period.equals(PeriodConstants.THIS_MONTH)) {
            mCurrUserSelectedExpenseList = mThisMonthsExpenseList;
            mCurrSelectedExpenseTotal = mThisMonthsExpenseTotal;
            mSelectedExpenseTotalLabel = mContext.getString(R.string.this_months_total_label);
        } else {
            mCurrUserSelectedExpenseList = mThisYearsExpenseList;
            mCurrSelectedExpenseTotal = mThisYearsExpenseTotal;
            mSelectedExpenseTotalLabel = mContext.getString(R.string.this_years_total_label);
        }

        displayCombinedCategoryTotal(mSelectedExpenseTotalLabel, mCurrSelectedExpenseTotal);
        updateRecyclerView(mCurrUserSelectedExpenseList);
    }

    private void updateUiAndSharedPeriod(String selectedPeriod) {
        mSharedPeriodViewModel.setLivePeriod(selectedPeriod);
        displayCombinedCategoryTotal(mSelectedExpenseTotalLabel, mCurrSelectedExpenseTotal);
        updateRecyclerView(mCurrUserSelectedExpenseList);
    }

    private void updateRecyclerView(List<Expense> expenseList) {
        mCategoryExpensesAdapter.setExpenseList(expenseList);
        mRecyclerView.setAdapter(mCategoryExpensesAdapter);
    }

    private void displayCombinedCategoryTotal(String period, double expenseTotal) {
        mExpenseTotal_tv.setText(mDf.format(expenseTotal));
        mExpenseTotalLabel_tv.setText(period);
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

    @Override
    public void onExpenseItemClicked(int itemPosition) {
        mEditExpenseViewModel.setLiveExpense(mCurrUserSelectedExpenseList.get(itemPosition));
        Navigation.findNavController(getActivity(), R.id.fragment).navigate(R.id.editExpense);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.category_expenses_menu, menu);
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
}





