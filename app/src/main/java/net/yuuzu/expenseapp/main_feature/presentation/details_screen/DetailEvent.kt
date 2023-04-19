package net.yuuzu.expenseapp.main_feature.presentation.details_screen

import java.time.LocalDate

sealed class DetailEvent {
    data class CategoriesOrTransactions(val isCategories: Int) : DetailEvent()
    data class SelectedDateRange(val firstDate: LocalDate, val lastDate: LocalDate?) : DetailEvent()
}