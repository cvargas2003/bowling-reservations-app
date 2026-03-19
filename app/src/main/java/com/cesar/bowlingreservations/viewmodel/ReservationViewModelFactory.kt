package com.cesar.bowlingreservations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cesar.bowlingreservations.data.local.ReservationDao

class ReservationViewModelFactory(
    private val dao: ReservationDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReservationViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}