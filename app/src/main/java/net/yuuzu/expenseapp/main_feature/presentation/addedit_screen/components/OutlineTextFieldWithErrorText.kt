package net.yuuzu.expenseapp.main_feature.presentation.addedit_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.yuuzu.expenseapp.ui.theme.CustomFont
import net.yuuzu.expenseapp.ui.theme.Shapes

@Composable
fun OutlinedTextFieldWithErrorText(
    text: String,
    hint: String,
    error: String = "",
    shape: Shape = Shapes.large,
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = CustomFont,
        fontWeight = FontWeight.SemiBold,
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    singleLine: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            label = { Text(text = hint, style = textStyle) },
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            singleLine = singleLine,
            maxLines = 1,
            shape = shape,
            isError = error.isNotEmpty(),
            trailingIcon = if (error.isNotEmpty()) {
                { Icon(Icons.Filled.Error, contentDescription = "Error", tint = MaterialTheme.colors.error) }
            } else trailingIcon,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (error.isNotEmpty()) MaterialTheme.colors.error else MaterialTheme.colors.primary,
                unfocusedBorderColor = if (error.isNotEmpty()) MaterialTheme.colors.error else MaterialTheme.colors.onSurface,
                errorLabelColor = MaterialTheme.colors.error,
                errorTrailingIconColor = MaterialTheme.colors.error
            ),

            modifier = modifier
        )

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colors.error,
                style = textStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.Normal),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}