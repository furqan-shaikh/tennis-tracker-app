package com.reversecurrent.tennistracker.dal.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.reversecurrent.tennistracker.dal.SESSION_PAYMENT_TABLE_AMOUNT_DUE_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_PAYMENT_TABLE_HAS_PAID_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_PAYMENT_TABLE_NAME
import com.reversecurrent.tennistracker.dal.SESSION_PAYMENT_TABLE_PAYMENT_DATE_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_PAYMENT_TABLE_PLAYER_ID_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_PAYMENT_TABLE_SESSION_ID_COLUMN

@Entity(tableName = SESSION_PAYMENT_TABLE_NAME,
    foreignKeys = [
        ForeignKey(entity = SessionEntity::class,
        childColumns = [SESSION_PAYMENT_TABLE_SESSION_ID_COLUMN],
        parentColumns = ["uid"]),

        ForeignKey(entity = PlayerEntity::class,
            childColumns = [SESSION_PAYMENT_TABLE_PLAYER_ID_COLUMN],
            parentColumns = ["uid"])
])
data class SessionPaymentEntity (
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = SESSION_PAYMENT_TABLE_SESSION_ID_COLUMN) val sessionId: Long, // Foreign key column to session
    @ColumnInfo(name = SESSION_PAYMENT_TABLE_PLAYER_ID_COLUMN) val playerId: Long,  // Foreign key column to player

    @ColumnInfo(name = SESSION_PAYMENT_TABLE_AMOUNT_DUE_COLUMN) val paymentAmountDue: Float,
    @ColumnInfo(name = SESSION_PAYMENT_TABLE_HAS_PAID_COLUMN) val hasPaid: Boolean,
    @ColumnInfo(name = SESSION_PAYMENT_TABLE_PAYMENT_DATE_COLUMN) val paymentDate: String
)

