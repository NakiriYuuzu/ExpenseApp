package net.yuuzu.expenseapp.main_feature.domain.usecases

data class ExpenseUseCase(
    val addExpense: AddExpense,
    val getSevenExpenses: GetExpenseByDate,
    val getExpenses: GetExpenses,
    val getExpense: GetExpense,
    val deleteExpense: DeleteExpense
)
