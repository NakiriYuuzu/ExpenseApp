package net.yuuzu.expenseapp.main_feature.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.yuuzu.expenseapp.main_feature.domain.model.Expense
import net.yuuzu.expenseapp.main_feature.domain.repository.ExpenseRepository

class GetExpenses(private val expenseRepository: ExpenseRepository) {
    operator fun invoke(): Flow<List<Expense>> {
        return expenseRepository.getAllExpenses().map {
            it.sortedByDescending {expense ->
                expense.timestamp
            }
        }
    }
}