package net.yuuzu.expenseapp.main_feature.presentation.addedit_screen.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import net.yuuzu.expenseapp.ui.theme.CustomFont
import net.yuuzu.expenseapp.ui.theme.Shapes

@Composable
fun OutlinedTextFieldWithErrorText(
    text: String,
    hint: String,
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
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        label = {
            Text(
                text = hint,
                style = textStyle
            )
        },
        trailingIcon = trailingIcon,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        maxLines = 1,
        shape = shape,
        modifier = modifier
    )
}