package com.reversecurrent.tennistracker.repository

import android.support.annotation.WorkerThread
import android.util.Log
import androidx.room.withTransaction
import com.reversecurrent.tennistracker.dal.entities.PlayerEntity
import com.reversecurrent.tennistracker.models.Player
import com.reversecurrent.tennistracker.dal.database.DatabaseAccessor
import com.reversecurrent.tennistracker.models.SELF_PLAYER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val selfPlayer = Player (
    id = 0,
    displayName = SELF_PLAYER,
    mobileNumber = "",
    playedBefore = true,
    playingLevel = "Intermediate"
)
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
            .playerDao().upsert(playerEntity)
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
            return@withContext players
        }

    }

    @WorkerThread
    suspend fun deleteByUid(context: Any, uid: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val dbClient = DatabaseAccessor.getDatabase(applicationContext = context)
                dbClient.withTransaction {
                    // get all sessions associated with this player
                    val sessions = dbClient.sessionPlayersDao().getSessionsForPlayer(uid)
                    Log.i("PlayerRepository", sessions.toString())
                    // for each session
                    for (sessionId in sessions) {
                        // delete the entries from sessions payment table
                        dbClient.sessionPaymentDao().deleteBySessionId(sessionId)
                        Log.i("PlayerRepository", "Deleted $sessionId from payments table")
                        // delete the session from sessions table
                        dbClient.sessionDao().deleteBySessionId(sessionId)
                        Log.i("PlayerRepository", "Deleted $sessionId from sessions table")
                    }
                    // first delete the player entries from player session join table
                    dbClient.sessionPlayersDao().deletePlayer(uid)
                    Log.i("PlayerRepository", "Deleted $uid from player session join table")
                    // now delete the player from players table
                    dbClient.playerDao().deleteByPlayerId(uid)
                    Log.i("PlayerRepository", "Deleted $uid from players table")
                }
                Log.i("PlayerRepository", "Deleted $uid")
                return@withContext true
            } catch (e: Exception) {
                Log.e("PlayerRepository", e.toString())
                return@withContext false
            }
        }
    }

    @WorkerThread
    suspend fun checkAndAddSelfPlayer(context: Any): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val dbClient = DatabaseAccessor.getDatabase(applicationContext = context)
                val playerExists = dbClient.playerDao().selfPlayerExists(selfPlayer.displayName)
                if (!playerExists) {
                    Log.i("PlayerRepository", "Adding self player")
                    dbClient.playerDao().upsert(player = PlayerEntity(
                        uid = selfPlayer.id,
                        playerName = selfPlayer.displayName,
                        mobileNumber = selfPlayer.mobileNumber,
                        playingLevel = selfPlayer.playingLevel,
                        playedBefore = selfPlayer.playedBefore
                    ))
                    return@withContext true
                }
                return@withContext true
            }
            catch (e: Exception) {
                Log.e("PlayerRepository", e.toString())
                return@withContext false
            }
        }
    }
}