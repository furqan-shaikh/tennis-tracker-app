package com.reversecurrent.tennistracker.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.ui.theme.TennisTrackerTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

class ListEntitiesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ListEntitiesScreenLayout()
                }
            }
        }
    }
}

@Preview
@Composable
fun ListEntitiesScreenLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(
                rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ListEntitiesButton("List Sessions", Icons.Default.Send)
        ListEntitiesButton("List Players", Icons.Default.Person)
        ListEntitiesButton("List Venues", Icons.Default.LocationOn)
    }
}

@Composable
fun ListEntitiesButton(text: String, icon: ImageVector) {
    val context = LocalContext.current
    Button(
        onClick = {
            when(text){
                "List Players" -> navigateToListPlayersActivity(context=context)
                "List Venues" -> navigateToListVenuesActivity(context=context)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        contentPadding = PaddingValues(16.dp),
    ) {
        Icon(icon, contentDescription = null) // Display icon
        Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
        Text(text)
    }
}

fun navigateToListPlayersActivity(context: android.content.Context) {
    val intent = Intent(context, ListPlayersActivity::class.java)
    context.startActivity(intent)
}

fun navigateToListVenuesActivity(context: android.content.Context) {
    val intent = Intent(context, ListVenuesActivity::class.java)
    context.startActivity(intent)
}
