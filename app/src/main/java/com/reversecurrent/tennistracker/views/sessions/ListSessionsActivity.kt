package com.reversecurrent.tennistracker.views.sessions


import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import com.reversecurrent.tennistracker.models.SESSION_ACTION_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.SESSION_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.Session
import com.reversecurrent.tennistracker.models.SessionActionEnum
import com.reversecurrent.tennistracker.models.SessionSummary
import com.reversecurrent.tennistracker.models.VENUE_ACTION_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.VENUE_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.Venue
import com.reversecurrent.tennistracker.models.VenueActionEnum
import com.reversecurrent.tennistracker.repository.SessionRepository
import com.reversecurrent.tennistracker.ui.theme.TennisTrackerTheme
import com.reversecurrent.tennistracker.views.venues.AddVenueActivity
import com.reversecurrent.tennistracker.views.venues.onEditVenueClick
import kotlinx.coroutines.runBlocking

class ListSessionsActivity: ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ListSessionsLayout()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ListSessionsLayout() {
    val context = LocalContext.current
    var sessionsSummary by remember { mutableStateOf(emptyList<SessionSummary>()) }


    LaunchedEffect(Unit) {
        runBlocking {
            // Fetch the list of players asynchronously
            sessionsSummary = SessionRepository().getAllSessions(context = context)
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
        sessionsSummary.forEach { sessionSummary ->
            SessionSummaryCardLayout(
                sessionSummary = sessionSummary,
                context = context,
                onEditSessionClick = { context: Context, sessionId: Long ->
                    runBlocking {
                        val session = SessionRepository().getSessionDetails(context = context, sessionId = sessionId)
                        val intent = Intent(context, AddSessionActivity::class.java)
                        intent.putExtra(SESSION_INTENT_EXTRA, session)
                        intent.putExtra(SESSION_ACTION_INTENT_EXTRA, SessionActionEnum.EDIT)
                        context.startActivity(intent)
                    }
                },
                onDeleteSessionClick = { context: Context, sessionId: Long ->
                    runBlocking {
                        val status = SessionRepository().deleteBySessionId(
                            context = context,
                            sessionId = sessionId
                        )
                        if (status) {
                            sessionsSummary = sessionsSummary.filter { it.sessionId != sessionId }
                            Toast.makeText(context, "Session $sessionId deleted", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to delete session $sessionId", Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            )
        }
    }
}
