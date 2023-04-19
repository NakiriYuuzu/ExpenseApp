package net.yuuzu.expenseapp.main_feature.presentation.util

sealed class Screen(val route: String) {
    object ExpenseScreen: Screen("expense_screen")
    object AddEditScreen: Screen("add_edit_screen")
    object DetailScreen: Screen("detail_screen")
}