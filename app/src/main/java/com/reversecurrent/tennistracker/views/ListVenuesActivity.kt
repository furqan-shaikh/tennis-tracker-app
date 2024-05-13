package com.reversecurrent.tennistracker.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.models.VENUE_ACTION_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.VENUE_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.Venue
import com.reversecurrent.tennistracker.models.VenueActionEnum
import com.reversecurrent.tennistracker.repository.VenueRepository
import com.reversecurrent.tennistracker.ui.theme.TennisTrackerTheme
import kotlinx.coroutines.runBlocking

class ListVenuesActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ListVenuesLayout()
                }
            }
        }
    }
}

@Preview
@Composable
fun ListVenuesLayout() {
    val context = LocalContext.current
    var venues by remember { mutableStateOf(emptyList<Venue>()) }


    LaunchedEffect(Unit) {
        runBlocking {
            // Fetch the list of players asynchronously
            venues = VenueRepository().getAllVenues(context = context)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(
                rememberScrollState()
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        venues.forEach { venue ->
            VenueCardLayout(
                venue = venue,
                context = context,
                onEditVenueClick = ::onEditVenueClick,
                onDeleteVenueClick = ::onDeleteVenueClick
            )
        }
    }
}

fun onEditVenueClick(context: Context, venue: Venue) {
    Log.i("onEditVenueClick", venue.toString())
    val intent = Intent(context, AddVenueActivity::class.java)
    intent.putExtra(VENUE_INTENT_EXTRA, venue)
    intent.putExtra(VENUE_ACTION_INTENT_EXTRA, VenueActionEnum.EDIT)
    context.startActivity(intent)
}

fun onDeleteVenueClick(venue: Venue) {
    Log.i("onDeleteVenueClick", venue.toString())
    // ask for confirmation
}
