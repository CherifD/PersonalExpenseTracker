package com.cherifcodes.personalexpensetracker.adaptersAndListeners;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cherifcodes.personalexpensetracker.ListSummary;
import com.cherifcodes.personalexpensetracker.R;
import com.cherifcodes.personalexpensetracker.backend.ExpenseCategoryTotal;

import java.util.List;

import androidx.navigation.Navigation;

public class ListSummaryAdapter extends RecyclerView.Adapter<ListSummaryAdapter.CategoryTotalHolder> {

    private List<ExpenseCategoryTotal> mCategoryTotals;
    private static CategoryTotalItemClickListener mCategoryTotalItemClickListener;

    public ListSummaryAdapter(List<ExpenseCategoryTotal> categoryTotals,
                              CategoryTotalItemClickListener itemClickListener){
        mCategoryTotalItemClickListener = itemClickListener;
        this.mCategoryTotals = categoryTotals;
    }

    @NonNull
    @Override
    public CategoryTotalHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View categoryTotalItem = inflater.inflate(R.layout.list_summary_item, viewGroup, false);

        return new CategoryTotalHolder(categoryTotalItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryTotalHolder categoryTotalHolder, int i) {
        String categoryName = mCategoryTotals.get(i).getCategoryName();
        String firstLetterOfCategoryName = String.valueOf(categoryName.charAt(0));

        categoryTotalHolder.mSummaryListFabLabel.setText(firstLetterOfCategoryName);
        categoryTotalHolder.mSummaryListCategory.setText(mCategoryTotals.get(i).getCategoryName());
        categoryTotalHolder.mSummaryListAmount.setText(
                String.valueOf(mCategoryTotals.get(i).getCategoryTotal()));
        categoryTotalHolder.mSummaryListUnit.setText(R.string.unit);
    }

    @Override
    public int getItemCount() {
        return mCategoryTotals.size();
    }

    public static class CategoryTotalHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView mSummaryListCategory;
        private TextView mSummaryListAmount;
        private TextView mSummaryListUnit;
        private TextView mSummaryListFabLabel;
        public CategoryTotalHolder(@NonNull View itemView) {
            super(itemView);
            mSummaryListCategory = itemView.findViewById(R.id.tv_sumary_list_item_category);
            mSummaryListAmount = itemView.findViewById(R.id.tv_sumary_list_item_amount);
            mSummaryListUnit = itemView.findViewById(R.id.tv_summary_list_item_unit);
            mSummaryListFabLabel = itemView.findViewById(R.id.tv_summary_list_item_fab_label);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Navigation.findNavController(v).navigate(R.id.categoryExpenses);
            mCategoryTotalItemClickListener.onCategoryTotalItemClicked(getAdapterPosition());
        }
    }
}
