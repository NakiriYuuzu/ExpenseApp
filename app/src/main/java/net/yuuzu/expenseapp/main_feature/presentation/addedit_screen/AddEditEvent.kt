package net.yuuzu.expenseapp.main_feature.presentation.addedit_screen

import androidx.compose.ui.focus.FocusState

sealed class AddEditEvent {
    data class EnteredTitle(val value: String) : AddEditEvent()
    data class EnteredAmount(val value: String) : AddEditEvent()
    data class PickCategory(val value: String) : AddEditEvent()
    data class PickDate(val value: String) : AddEditEvent()
    object SaveExpense : AddEditEvent()
}