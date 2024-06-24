package com.reversecurrent.tennistracker.views.players

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.reversecurrent.tennistracker.models.PLAYER_ACTION_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.PLAYER_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.Player
import com.reversecurrent.tennistracker.models.PlayerActionEnum
import com.reversecurrent.tennistracker.repository.PlayerRepository
import com.reversecurrent.tennistracker.ui.theme.TennisTrackerTheme
import kotlinx.coroutines.runBlocking

class ListPlayersActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ListPlayersLayout()
                }
            }
        }
    }
}

@Preview
@Composable
fun ListPlayersLayout() {
    val context = LocalContext.current
    var players by remember { mutableStateOf(emptyList<Player>()) }


    LaunchedEffect(Unit) {
        runBlocking {
            // Fetch the list of players asynchronously
            players = PlayerRepository().getAllPlayers(context = context)
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
        players.forEach { player ->
            PlayerCardLayout(
                player = player,
                context = context,
                onEditPlayerClick = ::onEditPlayerClick,
                onDeletePlayerClick = ::onDeletePlayerClick
            )
        }
    }
}

fun onEditPlayerClick(context: Context, player: Player) {
    Log.i("onEditPlayerClick", player.toString())
    val intent = Intent(context, AddPlayerFromManualActivity::class.java)
    intent.putExtra(PLAYER_INTENT_EXTRA, player)
    intent.putExtra(PLAYER_ACTION_INTENT_EXTRA, PlayerActionEnum.EDIT)
    context.startActivity(intent)
}

fun onDeletePlayerClick(player: Player) {
    Log.i("onDeletePlayerClick", player.toString())
    // ask for confirmation
}
