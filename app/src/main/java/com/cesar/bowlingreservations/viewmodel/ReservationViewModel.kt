package com.cesar.bowlingreservations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.bowlingreservations.data.local.ReservationDao
import com.cesar.bowlingreservations.data.model.Reservation
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ReservationViewModel(
    private val dao: ReservationDao
) : ViewModel() {

    // 🔥 LISTA DE RESERVAS EN TIEMPO REAL
    val reservations: StateFlow<List<Reservation>> =
        dao.getAll()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // 🔍 BUSCADOR
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val filteredReservations: StateFlow<List<Reservation>> =
        searchQuery
            .flatMapLatest { query ->
                if (query.isEmpty()) {
                    dao.getAll()
                } else {
                    dao.searchByName(query)
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun onSearchChange(query: String) {
        _searchQuery.value = query
    }

    // 🔥 MENSAJES PARA LA UI (ERRORES / ÉXITO)
    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    // 🚀 INSERTAR CON VALIDACIÓN
    fun insertReservation(reservation: Reservation) {
        viewModelScope.launch {

            val duplicate = dao.findDuplicate(
                reservation.laneNumber,
                reservation.date,
                reservation.time
            )

            if (duplicate != null) {
                _message.value = "❌ Esta pista ya está reservada en esa fecha y hora"
                return@launch
            }

            dao.insert(reservation)
            _message.value = "✅ Reserva guardada correctamente"
        }
    }

    // ✏️ ACTUALIZAR
    fun updateReservation(reservation: Reservation) {
        viewModelScope.launch {
            dao.update(reservation)
            _message.value = "✏️ Reserva actualizada"
        }
    }

    // 🗑️ ELIMINAR
    fun deleteReservation(reservation: Reservation) {
        viewModelScope.launch {
            dao.delete(reservation)
            _message.value = "🗑️ Reserva eliminada"
        }
    }

    // 🔄 LIMPIAR MENSAJE
    fun clearMessage() {
        _message.value = null
    }
}