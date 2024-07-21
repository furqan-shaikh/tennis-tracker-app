package com.reversecurrent.tennistracker.dal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.reversecurrent.tennistracker.dal.SESSION_PAYMENT_TABLE_HAS_PAID_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_PAYMENT_TABLE_NAME
import com.reversecurrent.tennistracker.dal.SESSION_PAYMENT_TABLE_SESSION_ID_COLUMN
import com.reversecurrent.tennistracker.dal.entities.SessionPaymentEntity

@Dao
interface SessionPaymentDao {
    @Upsert
    suspend fun insertSessionPayment(sessionPaymentEntity: SessionPaymentEntity): Long

    @Query("SELECT * FROM $SESSION_PAYMENT_TABLE_NAME WHERE $SESSION_PAYMENT_TABLE_HAS_PAID_COLUMN = 0")
    suspend fun getAllOutstandingPayments(): List<SessionPaymentEntity>

    @Query("DELETE FROM $SESSION_PAYMENT_TABLE_NAME WHERE $SESSION_PAYMENT_TABLE_SESSION_ID_COLUMN = :sessionId")
    suspend fun deleteBySessionId(sessionId: Long)

    @Query("SELECT * FROM $SESSION_PAYMENT_TABLE_NAME WHERE $SESSION_PAYMENT_TABLE_SESSION_ID_COLUMN = :sessionId")
    suspend fun getPaymentsForSession(sessionId: Long): List<SessionPaymentEntity>
}