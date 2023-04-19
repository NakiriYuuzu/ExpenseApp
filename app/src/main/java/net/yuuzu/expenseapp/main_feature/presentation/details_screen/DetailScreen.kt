package net.yuuzu.expenseapp.main_feature.presentation.details_screen

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.robertlevonyan.compose.buttontogglegroup.RowToggleButtonGroup
import com.squaredem.composecalendar.ComposeCalendar
import kotlinx.coroutines.flow.collectLatest
import net.yuuzu.expenseapp.R
import net.yuuzu.expenseapp.main_feature.presentation.details_screen.components.BarChart
import net.yuuzu.expenseapp.main_feature.presentation.details_screen.components.CategoryItem
import net.yuuzu.expenseapp.main_feature.presentation.main_screen.components.CardOfSpendAndBudget
import net.yuuzu.expenseapp.main_feature.presentation.main_screen.components.ExpenseItem
import net.yuuzu.expenseapp.main_feature.presentation.util.Screen
import net.yuuzu.expenseapp.main_feature.presentation.util.convertToColor
import net.yuuzu.expenseapp.main_feature.presentation.util.convertToDateString
import net.yuuzu.expenseapp.ui.theme.CustomFont
import net.yuuzu.expenseapp.ui.theme.Shapes
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DetailScreen(
    navController: NavController? = null,
    viewModel: DetailViewModel = hiltViewModel()
) {

    var isSwitch by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(true) }
    var isDatePicker by remember { mutableStateOf(false) }

    var firstDate by remember { mutableStateOf<LocalDate?>(null) }
    var lastDate by remember { mutableStateOf<LocalDate?>(null) }

    var title by remember { mutableStateOf("Transactions") }

    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is DetailViewModel.UiEvent.ChangeToCategories -> {
                    isSwitch = true
                    title = "Categories"
                }

                is DetailViewModel.UiEvent.ChangeToTransactions -> {
                    isSwitch = false
                    title = "Transactions"
                }

                is DetailViewModel.UiEvent.CalendarDialog -> {
                    isDatePicker = true
                }
            }
        }
    }

    if (isDatePicker) {
        ComposeCalendar(
            onDone = { selectedDate ->
                when {
                    firstDate == null -> {
                        firstDate = selectedDate
                        viewModel.onEvent(DetailEvent.SelectedDateRange(selectedDate, null))
                    }

                    lastDate == null -> {
                        lastDate = selectedDate
                        viewModel.onEvent(DetailEvent.SelectedDateRange(firstDate!!, lastDate))
                    }

                    else -> {
                        firstDate = selectedDate
                        lastDate = null
                    }
                }

                Log.e("TAG", "DetailScreen: $firstDate - $lastDate")

                if (firstDate != null && lastDate != null) {
                    firstDate = null
                    lastDate = null
                }

                isDatePicker = false
            },
            onDismiss = { isDatePicker = false }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExtendedFloatingActionButton(
                    onClick = { navController?.popBackStack() },
                    icon = {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    },
                    text = {
                        Text(
                            "Back",
                            fontFamily = CustomFont,
                            fontWeight = FontWeight.SemiBold,
                        )
                    },
                    backgroundColor = MaterialTheme.colors.surface,
                )

                Text(
                    text = title,
                    fontFamily = CustomFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colors.onSurface
                )

                IconButton(
                    onClick = {
                        isDatePicker = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.EditCalendar,
                        contentDescription = "Forward"
                    )
                }
            }

            RowToggleButtonGroup(
                modifier = Modifier,
                buttonCount = 2,
                primarySelection = 0,
                selectedColor = MaterialTheme.colors.onBackground,
                unselectedColor = MaterialTheme.colors.background,
                selectedContentColor = MaterialTheme.colors.background,
                unselectedContentColor = MaterialTheme.colors.onBackground,
                shape = MaterialTheme.shapes.large,
                elevation = ButtonDefaults.elevation(0.dp), // elevation of toggle group buttons
                buttonTexts = arrayOf(
                    "Transactions",
                    "Categories"
                ),
                buttonIcons = arrayOf(
                    painterResource(id = R.drawable.baseline_format_list_bulleted_24),
                    painterResource(id = R.drawable.baseline_category_24)
                ),
            ) { index ->
                Log.e("TAG", "DetailScreen: $index")
                viewModel.onEvent(DetailEvent.CategoriesOrTransactions(index))
            }

            if (isSwitch) {
                BarChart(
                    data = viewModel.getBarChartData(state.categoryColors, state.expenses),
                    offer = 0.0,
                    title = "Monthly Categories",
                    height = 250.dp,
                    isExpanded = isExpanded,
                    categoryColor = state.categoryColors,
                    bottomEndRadius = 30.dp,
                    bottomStartRadius = 30.dp,
                ) {
                    isExpanded = !isExpanded
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    val categories = state.categoryColors.map { it.category }
                    val categoriesDate = mutableListOf<String>()
                    val categoriesSpent = mutableListOf<Double>()

                    categories.forEach { category ->
                        val matchCategory = state.expenses.filter { it.category == category }
                        val defaultStrDate = "No recorded"

                        categoriesDate.add(
                            (matchCategory.firstOrNull()?.let {
                                convertToDateString(it.timestamp)
                            } ?: defaultStrDate).toString()
                        )

                        categoriesSpent.add(
                            matchCategory.sumOf { it.cost }
                        )
                    }

                    itemsIndexed(state.categoryColors) { index, categoryColor ->
                        if (index > 0) Spacer(modifier = Modifier.height(16.dp))
                        CategoryItem(
                            categoryColor = categoryColor,
                            categorySpent = categoriesSpent[index],
                            categoryDate = categoriesDate[index],
                        )
                    }
                }

            } else {
                CardOfSpendAndBudget(
                    title = "Monthly Expenses",
                    remainingCost = state.budget - state.totalMonthly,
                    budgetCost = state.budget
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(state.expenses) { index, expense ->
                        val previousExpense = if (index > 0) state.expenses[index - 1] else null
                        val currentDateString = convertToDateString(expense.timestamp)
                        val previousDateString =
                            previousExpense?.let { convertToDateString(it.timestamp) }

                        if (previousDateString != currentDateString) {
                            if (index > 0) Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = currentDateString.format(DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy")),
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 18.sp,
                                fontFamily = CustomFont,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        ExpenseItem(
                            expense = expense,
                            itemColor = convertToColor(state.categoryColors.find { it.category == expense.category }?.color),
                            modifier = Modifier
                                .clip(Shapes.large)
                                .clickable {
                                    navController?.navigate(
                                        Screen.AddEditScreen.route + "?expenseId=${expense.id}"
                                    )
                                }
                        )
                    }
                }
            }
        }
    }
}