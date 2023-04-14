package net.yuuzu.expenseapp.main_feature.data.repository

import kotlinx.coroutines.flow.Flow
import net.yuuzu.expenseapp.main_feature.data.data_source.ExpenseDao
import net.yuuzu.expenseapp.main_feature.domain.model.Expense
import net.yuuzu.expenseapp.main_feature.domain.repository.ExpenseRepository

class ExpenseRepositoryImpl(
    private val dao: ExpenseDao
): ExpenseRepository {

    override fun getAllExpenses(): Flow<List<Expense>> {
        return dao.getAllExpenses()
    }

    override fun getExpensesByDate(start: Long, end: Long): Flow<List<Expense>> {
        return dao.getExpensesByDate(start, end)
    }

    override suspend fun getExpenseById(id: Int): Expense? {
        return dao.getExpenseById(id)
    }

    override suspend fun insertExpense(expense: Expense) {
        dao.insertExpense(expense)
    }

    override suspend fun deleteExpense(expense: Expense) {
        dao.deleteExpense(expense)
    }
}