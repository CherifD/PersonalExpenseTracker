package com.cherifcodes.personalexpensetracker;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.cherifcodes.personalexpensetracker.adaptersAndListeners.OnFragmentInteractionListener;
import com.cherifcodes.personalexpensetracker.viewModels.CategoryExpensesViewModel;
import com.cherifcodes.personalexpensetracker.viewModels.CategoryTotalViewModel;
import com.google.android.gms.ads.MobileAds;

import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private CategoryExpensesViewModel mCategoryExpensesViewModel;
    private CategoryTotalViewModel mCategoryTotalViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the ViewModels to make them available when the fragments are created
        mCategoryTotalViewModel = ViewModelProviders.of(this).get(CategoryTotalViewModel.class);

        String category = getResources().getStringArray(R.array.expense_categories_array)[0];
        CategoryExpensesViewModel.Factory factory = new CategoryExpensesViewModel.Factory(
                getApplication(), category);
        mCategoryExpensesViewModel = ViewModelProviders.of(
                this, factory).get(CategoryExpensesViewModel.class);

        if (!isConnectedToTheInternet()) {
            Toast.makeText(this, getString(R.string.no_internet_error_message),
                    Toast.LENGTH_LONG).show();
            finishAndRemoveTask();

        }
        //MobileAds.initialize(this, "ca-app-pub-...");


    }

    /**
     * Determines if the device is connected to the internet
     *
     * @return true if there is a network connection, false otherwise
     */
    private boolean isConnectedToTheInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        }
        return false;
    }

    @Override
    public CategoryExpensesViewModel getCatExpenseViewModel() {
        return mCategoryExpensesViewModel;
    }

    @Override
    public void navigateToCategoryExpensesFragment(String category) {
        mCategoryExpensesViewModel.setCategory(category);
        mCategoryExpensesViewModel.updateExpenseList();
        Navigation.findNavController(this, R.id.fragment).navigate(R.id.categoryExpenses);
    }

    @Override
    public void navigateToCategoryTotalsFragment() {
        Navigation.findNavController(this, R.id.fragment).navigate(R.id.categoryTotals);
    }

    @Override
    public void navigateToNewExpenseFragment() {
        Navigation.findNavController(this, R.id.fragment).navigate(R.id.newExpense);
    }

    @Override
    public void navigateToEditExpenseFragment() {
        Navigation.findNavController(this, R.id.fragment).navigate(R.id.editExpense);
    }
}
