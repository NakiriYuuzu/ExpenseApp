package net.yuuzu.expenseapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import net.yuuzu.expenseapp.R

// Set of Material typography styles to start with
val Typography = Typography(

)

val CustomFont = FontFamily(
    Font(R.font.inter_light, FontWeight.Light),
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_black, FontWeight.Black),
    Font(R.font.inter_extralight, FontWeight.ExtraLight),
    Font(R.font.inter_extrabold, FontWeight.ExtraBold),
    Font(R.font.inter_thin, FontWeight.Thin),
)