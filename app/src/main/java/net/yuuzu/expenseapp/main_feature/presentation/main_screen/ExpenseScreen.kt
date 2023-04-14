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
    var setBudget by remember { mutableStateOf("") }

    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    if (showSheet) {
        BottomSheetDialog(
            onDismissRequest = { showSheet = false },
            properties = BottomSheetDialogProperties(

            )
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
                        text = "Hello World",
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
                        onValueChange = { setName = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextFieldWithErrorText(
                        text = setBudget,
                        hint = "Set Budget",
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
                            showSheet = false
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
                Text(
                    text = "Hello, Yuuzu!",
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

            Spacer(modifier = Modifier.height(16.dp))

            CardOfSpendAndBudget(
                title = "Total Remaining Budget",
                remainingCost = 9000f,
                budgetCost = viewModel.budget.value,
            )

            Spacer(modifier = Modifier.height(16.dp))

            BarChart(
                data = mapOf(
                    Pair("Mon", 5f),
                    Pair("Tue", 4f),
                    Pair("Wed", 3f),
                    Pair("Thu", 2f),
                    Pair("Fri", 3f),
                    Pair("Sat", 4f),
                    Pair("Sun", 5f),
                ),
                offer = 5f,
                title = "Weekly Expenses",
                height = 250.dp,
                isExpanded = showChart,
                bottomEndRadius = 30.dp,
                bottomStartRadius = 30.dp,
            ) {
                showChart = !showChart
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.expenses.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
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

                Spacer(modifier = Modifier.height(16.dp))
            }

            val today = LocalDate.now()
            val yesterday = today.minusDays(1)
            val thisWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))

            val todayItems = state.expenses.filter { convertToDateString(it.timestamp) == today }
            val yesterdayItems = state.expenses.filter { convertToDateString(it.timestamp) == yesterday }
            val thisWeekItems = state.expenses.filter {
                val date = convertToDateString(it.timestamp)
                date >= thisWeek && date != today && date != yesterday
            }

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