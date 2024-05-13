package com.reversecurrent.tennistracker.views

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.models.Venue

const val VENUE_CARD_NAME_LABEL = "Venue Name: "
const val VENUE_MOBILE_NUMBER_LABEL = "Primary Contact Mobile: "
const val VENUE_LEVEL_LABEL = "Primary Contact Name: "

@Composable
fun VenueCardLayout(venue: Venue, context: Context, onEditVenueClick: (Context, Venue) -> Unit, onDeleteVenueClick: (Venue) -> Unit) {
    Column{
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            VenueCardContent(venue = venue)
        }
        VenueActionButtons(venue = venue, context = context, onEditVenueClick = onEditVenueClick, onDeleteVenueClick)
    }
}


@Composable
fun VenueCardContent(venue: Venue) {
    TextWidget(label = VENUE_CARD_NAME_LABEL, value = venue.venueName)
    TextWidget(label = VENUE_MOBILE_NUMBER_LABEL, value = venue.primaryContactName)
    TextWidget(label = VENUE_LEVEL_LABEL, value = venue.primaryContactNumber)
}
@Composable
fun VenueActionButtons(venue: Venue, context: Context, onEditVenueClick: (Context, Venue) -> Unit, onDeleteVenueClick: (Venue) -> Unit) {
    Row {
        Button(
            onClick = {
                onEditVenueClick(context, venue)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = null) // Display icon
        }
        Button(
            onClick = {
                onDeleteVenueClick(venue)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.Default.Delete, contentDescription = null) // Display icon
        }
    }
}

