package com.reversecurrent.tennistracker.views.players

import android.content.Context
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.models.Player
import com.reversecurrent.tennistracker.views.widgets.CheckboxWidget
import com.reversecurrent.tennistracker.views.widgets.TextWidget
import kotlinx.coroutines.launch

const val PLAYER_NAME_LABEL = "Player Name: "
const val PLAYER_MOBILE_NUMBER_LABEL = "Mobile Number: "
const val PLAYER_LEVEL_LABEL = "Playing Level: "
const val PLAYER_PLAYED_BEFORE_LABEL = "Played Before? "

@Composable
fun PlayerCardLayout(player: Player, context: Context, onEditPlayerClick: (Context, Player) -> Unit, onDeletePlayerClick: (Context, Player) -> Unit) {
    Column{
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            CardContent(player = player)
        }
        ActionButtons(player = player, context = context, onEditPlayerClick = onEditPlayerClick, onDeletePlayerClick)
    }
}

@Composable
fun ColumnContainer() {
    Column(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = MaterialTheme.shapes.medium // You can specify the shape as needed
            )
            .fillMaxWidth()
    ) {}
}

@Composable
fun CardContent(player: Player) {
    TextWidget(label = PLAYER_NAME_LABEL, value = player.displayName)
    TextWidget(label = PLAYER_MOBILE_NUMBER_LABEL, value = player.mobileNumber)
    TextWidget(label = PLAYER_LEVEL_LABEL, value = player.playingLevel)
    CheckboxWidget(label= PLAYER_PLAYED_BEFORE_LABEL, value=player.playedBefore)
}
@Composable
fun ActionButtons(player: Player, context: Context, onEditPlayerClick: (Context, Player) -> Unit, onDeletePlayerClick: (Context, Player) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    Row {
        Button(
            onClick = {
                onEditPlayerClick(context, player)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = null) // Display icon
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        onDeletePlayerClick(context, player)
                        // Handle success (e.g., show a success message)
                    } catch (e: Exception) {
                        // Handle error
                    } finally {
                    }
                }

            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.Default.Delete, contentDescription = null) // Display icon
        }
    }
}

