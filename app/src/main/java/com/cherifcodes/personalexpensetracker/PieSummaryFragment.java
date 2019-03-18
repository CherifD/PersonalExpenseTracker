package com.cherifcodes.personalexpensetracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.cherifcodes.personalexpensetracker.viewModels.SharedPeriodViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

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
            R.color.darkGreen};

    private PublisherAdView mPublisherAdView;

    private double mCurrWeeksCategoryTotal;
    private double mCurrMonthsCategoryTotal;
    private double mCurrYearsCategoryTotal;
    private double mCurrUserSelectedCategoryTotal;

    private List<CategoryTotal> mCurrWeeksCategoryList = new ArrayList<>();
    private List<CategoryTotal> mCurrMonthsCategoryList = new ArrayList<>();
    private List<CategoryTotal> mCurrYearsCategoryList = new ArrayList<>();
    //Use the Week's category list as the default selected list
    private List<CategoryTotal> mCurrUserSelectedCategoryList = mCurrWeeksCategoryList;

    private CategoryTotalViewModel mCategoryTotalViewModel;
    private SharedPeriodViewModel mSharedPeriodViewModel;

    private DecimalFormat mDf = new DecimalFormat("##.##");

    private TextView mCurrCategoryTotalLabel_tv;
    private TextView mCurrCategoryTotal_tv;

    private PieChart mPieChart;

    private OnFragmentInteractionListener mOnFragmentInteractionListener;

    //Represents the currently selected period (This_Week, This_Year or This_Month)
    private String mSelectedPeriod =  ""; //getResources().getString(R.string.this_weeks_total_label);
    //Represents the resource string label for the currently selected category total
    private String mSelectedCategoryTotalLabel = "";

    public PieSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mSharedPeriodViewModel = ViewModelProviders.of(getActivity()).get(SharedPeriodViewModel.class);

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

        //Set up the ad
        mPublisherAdView = fragmentView.findViewById(R.id.pie_publisherAdView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);

        mSelectedPeriod = mSharedPeriodViewModel.getLivePeriod().getValue();
        mCategoryTotalViewModel = ViewModelProviders.of(getActivity()).get(CategoryTotalViewModel.class);
        listenToCategoryTotals();

        mCategoryTotalViewModel.getCurrWeeksCategoryTotalList().observe(getActivity(),
                new Observer<List<CategoryTotal>>() {
                    @Override
                    public void onChanged(@Nullable List<CategoryTotal> categoryTotals) {
                        mCurrWeeksCategoryList = categoryTotals;
                        mCurrUserSelectedCategoryList = mCurrWeeksCategoryList;

                        setUpPieChart();
                        displayCombinedCategoryTotal(getString(R.string.this_weeks_total_label),
                                mCurrWeeksCategoryTotal);
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
                        if (mCurrYearsCategoryList == null || mCurrYearsCategoryList.size() <= 0) {
                            mCurrCategoryTotal_tv.setText("");
                            mCurrCategoryTotalLabel_tv.setText(getString(R.string.empty_current_year_message));
                            mCurrCategoryTotalLabel_tv.setTextColor(Color.BLUE);
                            mCurrCategoryTotalLabel_tv.setTextSize(16f);
                        }
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

                        setUpPieChart();
                        displayCombinedCategoryTotal(mSelectedCategoryTotalLabel,
                                mCurrUserSelectedCategoryTotal);
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

        return fragmentView;
    }

    private void updateCurrSelectedTotalAndList(String s) {
        Toast toast = Toast.makeText(getContext(), getString(R.string.msg_invalid_livePeriod),
                Toast.LENGTH_LONG);
        if (s == null) {
            toast.show();
            return;
        } else if(s.equals(PeriodConstants.THIS_WEEK)) {
            mCurrUserSelectedCategoryList = mCurrWeeksCategoryList;
            mCurrUserSelectedCategoryTotal = mCurrWeeksCategoryTotal;
            mSelectedCategoryTotalLabel = getString(R.string.this_weeks_total_label);
        } else if(s.equals(PeriodConstants.THIS_MONTH)) {
            mCurrUserSelectedCategoryList = mCurrMonthsCategoryList;
            mCurrUserSelectedCategoryTotal = mCurrMonthsCategoryTotal;
            mSelectedCategoryTotalLabel = getString(R.string.this_months_total_label);
        } else if (s.equals(PeriodConstants.THIS_YEAR)) {
            mCurrUserSelectedCategoryList = mCurrYearsCategoryList;
            mCurrUserSelectedCategoryTotal = mCurrYearsCategoryTotal;
            mSelectedCategoryTotalLabel = getString(R.string.this_years_total_label);
        } else {
            toast.show();
        }
    }

    private void setUpPieChart() {
        mPieChart.setRotationEnabled(true);
        Legend legend = mPieChart.getLegend();
        legend.setWordWrapEnabled(true);
        mPieChart.setDrawEntryLabels(false);
        mPieChart.getDescription().setText(getString(R.string.chart_description));


        loadPieChart(mCurrUserSelectedCategoryList);

        //Respond to clicks
        mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int pos = (int) h.getX();
                String pieSliceSummary = mCurrUserSelectedCategoryList.get(pos).getCategoryName() +
                         ": " + mDf.format(mCurrUserSelectedCategoryList.get(pos).getCategoryTotal());
                Toast toast = Toast.makeText(getContext(), pieSliceSummary
                        , Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, -88);
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

        //if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        set.setValueTextSize(0f);// hide the text

        set.setColors(chartColors, getContext());

        PieData data = new PieData(set);
        mPieChart.setData(data);
        mPieChart.invalidate(); // refresh
    }

    private void listenToCategoryTotals() {
        mCategoryTotalViewModel.getCurrWeeksCategoryTotal().observe(this,
                 new Observer<Double>() {
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
        displayCombinedCategoryTotal(mSelectedPeriod, mCurrUserSelectedCategoryTotal);
        setUpPieChart();
    }

    private void displayCombinedCategoryTotal(String period, double categoryTotal) {
        mCurrCategoryTotal_tv.setText(mDf.format(categoryTotal));
        mCurrCategoryTotalLabel_tv.setText(period);
    }
}
