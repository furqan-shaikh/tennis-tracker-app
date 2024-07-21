package com.reversecurrent.tennistracker.views.sessions

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
import com.reversecurrent.tennistracker.models.Player
import com.reversecurrent.tennistracker.models.Session
import com.reversecurrent.tennistracker.models.SessionSummary
import com.reversecurrent.tennistracker.models.Venue
import com.reversecurrent.tennistracker.utils.listToString
import com.reversecurrent.tennistracker.views.widgets.TextWidget

const val SESSION_SUMMARY_CARD_DATE_LABEL = "Session Date: "
const val SESSION_SUMMARY_CARD_PERIOD_LABEL = "Session Period: "
const val SESSION_SUMMARY_CARD_DURATION_LABEL = "Session Duration: "
const val SESSION_SUMMARY_CARD_PLAYERS_LABEL = "Played With: "

@Composable
fun SessionSummaryCardLayout(sessionSummary: SessionSummary, context: Context,
                             onEditSessionClick: (Context, Long) -> Unit,
                             onDeleteSessionClick: (Context, Long) -> Unit) {
    Column{
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            SessionSummaryCardContent(sessionSummary = sessionSummary)
        }
        SessionActionButtons(sessionSummary = sessionSummary, context = context,
            onEditSessionClick = onEditSessionClick, onDeleteSessionClick = onDeleteSessionClick)
    }
}


@Composable
fun SessionSummaryCardContent(sessionSummary: SessionSummary) {
    TextWidget(label = SESSION_SUMMARY_CARD_DATE_LABEL, value =sessionSummary.sessionDate)
    TextWidget(label = SESSION_SUMMARY_CARD_PERIOD_LABEL, value = sessionSummary.sessionPeriod)
    TextWidget(label = SESSION_SUMMARY_CARD_DURATION_LABEL, value = "${sessionSummary.sessionDuration} hours")
    TextWidget(label = SESSION_SUMMARY_CARD_PLAYERS_LABEL, value = listToString(sessionSummary.players))
}

@Composable
fun SessionActionButtons(sessionSummary: SessionSummary, context: Context,
                         onEditSessionClick: (Context, Long) -> Unit,
                         onDeleteSessionClick: (Context, Long) -> Unit) {
    Row {
        Button(
            onClick = {
                onEditSessionClick(context, sessionSummary.sessionId)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = null) // Display icon
        }
        Button(
            onClick = {
                onDeleteSessionClick(context, sessionSummary.sessionId)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.Default.Delete, contentDescription = null) // Display icon
        }
    }
}

