package com.reversecurrent.tennistracker.views.analytics

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.ui.theme.TennisTrackerTheme

const val LABEL_VIEW_TENNIS_STATS_BASIC = "View Tennis Stats Basic"
class MainAnalyticsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnalyticsScreenLayout()
                }
            }
        }
    }
}

@Preview
@Composable
fun AnalyticsScreenLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AddButton(LABEL_VIEW_TENNIS_STATS_BASIC, Icons.Default.Info)
    }
}

@Composable
fun AddButton(text: String, icon: ImageVector) {
    val context = LocalContext.current
    Button(
        onClick = {
            when(text){
                LABEL_VIEW_TENNIS_STATS_BASIC -> navigateToViewTennisStatsBasicActivity(context=context)
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

fun navigateToViewTennisStatsBasicActivity(context: android.content.Context) {
    val intent = Intent(context, TennisStatsBasicActivity::class.java)
    context.startActivity(intent)

}