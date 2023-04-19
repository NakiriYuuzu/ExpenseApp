package net.yuuzu.expenseapp.main_feature.presentation.details_screen

import net.yuuzu.expenseapp.main_feature.data.util.CategoryColor
import net.yuuzu.expenseapp.main_feature.domain.model.Expense

data class DetailState(
    val expenses: List<Expense> = emptyList(),
    val totalMonthly: Double = 0.0,
    val budget: Double = 0.0,
    val categoryColors: List<CategoryColor> = emptyList(),
)