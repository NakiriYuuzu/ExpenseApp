package net.yuuzu.expenseapp.main_feature.domain.usecases

import net.yuuzu.expenseapp.main_feature.domain.model.Expense
import net.yuuzu.expenseapp.main_feature.domain.repository.ExpenseRepository

class GetExpense(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(id: Int): Expense? {
        return expenseRepository.getExpenseById(id)
    }
}