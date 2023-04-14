package net.yuuzu.expenseapp.main_feature.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import net.yuuzu.expenseapp.main_feature.domain.model.Expense

/**
 * The Room database for this app
 */
@Database(
    entities = [Expense::class],
    version = 1
)
abstract class ExpenseDatabase: RoomDatabase() {
    abstract val expenseDao: ExpenseDao

    companion object {
        const val DATABASE_NAME = "expense_db"
    }
}