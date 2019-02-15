package com.cherifcodes.personalexpensetracker.adaptersAndListeners;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cherifcodes.personalexpensetracker.R;

public class ListSummaryAdapter extends RecyclerView.Adapter<ListSummaryAdapter.CategoryHolder> {


    public ListSummaryAdapter(){

    }


    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View categoryItem = inflater.inflate(R.layout.summary_list_item, viewGroup, false);

        return new CategoryHolder(categoryItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder categoryHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class CategoryHolder extends RecyclerView.ViewHolder {

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
