package net.yuuzu.expenseapp.main_feature.presentation.addedit_screen

sealed class AddEditEvent {
    data class EnteredTitle(val value: String) : AddEditEvent()
    data class EnteredAmount(val value: String) : AddEditEvent()
    data class PickCategory(val value: String) : AddEditEvent()
    data class PickDate(val value: String) : AddEditEvent()
    object SaveExpense : AddEditEvent()
    object DeleteExpense: AddEditEvent()
}