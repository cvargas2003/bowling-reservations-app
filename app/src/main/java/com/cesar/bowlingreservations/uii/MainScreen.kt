package com.cesar.bowlingreservations.uii

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cesar.bowlingreservations.data.model.Reservation
import com.cesar.bowlingreservations.viewmodel.ReservationViewModel

@Composable
fun MainScreen(viewModel: ReservationViewModel) {

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var lane by remember { mutableStateOf("") }
    var players by remember { mutableStateOf("") }

    val reservations by viewModel.filteredReservations.collectAsState()
    val message by viewModel.message.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("🎳 Nueva Reserva", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // 🔍 BUSCAR
        OutlinedTextField(
            value = "",
            onValueChange = { viewModel.onSearchChange(it) },
            label = { Text("Buscar cliente") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 👤 NOMBRE
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre del cliente") },
            modifier = Modifier.fillMaxWidth()
        )

        // 📞 TELÉFONO
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )

        // 📅 FECHA
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Fecha (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        // ⏰ HORA
        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("Hora (HH:MM)") },
            modifier = Modifier.fillMaxWidth()
        )

        // 🎳 PISTA
        OutlinedTextField(
            value = lane,
            onValueChange = { lane = it },
            label = { Text("Número de pista") },
            modifier = Modifier.fillMaxWidth()
        )

        // 👥 JUGADORES
        OutlinedTextField(
            value = players,
            onValueChange = { players = it },
            label = { Text("Cantidad de jugadores") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🔘 BOTÓN
        Button(
            onClick = {

                val reservation = Reservation(
                    clientName = name,
                    phone = phone,
                    date = date,
                    time = time,
                    laneNumber = lane.toIntOrNull() ?: 0,
                    players = players.toIntOrNull() ?: 0,
                    status = "Activa"
                )

                viewModel.insertReservation(reservation)

                // 🔥 LIMPIAR CAMPOS
                name = ""
                phone = ""
                date = ""
                time = ""
                lane = ""
                players = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar reserva")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔥 MENSAJE
        message?.let {
            Text(text = it, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 📋 LISTA
        LazyColumn {
            items(reservations) { reserva ->
                ReservationItem(reserva)
            }
        }
    }
}
@Composable
fun ReservationItem(reserva: Reservation) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text("👤 ${reserva.clientName}")
            Text("📞 ${reserva.phone}")
            Text("📅 ${reserva.date} - ${reserva.time}")
            Text("🎳 Pista ${reserva.laneNumber}")
            Text("👥 ${reserva.players} jugadores")

            Text(
                text = "Estado: ${reserva.status}",
                color = if (reserva.status == "Activa")
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )
        }
    }
}