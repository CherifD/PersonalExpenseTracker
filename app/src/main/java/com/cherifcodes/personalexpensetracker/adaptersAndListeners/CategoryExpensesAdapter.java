package com.cherifcodes.personalexpensetracker.adaptersAndListeners;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cherifcodes.personalexpensetracker.R;
import com.cherifcodes.personalexpensetracker.backend.Expense;

import java.util.List;

import androidx.navigation.Navigation;

public class CategoryExpensesAdapter extends RecyclerView.Adapter<CategoryExpensesAdapter.ExpenseHolder> {

    private static ExpenseItemClickListener mExpenseItemClickListener;
    private List<Expense> mExpenseList;

    public CategoryExpensesAdapter(List<Expense> expenses, ExpenseItemClickListener clickListener) {
        mExpenseItemClickListener = clickListener;
        mExpenseList = expenses;
    }

    @NonNull
    @Override
    public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View expenseView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_expenses_item, viewGroup, false);
        return new ExpenseHolder(expenseView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseHolder expenseHolder, int i) {
        expenseHolder.dateTv.setText(String.valueOf(mExpenseList.get(i).getDate()));
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
            Navigation.findNavController(v).navigate(R.id.editExpense);
            mExpenseItemClickListener.onExpenseItemClicked(getAdapterPosition());
        }
    }
}
