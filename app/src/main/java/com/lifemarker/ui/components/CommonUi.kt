package com.lifemarker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.lifemarker.util.IconMapper

@Composable
fun CategoryIcon(
    iconName: String,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    if (IconMapper.isEmoji(iconName)) {
        Text(
            text = iconName,
            modifier = modifier,
            textAlign = TextAlign.Center
        )
    } else {
        val vector = IconMapper.getIconByName(iconName)
        if (vector != null) {
            Icon(
                imageVector = vector,
                contentDescription = null,
                modifier = modifier,
                tint = tint
            )
        } else {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null,
                modifier = modifier,
                tint = tint
            )
        }
    }
}
