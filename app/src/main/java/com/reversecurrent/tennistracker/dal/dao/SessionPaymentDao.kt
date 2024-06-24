package com.reversecurrent.tennistracker.dal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.reversecurrent.tennistracker.dal.SESSION_PAYMENT_TABLE_HAS_PAID_COLUMN
import com.reversecurrent.tennistracker.dal.entities.SessionPaymentEntity

@Dao
interface SessionPaymentDao {
    @Insert
    suspend fun insertSessionPayment(sessionPaymentEntity: SessionPaymentEntity): Long

    @Query("SELECT * FROM session_payments WHERE $SESSION_PAYMENT_TABLE_HAS_PAID_COLUMN = 0")
    suspend fun getAllOutstandingPayments(): List<SessionPaymentEntity>
}