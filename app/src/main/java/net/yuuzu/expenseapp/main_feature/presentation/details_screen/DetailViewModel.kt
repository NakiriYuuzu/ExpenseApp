package net.yuuzu.expenseapp.main_feature.presentation.details_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.yuuzu.expenseapp.main_feature.data.repository.StoreSettingRepositoryImpl
import net.yuuzu.expenseapp.main_feature.data.util.CategoryColor
import net.yuuzu.expenseapp.main_feature.domain.model.Expense
import net.yuuzu.expenseapp.main_feature.domain.usecases.ExpenseUseCase
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class DetailViewModel@Inject constructor(
    private val expenseUseCase: ExpenseUseCase,
    private val storeSettingRepositoryImpl: StoreSettingRepositoryImpl
): ViewModel() {

    private val _state = mutableStateOf(DetailState())
    val state: State<DetailState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var getExpensesJob: Job? = null

    init {
        getExpense()
        getBudget()
        getCategoryColor()
    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.CategoriesOrTransactions -> {
                Log.e("TAG", "onEvent: ${event.isCategories}")
                viewModelScope.launch {
                    if (event.isCategories == 1) {
                        _eventFlow.emit(UiEvent.ChangeToCategories)
                    } else {
                        _eventFlow.emit(UiEvent.ChangeToTransactions)
                    }
                }
            }

            is DetailEvent.SelectedDateRange -> {
                viewModelScope.launch {
                    if (event.lastDate == null) {
                        delay(100)
                        _eventFlow.emit(UiEvent.CalendarDialog)
                        return@launch
                    }

                    getExpensesForDateRange(event.firstDate, event.lastDate)
                }
            }
        }
    }

    fun getBarChartData(categoriesColor: List<CategoryColor>, expenses: List<Expense>): Map<String, Double> {
        val result = mutableMapOf<String, Double>()

        val categories = categoriesColor.map { it.category }

        categories.forEach { category ->
            val matchCategory = expenses.filter { it.category == category }
            val total = matchCategory.sumOf { expense -> expense.cost }
            result[category] = total
        }

        Log.e("TAG", "getBarChartData: $result")
        return result
    }

    private fun getExpense() {
        getExpensesJob?.cancel()

        // get this month first day
        val today = LocalDate.now()
        val startOfMonth = today.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
        val endOfMonth = today.plusMonths(1).withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant()

        val start = startOfMonth.toEpochMilli()
        val end = endOfMonth.toEpochMilli()

        // calculate total expense
        expenseUseCase.getExpenseByDate(start, end)
            .onEach { expenses ->
                val total = expenses.sumOf { it.cost }

                _state.value = _state.value.copy(
                    expenses = expenses,
                    totalMonthly = total
                )
            }
            .launchIn(viewModelScope)
    }

    private fun getExpensesForDateRange(startDate: LocalDate, endDate: LocalDate) {
        getExpensesJob?.cancel()

        val startOfMonth = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val endOfSelectedDate = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()

        val start = startOfMonth.toEpochMilli()
        val end = endOfSelectedDate.toEpochMilli()

        expenseUseCase.getExpenseByDate(start, end)
            .onEach { expenses ->
                val total = expenses.sumOf { it.cost }

                _state.value = _state.value.copy(
                    expenses = expenses,
                    totalMonthly = total
                )
            }
            .launchIn(viewModelScope)
    }

    private fun getBudget() {
        storeSettingRepositoryImpl.getOneFromStore(StoreSettingRepositoryImpl.BUDGET_KEY)
            .onEach { budget ->
                if (budget.isNotEmpty()) {
                    _state.value = state.value.copy(
                        budget = budget.toDouble()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getCategoryColor() {
        storeSettingRepositoryImpl.getCategoryColor()
            .onEach { categoryColors ->
                _state.value = state.value.copy(
                    categoryColors = categoryColors!!
                )
            }
            .launchIn(viewModelScope)
    }

    sealed class UiEvent {
        object ChangeToCategories: UiEvent()
        object ChangeToTransactions: UiEvent()
        object CalendarDialog: UiEvent()
    }
}