package com.pascal.dansandroid.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pascal.dansandroid.ui.theme.DansTheme

@Composable
fun FormComponent(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    enabled: Boolean = true
) {
    Column {
        BasicTextField(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 32.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .background(Color.White, RoundedCornerShape(8.dp)),
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            textStyle = MaterialTheme.typography.bodySmall,
            enabled = enabled,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 1.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormPreview() {
    DansTheme {
        FormComponent(
            value = "test",
            onValueChange = {},
        )
    }
}