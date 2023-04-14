package net.yuuzu.expenseapp.main_feature.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.yuuzu.expenseapp.main_feature.domain.model.Expense
import net.yuuzu.expenseapp.main_feature.domain.repository.ExpenseRepository

class GetExpenseByDate(
    private val expenseRepository: ExpenseRepository
) {
    operator fun invoke(start: Long, end: Long): Flow<List<Expense>> {
        return expenseRepository.getExpensesByDate(start, end).map {
            it.sortedByDescending {expense ->
                expense.timestamp
            }
        }
    }
}