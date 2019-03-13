package com.cherifcodes.personalexpensetracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cherifcodes.personalexpensetracker.backend.Expense;
import com.cherifcodes.personalexpensetracker.backend.Repository;
import com.cherifcodes.personalexpensetracker.viewModels.EditExpenseViewModel;
import com.cherifcodes.personalexpensetracker.viewModels.SharedViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewExpenseFragment extends Fragment {
    public static final String TAG = "NewExpenseFragment";
    private Spinner mSpinner;
    private EditText mBusinessNameEt;
    private EditText mAmountEt;

    private EditExpenseViewModel mEditExpenseViewModel;

    private Repository mRepository;

    public NewExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRepository = Repository.getInstance(getActivity().getApplication());

        mEditExpenseViewModel = ViewModelProviders.of(getActivity()).get(EditExpenseViewModel.class);



        /*Expense ex0 = new Expense("Education", "Udemy", 22.69,
                LocalDateTime.of(2019, 03, 04, 2, 23) );
        mRepository.insertExpense(ex0);*/

       /* Expense ex1 = new Expense("Sports", "Dick's", 89.65,
                LocalDateTime.of(2019, 03, 4, 11, 59) );
        mRepository.insertExpense(ex1);*/

        //Insert dummy Expenses for testing
       /* Expense ex0 = new Expense("Education", "NC State", 708.69,
                LocalDateTime.of(2019, 01, 25, 2, 23) );
        mRepository.insertExpense(ex0);

        Expense ex1 = new Expense("Recreation", "Museum", 78.56,
                LocalDateTime.of(2019, 04, 01, 2, 23) );
        mRepository.insertExpense(ex1);

        Expense ex2 = new Expense("Clothes", "Target", 90.34,
                LocalDateTime.of(2019, 05, 02, 2, 23) );
        mRepository.insertExpense(ex2);

        Expense ex3 = new Expense("Clothes", "Target", 20.29,
                LocalDateTime.of(2019, 03, 03, 2, 23) );
        mRepository.insertExpense(ex2);

        Expense ex4 = new Expense("Health", "Rite-Aid", 89.45,
                LocalDateTime.of(2019, 06, 04, 2, 23) );
        mRepository.insertExpense(ex4);

        Expense ex5 = new Expense("Health", "Rite-Aid", 120,
                LocalDateTime.of(2019, 04, 11, 2, 23) );
        mRepository.insertExpense(ex5);

        Expense ex6 = new Expense("Food", "Wal-Mart", 100,
                LocalDateTime.of(2019, 05, 16, 2, 23) );
        mRepository.insertExpense(ex6);

        Expense ex7 = new Expense("Transportation", "Exxon Mobile", 27.90,
                LocalDateTime.of(2019, 07, 17, 2, 23) );
        mRepository.insertExpense(ex7);

        Expense ex8 = new Expense("Food", "Food Lion", 78.32,
                LocalDateTime.of(2020, 02, 01, 2, 23) );
        mRepository.insertExpense(ex7);*/
        //mViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View newExpenseView = inflater.inflate(R.layout.fragment_new_expense, container, false);

        mBusinessNameEt = newExpenseView.findViewById(R.id.et_business_name_new_expense);
        mAmountEt = newExpenseView.findViewById(R.id.et_amount_new_expense);

        Button saveExpenseBtn = newExpenseView.findViewById(R.id.btn_save_new_expense);
        saveExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveExpense();
            }
        });

        mSpinner = newExpenseView.findViewById(R.id.spinner_category_new_expense);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.expense_categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);

        mEditExpenseViewModel.getLiveExpense().observe(getActivity(),
                new Observer<Expense>() {
                    @Override
                    public void onChanged(@Nullable Expense expense) {
                        //Update spinner value
                        int categoryPosition = adapter.getPosition(expense.getCategory());
                        mSpinner.setSelection(categoryPosition);
                    }
                });

        return newExpenseView;
    }

    private boolean isValidExpense() {
        return !(TextUtils.isEmpty(mSpinner.getSelectedItem().toString()) ||
            TextUtils.isEmpty(mBusinessNameEt.getText()) ||
            TextUtils.isEmpty(mAmountEt.getText()) ||
                    Double.parseDouble(mAmountEt.getText().toString()) <= 0);
    }

    public void saveExpense() {
        if (isValidExpense()) {
            final Expense newExpense = new Expense(mSpinner.getSelectedItem().toString(),
                    mBusinessNameEt.getText().toString(),
                    Double.parseDouble(mAmountEt.getText().toString()),
                    LocalDate.now());

            //Insert the new Expense into the database
            mRepository.insertExpense(newExpense);

            //Reset the new Expense form
            clearNewExpenseData();
        } else {
            Toast.makeText(getContext(), "Invalid expense.", Toast.LENGTH_LONG).show();
        }
    }

    private void clearNewExpenseData() {
        mAmountEt.setText("");
        mBusinessNameEt.setText("");
        mSpinner.requestFocus();
    }
}
