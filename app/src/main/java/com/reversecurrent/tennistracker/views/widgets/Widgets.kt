package com.reversecurrent.tennistracker.views.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TextWidget(label: String, value: String) {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(label, fontWeight = FontWeight.Bold)
        Text(value)
    }
}

@Composable
fun CheckboxWidget(label: String, value: Boolean) {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(label, fontWeight = FontWeight.Bold)
        Checkbox(
            checked = value,
            onCheckedChange = null, // No action on checkbox change
            enabled = false // Read-only checkbox
        )
    }
}