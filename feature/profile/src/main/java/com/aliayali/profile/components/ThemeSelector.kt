package com.aliayali.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aliayali.profile.model.ProfileTheme

@Composable
fun ThemeSelector(
    selectedTheme: ProfileTheme,
    onThemeSelected: (ProfileTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    val themes = listOf(
        ProfileTheme.Default to ("پیش‌فرض" to Color(0xFF607D8B)),
        ProfileTheme.Pink to ("صورتی" to Color(0xFFE91E63)),
        ProfileTheme.Green to ("سبز" to Color(0xFF4CAF50)),
        ProfileTheme.Blue to ("آبی" to Color(0xFF2196F3)),
        ProfileTheme.Purple to ("بنفش" to Color(0xFF9C27B0))
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "رنگ و ظاهر",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "تم دلخواهت را برای برنامه انتخاب کن",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            themes.forEach { (theme, details) ->
                val (title, color) = details
                val isSelected = theme == selectedTheme

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable {
                                onThemeSelected(theme)
                            },
                        shape = CircleShape,
                        color = color,
                        border = if (isSelected) {
                            BorderStroke(
                                width = 3.dp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        } else {
                            null
                        }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "تم انتخاب شده",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        },
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}