package net.yuuzu.expenseapp.main_feature.domain.usecases

import net.yuuzu.expenseapp.main_feature.domain.model.Expense
import net.yuuzu.expenseapp.main_feature.domain.repository.ExpenseRepository

class DeleteExpense(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(expense: Expense) {
        expenseRepository.deleteExpense(expense)
    }
}