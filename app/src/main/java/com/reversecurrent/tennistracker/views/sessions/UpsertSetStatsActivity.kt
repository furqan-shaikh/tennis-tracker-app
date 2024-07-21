package com.reversecurrent.tennistracker.views.sessions

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.models.Player
import com.reversecurrent.tennistracker.models.SESSION_SET_STATS_ACTION_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.SESSION_SET_STATS_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.SessionSetStatsActionEnum
import com.reversecurrent.tennistracker.models.SetStats
import com.reversecurrent.tennistracker.models.SetStatsSession
import com.reversecurrent.tennistracker.models.getEmptyPlayer
import com.reversecurrent.tennistracker.repository.PlayerRepository
import com.reversecurrent.tennistracker.repository.SessionSetStatsRepository
import com.reversecurrent.tennistracker.ui.theme.TennisTrackerTheme
import com.reversecurrent.tennistracker.utils.safeStrToInt
import com.reversecurrent.tennistracker.views.widgets.TextWidget
import kotlinx.coroutines.runBlocking

const val SESSION_DISPLAY_NAME_LABEL = "Session: "
const val SESSION_SET_WON_BY_LABEL = "Won By: "
const val SESSION_SET_SCORE_LABEL = "Set Score: "
const val SESSION_ACES_LABEL = "Aces: "
const val SESSION_WINNERS_LABEL = "Winners: "
const val SESSION_DOUBLE_FAULTS_LABEL = "Double Faults: "


class UpsertSetStatsActivity: ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sessionSetStatsIntent: SetStatsSession? = intent.getParcelableExtra(
            SESSION_SET_STATS_INTENT_EXTRA
        )
        val sessionSetStatsActionEnum: SessionSetStatsActionEnum? = intent.getSerializableExtra(
            SESSION_SET_STATS_ACTION_INTENT_EXTRA
        ) as? SessionSetStatsActionEnum
        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (sessionSetStatsIntent != null && sessionSetStatsActionEnum != null) {
                        UpsertSetStatsLayout(sessionSetStatsIntent, sessionSetStatsActionEnum)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertSetStatsLayout(sessionSetStatsIntent: SetStatsSession, sessionSetStatsActionEnum: SessionSetStatsActionEnum) {
    val context = LocalContext.current

    // define state
    var players by remember { mutableStateOf(emptyList<Player>()) }

    var set1Score by remember { mutableStateOf("") }
    var set1WonBy by remember { mutableStateOf(getEmptyPlayer()) }
    var set1WonByIsExpanded by remember { mutableStateOf(false) }
    var set1Aces by remember { mutableStateOf("0") }
    var set1Winners by remember { mutableStateOf("0") }
    var set1DoubleFaults by remember { mutableStateOf("0") }

    var set2Score by remember { mutableStateOf("") }
    var set2WonBy by remember { mutableStateOf(getEmptyPlayer()) }
    var set2WonByIsExpanded by remember { mutableStateOf(false) }
    var set2Aces by remember { mutableStateOf("0") }
    var set2Winners by remember { mutableStateOf("0") }
    var set2DoubleFaults by remember { mutableStateOf("0") }

//    var set3Score by remember { mutableStateOf("") }
//    var set3WonBy by remember { mutableIntStateOf(0) }
//    var set3WonByIsExpanded by remember { mutableStateOf(false) }
//    var set3Aces by remember { mutableIntStateOf(0) }
//    var set3Winners by remember { mutableIntStateOf(0) }
//    var set3DoubleFaults by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        runBlocking {
            // Fetch the list of players asynchronously
            players = PlayerRepository().getAllPlayers(context = context)
            if (sessionSetStatsActionEnum == SessionSetStatsActionEnum.EDIT) {
                if (sessionSetStatsIntent.sessionSetStats.size == 2) {
                    set1Score = sessionSetStatsIntent.sessionSetStats[0].setScore
                    set1WonBy = players.find { it.id == sessionSetStatsIntent.sessionSetStats[0].playerId } ?: getEmptyPlayer()
                    set1Aces = sessionSetStatsIntent.sessionSetStats[0].setAces.toString()
                    set1Winners = sessionSetStatsIntent.sessionSetStats[0].setWinners.toString()
                    set1DoubleFaults = sessionSetStatsIntent.sessionSetStats[0].setDoubleFaults.toString()

                    set2Score = sessionSetStatsIntent.sessionSetStats[1].setScore
                    set2WonBy = players.find { it.id == sessionSetStatsIntent.sessionSetStats[1].playerId } ?: getEmptyPlayer()
                    set2Aces = sessionSetStatsIntent.sessionSetStats[1].setAces.toString()
                    set2Winners = sessionSetStatsIntent.sessionSetStats[1].setWinners.toString()
                    set2DoubleFaults = sessionSetStatsIntent.sessionSetStats[1].setDoubleFaults.toString()
                }
                if (sessionSetStatsIntent.sessionSetStats.size == 1) {
                    set1Score = sessionSetStatsIntent.sessionSetStats[0].setScore
                    set1WonBy = players.find { it.id == sessionSetStatsIntent.sessionSetStats[0].playerId } ?: getEmptyPlayer()
                    set1Aces = sessionSetStatsIntent.sessionSetStats[0].setAces.toString()
                    set1Winners = sessionSetStatsIntent.sessionSetStats[0].setWinners.toString()
                    set1DoubleFaults = sessionSetStatsIntent.sessionSetStats[0].setDoubleFaults.toString()
                }
            }
        }
    }

    // define the UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextWidget(label = SESSION_DISPLAY_NAME_LABEL, value = sessionSetStatsIntent.sessionDisplayName)

        // Set 1
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            // Set Score
            OutlinedTextField(
                label = { Text(text = "$SESSION_SET_SCORE_LABEL  Set 1") },
                value = set1Score,
                onValueChange = {text -> set1Score = text },
            )
            // Won by
            Column {
                Text(text = SESSION_SET_WON_BY_LABEL)
                ExposedDropdownMenuBox(
                    expanded = set1WonByIsExpanded,
                    onExpandedChange = { set1WonByIsExpanded = !set1WonByIsExpanded }
                ) {

                    TextField(
                        value = set1WonBy.displayName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = set1WonByIsExpanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = set1WonByIsExpanded,
                        onDismissRequest = { set1WonByIsExpanded = false }
                    ) {
                        players.forEach { player ->
                            Row {
                                DropdownMenuItem(
                                    text = { Text(text = player.displayName) },
                                    onClick = {
                                        set1WonBy = player
                                        set1WonByIsExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            // Aces
            OutlinedTextField(
                label = { Text(text = "$SESSION_ACES_LABEL  Set 1") },
                value = set1Aces,
                onValueChange = {text -> set1Aces = text },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            // Winners
            OutlinedTextField(
                label = { Text(text = "$SESSION_WINNERS_LABEL  Set 1") },
                value = set1Winners,
                onValueChange = {text -> set1Winners = text },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            // Double Faults
            OutlinedTextField(
                label = { Text(text = "$SESSION_DOUBLE_FAULTS_LABEL  Set 1") },
                value = set1DoubleFaults,
                onValueChange = {text -> set1DoubleFaults = text },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        // Set 2
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            // Set Score
            OutlinedTextField(
                label = { Text(text = "$SESSION_SET_SCORE_LABEL  Set 2") },
                value = set2Score,
                onValueChange = { text -> set2Score = text },
            )
            // Won by
            Column {
                Text(text = SESSION_SET_WON_BY_LABEL)
                ExposedDropdownMenuBox(
                    expanded = set2WonByIsExpanded,
                    onExpandedChange = { set2WonByIsExpanded = !set2WonByIsExpanded }
                ) {

                    TextField(
                        value = set2WonBy.displayName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = set2WonByIsExpanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = set2WonByIsExpanded,
                        onDismissRequest = { set2WonByIsExpanded = false }
                    ) {
                        players.forEach { player ->
                            Row {
                                DropdownMenuItem(
                                    text = { Text(text = player.displayName) },
                                    onClick = {
                                        set2WonBy = player
                                        set2WonByIsExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            // Aces
            OutlinedTextField(
                label = { Text(text = "$SESSION_ACES_LABEL  Set 2") },
                value = set2Aces,
                onValueChange = { text -> set2Aces = text },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            // Winners
            OutlinedTextField(
                label = { Text(text = "$SESSION_WINNERS_LABEL  Set 2") },
                value = set2Winners,
                onValueChange = { text -> set2Winners = text },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            // Double Faults
            OutlinedTextField(
                label = { Text(text = "$SESSION_DOUBLE_FAULTS_LABEL  Set 2") },
                value = set2DoubleFaults,
                onValueChange = { text -> set2DoubleFaults = text },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Button(
            onClick = {
                runBlocking {
                    val setStatsList = mutableListOf<SetStats>()
                    // set 1
                    val set1 = SetStats(
                        setScore = set1Score,
                        sessionId = sessionSetStatsIntent.sessionId,
                        playerId = set1WonBy.id,
                        setAces = safeStrToInt(set1Aces),
                        setWinners = safeStrToInt(set1Winners),
                        setDoubleFaults = safeStrToInt(set1DoubleFaults)
                    )
                    setStatsList.add(set1)

                    // set 2
                    if(set2Score.isNotEmpty()) {
                        val set2 = SetStats(
                            setScore = set2Score,
                            sessionId = sessionSetStatsIntent.sessionId,
                            playerId = set2WonBy.id,
                            setAces = safeStrToInt(set2Aces),
                            setWinners = safeStrToInt(set2Winners),
                            setDoubleFaults = safeStrToInt(set2DoubleFaults))
                        setStatsList.add(set2)
                    }
                    try{
                        val ids = SessionSetStatsRepository().upsertBatch(context = context, sessionSetStatsList = setStatsList)
                        if (ids.isNotEmpty()) {
                            Toast.makeText(context, "Set Stats Saved", Toast.LENGTH_SHORT).show()
                            navigateToListSessionActivity(context)
                        }
                    }
                    catch (e: Exception) {
                        Log.e("UpsertSetStatsActivity", e.message.toString())
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }

                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            contentPadding = PaddingValues(16.dp),
        ){
            Icon(getSaveActionButtonIcon(sessionSetStatsActionEnum), contentDescription = null) // Display icon
            Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
            Text(getSaveActionButtonLabel(sessionSetStatsActionEnum))
        }
        Button(
            onClick = {
                navigateToListSessionActivity(context)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            contentPadding = PaddingValues(16.dp),
        ) {
            Icon(Icons.Default.Clear, contentDescription = null) // Display icon
            Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
            Text("Close")
        }
    }
}

private fun navigateToListSessionActivity(context: android.content.Context) {
    val intent = Intent(context, ListSessionsActivity::class.java)
    context.startActivity(intent)
}

fun getSaveActionButtonLabel(actionType: SessionSetStatsActionEnum): String {
    return when (actionType) {
        SessionSetStatsActionEnum.ADD -> "Add Set Stats"
        SessionSetStatsActionEnum.EDIT -> "Edit Set Stats"
    }
}

fun getSaveActionButtonIcon(actionType: SessionSetStatsActionEnum): ImageVector {
    return when (actionType) {
        SessionSetStatsActionEnum.ADD -> Icons.Default.Add
        SessionSetStatsActionEnum.EDIT -> Icons.Default.Edit
    }
}