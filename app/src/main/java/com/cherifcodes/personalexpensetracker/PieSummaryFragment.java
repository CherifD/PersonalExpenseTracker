package com.cherifcodes.personalexpensetracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cherifcodes.personalexpensetracker.adaptersAndListeners.OnFragmentInteractionListener;
import com.cherifcodes.personalexpensetracker.appConstants.PeriodConstants;
import com.cherifcodes.personalexpensetracker.backend.CategoryTotal;
import com.cherifcodes.personalexpensetracker.viewModels.CategoryTotalViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;

/**
 * Represents the Home fragment
 */
public class PieSummaryFragment extends Fragment {

    public static final String TAG = "PieSummaryFragment";

    private int [] chartColors = new int[]{
            R.color.red, R.color.green, R.color.orange, R.color.blue, R.color.grey, R.color.brightRed,
            R.color.darkGreen

    };

    private double mCurrWeeksCategoryTotal;
    private double mCurrMonthsCategoryTotal;
    private double mCurrYearsCategoryTotal;

    private List<CategoryTotal> mCurrWeeksCategoryList = new ArrayList<>();
    private List<CategoryTotal> mCurrMonthsCategoryList = new ArrayList<>();
    private List<CategoryTotal> mCurrYearsCategoryList = new ArrayList<>();
    //Use the Week's category list as the default selected list
    private List<CategoryTotal> mCurrUserSelectedCategoryList = mCurrWeeksCategoryList;

    private CategoryTotalViewModel mCategoryTotalViewModel;

    private DecimalFormat mDf = new DecimalFormat("##.##");

    private TextView mCurrCategoryTotalLabel_tv;
    private TextView mCurrCategoryTotal_tv;

    private PieChart mPieChart;


    private OnFragmentInteractionListener mOnFragmentInteractionListener;

    //Represents the currently selected period (This_Week, This_Year or This_Month)
    private String mSelectedPeriod = PeriodConstants.THIS_WEEK;

    public PieSummaryFragment() {
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
        View fragmentView = inflater.inflate(R.layout.fragment_pie_summary, container, false);
        FloatingActionButton categoryTotalsFab = fragmentView.findViewById(R.id.fab_pie_summary);
        FloatingActionButton addNewExpenseFab =
                fragmentView.findViewById(R.id.fab_add_expense_pie_summary);
        mCurrCategoryTotalLabel_tv = fragmentView.findViewById(R.id.tv_pie_summary_period);
        mCurrCategoryTotal_tv = fragmentView.findViewById(R.id.tv_pie_summary_total);
        mPieChart = fragmentView.findViewById(R.id.pie_chart);

        mCategoryTotalViewModel = ViewModelProviders.of(getActivity()).get(CategoryTotalViewModel.class);
        listenToCategoryTotals();

        mCategoryTotalViewModel.getCurrWeeksCategoryTotalList().observe(getActivity(),
                new Observer<List<CategoryTotal>>() {
                    @Override
                    public void onChanged(@Nullable List<CategoryTotal> categoryTotals) {
                        mCurrWeeksCategoryList = categoryTotals;
                        mCurrUserSelectedCategoryList = mCurrWeeksCategoryList;

                        setUpPieChart();
                        displayCombinedCategoryTotal(getString(R.string.this_weeks_total_label), mCurrWeeksCategoryTotal);
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

        setUpPieChart();

        return fragmentView;
    }

    private void setUpPieChart() {
        mPieChart.setRotationEnabled(true);
        Legend legend = mPieChart.getLegend();
        legend.setWordWrapEnabled(true);
        mPieChart.setDrawEntryLabels(false);
        mPieChart.getDescription().setText("Expenses by Category");

        loadPieChart(mCurrUserSelectedCategoryList);

        //Respond to clicks
        mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int pos = (int) h.getX();

                Toast toast = Toast.makeText(getContext(),
                        mCurrUserSelectedCategoryList.get(pos).getCategoryName(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    /**
     * Loads the pie chart with the specified list of expense category totals
     * @param categoryTotalsList the specified list of expense totals grouped by category
     */
    private void loadPieChart(List<CategoryTotal> categoryTotalsList) {
        List<PieEntry> pieEntryList = new ArrayList<>();
        for (CategoryTotal catTotal : categoryTotalsList) {
            pieEntryList.add(new PieEntry((float)catTotal.getCategoryTotal(), catTotal.getCategoryName()));
        }

        PieDataSet set = new PieDataSet(pieEntryList, "");
        set.setSliceSpace(3f);
        set.setValueTextSize(16f);
        set.setValueTextColor(Color.WHITE);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            set.setValueTextSize(0f);// hide the text

        set.setColors(chartColors, getContext());

        PieData data = new PieData(set);
        mPieChart.setData(data);
        mPieChart.invalidate(); // refresh
    }

    private void listenToCategoryTotals() {
        mCategoryTotalViewModel.getCurrWeeksCategoryTotal().observe(
                getActivity(), new Observer<Double>() {
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.category_expenses_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_cat_expense_week) {
            mSelectedPeriod = PeriodConstants.THIS_WEEK;
            mCurrUserSelectedCategoryList = mCurrWeeksCategoryList;

            displayCombinedCategoryTotal(getString(R.string.this_weeks_total_label), mCurrWeeksCategoryTotal);
            setUpPieChart();
        } else if (item.getItemId() == R.id.menu_cat_expense_month) {
            mSelectedPeriod = PeriodConstants.THIS_MONTH;
            mCurrUserSelectedCategoryList = mCurrMonthsCategoryList;

            displayCombinedCategoryTotal(getString(R.string.this_months_total_label), mCurrMonthsCategoryTotal);
            setUpPieChart();
        } else {
            mSelectedPeriod = PeriodConstants.THIS_YEAR;
            mCurrUserSelectedCategoryList = mCurrYearsCategoryList;

            displayCombinedCategoryTotal(getString(R.string.this_years_total_label), mCurrYearsCategoryTotal);
            setUpPieChart();
        }
        return true;
    }

    private void displayCombinedCategoryTotal(String period, double categoryTotal) {
        mCurrCategoryTotal_tv.setText(mDf.format(categoryTotal));
        mCurrCategoryTotalLabel_tv.setText(period);
    }
}
