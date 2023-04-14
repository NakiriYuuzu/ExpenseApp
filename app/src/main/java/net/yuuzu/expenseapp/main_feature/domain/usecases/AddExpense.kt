package net.yuuzu.expenseapp.main_feature.domain.usecases

import net.yuuzu.expenseapp.main_feature.domain.model.Expense
import net.yuuzu.expenseapp.main_feature.domain.model.InvalidExpenseException
import net.yuuzu.expenseapp.main_feature.domain.repository.ExpenseRepository

class AddExpense(private val expenseRepository: ExpenseRepository) {
    @Throws(InvalidExpenseException::class)
    suspend operator fun invoke(expense: Expense) {
        if (expense.title.isBlank()) {
            throw InvalidExpenseException("The title of the expense can't be empty.")
        }
        expenseRepository.insertExpense(expense)
    }
}