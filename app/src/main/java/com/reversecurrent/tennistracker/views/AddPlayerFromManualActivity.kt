package com.reversecurrent.tennistracker.views

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.ui.theme.TennisTrackerTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.reversecurrent.tennistracker.models.PLAYER_ACTION_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.PLAYER_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.Player
import com.reversecurrent.tennistracker.models.PlayerActionEnum
import com.reversecurrent.tennistracker.repository.PlayerRepository
import kotlinx.coroutines.runBlocking

const val NAME_LABEL = "Name"
const val MOBILE_NUMBER_LABEL = "Mobile Number"
const val PLAYED_BEFORE_LABEL = "Played Before?"


class AddPlayerFromManualActivity : ComponentActivity() {
//    private val viewModel by viewModels<PlayerViewModel>()
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val player = intent.getParcelableExtra<Player>(PLAYER_INTENT_EXTRA)
        val playerActionEnum: PlayerActionEnum? = intent.getSerializableExtra(PLAYER_ACTION_INTENT_EXTRA) as? PlayerActionEnum
        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (player != null) {
                        if (playerActionEnum != null) {
                            AddPlayerFormLayout(player=player, actionType = playerActionEnum)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlayerFormLayout(player: Player, actionType: PlayerActionEnum) {
    val context = LocalContext.current
    var playerName by remember {
        mutableStateOf(player.displayName)
    }
    var mobileNumber by remember {  mutableStateOf(player.mobileNumber) }
    var isExpanded by remember { mutableStateOf(false) }
    var playingLevel by remember { mutableStateOf(player.playingLevel) }
    var playedBefore by remember { mutableStateOf(player.playedBefore) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            label = { Text(text = NAME_LABEL)},
            value = playerName,
            onValueChange = { text -> playerName = text})
        OutlinedTextField(
            label = { Text(text = MOBILE_NUMBER_LABEL) },
            value = mobileNumber,
            onValueChange = { text -> mobileNumber = text },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            TextField(
                value = playingLevel,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Beginner") },
                    onClick = {
                        playingLevel = "Beginner"
                        isExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Intermediate") },
                    onClick = {
                        playingLevel = "Intermediate"
                        isExpanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("Advanced") },
                    onClick = {
                        playingLevel = "Advanced"
                        isExpanded = false
                    }
                )
            }
        }
        Row (
            verticalAlignment = Alignment.CenterVertically,
        )
        {
            Checkbox(
                checked = playedBefore,
                onCheckedChange = { isChecked -> playedBefore = isChecked})
            Spacer(Modifier.size(6.dp))
            Text(text = PLAYED_BEFORE_LABEL)
        }

        Button(
            onClick = {
                val playerNewOrEdit = Player(
                    player.id,
                    displayName = playerName,
                    mobileNumber = mobileNumber,
                    playingLevel = playingLevel,
                    playedBefore = playedBefore
                )
                Log.i("AddPlayerFromManualActivity", playerNewOrEdit.toString())
                runBlocking {
                    PlayerRepository().upsert(context = context, player = playerNewOrEdit)
                    var addOrEditMessage = ""
                    if (actionType == PlayerActionEnum.ADD) {
                        addOrEditMessage = "Added"
                    } else if (actionType == PlayerActionEnum.EDIT) {
                        addOrEditMessage = "Edited"
                    }
                    Toast.makeText(context, "Successfully $addOrEditMessage ${playerNewOrEdit.displayName}", Toast.LENGTH_LONG).show()
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

fun navigateToParentActivity(context: android.content.Context) {
    val intent = Intent(context, AddPlayerActivity::class.java)
    context.startActivity(intent)
}

fun getSaveActionButtonLabel(actionType: PlayerActionEnum): String {
    return when (actionType) {
        PlayerActionEnum.ADD -> "Add Player"
        PlayerActionEnum.EDIT -> "Edit Player"
    }
}

fun getSaveActionButtonIcon(actionType: PlayerActionEnum): ImageVector {
    return when (actionType) {
        PlayerActionEnum.ADD -> Icons.Default.Add
        PlayerActionEnum.EDIT -> Icons.Default.Edit
    }
}

