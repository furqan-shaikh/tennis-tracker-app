package com.reversecurrent.tennistracker.application

import android.app.Application
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.reversecurrent.tennistracker.dal.database.DatabaseAccessor
import com.reversecurrent.tennistracker.repository.PlayerRepository
import com.reversecurrent.tennistracker.repository.selfPlayer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TennisTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        this.checkAndAddSelfPlayer()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun checkAndAddSelfPlayer() {
        val context = this
        Log.d("TennisTrackerApplication", "checkAndAddSelfPlayer: ")
        // Check if player exists at startup
        GlobalScope.launch(Dispatchers.IO) {
            val result = PlayerRepository().checkAndAddSelfPlayer(context)
            withContext(Dispatchers.Main) {
                Log.d("TennisTrackerApplication", "checkAndAddSelfPlayer: $result")
            }
        }
    }

}