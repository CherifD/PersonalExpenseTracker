package com.cherifcodes.personalexpensetracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cherifcodes.personalexpensetracker.backend.Expense;
import com.cherifcodes.personalexpensetracker.backend.Repository;
import com.cherifcodes.personalexpensetracker.viewModels.EditExpenseViewModel;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;

import androidx.annotation.RequiresApi;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewExpenseFragment extends Fragment {
    public static final String TAG = "NewExpenseFragment";
    private Spinner mSpinner;
    private EditText mBusinessNameEt;
    private EditText mAmountEt;
    private ImageView mMoneyImageView;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.title_new_expense));
        // Inflate the layout for this fragment
        View newExpenseView = inflater.inflate(R.layout.fragment_new_expense, container, false);

        mBusinessNameEt = newExpenseView.findViewById(R.id.et_business_name_new_expense);
        mAmountEt = newExpenseView.findViewById(R.id.et_amount_new_expense);
        mMoneyImageView = newExpenseView.findViewById(R.id.imv_new_expense);

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

        // Load Image using Picasso
        try {
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500/dYtAyg4vD88hIfrR1VKDnVGhnE6.jpg")
                    .into(mMoneyImageView);
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.msg_unavailable_picaso_image), Toast.LENGTH_LONG)
                    .show();
        }

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

    @RequiresApi(api = Build.VERSION_CODES.O)
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
            Toast.makeText(getContext(), getString(R.string.msg_invalid_expense), Toast.LENGTH_LONG).show();
        }
    }

    private void clearNewExpenseData() {
        mAmountEt.setText("");
        mBusinessNameEt.setText("");
        mSpinner.requestFocus();
    }
}
