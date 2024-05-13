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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.models.VENUE_ACTION_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.VENUE_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.Venue
import com.reversecurrent.tennistracker.models.VenueActionEnum
import com.reversecurrent.tennistracker.repository.VenueRepository
import com.reversecurrent.tennistracker.ui.theme.TennisTrackerTheme
import kotlinx.coroutines.runBlocking

const val VENUE_NAME_LABEL = "Venue Name"
const val VENUE_ALIAS_LABEL = "Venue Alias"
const val PRIMARY_CONTACT_NAME_LABEL = "Primary Contact Name"
const val PRIMARY_CONTACT_NUMBER_LABEL = "Primary Contact Mobile Number"
const val ADDRESS_LABEL = "Venue Address"
const val NO_OF_COURTS_LABEL = "Number of Courts"
const val COST_PER_HOUR_LABEL = "Cost Per Hour"
const val COACHING_AVAILABLE_LABEL = "Coaching Available"


class AddVenueActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val venue = intent.getParcelableExtra<Venue>(
            VENUE_INTENT_EXTRA)
        val venueActionEnum: VenueActionEnum? = intent.getSerializableExtra(
            VENUE_ACTION_INTENT_EXTRA
        ) as? VenueActionEnum
        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (venue != null) {
                        if (venueActionEnum != null) {
                            AddVenueFormLayout(venue = venue, actionType = venueActionEnum)
                        }
                    }

                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddVenueFormLayout(venue: Venue, actionType: VenueActionEnum) {
        val context = LocalContext.current

        // define state for each form element
        var venueName by remember { mutableStateOf(venue.venueName) }
        var venueAlias by remember { mutableStateOf(venue.venueAlias) }
        var primaryContactNumber by remember { mutableStateOf(venue.primaryContactNumber) }
        var primaryContactName by remember { mutableStateOf(venue.primaryContactName) }
        var address by remember { mutableStateOf(venue.address) }
        var numberOfCourts by remember { mutableStateOf(venue.numberOfCourts) }
        var isExpandedCourtType by remember { mutableStateOf(false) }
        var courtType by remember { mutableStateOf(venue.courtType) }
        var costPerHour by remember { mutableStateOf(venue.costPerHour) }
        var coachingAvailable by remember { mutableStateOf(venue.coachingAvailable) }

        // define the UI
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Venue Name
            OutlinedTextField(
                label = { Text(text = VENUE_NAME_LABEL) },
                value = venueName,
                onValueChange = { text -> venueName = text})
            // Venue Alias
            OutlinedTextField(
                label = { Text(text = VENUE_ALIAS_LABEL) },
                value = venueAlias,
                onValueChange = { text -> venueAlias = text})
            // Primary Contact Name
            OutlinedTextField(
                label = { Text(text = PRIMARY_CONTACT_NAME_LABEL) },
                value = primaryContactName,
                onValueChange = { text -> primaryContactName = text})
            // Primary Contact Number
            OutlinedTextField(
                label = { Text(text = PRIMARY_CONTACT_NUMBER_LABEL) },
                value = primaryContactNumber,
                onValueChange = { text -> primaryContactNumber = text},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
            OutlinedTextField(
                label = { Text(text = ADDRESS_LABEL) },
                value = address,
                onValueChange = { text -> address = text},
                modifier = Modifier
                    .fillMaxWidth().height(120.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)))
            // Number of courts
            OutlinedTextField(
                label = { Text(text = NO_OF_COURTS_LABEL) },
                value = numberOfCourts,
                onValueChange = { text -> numberOfCourts = text},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            // Court Type
            ExposedDropdownMenuBox(
                expanded = isExpandedCourtType,
                onExpandedChange = { isExpandedCourtType = !isExpandedCourtType }
            ) {
                TextField(
                    value = courtType,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedCourtType) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isExpandedCourtType,
                    onDismissRequest = { isExpandedCourtType = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Hard Court") },
                        onClick = {
                            courtType = "Hard Court"
                            isExpandedCourtType = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Clay Court") },
                        onClick = {
                            courtType = "Intermediate"
                            isExpandedCourtType = false
                        }
                    )
                }
            }
            // Cost per hour
            OutlinedTextField(
                label = { Text(text = COST_PER_HOUR_LABEL) },
                value = costPerHour,
                onValueChange = { text -> costPerHour = text},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            // Coaching available
            Row (
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                Checkbox(
                    checked = coachingAvailable,
                    onCheckedChange = { isChecked -> coachingAvailable = isChecked})
                Spacer(Modifier.size(6.dp))
                Text(text = COACHING_AVAILABLE_LABEL)
            }
            Button(
                    onClick = {
                        Log.i("TAG", "add clicked")
                        val venueNewOrEdit = Venue(
                            venueId = venue.venueId,
                            primaryContactNumber = primaryContactNumber,
                            primaryContactName = primaryContactName,
                            numberOfCourts = numberOfCourts,
                            coachingAvailable = coachingAvailable,
                            courtType = courtType,
                            costPerHour = costPerHour,
                            address = address,
                            venueAlias = venueAlias,
                            venueName = venueName
                        )
                        Log.i("TAG", venueNewOrEdit.toString())
                        var addOrEditMessage = ""
                        if (actionType == VenueActionEnum.ADD) {
                            addOrEditMessage = "Added"
                        } else if (actionType == VenueActionEnum.EDIT) {
                            addOrEditMessage = "Edited"
                        }
                        runBlocking {
                            VenueRepository().upsert(context = context, venue = venueNewOrEdit)
                            Toast.makeText(context, "Successfully $addOrEditMessage ${venueNewOrEdit.venueName}", Toast.LENGTH_LONG).show()
                            navigateToParentActivity(context = context)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    contentPadding = PaddingValues(16.dp),
                ) {
                    Icon(getSaveActionButtonIcon(actionType), contentDescription = null) // Display icon
                    Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
                    Text(getSaveActionButtonLabel(actionType))
                }
            Button(
                    onClick = {
                        navigateToParentActivity(context)
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

    private fun getSaveActionButtonLabel(actionType: VenueActionEnum): String {
        return when (actionType) {
            VenueActionEnum.ADD -> "Add Venue"
            VenueActionEnum.EDIT -> "Edit Venue"
        }
    }

    private fun getSaveActionButtonIcon(actionType: VenueActionEnum): ImageVector {
        return when (actionType) {
            VenueActionEnum.ADD -> Icons.Default.Add
            VenueActionEnum.EDIT -> Icons.Default.Edit
        }
    }
}