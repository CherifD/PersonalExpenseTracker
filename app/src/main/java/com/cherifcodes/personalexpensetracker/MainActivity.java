package com.cherifcodes.personalexpensetracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cherifcodes.personalexpensetracker.adaptersAndListeners.OnFragmentInteractionListener;
import com.cherifcodes.personalexpensetracker.appConstants.PeriodConstants;
import com.cherifcodes.personalexpensetracker.backend.CategoryTotal;
import com.cherifcodes.personalexpensetracker.backend.Expense;
import com.cherifcodes.personalexpensetracker.viewModels.CategoryExpensesViewModel;
import com.cherifcodes.personalexpensetracker.viewModels.CategoryTotalViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.List;

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

    /*    FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
*/
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
