package com.example.composeday3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Appearance",
            style = MaterialTheme.typography.headlineMedium
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dark Mode")

            Switch(
                checked = isDarkMode,
                onCheckedChange = { onToggleDarkMode() }
            )
        }
    }
}
