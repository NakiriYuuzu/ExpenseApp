package net.yuuzu.expenseapp.main_feature.presentation.details_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.yuuzu.expenseapp.main_feature.data.util.CategoryColor
import net.yuuzu.expenseapp.main_feature.presentation.util.convertToColor
import net.yuuzu.expenseapp.main_feature.presentation.util.convertToDecimal
import net.yuuzu.expenseapp.ui.theme.CustomFont

@Composable
fun CategoryItem(
    categoryColor: CategoryColor,
    categorySpent: Double,
    categoryDate: String,
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
                        color = convertToColor(categoryColor.color),
                        shape = iconShape
                    )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = categoryColor.category,
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 20.sp,
                    fontFamily = CustomFont,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = categoryDate,
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontFamily = CustomFont,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Text(
                text = "$${convertToDecimal(categorySpent)}",
                color = MaterialTheme.colors.onBackground,
                fontSize = 16.sp,
                fontFamily = CustomFont,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}