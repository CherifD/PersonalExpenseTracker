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
import android.view.View;
import android.view.ViewGroup;

import com.cherifcodes.personalexpensetracker.adaptersAndListeners.CategoryTotalItemClickListener;
import com.cherifcodes.personalexpensetracker.adaptersAndListeners.CategoryTotalsAdapter;
import com.cherifcodes.personalexpensetracker.adaptersAndListeners.OnFragmentInteractionListener;
import com.cherifcodes.personalexpensetracker.backend.CategoryTotal;
import com.cherifcodes.personalexpensetracker.viewModels.CategoryExpensesViewModel;
import com.cherifcodes.personalexpensetracker.viewModels.CategoryTotalViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Displays expenses totals grouped by category
 */
public class CategoryTotalsFragment extends Fragment implements CategoryTotalItemClickListener {

    private List<CategoryTotal> mCategoryTotalsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private CategoryTotalsAdapter mCategoryTotalsAdapter;

    private CategoryTotalViewModel mCategoryTotalViewModel;
    private CategoryExpensesViewModel mCategoryExpensesViewModel;

    private OnFragmentInteractionListener mOnFragmentInteractionListener;

    public CategoryTotalsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        View fragmentView =  inflater.inflate(R.layout.fragment_list_summary, container, false);
        mRecyclerView = fragmentView.findViewById(R.id.reclView_list_summary);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCategoryTotalViewModel = ViewModelProviders.of(getActivity()).get(CategoryTotalViewModel.class);

        ////VIPT!!!! THE FIRST PARAMETER OF observe() MUST BE getActivity, not THIS!!!
        ///OTHERWISE THE RETURNED LIST WILL BE EMPTY OR NULL
        //mCategoryTotalViewModel.getEntireCategoryTotalsList().observe(this, new Observer<List<CategoryTotal>>() {
       // mCategoryTotalsAdapter = new CategoryTotalsAdapter(mCategoryTotalsList, this);


        mCategoryTotalsAdapter = new CategoryTotalsAdapter(mCategoryTotalsList, this);
        mRecyclerView.setAdapter(mCategoryTotalsAdapter);

        mCategoryTotalViewModel.getAllCategoryTotals().observe(getActivity(), new Observer<List<CategoryTotal>>() {
            @Override
            public void onChanged(@Nullable List<CategoryTotal> categoryTotals) {
                mCategoryTotalsList = categoryTotals;
                mCategoryTotalsAdapter.setCategoryTotalsList(mCategoryTotalsList);
                mRecyclerView.setAdapter(mCategoryTotalsAdapter);
            }
        });

        return fragmentView;
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
    public void onCategoryTotalItemClicked(int itemPosition) {
        String clickedCategory = mCategoryTotalsList.get(itemPosition).getCategoryName();


        mCategoryExpensesViewModel.setCategory(mCategoryTotalsList.get(itemPosition).getCategoryName());
        mCategoryExpensesViewModel.updateExpenseList();

        mOnFragmentInteractionListener.navigateToCategoryExpensesFragment(clickedCategory);
    }
}
