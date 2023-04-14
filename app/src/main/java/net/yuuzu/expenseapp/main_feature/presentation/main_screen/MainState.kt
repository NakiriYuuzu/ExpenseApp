package net.yuuzu.expenseapp.main_feature.presentation.main_screen

import net.yuuzu.expenseapp.main_feature.domain.model.Expense

data class MainState(
    val expenses: List<Expense> = emptyList()
)