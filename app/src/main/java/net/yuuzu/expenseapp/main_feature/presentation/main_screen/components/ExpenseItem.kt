package net.yuuzu.expenseapp.main_feature.presentation.main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.yuuzu.expenseapp.main_feature.domain.model.Expense
import net.yuuzu.expenseapp.ui.theme.CustomFont
import java.text.DecimalFormat

@Composable
fun ExpenseItem(
    expense: Expense,
    itemColor: Color,
    backgroundColor: Color = MaterialTheme.colors.surface,
    iconShape: RoundedCornerShape = RoundedCornerShape(16.dp),
    shape: RoundedCornerShape = RoundedCornerShape(25.dp),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = iconShape)
            .background(color = backgroundColor, shape = shape)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp, 50.dp)
                    .clip(shape = iconShape)
                    .background(
                        color = itemColor,
                        shape = iconShape
                    )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = expense.title,
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 20.sp,
                    fontFamily = CustomFont,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = expense.category,
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontFamily = CustomFont,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Text(
                text = "$${convertToDecimal(expense.cost)}",
                color = MaterialTheme.colors.onBackground,
                fontSize = 16.sp,
                fontFamily = CustomFont,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}

private fun convertToDecimal(amount: Double): String {
    val decimalFormat = DecimalFormat("#.00")
    decimalFormat.maximumFractionDigits = amount.toString().length
    return decimalFormat.format(amount)
}
