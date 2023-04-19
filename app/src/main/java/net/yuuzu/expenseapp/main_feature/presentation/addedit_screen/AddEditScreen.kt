package net.yuuzu.expenseapp.main_feature.presentation.addedit_screen

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Paid
import androidx.compose.material.icons.twotone.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.squaredem.composecalendar.ComposeCalendar
import kotlinx.coroutines.flow.collectLatest
import net.yuuzu.expenseapp.main_feature.presentation.addedit_screen.components.OutlineDropdownMenu
import net.yuuzu.expenseapp.main_feature.presentation.addedit_screen.components.OutlinedPicker
import net.yuuzu.expenseapp.main_feature.presentation.addedit_screen.components.OutlinedTextFieldWithErrorText
import net.yuuzu.expenseapp.ui.theme.CustomFont
import net.yuuzu.expenseapp.ui.theme.Shapes
import java.time.LocalDate
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditScreen(
    navController: NavController? = null,
    viewModel: AddEditViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val categories = state.categoryColors.map { it.category }

    val title = if (viewModel.currentExpenseId == null) "New Expense" else state.title

    var isDatePickerOpen by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                    )
                }
                is AddEditViewModel.UiEvent.SaveExpense -> {
                    navController?.navigateUp()
                }
                is AddEditViewModel.UiEvent.DeleteExpense -> {
                    navController?.navigateUp()
                }
            }
        }
    }

    if (isDatePickerOpen) {

        val startDate = if (state.date.isNotEmpty()) {
            LocalDate.parse(state.date)
        } else {
            LocalDate.now()
        }

        ComposeCalendar(
            onDone = {
                isDatePickerOpen = false
                viewModel.onEvent(AddEditEvent.PickDate(it.toString()))
            },
            onDismiss = {
                isDatePickerOpen = false
            },
            startDate = startDate
        )
    }

    Scaffold(
        floatingActionButton = {
            Column {
                if (viewModel.currentExpenseId != null) {
                    FloatingActionButton(
                        onClick = {
                            viewModel.onEvent(AddEditEvent.DeleteExpense)
                        },
                        backgroundColor = MaterialTheme.colors.surface
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete expense",
                            tint = MaterialTheme.colors.error
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(AddEditEvent.SaveExpense)
                    },
                    backgroundColor = MaterialTheme.colors.surface
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = "Add expense")
                }
            }
        },
        scaffoldState = scaffoldState,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .indication(interactionSource = MutableInteractionSource(), indication = null)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { focusManager.clearFocus() }
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
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
                        fontSize = 24.sp,
                        fontFamily = CustomFont,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                OutlinedTextFieldWithErrorText(
                    text = state.title,
                    hint = "Enter Title",
                    onValueChange = { viewModel.onEvent(AddEditEvent.EnteredTitle(it)) },
                    modifier = Modifier.focusRequester(focusRequester),
                )

                OutlinedTextFieldWithErrorText(
                    text = state.amount,
                    hint = "Enter Amount",
                    onValueChange = { viewModel.onEvent(AddEditEvent.EnteredAmount(it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    trailingIcon = {
                        val color by animateColorAsState(
                            targetValue =
                            if (state.amount.isNotEmpty())
                                MaterialTheme.colors.onSurface
                            else MaterialTheme.colors.onSurface.copy(
                                alpha = 0.5f
                            )
                        )

                        Icon(Icons.Rounded.Paid, contentDescription = null, tint = color)
                    },
                    modifier = Modifier.focusRequester(focusRequester),
                )

                OutlineDropdownMenu(
                    list = categories,
                    text = state.category,
                    label = "Pick Category",
                    shape = RoundedCornerShape(16.dp),
                    onSelected = {
                        viewModel.onEvent(AddEditEvent.PickCategory(categories[it]))
                    },
                    modifier = Modifier
                )

                OutlinedPicker(
                    text = state.date,
                    hint = "Pick Date",
                    trailingIcon = Icons.Rounded.DateRange,
                    onClick = {
                        isDatePickerOpen = true
                    },
                    modifier = Modifier.clip(Shapes.large)
                )
            }
        }
    }
}
