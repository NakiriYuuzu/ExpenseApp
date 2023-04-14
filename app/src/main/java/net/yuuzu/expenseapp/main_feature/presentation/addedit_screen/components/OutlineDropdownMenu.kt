package net.yuuzu.expenseapp.main_feature.presentation.addedit_screen.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import net.yuuzu.expenseapp.ui.theme.CustomFont

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OutlineDropdownMenu(
    list: List<String>, // Menu Options
    text: String, // Default Selected Option on load
    label: String,
    shape: RoundedCornerShape,
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = CustomFont,
        fontWeight = FontWeight.SemiBold,
    ),
    modifier: Modifier = Modifier,
    onSelected: (Int) -> Unit, // Pass the Selected Option
) {
    var expand by remember { mutableStateOf(false) }

    var result by remember { mutableStateOf("") }

    val rotate by animateFloatAsState(
        targetValue = if (expand) 180f else 0f,
        animationSpec = tween(
            700,
            easing = LinearOutSlowInEasing
        )
    )

    ExposedDropdownMenuBox(
        expanded = expand,
        onExpandedChange = { expand = !expand },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { },
            readOnly = true,
            label = {
                Text(
                    text = label,
                    style = textStyle
                )
            },
            trailingIcon = {
                val color by animateColorAsState(
                    targetValue =
                    if (result.isNotEmpty())
                        MaterialTheme.colors.onSurface
                    else MaterialTheme.colors.onSurface.copy(
                        alpha = 0.5f
                    )
                )

                Icon(
                    Icons.Rounded.ArrowDropDown,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                        .rotate(rotate)
                )
            },
            shape = shape,
            textStyle = textStyle.copy(color = MaterialTheme.colors.onSurface),
            enabled = false,
        )

        ExposedDropdownMenu(
            expanded = expand,
            onDismissRequest = {
                expand = false
            }
        ) {
            list.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onSelected(list.indexOf(selectionOption))
                        result = selectionOption
                        expand = false
                    }
                ){
                    Text(
                        text = selectionOption,
                        style = textStyle
                    )
                }
            }
        }
    }
}