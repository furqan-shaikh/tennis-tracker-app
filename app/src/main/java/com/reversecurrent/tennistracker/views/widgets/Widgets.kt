package com.reversecurrent.tennistracker.views.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reversecurrent.tennistracker.views.sessions.SESSION_SELECT_VENUE_LABEL

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
fun ClickableTextWidget(label: String, value: String, id: String, onClick: (id: String) -> Unit) {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(label, fontWeight = FontWeight.Bold)
        Text(value,
            modifier = Modifier.clickable { onClick(id) },
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleSelectDropDown(label: String, options: List<String>, value: String, isExpanded: Boolean=false,
                         onValueChange: (String, Boolean) -> Unit,
                         onExpandedChange: (Boolean) -> Unit,
                         onDismissRequest: (Boolean) -> Unit) {
    Column {
        Text(text = label)
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { onExpandedChange(it) }
        ) {

            TextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { onDismissRequest(false) }
            ) {
                options.forEach { value ->
                    Row {
                        DropdownMenuItem(
                            text = { Text(text = value) },
                            onClick = {
                                onValueChange(value, false)
                            }
                        )
                    }
                }
            }
        }
    }
}