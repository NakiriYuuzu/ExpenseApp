package net.yuuzu.expenseapp.main_feature.presentation.addedit_screen

import net.yuuzu.expenseapp.main_feature.data.util.CategoryColor

data class AddEditState(
    var title: String = "",
    var detail: String = "",
    var amount: String = "",
    var category: String = "Others",
    var date: String = "",

    val categoryColors: List<CategoryColor> = emptyList(),
)