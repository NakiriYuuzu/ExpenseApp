package net.yuuzu.expenseapp.main_feature.presentation.util

import android.annotation.SuppressLint
import androidx.compose.ui.graphics.Color
import net.yuuzu.expenseapp.main_feature.domain.model.InvalidExpenseException
import net.yuuzu.expenseapp.ui.theme.SeedColor
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

@SuppressLint("SimpleDateFormat")
fun convertToDateString(timestamp: Long): LocalDate {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return LocalDate.parse(formatter.format(date))
}

fun convertToTimestamp(localDate: String): Long {
    val currentDate = LocalDate.parse(localDate).atTime(LocalTime.now())
    val timeZone = ZoneId.systemDefault()
    val instant = currentDate.atZone(timeZone).toInstant()
    return instant.toEpochMilli()
}

fun convertToColor(strColor: String?): Color {
    if (strColor == null) return SeedColor
    return Color(android.graphics.Color.parseColor(strColor))
}

fun convertToDouble(amount: String): Double {
    return amount.toDoubleOrNull() ?: throw InvalidExpenseException("Invalid amount.")
}

fun convertToDecimal(amount: Double): String {
    return if (amount > 9999999) {
        val decimalFormat = DecimalFormat("#")
        decimalFormat.maximumFractionDigits = amount.toString().length

        decimalFormat.format(amount)
    } else {
        amount.toString()
    }
}