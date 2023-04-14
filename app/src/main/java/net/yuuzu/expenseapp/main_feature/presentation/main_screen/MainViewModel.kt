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
import net.yuuzu.expenseapp.main_feature.domain.usecases.ExpenseUseCase
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val expenseUseCase: ExpenseUseCase,
    private val storeSettingRepositoryImpl: StoreSettingRepositoryImpl
): ViewModel() {

    private val _state = mutableStateOf(MainState())
    val state: State<MainState> = _state

    private val _budget = mutableStateOf(10000f)
    val budget: State<Float> = _budget

    private var recentlyDeletedExpense: Expense? = null

    private var getExpensesJob: Job? = null

    init {
        getExpenses()
        getBudget()
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
            else -> {}
        }
    }

    private fun getExpenses() {
        val today = LocalDate.now()
        val startOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
        val sevenDayBefore = today.minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant()

        val start = sevenDayBefore.toEpochMilli()
        val end = startOfDay.toEpochMilli()

        getExpensesJob?.cancel()
        getExpensesJob = expenseUseCase.getSevenExpenses(start, end)
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
                if (budget.isNotEmpty()) _budget.value = budget.toFloat()
            }
            .launchIn(viewModelScope)
    }
}