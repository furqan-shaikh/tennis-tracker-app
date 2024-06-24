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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
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
import com.reversecurrent.tennistracker.views.analytics.MainAnalyticsActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreenLayout()
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScreenLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TennisTrackerButton("Add", Icons.Default.Add, "Add")
        TennisTrackerButton("List", Icons.Default.List, "List")
        TennisTrackerButton("Analytics", Icons.Default.List, "Analytics")
    }
}

@Composable
fun TennisTrackerButton(text: String, icon: ImageVector, activity_type: String) {
    val context = LocalContext.current
    Button(
        onClick = {
            when(activity_type){
                "Add" -> navigateToAddEntitiesActivity(context=context)
                "List" -> navigateToListEntitiesActivity(context=context)
                "Analytics" -> navigateToAnalyticsActivity(context=context)
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

//@Composable
fun navigateToAddEntitiesActivity(context: android.content.Context) {
    val intent = Intent(context, AddEntitiesActivity::class.java)
    context.startActivity(intent)
}

fun navigateToListEntitiesActivity(context: android.content.Context) {
    val intent = Intent(context, ListEntitiesActivity::class.java)
    context.startActivity(intent)
}

fun navigateToAnalyticsActivity(context: android.content.Context) {
    val intent = Intent(context, MainAnalyticsActivity::class.java)
    context.startActivity(intent)
}

@Composable
fun ShowToast(text: String) {
    val context = LocalContext.current
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}
