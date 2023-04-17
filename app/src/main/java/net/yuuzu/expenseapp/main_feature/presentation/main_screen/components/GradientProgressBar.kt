package net.yuuzu.expenseapp.main_feature.presentation.main_screen.components

import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun GradientProgressbar(
    indicatorHeight: Dp = 20.dp,
    backgroundIndicatorColor: Color = Color.LightGray.copy(alpha = 0.75f),
    indicatorPadding: Dp = 16.dp,
    gradientColors: List<Color> = listOf(
        Color(0xFF40C7D7),
        Color(0xFF40C7D7),
        Color(0xff81deea),
        Color(0xff81deea)
    ),
    warningGradientColor: List<Color> = listOf(
        Color(0xFFE06C83),
        Color(0xFFE74061),
        Color(0xFFE06C83),
        Color(0xFFE74061)
    ),
    animationDuration: Int = 1000,
    animationDelay: Int = 0,
    remainingCost: Double,
    budgetCost: Double,
) {

    val calculateValue = if (remainingCost > budgetCost) 100f else if (remainingCost <= 0) 0f else (remainingCost / budgetCost) * 100
    val targetValue = calculateValue.toFloat()

    val animateNumber = animateFloatAsState(
        targetValue = targetValue,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animationDelay
        )
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(indicatorHeight)
            .padding(start = indicatorPadding, end = indicatorPadding)
    ) {

        // Background indicator
        drawLine(
            color = backgroundIndicatorColor,
            cap = StrokeCap.Round,
            strokeWidth = size.height,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = size.width, y = 0f)
        )

        // Convert the downloaded percentage into progress (width of foreground indicator)
        val progress = (animateNumber.value / 100) * size.width
        val color = if (targetValue < 30) warningGradientColor else gradientColors
        val paint = Paint().apply {
            this.color = Color.White.toArgb()
            textAlign = Paint.Align.LEFT
            textSize = 40f
        }

        // Foreground indicator
        drawLine(
            brush = Brush.linearGradient(
                colors = color
            ),
            cap = StrokeCap.Round,
            strokeWidth = size.height,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = progress, y = 0f)
        )

        drawContext.canvas.nativeCanvas.drawText(
            "Spent: $%.2f".format(budgetCost - remainingCost),
            indicatorPadding.value,
            size.height / 4,
            paint,
        )
    }
}