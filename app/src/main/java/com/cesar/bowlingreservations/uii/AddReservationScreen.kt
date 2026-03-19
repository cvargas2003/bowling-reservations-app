package com.cesar.bowlingreservations.uii

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavController
import com.cesar.bowlingreservations.data.model.Reservation
import com.cesar.bowlingreservations.viewmodel.ReservationViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddReservationScreen(
    navController: NavController,
    viewModel: ReservationViewModel,
    reservationId: Int = 0
) {

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var lane by remember { mutableStateOf("") }
    var players by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Activa") }

    val reservations by viewModel.reservations.collectAsState()
    val message by viewModel.message.collectAsState()

    // 🔥 CARGAR DATOS SI ES EDICIÓN
    LaunchedEffect(reservationId) {
        if (reservationId != 0) {
            val res = reservations.find { it.id == reservationId }
            res?.let {
                name = it.clientName
                phone = it.phone
                date = it.date
                time = it.time
                lane = it.laneNumber.toString()
                players = it.players.toString()
                status = it.status
            }
        }
    }

    // 📅 PICKERS
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    if (reservationId == 0) "Nueva Reserva 🎳" else "Editar Reserva ✏️",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(name, { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(phone, { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(10.dp))

                // 📅 FECHA
                Button(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (date.isEmpty()) "Seleccionar Fecha" else date)
                }

                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("OK")
                            }
                        }
                    ) {
                        val state = rememberDatePickerState()
                        DatePicker(state = state)

                        LaunchedEffect(state.selectedDateMillis) {
                            state.selectedDateMillis?.let {
                                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    .format(Date(it))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ⏰ HORA
                Button(onClick = { showTimePicker = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (time.isEmpty()) "Seleccionar Hora" else time)
                }

                if (showTimePicker) {
                    val state = rememberTimePickerState()

                    AlertDialog(
                        onDismissRequest = { showTimePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                time = "${state.hour}:${state.minute}"
                                showTimePicker = false
                            }) {
                                Text("OK")
                            }
                        },
                        text = {
                            TimePicker(state = state)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(lane, { lane = it }, label = { Text("Pista") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(players, { players = it }, label = { Text("Jugadores") }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {

                        val reservation = Reservation(
                            id = if (reservationId == 0) 0 else reservationId,
                            clientName = name,
                            phone = phone,
                            date = date,
                            time = time,
                            laneNumber = lane.toIntOrNull() ?: 1,
                            players = players.toIntOrNull() ?: 1,
                            status = status
                        )

                        if (reservationId == 0) {
                            viewModel.insertReservation(reservation)
                        } else {
                            viewModel.updateReservation(reservation)
                        }

                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver")
                }

                message?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it)
                }
            }
        }
    }
}