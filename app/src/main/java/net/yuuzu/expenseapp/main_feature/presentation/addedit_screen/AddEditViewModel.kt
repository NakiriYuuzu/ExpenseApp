package net.yuuzu.expenseapp.main_feature.presentation.addedit_screen

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import net.yuuzu.expenseapp.main_feature.domain.model.Expense
import net.yuuzu.expenseapp.main_feature.domain.model.InvalidExpenseException
import net.yuuzu.expenseapp.main_feature.domain.usecases.ExpenseUseCase
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val expenseUseCase: ExpenseUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = mutableStateOf(AddEditState())
    val state: MutableState<AddEditState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var currentExpenseId: Int? = null

    init {
        savedStateHandle.get<Int>("expenseId")?.let { expenseId ->
            if (expenseId != -1) {
                viewModelScope.launch {
                    expenseUseCase.getExpense(expenseId)?.also { expense ->

                        currentExpenseId = expense.id

                        _state.value = state.value.copy(
                            title = expense.title,
                            detail = expense.detail,
                            amount = convertToDecimal(expense.cost),
                            category = expense.category,
                            date = convertToDateString(expense.timestamp)
                        )
                    }
                }
            } else {
                val date = Calendar.getInstance().timeInMillis
                _state.value = state.value.copy(date = convertToDateString(date))
            }
        }
    }

    fun onEvent(event: AddEditEvent) {
        when(event) {
            is AddEditEvent.EnteredTitle -> {
                _state.value = state.value.copy(title = event.value)
            }
            is AddEditEvent.EnteredAmount -> {
                _state.value = state.value.copy(amount = event.value)
            }
            is AddEditEvent.PickCategory -> {
                _state.value = state.value.copy(category = event.value)
            }
            is AddEditEvent.PickDate -> {
                _state.value = state.value.copy(date = event.value)
            }
            is AddEditEvent.SaveExpense -> {
                viewModelScope.launch {
                    try {
                        expenseUseCase.addExpense(
                            Expense(
                                title = state.value.title,
                                detail = "null",
                                cost = convertToDouble(state.value.amount),
                                category = state.value.category,
                                timestamp = convertToTimestamp(state.value.date),
                                id = currentExpenseId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveExpense)
                    } catch (e: InvalidExpenseException) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(e.message ?: "Invalid to save expense."))
                    }
                }
            }
            is AddEditEvent.DeleteExpense -> {
                viewModelScope.launch {
                    expenseUseCase.deleteExpense(
                        Expense(
                            title = state.value.title,
                            detail = "null",
                            cost = convertToDouble(state.value.amount),
                            category = state.value.category,
                            timestamp = convertToTimestamp(state.value.date),
                            id = currentExpenseId
                        )
                    )
                    _eventFlow.emit(UiEvent.DeleteExpense)
                }
            }
        }
    }

    private fun convertToTimestamp(localDate: String): Long {
        val currentDate = LocalDate.parse(localDate).atTime(LocalTime.now())
        val timeZone = ZoneId.systemDefault()
        val instant = currentDate.atZone(timeZone).toInstant()
        return instant.toEpochMilli()
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertToDateString(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(date)
    }

    private fun convertToDouble(amount: String): Double {
        return amount.toDoubleOrNull() ?: throw InvalidExpenseException("Invalid amount.")
    }

    private fun convertToDecimal(amount: Double): String {
        return if (amount > 9999999) {
            val decimalFormat = DecimalFormat("#")
            decimalFormat.maximumFractionDigits = amount.toString().length

            decimalFormat.format(amount)
        } else {
            amount.toString()
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveExpense: UiEvent()

        object DeleteExpense: UiEvent()
    }
}