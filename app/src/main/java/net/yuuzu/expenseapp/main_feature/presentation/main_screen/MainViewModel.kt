package net.yuuzu.expenseapp.main_feature.presentation.main_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.yuuzu.expenseapp.main_feature.data.repository.StoreSettingRepositoryImpl
import net.yuuzu.expenseapp.main_feature.domain.model.Expense
import net.yuuzu.expenseapp.main_feature.domain.model.InvalidExpenseException
import net.yuuzu.expenseapp.main_feature.domain.usecases.ExpenseUseCase
import net.yuuzu.expenseapp.main_feature.presentation.util.convertToDateString
import net.yuuzu.expenseapp.main_feature.presentation.util.convertToDouble
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val expenseUseCase: ExpenseUseCase,
    private val storeSettingRepositoryImpl: StoreSettingRepositoryImpl
): ViewModel() {

    private val _state = mutableStateOf(MainState())
    val state: State<MainState> = _state

    private val _budget = mutableStateOf(0.0)
    val budget: State<Double> = _budget

    private val _name = mutableStateOf("User")
    val name: State<String> = _name

    private val _monthlyExpense = mutableStateOf(0.0)
    val monthlyExpense: State<Double> = _monthlyExpense

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var recentlyDeletedExpense: Expense? = null

    private var getExpensesJob: Job? = null

    init {
        getExpensesWeekly()
        getExpenseMonthly()
        getCategoryColor()
        getBudget()
        getUserName()
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.DeleteExpense -> {
                viewModelScope.launch {
                    expenseUseCase.deleteExpense(event.expense)
                    recentlyDeletedExpense = event.expense
                }
            }
            is MainEvent.RestoreExpense -> {
                viewModelScope.launch {
                    expenseUseCase.addExpense(recentlyDeletedExpense ?: return@launch)
                    recentlyDeletedExpense = null
                }
            }
            is MainEvent.SaveToStore -> {
                viewModelScope.launch {
                    try {
                        if (event.name.isNotEmpty()) {
                            storeSettingRepositoryImpl.saveOneToStore(StoreSettingRepositoryImpl.NAME_KEY, event.name)
                            _name.value = event.name
                        }

                        if (event.budget.isEmpty()) throw InvalidExpenseException("Budget is empty")

                        val newBudget = convertToDouble(event.budget)
                        if (newBudget <= 0) throw InvalidExpenseException("Budget can't be negative or zero")

                        storeSettingRepositoryImpl.saveOneToStore(StoreSettingRepositoryImpl.BUDGET_KEY, event.budget)
                        _budget.value = newBudget

                        _eventFlow.emit(UiEvent.SaveToStore)

                    } catch (e: InvalidExpenseException) {
                        val firstText = e.message?.split(" ")?.first() ?: "error"
                        _eventFlow.emit(UiEvent.ShowErrorField(firstText, e.message ?: "Unknown error"))
                    }
                }
            }
        }
    }

    private fun getExpenseMonthly() {
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

                _monthlyExpense.value = total
            }
            .launchIn(viewModelScope)
    }

    private fun getExpensesWeekly() {
        val today = LocalDate.now()
        val startOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
        val sevenDayBefore = today.minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant()

        val start = sevenDayBefore.toEpochMilli()
        val end = startOfDay.toEpochMilli()

        getExpensesJob?.cancel()
        getExpensesJob = expenseUseCase.getExpenseByDate(start, end)
            .onEach { expenses ->
                _state.value = state.value.copy(
                    expenses = expenses
                )
            }
            .launchIn(viewModelScope)
    }

    private fun getBudget() {
        storeSettingRepositoryImpl.getOneFromStore(StoreSettingRepositoryImpl.BUDGET_KEY)
            .onEach { budget ->
                // check budget is null or not
                if (budget.isNotEmpty()) _budget.value = budget.toDouble()
            }
            .launchIn(viewModelScope)
    }

    private fun getUserName() {
        storeSettingRepositoryImpl.getOneFromStore(StoreSettingRepositoryImpl.NAME_KEY)
            .onEach { name ->
                // check name is null or not
                if (name.isNotEmpty()) _name.value = name
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

    fun getBarChartData(expensesList: List<Expense>): Map<String, Double> {
        val today = LocalDate.now()
        val weeklySpent = mutableMapOf<String, Double>()

        for (i in 6 downTo 0) {
            val day = today.minusDays(i.toLong())
            val expensesOnDay = expensesList.filter { convertToDateString(it.timestamp) == day }
            val total = expensesOnDay.sumOf { expense -> expense.cost }
            val dayOfWeek = day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            weeklySpent[dayOfWeek] = total
        }

        return weeklySpent
    }

    sealed class UiEvent {
        data class ShowErrorField(val type: String, val message: String): UiEvent()
        object SaveToStore: UiEvent()
    }
}