package com.cherifcodes.personalexpensetracker.adaptersAndListeners;

import com.cherifcodes.personalexpensetracker.viewModels.CategoryExpensesViewModel;

public interface OnFragmentInteractionListener {
    CategoryExpensesViewModel getCatExpenseViewModel();
    void navigateToCategoryExpensesFragment(String category);
    void navigateToCategoryTotalsFragment();
    void navigateToNewExpenseFragment();
    void navigateToEditExpenseFragment();
}
