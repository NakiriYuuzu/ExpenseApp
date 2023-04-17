package net.yuuzu.expenseapp.main_feature.presentation.main_screen

import net.yuuzu.expenseapp.main_feature.domain.model.Expense

sealed class MainEvent {

    data class DeleteExpense(val expense: Expense): MainEvent()
    data class SaveToStore(val name: String, val budget: String): MainEvent()
    object RestoreExpense: MainEvent()
}