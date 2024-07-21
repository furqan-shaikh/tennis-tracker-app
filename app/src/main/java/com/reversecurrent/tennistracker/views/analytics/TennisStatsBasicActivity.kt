package com.reversecurrent.tennistracker.views.analytics

import android.os.Bundle
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
import com.reversecurrent.tennistracker.models.TennisAnalytics
import com.reversecurrent.tennistracker.repository.TennisAnalyticsRepository
import com.reversecurrent.tennistracker.ui.theme.TennisTrackerTheme
import com.reversecurrent.tennistracker.views.widgets.TextWidget
import kotlinx.coroutines.runBlocking

const val LABEL_TOTAL_HOURS_PLAYED = "Total Hours Played: "
const val LABEL_TOTAL_STEPS_TAKEN = "Total Steps Taken: "
const val LABEL_TOTAL_SHOTS_PLAYED = "Total Shots Played: "

class TennisStatsBasicActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TennisStatsBasicLayout()
                }
            }
        }
    }
}

@Preview
@Composable
fun TennisStatsBasicLayout() {
    val context = LocalContext.current
    var tennisStats by remember { mutableStateOf(TennisAnalytics()) }
    LaunchedEffect(Unit) {
        runBlocking {
            // Fetch the tennis stats asynchronously
            tennisStats = TennisAnalyticsRepository().getTennisAnalytics(context=context)
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
        Column {
            TextWidget(label = LABEL_TOTAL_HOURS_PLAYED, value = tennisStats.totalHoursPlayed.toString())
            TextWidget(label = LABEL_TOTAL_STEPS_TAKEN, value = tennisStats.totalStepsTaken.toString())
            TextWidget(label = LABEL_TOTAL_SHOTS_PLAYED, value = tennisStats.totalShotsPlayed.toString())
        }
    }
}