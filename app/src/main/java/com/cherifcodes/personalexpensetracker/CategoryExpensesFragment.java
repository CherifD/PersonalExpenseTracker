package com.cherifcodes.personalexpensetracker;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.cherifcodes.personalexpensetracker.backend.Expense;
import com.cherifcodes.personalexpensetracker.viewModels.CategoryExpensesViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;


/**
 * Displays list of expenses for a given category
 */
public class CategoryExpensesFragment extends Fragment implements ExpenseItemClickListener {

    private CategoryExpensesViewModel mCategoryExpensesViewModel;
    private RecyclerView mRecyclerView;
    private CategoryExpensesAdapter mCategoryExpensesAdapter;

    private List<Expense> mThisWeeksExpenseList = new ArrayList<>();
    private List<Expense> mThisMonthsExpenseList = new ArrayList<>();
    private List<Expense> mThisYearsExpenseList = new ArrayList<>();

    private double mThisWeeksTotal;
    private double mThisMonthsTotal;
    private double mThisYearsTotal;

    private TextView mExpenseTotal_tv;
    private TextView mExpensePeriod_tv;
    private DecimalFormat mDf = new DecimalFormat("##.##");

    private OnFragmentInteractionListener mOnFragmentInteractionListener;

    public CategoryExpensesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View categoryExpensesView = inflater.inflate(R.layout.fragment_category_expenses, container, false);
        mExpenseTotal_tv = categoryExpensesView.findViewById(R.id.tv_category_total);
        mExpensePeriod_tv = categoryExpensesView.findViewById(R.id.tv_category_period);
        TextView categoryTotalUnit = categoryExpensesView.findViewById(R.id.tv_category_unit);

        categoryTotalUnit.setText(getString(R.string.us_dollars));
        mExpensePeriod_tv.setText(getString(R.string.this_weeks_total_label));

        mRecyclerView = categoryExpensesView.findViewById(R.id.reclView_category_expenses);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCategoryExpensesAdapter = new CategoryExpensesAdapter(mThisMonthsExpenseList, this);
        mRecyclerView.setAdapter(mCategoryExpensesAdapter);

        mCategoryExpensesViewModel = mOnFragmentInteractionListener.getCatExpenseViewModel();

        //Listen to this week's expense list
        mCategoryExpensesViewModel.getThisWeeksExpenseList().observe(getActivity(),
                new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                mThisWeeksExpenseList = expenses;
                mCategoryExpensesAdapter.setExpenseList(expenses);
                mRecyclerView.setAdapter(mCategoryExpensesAdapter);
            }
        });

        //Listen to this month's expense list
        mCategoryExpensesViewModel.getThisMonthsExpenseList().observe(getActivity(), new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                mThisMonthsExpenseList = expenses;
            }
        });

        //Listen to this Year's expense list
        mCategoryExpensesViewModel.getThisYearsExpenseList().observe(getActivity(), new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                mThisYearsExpenseList = expenses;
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

        listenToCategoryTotals();

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
                            mThisWeeksTotal = aDouble;
                            mExpenseTotal_tv.setText(mDf.format(mThisWeeksTotal));
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
                            mThisMonthsTotal = aDouble;
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
                            mThisYearsTotal = aDouble;
                        }
                    }
                });
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

    @Override
    public void onExpenseItemClicked(int itemPosition) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.category_expenses_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_cat_expense_week) {
            mCategoryExpensesAdapter.setExpenseList(mThisWeeksExpenseList);
            mRecyclerView.setAdapter(mCategoryExpensesAdapter);

            mExpenseTotal_tv.setText(mDf.format(mThisWeeksTotal));
            mExpensePeriod_tv.setText(getString(R.string.this_weeks_total_label));
        } else if (item.getItemId() == R.id.menu_cat_expense_month) {
            mCategoryExpensesAdapter.setExpenseList(mThisMonthsExpenseList);
            mRecyclerView.setAdapter(mCategoryExpensesAdapter);

            mExpenseTotal_tv.setText(mDf.format(mThisMonthsTotal));
            mExpensePeriod_tv.setText(getString(R.string.this_months_total_label));
        } else {
            mCategoryExpensesAdapter.setExpenseList(mThisYearsExpenseList);
            mRecyclerView.setAdapter(mCategoryExpensesAdapter);

            mExpenseTotal_tv.setText(mDf.format(mThisYearsTotal));
            mExpensePeriod_tv.setText(getString(R.string.this_years_total_label));
        }
        return true;
    }
}
