package net.yuuzu.expenseapp.main_feature.presentation.addedit_screen

data class AddEditState(
    var title: String = "",
    var detail: String = "",
    var amount: String = "",
    var category: String = "None",
    var date: String = "",
)