package com.cherifcodes.personalexpensetracker.adaptersAndListeners;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cherifcodes.personalexpensetracker.R;
import com.cherifcodes.personalexpensetracker.backend.Expense;

import java.util.List;

public class CategoryExpensesAdapter extends RecyclerView.Adapter<CategoryExpensesAdapter.ExpenseHolder> {

    private static ExpenseItemClickListener mExpenseItemClickListener;
    private List<Expense> mExpenseList;

    public CategoryExpensesAdapter(List<Expense> expenses, ExpenseItemClickListener clickListener) {
        mExpenseItemClickListener = clickListener;
        this.mExpenseList = expenses;
    }

    public void setExpenseList(List<Expense> expenseList) {
        mExpenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View expenseItem = inflater.inflate(R.layout.category_expenses_item, viewGroup, false);

        return new ExpenseHolder(expenseItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseHolder expenseHolder, int i) {
        String expenseDate = String.valueOf(mExpenseList.get(i).getDate().toLocalDate());
        expenseHolder.dateTv.setText(expenseDate);
        expenseHolder.businessNameTv.setText(mExpenseList.get(i).getBusinessName());
        expenseHolder.amountTv.setText(String.valueOf(mExpenseList.get(i).getAmount()));

    }

    @Override
    public int getItemCount() {
        return mExpenseList.size();
    }

    public static class ExpenseHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView businessNameTv;
        private TextView amountTv;
        private TextView dateTv;
        public ExpenseHolder(@NonNull View itemView) {
            super(itemView);
            businessNameTv = itemView.findViewById(R.id.tv_business_name_cat_expense_item);
            amountTv = itemView.findViewById(R.id.tv_amount_cat_expense_item);
            dateTv = itemView.findViewById(R.id.tv_date_cat_expense_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mExpenseItemClickListener.onExpenseItemClicked(getAdapterPosition());
        }
    }
}
