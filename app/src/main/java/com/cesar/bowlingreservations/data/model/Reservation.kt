package com.cesar.bowlingreservations.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class Reservation(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val clientName: String,

    val phone: String,

    val date: String,

    val time: String,

    val laneNumber: Int,

    val players: Int,

    val status: String
)