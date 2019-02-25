package com.cherifcodes.personalexpensetracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;

/**
 * Represents the Home fragment
 */
public class PieSummaryFragment extends Fragment {

    public PieSummaryFragment() {
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
        View fragmentView = inflater.inflate(R.layout.fragment_pie_summary, container, false);
        FloatingActionButton categoryTotalsFab = fragmentView.findViewById(R.id.fab_pie_summary);
        FloatingActionButton addNewExpenseFab =
                fragmentView.findViewById(R.id.fab_add_expense_pie_summary);

        //Setup navigation to CategoryTotalsFragment
        categoryTotalsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.fragment).navigate(R.id.categoryTotals);
            }
        });

        //Setup navigation to NewExpenseFragment
        addNewExpenseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.fragment).navigate(R.id.newExpense);
            }
        });

        return fragmentView;
    }
}
