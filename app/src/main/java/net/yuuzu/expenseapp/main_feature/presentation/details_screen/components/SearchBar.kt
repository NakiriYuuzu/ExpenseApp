package net.yuuzu.expenseapp.main_feature.presentation.details_screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.yuuzu.expenseapp.ui.theme.CustomFont

@Composable
fun SearchBar(
    searchString: String,
    searchHint: String,
    textStyle: TextStyle = TextStyle(fontFamily = CustomFont, fontWeight = FontWeight.SemiBold),
    fontSize: Int = 16,
    onValueChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    shape: RoundedCornerShape = RoundedCornerShape(50.dp),
    modifier: Modifier = Modifier,
) {
    TextField(
        value = searchString,
        shape = shape,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {

            }
        ),
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (searchString.isNotEmpty()) {
                IconButton(onClick = onCloseClick) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = searchHint,
                style = textStyle,
                fontSize = fontSize.sp
            )
        },
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}