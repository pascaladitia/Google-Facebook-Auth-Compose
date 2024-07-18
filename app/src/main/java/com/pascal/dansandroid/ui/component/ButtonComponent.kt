package com.pascal.dansandroid.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pascal.dansandroid.ui.theme.DansTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Home

@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    text: String,
    isIcon: Int = 0,
    icon: ImageVector = FeatherIcons.Home,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    ElevatedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(color),
        onClick = { onClick() },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isIcon == 1) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (isIcon == 2) {
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ButtonOutlineComponent(
    modifier: Modifier = Modifier,
    text: String,
    isIcon: Int = 0,
    icon: ImageVector = Icons.Default.Home,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    OutlinedIconButton(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        border = BorderStroke(1.dp, color),
        shape = RoundedCornerShape(16.dp),
        onClick = { onClick() },
    ) {
        Row {
            if (isIcon == 1) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = color
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = color,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (isIcon == 2) {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = color
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPreview() {
    DansTheme {
        ButtonComponent(text = "Masuk") {}
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonOutlinePreview() {
    DansTheme {
        ButtonOutlineComponent(text = "Keluar") {}
    }
}