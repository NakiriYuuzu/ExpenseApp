package net.yuuzu.expenseapp.main_feature.presentation.main_screen.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.yuuzu.expenseapp.ui.theme.CustomFont

@Composable
fun CardOfSpendAndBudget(
    title: String,
    remainingCost: Float,
    budgetCost: Float,
    backgroundColor: Color = MaterialTheme.colors.surface,
    shape: RoundedCornerShape = RoundedCornerShape(25.dp)
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(0.dp, shape = shape)
            .clip(shape = shape)
            .background(
                color = backgroundColor,
                shape = shape
            )
            .animateContentSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                color = Color.Gray,
                fontSize = 20.sp,
                fontFamily = CustomFont,
                fontWeight = FontWeight.SemiBold,
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "$%.2f".format(remainingCost),
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 28.sp,
                    fontFamily = CustomFont,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "/$%.2f".format(budgetCost),
                    color = Color.Gray,
                    fontSize = 20.sp,
                    fontFamily = CustomFont,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            GradientProgressbar(
                remainingCost = remainingCost,
                budgetCost = budgetCost,
            )
        }
    }
}