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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerDialog
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
import androidx.compose.material3.rememberDatePickerState
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
import com.reversecurrent.tennistracker.views.players.NAME_LABEL
import kotlinx.coroutines.runBlocking
import androidx.compose.material3.DatePicker
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.vector.ImageVector
import com.reversecurrent.tennistracker.models.SESSION_ACTION_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.SESSION_DATE_FORMAT
import com.reversecurrent.tennistracker.models.SESSION_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.SESSION_SET_STATS_ACTION_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.SESSION_SET_STATS_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.SessionActionEnum
import com.reversecurrent.tennistracker.models.SessionSetStatsActionEnum
import com.reversecurrent.tennistracker.models.SetStatsSession
import com.reversecurrent.tennistracker.utils.convertMillisToLocalDate
import com.reversecurrent.tennistracker.views.widgets.SingleSelectDropDown
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


// UI Labels defined here
const val SESSION_DATE_LABEL = "Session Date: "
const val SESSION_TIME_LABEL = "Session Time: HH:MM"
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
const val SESSION_BOOKED_BY_LABEL = "Booked By"
const val SESSION_PLAYER_PAY_AMOUNT_LABEL = "Amount Due"
const val SESSION_PLAYER_PAY_HAS_PAID_LABEL = "Has Paid"
const val SESSION_PAID_TO_COURT = "Paid to Court?"
const val SESSION_PAID_TO_COURT_DATE = "Paid to Court Date: yyyy-MM-dd"
const val SESSION_PAYMENT_DATE = "Paid Date"


class AddSessionActivity: ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val session = intent.getParcelableExtra<Session>(
            SESSION_INTENT_EXTRA
        )
        val sessionActionEnum: SessionActionEnum? = intent.getSerializableExtra(
            SESSION_ACTION_INTENT_EXTRA
        ) as? SessionActionEnum
        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (session != null && sessionActionEnum != null) {
                        AddSessionFormLayout(
                            session = session,
                            sessionActionEnum = sessionActionEnum
                        )
                    }
                }
            }
        }
    }
}

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PlayerPaymentLayout(playerPayment: PlayerPayment, onChange: (playerPayment: PlayerPayment) -> Unit) {
        var playerName by remember { mutableStateOf(playerPayment.playerName) }
        var amountDue by remember { mutableStateOf(playerPayment.paymentAmount.toString()) }
        var hasPaid by remember { mutableStateOf(playerPayment.hasPaid) }
        var paymentDate by remember { mutableStateOf(playerPayment.paymentDate) }
        var showPaymentDateDialog by remember { mutableStateOf(false) }
        val paymentDateState = rememberDatePickerState()
        Column()
        {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                OutlinedTextField(
                    label = { Text(text = NAME_LABEL)},
                    value = playerName,
                    onValueChange = { text ->
                        playerName = text
                        onChange(playerPayment.copy(playerName = text))})

                OutlinedTextField(
                    label = { Text(text = SESSION_PLAYER_PAY_AMOUNT_LABEL)},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = amountDue,
                    onValueChange = { text ->
                        amountDue = text
                        onChange(playerPayment.copy(paymentAmount = safeStrToFloat(text)))})

                Row (
                verticalAlignment = Alignment.CenterVertically,
                )
                {
                    Checkbox(
                        checked = hasPaid,
                        onCheckedChange = { isChecked ->
                            hasPaid = isChecked
                            onChange(playerPayment.copy(hasPaid = isChecked))})
                    Spacer(Modifier.size(6.dp))
                    Text(text = SESSION_PLAYER_PAY_HAS_PAID_LABEL)
                }
                OutlinedTextField(
                    label = { Text(text = SESSION_PAYMENT_DATE)},
                    value = paymentDate,
                    onValueChange = { },
                    readOnly = true,
                    enabled = false
                    )
                IconButton(onClick = { showPaymentDateDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null
                    )
                }
                if (showPaymentDateDialog) {
                    DatePickerDialog(
                        onDismissRequest = { showPaymentDateDialog = false },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showPaymentDateDialog = false
                                    paymentDate = paymentDateState.selectedDateMillis?.let {
                                        convertMillisToLocalDate(it)
                                    }.toString()
                                    onChange(playerPayment.copy(paymentDate = paymentDate))
                                }
                            ) {
                                Text(text = "OK")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showPaymentDateDialog = false }
                            ) {
                                Text(text = "Cancel")
                            }
                        }
                    ) {
                        DatePicker(
                            state = paymentDateState,
                            showModeToggle = true
                        )

                    }
                }
            }
        }
    }

//    @Composable
//    fun SelfPaymentLayout(amountDue: Float, hasPaid: Boolean, paymentDate: String) {
////        var amountDue by remember { mutableFloatStateOf(playerPayment.paymentAmount) }
////        var hasPaid by remember { mutableStateOf(playerPayment.hasPaid) }
////        var paymentDate by remember { mutableStateOf(playerPayment.paymentDate) }
//        Column()
//        {
//            ElevatedCard(
//                elevation = CardDefaults.cardElevation(
//                    defaultElevation = 6.dp
//                ),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//            ) {
//                Row (
//                    verticalAlignment = Alignment.CenterVertically,
//                )
//                {
//                    Checkbox(
//                        checked = hasPaid,
//                        onCheckedChange = { isChecked -> hasPaid = isChecked})
//                    Spacer(Modifier.size(6.dp))
//                    Text(text = SESSION_PLAYER_PAY_HAS_PAID_LABEL)
//                }
//                OutlinedTextField(
//                    label = { Text(text = SESSION_PAYMENT_DATE)},
//                    value = paymentDate,
//                    onValueChange = { text -> paymentDate = text})
//
//                OutlinedTextField(
//                    label = { Text(text = SESSION_PLAYER_PAY_AMOUNT_LABEL)},
//                    value = amountDue.toString(),
//                    onValueChange = { text -> amountDue = safeStrToFloat(text)})
//            }
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddSessionFormLayout(session: Session, sessionActionEnum: SessionActionEnum) {
        val context = LocalContext.current

        var sessionDateFromModel = ""
        var sessionTimeFromModel = ""
        var showSetScoresFromModel = false
        if(sessionActionEnum == SessionActionEnum.EDIT) {
            val formatter = DateTimeFormatter.ofPattern(SESSION_DATE_FORMAT)
            val localDateTime = LocalDateTime.parse(session.sessionDate, formatter)

            if (localDateTime != null) {
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

                sessionDateFromModel = localDateTime.format(dateFormatter)
                sessionTimeFromModel = localDateTime.format(timeFormatter)
            }


            if(session.sessionPlayingStructure.contains("Sets")){
                showSetScoresFromModel = true
            }
        }

        // define state for each form element
        var showSessionDateDialog by remember { mutableStateOf(false) }
        val sessionDateState = rememberDatePickerState()
        var sessionDate by remember { mutableStateOf(sessionDateFromModel) }
        var sessionTime by remember { mutableStateOf(sessionTimeFromModel) }
        var sessionDuration by remember { mutableStateOf(session.sessionDuration.toString()) }
        var players by remember { mutableStateOf(emptyList<Player>()) }
        var selectedPlayers by remember { mutableStateOf( emptyList<Player>()) }
        var isPlayersExpanded by remember { mutableStateOf(false) }
        var venues by remember { mutableStateOf(emptyList<Venue>()) }
        var isVenuesExpanded by remember { mutableStateOf(false) }
        var selectedVenue by remember { mutableStateOf(getEmptyVenue()) }
        var sessionQualityOfTennis by remember { mutableStateOf(session.sessionQualityOfTennis) }
        var isQualityOfTennisExpanded by remember { mutableStateOf(false) }
        var selectedPlayingFormats by remember { mutableStateOf(session.sessionPlayingFormat.split(",")) }
        var isPlayingFormatsExpanded by remember { mutableStateOf(false) }
        var sessionCost by remember { mutableStateOf(session.sessionCost.toString()) }
        var sessionNotes by remember { mutableStateOf(session.sessionNotes) }
        var selectedPlayingStructures by remember { mutableStateOf(session.sessionPlayingStructure.split(",")) }
        var isPlayingStructuresExpanded by remember { mutableStateOf(false) }
        var showSetScores by remember { mutableStateOf(showSetScoresFromModel) }
        var setScores by remember { mutableStateOf("") }
        var sessionWashedOut by remember { mutableStateOf(session.sessionWashedOut) }
        var sessionNumberOfSteps by remember { mutableIntStateOf(session.sessionNumberOfSteps) }
        var sessionReachedOnTime by remember { mutableStateOf(session.sessionReachedOnTime) }
        var sessionTotalShotsPlayed by remember { mutableIntStateOf(session.sessionNumberOfShotsPlayed) }
        var bookedBy by remember { mutableStateOf(session.bookedBy) }
        var isBookedBySelf by remember { mutableStateOf(session.isSelfBooked) }
        var isBookedByExpanded by remember { mutableStateOf(false) }
        var isPaidToCourt by remember { mutableStateOf(session.sessionIsPaidToCourt)}
        var paidToCourtDate by remember { mutableStateOf(session.sessionPaidToCourtDate) }
        val paidToCourtDateState = rememberDatePickerState()
        var showPaidToCourtDateDialog by remember { mutableStateOf(false) }

        var amountDue by remember { mutableFloatStateOf(session.sessionAmountDue) }
        var hasPaid by remember { mutableStateOf(session.sessionHasPaid) }
        var paymentDate by remember { mutableStateOf(session.sessionPaidDate) }

        var playerPayments by remember { mutableStateOf(session.sessionPayments) }
        var showPaidToBookerDateDialog by remember { mutableStateOf(false) }
        val paidToBookerDateState = rememberDatePickerState()

        LaunchedEffect(Unit) {
            runBlocking {
                // Fetch the list of players asynchronously
                players = PlayerRepository().getAllPlayers(context = context)
                venues = VenueRepository().getAllVenues(context = context)
                if(sessionActionEnum == SessionActionEnum.EDIT) {
//                    for (player in players) {
//                        if (player.id in session.se) {
//                            selectedPlayersFromModel = selectedPlayersFromModel + player
//                        }
//                    }
                    selectedPlayers = players.filter { player -> session.players.contains(player.id) }
                    selectedVenue = venues.find { it.venueId == session.venueId }!!
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
            // Session Date
            Row{
                OutlinedTextField(
                    label = { Text(text = SESSION_DATE_LABEL) },
                    value = sessionDate,
                    onValueChange = { },
                    readOnly = true,
                    enabled = false
                )
                IconButton(onClick = { showSessionDateDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null
                    )
                }
            }

            if (showSessionDateDialog) {
                DatePickerDialog(
                    onDismissRequest = { showSessionDateDialog = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                showSessionDateDialog = false
                                sessionDate = sessionDateState.selectedDateMillis?.let {
                                    convertMillisToLocalDate(it)
                                }.toString()
                            }
                        ) {
                            Text(text = "OK")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showSessionDateDialog = false }
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = sessionDateState,
                        showModeToggle = true
                    )

                }
            }

            // Session Time
            OutlinedTextField(
                label = { Text(text = SESSION_TIME_LABEL) },
                value = sessionTime,
                onValueChange = { text -> sessionTime = text})


            // Session Duration in hours
            OutlinedTextField(
                label = { Text(text = SESSION_DURATION_LABEL) },
                value = sessionDuration,
                onValueChange = { text -> sessionDuration = text },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
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
            SingleSelectDropDown(label = SESSION_SELECT_VENUE_LABEL,
                options = venues.map { it.venueName },
                value = selectedVenue.venueName,
                isExpanded = isVenuesExpanded,
                onValueChange = { value: String, expanded: Boolean ->
                                    selectedVenue = venues.find { it.venueName == value }!!
                                    isVenuesExpanded = false
                                    },
                onExpandedChange = { expanded: Boolean, -> isVenuesExpanded = !isVenuesExpanded },
                onDismissRequest = { expanded: Boolean, -> isVenuesExpanded = false })

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
                        onDismissRequest = {
                            isPlayingStructuresExpanded = false
                            // if Sets selected, show UI to add set scores
//                            showSetScores = selectedPlayingStructures.contains("Sets")
                        }
                    ) {
                        getPlayingStructures().forEach { playingStructure ->
                            Row {
                                DropdownMenuItem(
                                    text = { Text(text = playingStructure) },
                                    onClick = {
                                        selectedPlayingStructures = if (selectedPlayingStructures.contains(playingStructure)) {
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
                if(showSetScores) {
                    Button(
                        onClick = {
                            navigateToSetScoresActivity(context=context, session=session, sessionId=session.sessionId, actionType=sessionActionEnum)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    ) {
                        Icon(Icons.Default.List, contentDescription = null) // Display icon
                        Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
                        Text("Sets")
                    }
                }
            }

            // Session Cost
            OutlinedTextField(
                label = { Text(text = SESSION_COST_LABEL) },
                value = sessionCost,
                onValueChange = { text -> sessionCost = text},
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

            if(isBookedBySelf) {
                Column{
                    selectedPlayers.forEach{player->
                        val playerPayment=PlayerPayment(
                            uid=playerPayments.find { it.playerId == player.id }?.uid ?: 0L,
                            playerId=player.id,
                            playerName=player.displayName,
                            paymentAmount=playerPayments.find { it.playerId == player.id }?.paymentAmount ?: 0f,
                            hasPaid=playerPayments.find { it.playerId == player.id }?.hasPaid ?: false,
                            paymentDate=playerPayments.find { it.playerId == player.id }?.paymentDate ?: ""
                        )
//                        playerPayments = playerPayments+playerPayment
                        PlayerPaymentLayout(
                            playerPayment=playerPayment,
                            onChange={ modifiedPlayerPayment->
                                Log.i("AddSessionActivity", modifiedPlayerPayment.toString())
                                if(playerPayments.isEmpty()) {
                                    playerPayments = playerPayments + getSelectedPlayersPayments(selectedPlayers)
                                }
                                playerPayments=playerPayments.map{
                                    if(it.playerId == modifiedPlayerPayment.playerId) modifiedPlayerPayment else it
                                }
                                Log.i("AddSessionActivity", playerPayments.toString())
                            }
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                )
                {
                    Checkbox(
                        checked = isPaidToCourt,
                        onCheckedChange = { isChecked -> isPaidToCourt = isChecked })
                    Spacer(Modifier.size(6.dp))
                    Text(text = SESSION_PAID_TO_COURT)
                }
                // Paid to court date
                OutlinedTextField(
                    label = { Text(text = SESSION_PAID_TO_COURT_DATE) },
                    value = paidToCourtDate,
                    onValueChange = { },
                    readOnly = true,
                    enabled = false
                )
                IconButton(onClick = { showPaidToCourtDateDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null
                    )
                }
                if (showPaidToCourtDateDialog) {
                    DatePickerDialog(
                        onDismissRequest = { showPaidToCourtDateDialog = false },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showPaidToCourtDateDialog = false
                                    paidToCourtDate = paidToCourtDateState.selectedDateMillis?.let {
                                        convertMillisToLocalDate(it)
                                    }.toString()
                                }
                            ) {
                                Text(text = "OK")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showPaidToCourtDateDialog = false }
                            ) {
                                Text(text = "Cancel")
                            }
                        }
                    ) {
                        DatePicker(
                            state = paidToCourtDateState,
                            showModeToggle = true
                        )

                    }
                }

                } else {
                Column {
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        )
                        {
                            Checkbox(
                                checked = hasPaid,
                                onCheckedChange = { isChecked -> hasPaid = isChecked })
                            Spacer(Modifier.size(6.dp))
                            Text(text = SESSION_PLAYER_PAY_HAS_PAID_LABEL)
                        }
                        OutlinedTextField(
                            label = { Text(text = SESSION_PAYMENT_DATE) },
                            value = paymentDate,
                            onValueChange = { },
                            readOnly = true,
                            enabled = false)
                        IconButton(onClick = { showPaidToBookerDateDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = null
                                )
                            }
                        }

                        if (showPaidToBookerDateDialog) {
                            DatePickerDialog(
                                onDismissRequest = { showPaidToBookerDateDialog = false },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            showPaidToBookerDateDialog = false
                                            paymentDate = paidToBookerDateState.selectedDateMillis?.let {
                                                convertMillisToLocalDate(it)
                                            }.toString()
                                        }
                                    ) {
                                        Text(text = "OK")
                                    }
                                },
                                dismissButton = {
                                    Button(
                                        onClick = { showPaidToBookerDateDialog = false }
                                    ) {
                                        Text(text = "Cancel")
                                    }
                                }
                            ) {
                                DatePicker(
                                    state = paidToBookerDateState,
                                    showModeToggle = true
                                )

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
                    // create Session object
                    val sessionNewOrEdit = Session(
                        sessionId = session.sessionId,
                        // Session dates related
                        sessionDate = "$sessionDate $sessionTime",
                        sessionDuration = safeStrToFloat(sessionDuration),
                        sessionReachedOnTime = sessionReachedOnTime,
                        sessionWashedOut = sessionWashedOut,

                        // session stats related
                        sessionPlayingFormat = getSelectedPlayingFormats(selectedPlayingFormats = selectedPlayingFormats),
                        sessionPlayingStructure = getSelectedPlayingStructures(selectedPlayingStructures = selectedPlayingStructures),
                        sessionSetScores = setScores,
                        sessionNumberOfSteps = sessionNumberOfSteps,
                        sessionQualityOfTennis = sessionQualityOfTennis,
                        sessionNotes = sessionNotes,
                        sessionNumberOfShotsPlayed = sessionTotalShotsPlayed,

                        // venue
                        venueId = selectedVenue.venueId,

                        // players
                        players = selectedPlayers.map { player: Player -> player.id }.toList(),

                        // payment related
                        sessionCost = safeStrToFloat(sessionCost),
                        bookedBy = bookedBy,
                        isSelfBooked = isBookedBySelf,
                        sessionPayments = playerPayments,

                        // when self needs to pay
                        sessionAmountDue = amountDue,
                        sessionHasPaid = hasPaid,
                        sessionPaidDate = paymentDate,

                        // paid to court details
                        sessionIsPaidToCourt = isPaidToCourt,
                        sessionPaidToCourtDate = paidToCourtDate,
                    )
                    Log.i("AddSessionActivity", playerPayments.size.toString())
                    Log.i("AddSessionActivity", sessionNewOrEdit.toString())
                    runBlocking {
                        // 1. Add session to database
                        val insertSessionResult = SessionRepository().insertSession(context = context, session = sessionNewOrEdit)
                        if(insertSessionResult.isSuccessful){
                            // 2. Show success toast
                            showSuccessToast(context = context, sessionActionEnum = sessionActionEnum, session = sessionNewOrEdit)
                            // 3. If playing structure has sets, navigate to set scores activity
                            if (sessionNewOrEdit.sessionPlayingStructure.contains("Sets")) {

                                navigateToSetScoresActivity(context = context, session = sessionNewOrEdit, sessionId = insertSessionResult.sessionId, actionType = sessionActionEnum)
                            } else {
                                navigateToListSessionActivity(context)
                            }
                        }else{
                            Log.e("AddSessionActivity", insertSessionResult.error)
                            Toast.makeText(context, "Failed to save session - ${insertSessionResult.error}", Toast.LENGTH_LONG).show()
                        }

                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                contentPadding = PaddingValues(16.dp),
            ) {
                Icon(getSaveActionButtonIcon(sessionActionEnum), contentDescription = null) // Display icon
                Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
                Text(getSaveActionButtonLabel(sessionActionEnum))
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

fun navigateToSetScoresActivity(context: Context, session: Session, sessionId: Long, actionType: SessionActionEnum) {
    val intent = Intent(context, UpsertSetStatsActivity::class.java)
    val sessionSetStats = SetStatsSession(
        sessionId = sessionId,
        sessionDisplayName = session.sessionDate,
        sessionSetStats = session.sessionSetStats
    )
    Log.i("AddSessionActivity", sessionSetStats.toString())
    intent.putExtra(SESSION_SET_STATS_INTENT_EXTRA, sessionSetStats)

    if (actionType == SessionActionEnum.EDIT) {
        intent.putExtra(SESSION_SET_STATS_ACTION_INTENT_EXTRA, SessionSetStatsActionEnum.EDIT)
    } else {
        intent.putExtra(SESSION_SET_STATS_ACTION_INTENT_EXTRA, SessionSetStatsActionEnum.ADD)
    }
    context.startActivity(intent)
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

    private fun getSelectedPlayersPayments(selectedPlayers: List<Player>): List<PlayerPayment> {
        var selectedPlayersPayments = emptyList<PlayerPayment>()
        selectedPlayers.forEach { player ->
            selectedPlayersPayments = selectedPlayersPayments +  PlayerPayment(
                playerId = player.id,
                playerName = player.displayName
            )
        }
        return selectedPlayersPayments
    }

    private fun navigateToListSessionActivity(context: Context) {
        val intent = Intent(context, ListSessionsActivity::class.java)
        context.startActivity(intent)
    }

    fun getSaveActionButtonLabel(actionType: SessionActionEnum): String {
        return when (actionType) {
            SessionActionEnum.ADD -> "Add Session"
            SessionActionEnum.EDIT -> "Edit Session"
        }
    }

    fun getSaveActionButtonIcon(actionType: SessionActionEnum): ImageVector {
        return when (actionType) {
            SessionActionEnum.ADD -> Icons.Default.Add
            SessionActionEnum.EDIT -> Icons.Default.Edit
        }
    }

    fun showSuccessToast(context: Context, sessionActionEnum: SessionActionEnum, session: Session) {
        var addOrEditMessage = ""
        if (sessionActionEnum == SessionActionEnum.ADD) {
            addOrEditMessage = "Added"
        } else if (sessionActionEnum == SessionActionEnum.EDIT) {
            addOrEditMessage = "Edited"
        }
        Toast.makeText(context, "Successfully $addOrEditMessage ${session.sessionDate}", Toast.LENGTH_LONG).show()
    }
