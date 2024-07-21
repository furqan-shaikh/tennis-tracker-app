package com.reversecurrent.tennistracker.dal.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.reversecurrent.tennistracker.dal.DATABASE_NAME
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_NAME
import com.reversecurrent.tennistracker.dal.dao.PlayerDao
import com.reversecurrent.tennistracker.dal.dao.SessionDao
import com.reversecurrent.tennistracker.dal.dao.SessionPaymentDao
import com.reversecurrent.tennistracker.dal.dao.SessionPlayersDao
import com.reversecurrent.tennistracker.dal.dao.SessionSetStatsDao
import com.reversecurrent.tennistracker.dal.dao.TennisAnalyticsDao
import com.reversecurrent.tennistracker.dal.dao.VenueDao
import com.reversecurrent.tennistracker.dal.entities.PlayerEntity
import com.reversecurrent.tennistracker.dal.entities.SessionEntity
import com.reversecurrent.tennistracker.dal.entities.SessionPaymentEntity
import com.reversecurrent.tennistracker.dal.entities.SessionPlayerJoinEntity
import com.reversecurrent.tennistracker.dal.entities.SessionSetStatsEntity
import com.reversecurrent.tennistracker.dal.entities.VenueEntity

@Database(entities = [PlayerEntity::class,
    VenueEntity::class,
    SessionEntity::class,
    SessionPlayerJoinEntity::class,
    SessionPaymentEntity::class,
    SessionSetStatsEntity::class],
    version = 4,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = AppDatabase.Migration2to3::class),
        AutoMigration(from = 3, to = 4)
    ],
    exportSchema = true
    )
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun venueDao(): VenueDao
    abstract fun sessionDao(): SessionDao
    abstract fun sessionPaymentDao(): SessionPaymentDao
    abstract fun tennisAnalyticsDao(): TennisAnalyticsDao
    abstract fun sessionPlayersDao(): SessionPlayersDao
    abstract fun sessionSetStatsDao(): SessionSetStatsDao

    @DeleteColumn(tableName = SESSION_TABLE_NAME, columnName = "session_set_scores")
    class Migration2to3 : AutoMigrationSpec

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(applicationContext: Any): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    applicationContext as Context,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

object DatabaseAccessor {
    fun getDatabase(applicationContext: Any): AppDatabase {
        return AppDatabase.getDatabase(applicationContext)
    }
}

