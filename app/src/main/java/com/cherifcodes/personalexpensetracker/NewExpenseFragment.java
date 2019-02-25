package com.cherifcodes.personalexpensetracker;

import android.os.Bundle;
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
import com.cherifcodes.personalexpensetracker.viewModels.SharedViewModel;

import java.time.LocalDateTime;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewExpenseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner mSpinner;
    private EditText mBusinessNameEt;
    private EditText mAmountEt;

    private SharedViewModel mViewModel;
    private Repository mRepository;

    public NewExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewExpenseFragment newInstance(String param1, String param2) {
        NewExpenseFragment fragment = new NewExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mRepository = Repository.getInstance(getActivity().getApplication());
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
                System.out.print("New Expense Added!!!!!!!!!");
                Toast.makeText(getActivity(), "No error while saving!", Toast.LENGTH_LONG).show();
            }
        });

        mSpinner = newExpenseView.findViewById(R.id.spinner_category_new_expense);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.expense_categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);

        return newExpenseView;
    }


    private boolean isValidExpense() {
        return !(TextUtils.isEmpty(mSpinner.getSelectedItem().toString()) ||
            TextUtils.isEmpty(mBusinessNameEt.getText()) ||
            TextUtils.isEmpty(mAmountEt.getText()) ||
                    Double.parseDouble(mAmountEt.getText().toString()) <= 0);
    }

    public void saveExpense() {
        //if (isValidExpense()) {
            final Expense newExpense = new Expense(mSpinner.getSelectedItem().toString(),
                    mBusinessNameEt.getText().toString(),
                    Double.parseDouble(mAmountEt.getText().toString()),
                    LocalDateTime.now());

            //Insert the new Expense into the database
            Repository repository = Repository.getInstance(getActivity().getApplication());
            repository.insertExpense(newExpense);

            //mViewModel.insertExpense(newExpense);
            /*new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    mRepository.insertExpense(newExpense);
                    System.out.print("Inserted New Expense is: " + newExpense);
                    return null;
                }
            }.execute();
*/          //Reset the new Expense form
            clearNewExpenseData();
    }


    private void clearNewExpenseData() {
        mAmountEt.setText("");
        mBusinessNameEt.setText("");
        mSpinner.requestFocus();
    }
}
