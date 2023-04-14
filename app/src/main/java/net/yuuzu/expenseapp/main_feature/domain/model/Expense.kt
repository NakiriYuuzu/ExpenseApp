package net.yuuzu.expenseapp.main_feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expense(
    val title: String,
    val detail: String,
    val cost: Double,
    val category: String,
    val timestamp: Long,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val categories = listOf("Food", "Transport", "Entertainment", "Subscribe", "Other")
    }
}

/**
 * The exception thrown when the expense is invalid
 * @param message The message of the exception
 */
class InvalidExpenseException(message: String): Exception(message)