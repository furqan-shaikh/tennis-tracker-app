package com.reversecurrent.tennistracker.views

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.models.Player
import com.reversecurrent.tennistracker.models.PlayerPayment
import com.reversecurrent.tennistracker.models.SELF_PLAYER
import com.reversecurrent.tennistracker.models.Session
import com.reversecurrent.tennistracker.models.Venue
import com.reversecurrent.tennistracker.models.getEmptyVenue
import com.reversecurrent.tennistracker.models.getPlayingFormats
import com.reversecurrent.tennistracker.models.getPlayingStructures
import com.reversecurrent.tennistracker.repository.PlayerRepository
import com.reversecurrent.tennistracker.repository.SessionRepository
import com.reversecurrent.tennistracker.repository.VenueRepository
import com.reversecurrent.tennistracker.ui.theme.TennisTrackerTheme
import com.reversecurrent.tennistracker.utils.safeStrToFloat
import com.reversecurrent.tennistracker.utils.safeStrToInt
import kotlinx.coroutines.runBlocking

// UI Labels defined here
const val SESSION_DATE_LABEL = "Session Date: yyyy-MM-dd HH:mm"
const val SESSION_DURATION_LABEL = "Session Duration: (in hours)"
const val SESSION_SELECT_PLAYERS_LABEL = "Select Players"
const val SESSION_SELECT_VENUE_LABEL = "Select Venue"
const val SESSION_COST_LABEL = "Session Total Cost"
const val SESSION_SELECT_QOS_LABEL = "Select Quality of Tennis"
const val SESSION_SELECT_PLAYING_FORMAT_LABEL ="Select Playing Format"
const val SESSION_SELECT_PLAYING_STRUCTURE_LABEL ="Select Playing Structure"
const val SESSION_WASHED_OUT_LABEL = "Is Washed Out?"
const val SESSION_NUMBER_OF_STEPS_LABEL = "Number of Steps"
const val SESSION_NOTES_LABEL = "How did the session go?"
const val SESSION_REACHED_ON_TIME_LABEL = "Reached on Time?"
const val SESSION_NUMBER_OF_SHOTS_PLAYED = "Total Shots Played"
const val SESSION_ADD_SESSION_BUTTON_LABEL = "Add Session"
const val SESSION_BOOKED_BY_LABEL = "Booked By"
const val SESSION_PLAYER_PAY_AMOUNT_LABEL = "Amount Due"
const val SESSION_PLAYER_PAY_HAS_PAID_LABEL = "Has Paid"


class AddSessionActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AddSessionFormLayout()
                }
            }
        }
    }

    @Composable
    fun PlayerPaymentLayout(playerPayment: PlayerPayment) {
        var playerName by remember { mutableStateOf(playerPayment.playerName) }
        var amountDue by remember { mutableFloatStateOf(playerPayment.paymentAmount) }
        var hasPaid by remember { mutableStateOf(playerPayment.hasPaid) }
        Column()
        {
            OutlinedTextField(
                label = { Text(text = NAME_LABEL)},
                value = playerName,
                onValueChange = { text -> playerName = text})
            OutlinedTextField(
                label = { Text(text = SESSION_PLAYER_PAY_AMOUNT_LABEL)},
                value = amountDue.toString(),
                onValueChange = { text -> amountDue = safeStrToFloat(text)})
            OutlinedTextField(
                label = { Text(text = NAME_LABEL)},
                value = playerName,
                onValueChange = { text -> playerName = text})
            Row (
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                Checkbox(
                    checked = hasPaid,
                    onCheckedChange = { isChecked -> hasPaid = isChecked})
                Spacer(Modifier.size(6.dp))
                Text(text = SESSION_PLAYER_PAY_HAS_PAID_LABEL)
            }

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun AddSessionFormLayout() {
        val context = LocalContext.current

        // define state for each form element
        var sessionDate by remember { mutableStateOf("") }
        var sessionDuration by remember { mutableFloatStateOf(0.0F) }
        var players by remember { mutableStateOf(emptyList<Player>()) }
        var selectedPlayers by remember { mutableStateOf(emptyList<Player>()) }
        var isPlayersExpanded by remember { mutableStateOf(false) }
        var venues by remember { mutableStateOf(emptyList<Venue>()) }
        var isVenuesExpanded by remember { mutableStateOf(false) }
        var selectedVenue by remember { mutableStateOf(getEmptyVenue()) }
        var sessionQualityOfTennis by remember { mutableStateOf("") }
        var isQualityOfTennisExpanded by remember { mutableStateOf(false) }
        var selectedPlayingFormats by remember { mutableStateOf(emptyList<String>()) }
        var isPlayingFormatsExpanded by remember { mutableStateOf(false) }
        var sessionCost by remember { mutableFloatStateOf(0.0F) }
        var sessionNotes by remember { mutableStateOf("") }
        var selectedPlayingStructures by remember { mutableStateOf(emptyList<String>()) }
        var isPlayingStructuresExpanded by remember { mutableStateOf(false) }
        var sessionWashedOut by remember { mutableStateOf(false) }
        var sessionNumberOfSteps by remember { mutableIntStateOf(0) }
        var sessionReachedOnTime by remember { mutableStateOf(false) }
        var sessionTotalShotsPlayed by remember { mutableIntStateOf(0) }
        var bookedBy by remember { mutableStateOf("") }
        var isBookedBySelf by remember { mutableStateOf(false) }
        var isBookedByExpanded by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            runBlocking {
                // Fetch the list of players asynchronously
                players = PlayerRepository().getAllPlayers(context = context)
                venues = VenueRepository().getAllVenues(context = context)
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
            // Venue Name
            OutlinedTextField(
                label = { Text(text = SESSION_DATE_LABEL) },
                value = sessionDate,
                onValueChange = { text -> sessionDate = text })
            // Session Duration in hours
            OutlinedTextField(
                label = { Text(text = SESSION_DURATION_LABEL) },
                value = sessionDuration.toString(),
                onValueChange = { text -> sessionDuration = safeStrToFloat(text) })

            // Select Players
            Column {
                Text(text = SESSION_SELECT_PLAYERS_LABEL)
                ExposedDropdownMenuBox(
                    expanded = isPlayersExpanded,
                    onExpandedChange = { isPlayersExpanded = !isPlayersExpanded }
                ) {

                    TextField(
                        value = getSelectedPlayerNames(selectedPlayers = selectedPlayers),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPlayersExpanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isPlayersExpanded,
                        onDismissRequest = { isPlayersExpanded = false }
                    ) {
                        players.forEach { player ->
                            Row {
                                DropdownMenuItem(
                                    text = { Text(text = player.displayName) },
                                    onClick = {
                                        selectedPlayers = if (selectedPlayers.contains(player)) {
                                            selectedPlayers - player
                                        } else {
                                            selectedPlayers + player
                                        }
                                    }
                                )
                                Checkbox(
                                    checked = selectedPlayers.contains(player),
                                    onCheckedChange = null
                                )
                            }

                        }
                    }
                }
            }

            // Select Venue
            Column {
                Text(text = SESSION_SELECT_VENUE_LABEL)
                ExposedDropdownMenuBox(
                    expanded = isVenuesExpanded,
                    onExpandedChange = { isVenuesExpanded = !isVenuesExpanded }
                ) {

                    TextField(
                        value = selectedVenue.venueName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isVenuesExpanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isVenuesExpanded,
                        onDismissRequest = { isVenuesExpanded = false }
                    ) {
                        venues.forEach { venue ->
                            Row {
                                DropdownMenuItem(
                                    text = { Text(text = venue.venueName) },
                                    onClick = {
                                        selectedVenue = venue
                                        isVenuesExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Column {
                Text(text = SESSION_SELECT_QOS_LABEL)
                ExposedDropdownMenuBox(
                    expanded = isQualityOfTennisExpanded,
                    onExpandedChange = { isQualityOfTennisExpanded = !isQualityOfTennisExpanded }
                ) {
                    TextField(
                        value = sessionQualityOfTennis,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isQualityOfTennisExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isQualityOfTennisExpanded,
                        onDismissRequest = { isQualityOfTennisExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Low") },
                            onClick = {
                                sessionQualityOfTennis = "Low"
                                isQualityOfTennisExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Medium") },
                            onClick = {
                                sessionQualityOfTennis = "Medium"
                                isQualityOfTennisExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("High") },
                            onClick = {
                                sessionQualityOfTennis = "High"
                                isQualityOfTennisExpanded = false
                            }
                        )
                    }
                }
            }

            // Playing Formats: Singles, Doubles, Three
            Column {
                Text(text = SESSION_SELECT_PLAYING_FORMAT_LABEL)
                ExposedDropdownMenuBox(
                    expanded = isPlayingFormatsExpanded,
                    onExpandedChange = { isPlayingFormatsExpanded = !isPlayingFormatsExpanded }
                ) {

                    TextField(
                        value = getSelectedPlayingFormats(selectedPlayingFormats = selectedPlayingFormats),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPlayingFormatsExpanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isPlayingFormatsExpanded,
                        onDismissRequest = { isPlayingFormatsExpanded = false }
                    ) {
                        getPlayingFormats().forEach { playingFormat ->
                            Row {
                                DropdownMenuItem(
                                    text = { Text(text = playingFormat) },
                                    onClick = {
                                        selectedPlayingFormats = if (selectedPlayingFormats.contains(playingFormat)) {
                                            selectedPlayingFormats - playingFormat
                                        } else {
                                            selectedPlayingFormats + playingFormat
                                        }
                                    }
                                )
                                Checkbox(
                                    checked = selectedPlayingFormats.contains(playingFormat),
                                    onCheckedChange = null
                                )
                            }

                        }
                    }
                }
            }

            // Playing Structures: Rally, Set, Both
            Column {
                Text(text = SESSION_SELECT_PLAYING_STRUCTURE_LABEL)
                ExposedDropdownMenuBox(
                    expanded = isPlayingStructuresExpanded,
                    onExpandedChange = { isPlayingStructuresExpanded = !isPlayingStructuresExpanded }
                ) {

                    TextField(
                        value = getSelectedPlayingStructures(selectedPlayingStructures = selectedPlayingStructures),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPlayingStructuresExpanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isPlayingStructuresExpanded,
                        onDismissRequest = { isPlayingStructuresExpanded = false }
                    ) {
                        getPlayingStructures().forEach { playingStructure ->
                            Row {
                                DropdownMenuItem(
                                    text = { Text(text = playingStructure) },
                                    onClick = {
                                        selectedPlayingStructures = if (selectedPlayingFormats.contains(playingStructure)) {
                                            selectedPlayingStructures - playingStructure
                                        } else {
                                            selectedPlayingStructures + playingStructure
                                        }
                                    }
                                )
                                Checkbox(
                                    checked = selectedPlayingStructures.contains(playingStructure),
                                    onCheckedChange = null
                                )
                            }

                        }
                    }
                }
            }
            // Session Cost
            OutlinedTextField(
                label = { Text(text = SESSION_COST_LABEL) },
                value = sessionCost.toString(),
                onValueChange = { text -> sessionCost = safeStrToFloat(text)},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            // Session Number of Steps
            OutlinedTextField(
                label = { Text(text = SESSION_NUMBER_OF_STEPS_LABEL) },
                value = sessionNumberOfSteps.toString(),
                onValueChange = { text -> sessionNumberOfSteps = safeStrToInt(text)},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            // Session Total Number of Shots Played
            OutlinedTextField(
                label = { Text(text = SESSION_NUMBER_OF_SHOTS_PLAYED) },
                value = sessionTotalShotsPlayed.toString(),
                onValueChange = { text -> sessionTotalShotsPlayed = safeStrToInt(text)},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            // Session Reached on Time?
            Row (
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                Checkbox(
                    checked = sessionReachedOnTime,
                    onCheckedChange = { isChecked -> sessionReachedOnTime = isChecked})
                Spacer(Modifier.size(6.dp))
                Text(text = SESSION_REACHED_ON_TIME_LABEL)
            }
            // Session Washed Out?
            Row (
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                Checkbox(
                    checked = sessionWashedOut,
                    onCheckedChange = { isChecked -> sessionWashedOut = isChecked})
                Spacer(Modifier.size(6.dp))
                Text(text = SESSION_WASHED_OUT_LABEL)
            }

            // Booked by
            Column {
                Text(text = SESSION_BOOKED_BY_LABEL)
                ExposedDropdownMenuBox(
                    expanded = isBookedByExpanded,
                    onExpandedChange = { isBookedByExpanded = !isBookedByExpanded }
                ) {

                    TextField(
                        value = bookedBy,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isBookedByExpanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isBookedByExpanded,
                        onDismissRequest = { isBookedByExpanded = false }
                    ) {
                        players.forEach { player ->
                            Row {
                                DropdownMenuItem(
                                    text = { Text(text = player.displayName) },
                                    onClick = {
                                        bookedBy = player.displayName
                                        isBookedByExpanded = false
                                        isBookedBySelf = player.displayName == SELF_PLAYER
                                    }
                                )
                            }
                        }
                    }
                }
            }
            OutlinedTextField(
                label = { Text(text = SESSION_NOTES_LABEL) },
                value = sessionNotes,
                onValueChange = { text -> sessionNotes = text},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)))
            Button(
                onClick = {
                    Log.i("AddSessionActivity", "add clicked")
                    // create Session object
                    val session = Session(
                        sessionDate = sessionDate,
                        sessionDuration = sessionDuration,
                        sessionPlayingFormat = getSelectedPlayingFormats(selectedPlayingFormats = selectedPlayingFormats),
                        sessionPlayingStructure = getSelectedPlayingStructures(selectedPlayingStructures = selectedPlayingStructures),
                        sessionCost = sessionCost,
                        sessionNotes = sessionNotes,
                        sessionWashedOut = sessionWashedOut,
                        sessionNumberOfSteps = sessionNumberOfSteps,
                        sessionQualityOfTennis = sessionQualityOfTennis,
                        sessionReachedOnTime = sessionReachedOnTime,
                        sessionNumberOfShotsPlayed = sessionTotalShotsPlayed,
                        venueId = selectedVenue.venueId,
                        players = selectedPlayers.map { player: Player -> player.id }.toList()
                    )

                    Log.i("AddSessionActivity", session.toString())
                    runBlocking {
                        SessionRepository().insertSession(context = context, session = session)
                        Toast.makeText(context, "Successfully added ${session.sessionDate}", Toast.LENGTH_LONG).show()
                        navigateToParentActivity(context = context)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                contentPadding = PaddingValues(16.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = null) // Display icon
                Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
                Text(SESSION_ADD_SESSION_BUTTON_LABEL)
            }
        }
    }

    private fun getSelectedPlayerNames(selectedPlayers: List<Player>): String {
        return selectedPlayers.joinToString { selectedPlayer -> selectedPlayer.displayName }
    }

    private fun getSelectedPlayingFormats(selectedPlayingFormats: List<String>): String {
        return selectedPlayingFormats.joinToString { selectedPlayingFormat -> selectedPlayingFormat }
    }

    private fun getSelectedPlayingStructures(selectedPlayingStructures: List<String>): String {
        return selectedPlayingStructures.joinToString { selectedPlayingStructure -> selectedPlayingStructure }
    }
}