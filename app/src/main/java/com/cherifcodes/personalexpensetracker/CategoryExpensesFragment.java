package com.cherifcodes.personalexpensetracker;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cherifcodes.personalexpensetracker.adaptersAndListeners.OnFragmentInteractionListener;
import com.cherifcodes.personalexpensetracker.adaptersAndListeners.CategoryExpensesAdapter;
import com.cherifcodes.personalexpensetracker.adaptersAndListeners.ExpenseItemClickListener;
import com.cherifcodes.personalexpensetracker.backend.Expense;
import com.cherifcodes.personalexpensetracker.viewModels.CategoryExpensesViewModel;
import com.cherifcodes.personalexpensetracker.viewModels.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;


/**
 * Displays list of expenses for a given category
 */
public class CategoryExpensesFragment extends Fragment implements ExpenseItemClickListener {

    private SharedViewModel mSharedViewModel;
    private CategoryExpensesViewModel mCategoryExpensesViewModel;
    private RecyclerView mRecyclerView;
    private CategoryExpensesAdapter mCategoryExpensesAdapter;
    private List<Expense> mExpenseList = new ArrayList<>();
    private String mCategory;
    private OnFragmentInteractionListener mOnFragmentInteractionListener;

    public CategoryExpensesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View categoryExpensesView = inflater.inflate(R.layout.fragment_category_expenses, container, false);
        TextView categoryAmount = categoryExpensesView.findViewById(R.id.tv_category_total);
        TextView categoryName = categoryExpensesView.findViewById(R.id.tv_category_period);
        TextView categoryTotalUnit = categoryExpensesView.findViewById(R.id.tv_category_unit);

        categoryTotalUnit.setText(getString(R.string.us_dollars));
        categoryName.setText(getString(R.string.this_month_total));


        mRecyclerView = categoryExpensesView.findViewById(R.id.reclView_category_expenses);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCategoryExpensesAdapter = new CategoryExpensesAdapter(mExpenseList, this);
        mRecyclerView.setAdapter(mCategoryExpensesAdapter);

        mCategoryExpensesViewModel = mOnFragmentInteractionListener.getCatExpenseViewModel();
        mCategoryExpensesViewModel.updateThisMonthExpenseList();
        mCategoryExpensesViewModel.getThisMonthExpenseList().observe(getActivity(), new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                //mExpenseList = expenses;
                mCategoryExpensesAdapter.setExpenseList(expenses);
                mRecyclerView.setAdapter(mCategoryExpensesAdapter);
            }
        });

        //Below commented code runs before the above onChanged() code
       /* mRecyclerView = categoryExpensesView.findViewById(R.id.reclView_category_expenses);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCategoryExpensesAdapter = new CategoryExpensesAdapter(mExpenseList, this);
        Log.i("CatExpFragment", "The expenseList in onCreateView is: " + mExpenseList);
        mRecyclerView.setAdapter(mCategoryExpensesAdapter);*/

        FloatingActionButton fab = categoryExpensesView.findViewById(R.id.fab_cat_expenses);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.fragment)
                        .navigate(R.id.newExpense);
            }
        });

        return categoryExpensesView;
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
}
