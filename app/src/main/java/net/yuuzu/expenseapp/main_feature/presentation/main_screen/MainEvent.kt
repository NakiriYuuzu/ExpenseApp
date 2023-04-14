package net.yuuzu.expenseapp.main_feature.presentation.main_screen

import net.yuuzu.expenseapp.main_feature.domain.model.Expense

sealed class MainEvent {
    data class ShowExpenseMoney(val expense: Expense): MainEvent()
    data class DeleteExpense(val expense: Expense): MainEvent()
    object RestoreExpense: MainEvent()
}