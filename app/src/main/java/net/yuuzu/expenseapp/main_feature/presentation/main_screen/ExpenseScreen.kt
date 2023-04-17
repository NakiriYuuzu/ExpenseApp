package net.yuuzu.expenseapp.main_feature.presentation.main_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Paid
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import kotlinx.coroutines.flow.collectLatest
import net.yuuzu.expenseapp.main_feature.presentation.addedit_screen.components.OutlinedTextFieldWithErrorText
import net.yuuzu.expenseapp.main_feature.presentation.details_screen.components.BarChart
import net.yuuzu.expenseapp.main_feature.presentation.main_screen.components.CardOfSpendAndBudget
import net.yuuzu.expenseapp.main_feature.presentation.main_screen.components.ExpenseItem
import net.yuuzu.expenseapp.main_feature.presentation.util.Screen
import net.yuuzu.expenseapp.ui.theme.CustomFont
import net.yuuzu.expenseapp.ui.theme.SeedColor
import net.yuuzu.expenseapp.ui.theme.Shapes
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAdjusters
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpenseScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {

    var showChart by remember { mutableStateOf(true) }
    var showSheet by remember { mutableStateOf(false) }

    var setName by remember { mutableStateOf("") }
    var errorName by remember { mutableStateOf("") }
    var errorBudget by remember { mutableStateOf("") }
    var setBudget by remember { mutableStateOf("") }

    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is MainViewModel.UiEvent.ShowErrorField -> {
                    when (event.type) {
                        "Name" -> {
                            errorName = event.message
                        }

                        "Budget" -> {
                            errorBudget = event.message
                        }

                        "error" -> {
                            errorName = event.message
                            errorBudget = event.message
                        }
                    }
                }

                is MainViewModel.UiEvent.SaveToStore -> {
                    showSheet = false
                }
            }
        }
    }

    if (showSheet) {
        BottomSheetDialog(
            onDismissRequest = { showSheet = false },
            properties = BottomSheetDialogProperties()
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Settings",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = CustomFont,
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextFieldWithErrorText(
                        text = setName,
                        hint = "Set Name",
                        error = errorName,
                        onValueChange = { setName = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextFieldWithErrorText(
                        text = setBudget,
                        hint = "Set Budget",
                        error = errorBudget,
                        onValueChange = { setBudget = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        trailingIcon = {
                            val color by animateColorAsState(
                                targetValue =
                                if (setBudget.isNotEmpty())
                                    MaterialTheme.colors.onSurface
                                else MaterialTheme.colors.onSurface.copy(
                                    alpha = 0.5f
                                )
                            )

                            Icon(Icons.Rounded.Paid, contentDescription = null, tint = color)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.onEvent(MainEvent.SaveToStore(setName, setBudget))
                        }
                    ) {
                        Text(
                            text = "Submit",
                            style = TextStyle(
                                fontFamily = CustomFont,
                                fontWeight = FontWeight.SemiBold,
                            ),
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditScreen.route)
                },
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add expense")
            }
        },
        scaffoldState = scaffoldState
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .height(400.dp)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                val timeOfDay =
                    when (LocalDateTime.now(ZoneId.systemDefault()).get(ChronoField.HOUR_OF_DAY)) {
                        in 0..11 -> "Morning"
                        12 -> "Afternoon"
                        in 13..17 -> "Evening"
                        else -> "Night"
                    }

                Text(
                    text = "$timeOfDay, ${viewModel.name.value}!",
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 24.sp,
                    fontFamily = CustomFont,
                    fontWeight = FontWeight.SemiBold,
                )

                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.large)
                        .clickable(
                            onClick = {
                                showSheet = true
                            }
                        )
                )
            }

            val today = LocalDate.now()
            val yesterday = today.minusDays(1)
            val thisWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))

            val todayItems = state.expenses.filter { convertToDateString(it.timestamp) == today }
            val yesterdayItems =
                state.expenses.filter { convertToDateString(it.timestamp) == yesterday }
            val thisWeekItems = state.expenses.filter {
                val date = convertToDateString(it.timestamp)
                date >= thisWeek && date != today && date != yesterday
            }

            Spacer(modifier = Modifier.height(16.dp))

            CardOfSpendAndBudget(
                title = "Monthly Remaining Budget",
                remainingCost = viewModel.budget.value - viewModel.monthlyExpense.value,
                budgetCost = viewModel.budget.value,
            )

            Spacer(modifier = Modifier.height(16.dp))

            BarChart(
                data = viewModel.getBarChartData(state.expenses),
                offer = viewModel.budget.value / 30,
                title = "Weekly Expenses",
                height = 250.dp,
                isExpanded = showChart,
                bottomEndRadius = 30.dp,
                bottomStartRadius = 30.dp,
            ) {
                showChart = !showChart
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "This Week Expenses",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = CustomFont,
                        fontWeight = FontWeight.Bold,
                    )
                )

                Row(
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.large)
                        .clickable {
                            // TODO: navigate to expense list screen
                            Log.e("ExpenseScreen ", "ROW: $it")
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "See All",
                        color = MaterialTheme.colors.onSurface,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = CustomFont,
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "向右箭头",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {

                itemsIndexed(todayItems) { index, expense ->
                    if (index == 0) {
                        Text(
                            text = "Today",
                            color = MaterialTheme.colors.onSurface,
                            fontSize = 18.sp,
                            fontFamily = CustomFont,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ExpenseItem(
                        expense = expense,
                        itemColor = SeedColor,
                        modifier = Modifier
                            .clip(Shapes.large)
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditScreen.route + "?expenseId=${expense.id}"
                                )
                            }
                    )
                }

                itemsIndexed(yesterdayItems) { index, expense ->
                    if (index == 0) {

                        if (todayItems.isNotEmpty())
                            Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Yesterday",
                            color = MaterialTheme.colors.onSurface,
                            fontSize = 18.sp,
                            fontFamily = CustomFont,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ExpenseItem(
                        expense = expense,
                        itemColor = SeedColor,
                        modifier = Modifier
                            .clip(Shapes.large)
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditScreen.route + "?expenseId=${expense.id}"
                                )
                            }
                    )
                }

                itemsIndexed(thisWeekItems) { index, expense ->
                    if (index == 0) {

                        if (todayItems.isNotEmpty() || yesterdayItems.isNotEmpty())
                            Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "This Week",
                            color = MaterialTheme.colors.onSurface,
                            fontSize = 18.sp,
                            fontFamily = CustomFont,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ExpenseItem(
                        expense = expense,
                        itemColor = SeedColor,
                        modifier = Modifier
                            .clip(Shapes.large)
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditScreen.route + "?expenseId=${expense.id}"
                                )
                            }
                    )
                }
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
private fun convertToDateString(timestamp: Long): LocalDate {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return LocalDate.parse(formatter.format(date))
}