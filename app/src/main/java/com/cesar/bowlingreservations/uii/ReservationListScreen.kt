package com.cesar.bowlingreservations.uii

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cesar.bowlingreservations.viewmodel.ReservationViewModel
import com.cesar.bowlingreservations.navigation.Screen
import com.cesar.bowlingreservations.data.model.Reservation

@Composable
fun ReservationListScreen(
    navController: NavController,
    viewModel: ReservationViewModel
) {

    var search by remember { mutableStateOf("") }

    val reservations by viewModel.filteredReservations.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("📋 Lista de Reservas", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = search,
            onValueChange = {
                search = it
                viewModel.onSearchChange(it)
            },
            label = { Text("Buscar cliente") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(reservations) { res ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        Text("Cliente: ${res.clientName}")
                        Text("Fecha: ${res.date} ${res.time}")
                        Text("Pista: ${res.laneNumber}")
                        Text("Estado: ${res.status}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Row {

                            // 🗑️ ELIMINAR
                            Button(onClick = {
                                viewModel.deleteReservation(res)
                            }) {
                                Text("Eliminar")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // ✏️ EDITAR
                            Button(onClick = {
                                navController.navigate("edit/${res.id}")
                            }) {
                                Text("Editar")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}