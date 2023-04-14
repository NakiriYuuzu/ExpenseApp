package net.yuuzu.expenseapp.main_feature.presentation.addedit_screen.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import net.yuuzu.expenseapp.ui.theme.CustomFont
import net.yuuzu.expenseapp.ui.theme.Shapes

@Composable
fun OutlinedPicker(
    text: String,
    hint: String,
    shape: Shape = Shapes.large,
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = CustomFont,
        fontWeight = FontWeight.SemiBold,
    ),
    trailingIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = text,
        onValueChange = {  },
        label = {
            Text(
                text = hint,
                style = textStyle,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
        },
        textStyle = textStyle.copy(color = MaterialTheme.colors.onSurface),
        trailingIcon = {
            val color by animateColorAsState(
                targetValue = if (text.isNotEmpty()) MaterialTheme.colors.onSurface else MaterialTheme.colors.onSurface.copy(
                    alpha = 0.5f
                )
            )
            Icon(trailingIcon, contentDescription = null, tint = color)
        },
        enabled = false,
        shape = shape,
        modifier = modifier.clickable { onClick() }
    )
}