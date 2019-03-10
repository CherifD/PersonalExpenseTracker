package com.cherifcodes.personalexpensetracker.adaptersAndListeners;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cherifcodes.personalexpensetracker.R;
import com.cherifcodes.personalexpensetracker.backend.CategoryTotal;

import java.text.DecimalFormat;
import java.util.List;


public class CategoryTotalsAdapter extends RecyclerView.Adapter<CategoryTotalsAdapter.CategoryTotalHolder> {

    private List<CategoryTotal> mCategoryTotals;
    private static CategoryTotalItemClickListener mCategoryTotalItemClickListener;

    public CategoryTotalsAdapter(List<CategoryTotal> categoryTotals,
                                 CategoryTotalItemClickListener itemClickListener){
        mCategoryTotalItemClickListener = itemClickListener;
        this.mCategoryTotals = categoryTotals;
    }

    public void setCategoryTotalsList(List<CategoryTotal> categoryTotalList) {
        this.mCategoryTotals = categoryTotalList;
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
        categoryTotalHolder.mSummaryListUnit.setText(R.string.unit);

        DecimalFormat df = new DecimalFormat("###.##");
        double total = mCategoryTotals.get(i).getCategoryTotal();
        categoryTotalHolder.mSummaryListAmount.setText(
                String.valueOf(df.format(total)));
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

            mSummaryListCategory = itemView.findViewById(R.id.tv_summary_list_item_category);
            mSummaryListAmount = itemView.findViewById(R.id.tv_summary_list_item_amount);
            mSummaryListUnit = itemView.findViewById(R.id.tv_summary_list_item_unit);
            mSummaryListFabLabel = itemView.findViewById(R.id.tv_summary_list_item_fab_label);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCategoryTotalItemClickListener.onCategoryTotalItemClicked(getAdapterPosition());
        }
    }
}
