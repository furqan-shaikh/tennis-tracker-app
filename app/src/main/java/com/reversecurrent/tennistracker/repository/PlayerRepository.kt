package com.reversecurrent.tennistracker.repository

import android.support.annotation.WorkerThread
import android.util.Log
import com.reversecurrent.tennistracker.dal.entities.PlayerEntity
import com.reversecurrent.tennistracker.models.Player
import com.reversecurrent.tennistracker.dal.database.DatabaseAccessor
import com.reversecurrent.tennistracker.models.SELF_PLAYER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayerRepository {
    @WorkerThread
    suspend fun upsert(context: Any, player: Player) {
        val playerEntity = PlayerEntity (
                    playerName = player.displayName,
                    mobileNumber = player.mobileNumber,
                    playingLevel = player.playingLevel,
                    playedBefore = player.playedBefore,
                    uid = player.id
                )
        Log.i("PlayerRepository", playerEntity.toString())
        Log.i("PlayerRepository", "Before upserting")
        DatabaseAccessor.getDatabase(applicationContext = context)
            .playerDao().Upsert(playerEntity)
    }

    @WorkerThread
    suspend fun getAllPlayers(context: Any): List<Player> {
        return withContext(Dispatchers.IO) {
            val playerEntities = DatabaseAccessor.getDatabase(applicationContext = context)
                .playerDao().getAll()

            var players = playerEntities.map { playerEntity ->
                Player(
                    id = playerEntity.uid,
                    displayName = playerEntity.playerName,
                    mobileNumber = playerEntity.mobileNumber,
                    playingLevel = playerEntity.playingLevel,
                    playedBefore = playerEntity.playedBefore
                )
            }
            // add self to the list of players
            val selfPlayer = Player (
                id = 0,
                displayName = SELF_PLAYER,
                mobileNumber = "",
                playedBefore = true,
                playingLevel = "Intermediate"
            )
            players = players.plus(selfPlayer)
            return@withContext players
        }

    }
}