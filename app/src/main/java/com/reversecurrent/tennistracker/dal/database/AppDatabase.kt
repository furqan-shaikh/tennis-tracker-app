package com.reversecurrent.tennistracker.dal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.reversecurrent.tennistracker.dal.DATABASE_NAME
import com.reversecurrent.tennistracker.dal.dao.PlayerDao
import com.reversecurrent.tennistracker.dal.dao.SessionDao
import com.reversecurrent.tennistracker.dal.dao.VenueDao
import com.reversecurrent.tennistracker.dal.entities.PlayerEntity
import com.reversecurrent.tennistracker.dal.entities.SessionEntity
import com.reversecurrent.tennistracker.dal.entities.SessionPlayerJoinEntity
import com.reversecurrent.tennistracker.dal.entities.VenueEntity

@Database(entities = [PlayerEntity::class,
    VenueEntity::class,
    SessionEntity::class,
    SessionPlayerJoinEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun venueDao(): VenueDao
    abstract fun sessionDao(): SessionDao
}

object DatabaseAccessor {
    fun getDatabase(applicationContext: Any): AppDatabase {
        return Room.databaseBuilder(
            applicationContext as Context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}

