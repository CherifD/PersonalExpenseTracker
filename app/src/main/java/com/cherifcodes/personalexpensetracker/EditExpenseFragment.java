package com.cherifcodes.personalexpensetracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cherifcodes.personalexpensetracker.backend.Expense;
import com.cherifcodes.personalexpensetracker.backend.Repository;
import com.cherifcodes.personalexpensetracker.viewModels.EditExpenseViewModel;

import java.text.DecimalFormat;

import androidx.navigation.Navigation;


public class EditExpenseFragment extends Fragment {
    public static final String TAG = "EditExpenseFragment";

    private Spinner mSpinner;
    private EditText mBusinessNameEt;
    private EditText mAmountEt;
    private Button mDeleteExpenseBtn;
    private Button mUpdateExpenseBtn;
    private DecimalFormat mDf = new DecimalFormat("##.##");

    private EditExpenseViewModel mEditExpenseViewModel;
    private Repository mRepository;
    private Expense mCurrExpense;
    private boolean unsavedChanges;

    public EditExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEditExpenseViewModel = ViewModelProviders.of(getActivity()).get(EditExpenseViewModel.class);
        mRepository = Repository.getInstance(getActivity().getApplication());

        /*// This callback will only be called when MyFragment is at least Started.
        this.getActivity().addOnBackPressedCallback(this, new OnBackPressedCallback() {
            @Override
            public boolean handleOnBackPressed() {
                ...
                return true; // return true if event was handled
            }
        });*/

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.title_edit_expense));

        // Inflate the layout for this fragment
        View editExpenseView =inflater.inflate(R.layout.fragment_edit_expense, container, false);

        mBusinessNameEt = editExpenseView.findViewById(R.id.et_business_name_edit_expense);
        mAmountEt = editExpenseView.findViewById(R.id.et_amount_edit_expense);
        mSpinner = editExpenseView.findViewById(R.id.spinner_category_edit_expense);
        mDeleteExpenseBtn = editExpenseView.findViewById(R.id.btn_delete_edit_expense);
        mUpdateExpenseBtn = editExpenseView.findViewById(R.id.btn_update_edit_expense);

        recordUnsavedChanges(mBusinessNameEt, mAmountEt);

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
                        mCurrExpense = expense;
                        updateUI(expense, adapter);
                    }
                });

        mUpdateExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateExpense();
            }
        });
        mDeleteExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteExpense();
            }
        });
        return editExpenseView;
    }

    private void updateUI(Expense expense, ArrayAdapter<CharSequence> adapter) {
        mBusinessNameEt.setText(expense.getBusinessName());
        mAmountEt.setText(mDf.format(expense.getAmount()));
        // set Spinner value
        int categoryPosition = adapter.getPosition(expense.getCategory());
        mSpinner.setSelection(categoryPosition);
    }

    private void recordUnsavedChanges(EditText businessNameEt, EditText amountEt) {
        businessNameEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                unsavedChanges = true;
                return false;
            }
        });

        amountEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                unsavedChanges = true;
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        unsavedChanges = false;
    }



    @Override
    public void onPause() {

        /*if (unsavedChanges) {
            AlertDialog.Builder alertBox = new AlertDialog.Builder(getContext());
            alertBox.setTitle(R.string.alert_dialog_title);
            alertBox.setMessage(R.string.alert_dialog_message);

            alertBox.setPositiveButton(R.string.alert_dialog_yes_label,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            updateExpense();
                        }
                    });

            alertBox.setNeutralButton(R.string.alert_dialog_no_label,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });

            alertBox.show();
        }*/

        super.onPause();
    }

    private boolean isValidExpense(){
        double editedAmount = Double.parseDouble(mAmountEt.getText().toString());
        String editedBusinessName = mBusinessNameEt.getText().toString();

        return  (mCurrExpense != null && editedAmount > 0.0 && !TextUtils.isEmpty(editedBusinessName));
    }

    public void updateExpense() {

        if (isValidExpense()) {
            double editedAmount = Double.parseDouble(mAmountEt.getText().toString());
            String editedBusinessName = mBusinessNameEt.getText().toString();
            mCurrExpense.setAmount(editedAmount);
            mCurrExpense.setBusinessName(editedBusinessName);

            new UpdateExpenseAsyncTask(mRepository, mCurrExpense).execute();

            Toast.makeText(getContext(), getString(R.string.msg_expense_updated), Toast.LENGTH_LONG);
            unsavedChanges = false;
            Navigation.findNavController(getActivity(), R.id.fragment).navigateUp();
        }
    }

    private static class UpdateExpenseAsyncTask extends AsyncTask<Void, Void, Void> {
        private Repository asyncRepository;
        private Expense asyncExpense;
        public UpdateExpenseAsyncTask(Repository repository, Expense expense) {
            asyncRepository = repository;
            asyncExpense = expense;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncRepository.updateExpense(asyncExpense);
            return null;
        }
    }

    public void deleteExpense() {
        new DeleteExpenseAsyncTask(mRepository, mCurrExpense).execute();
        Toast.makeText(getContext(), getString(R.string.msg_deleted_expense), Toast.LENGTH_LONG);
        Navigation.findNavController(getActivity(), R.id.fragment).navigateUp();
    }

    private static class DeleteExpenseAsyncTask extends AsyncTask<Void, Void, Void> {

        private Repository asyncRepository;
        private Expense asyncExpense;

        public DeleteExpenseAsyncTask(Repository repository, Expense expense) {
            asyncRepository = repository;
            asyncExpense = expense;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncRepository.deleteExpense(asyncExpense);
            return null;
        }
    }
}
