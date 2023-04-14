package net.yuuzu.expenseapp.main_feature.domain.repository

import kotlinx.coroutines.flow.Flow
import net.yuuzu.expenseapp.main_feature.domain.model.Expense
import javax.inject.Named
import javax.inject.Singleton

@Named("storeSettingRepository")
@Singleton
interface ExpenseRepository {

    fun getAllExpenses(): Flow<List<Expense>>

    fun getExpensesByDate(start: Long, end: Long): Flow<List<Expense>>

    suspend fun getExpenseById(id: Int): Expense?

    suspend fun insertExpense(expense: Expense)

    suspend fun deleteExpense(expense: Expense)

}