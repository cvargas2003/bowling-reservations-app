package com.cesar.bowlingreservations.data.local

import androidx.room.*
import com.cesar.bowlingreservations.data.model.Reservation
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {

    @Insert
    suspend fun insert(reservation: Reservation)

    @Update
    suspend fun update(reservation: Reservation)

    @Delete
    suspend fun delete(reservation: Reservation)

    @Query("SELECT * FROM reservations ORDER BY date, time")
    fun getAll(): Flow<List<Reservation>>

    @Query("SELECT * FROM reservations WHERE clientName LIKE '%' || :name || '%'")
    fun searchByName(name: String): Flow<List<Reservation>>

    // 🔥 CLAVE DEL TALLER
    @Query("""
        SELECT * FROM reservations
        WHERE laneNumber = :lane
        AND date = :date
        AND time = :time
        AND status = 'Activa'
        LIMIT 1
    """)
    suspend fun findDuplicate(
        lane: Int,
        date: String,
        time: String
    ): Reservation?
}