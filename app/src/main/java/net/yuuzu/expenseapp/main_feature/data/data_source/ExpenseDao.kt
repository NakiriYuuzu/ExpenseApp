package net.yuuzu.expenseapp.main_feature.data.data_source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import net.yuuzu.expenseapp.main_feature.domain.model.Expense

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM Expense")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE timestamp BETWEEN :start AND :end")
    fun getExpensesByDate(start: Long, end: Long): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE id = :id")
    suspend fun getExpenseById(id: Int): Expense?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)
}