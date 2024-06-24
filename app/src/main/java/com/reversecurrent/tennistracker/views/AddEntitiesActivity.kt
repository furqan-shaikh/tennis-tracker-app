package com.reversecurrent.tennistracker.views

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
import com.reversecurrent.tennistracker.models.VENUE_ACTION_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.VENUE_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.VenueActionEnum
import com.reversecurrent.tennistracker.models.getEmptyVenue
import com.reversecurrent.tennistracker.views.players.AddPlayerActivity
import com.reversecurrent.tennistracker.views.sessions.AddSessionActivity
import com.reversecurrent.tennistracker.views.venues.AddVenueActivity

class AddEntitiesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AddEntitiesScreenLayout()
                }
            }
        }
    }
}

@Preview
@Composable
fun AddEntitiesScreenLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AddEntitiesButton("Add Session", Icons.Default.Send)
        AddEntitiesButton("Add Player", Icons.Default.Person)
        AddEntitiesButton("Add Venue", Icons.Default.LocationOn)
    }
}

@Composable
fun AddEntitiesButton(text: String, icon: ImageVector) {
    val context = LocalContext.current
    Button(
        onClick = {
            when(text){
                "Add Player" -> navigateToAddPlayerActivity(context=context)
                "Add Venue" -> navigateToAddVenueActivity(context=context)
                "Add Session" -> navigateToAddSessionActivity(context = context)
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

fun navigateToAddPlayerActivity(context: android.content.Context) {
    val intent = Intent(context, AddPlayerActivity::class.java)
    context.startActivity(intent)
}
fun navigateToAddVenueActivity(context: android.content.Context) {
    val intent = Intent(context, AddVenueActivity::class.java)
    intent.putExtra(VENUE_INTENT_EXTRA, getEmptyVenue())
    intent.putExtra(VENUE_ACTION_INTENT_EXTRA, VenueActionEnum.ADD)
    context.startActivity(intent)
}
fun navigateToAddSessionActivity(context: android.content.Context) {
    val intent = Intent(context, AddSessionActivity::class.java)
    context.startActivity(intent)
}
