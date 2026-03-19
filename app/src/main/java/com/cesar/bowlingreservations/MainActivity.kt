package com.cesar.bowlingreservations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*

import com.cesar.bowlingreservations.data.local.AppDatabase
import com.cesar.bowlingreservations.ui.theme.BowlingReservationsTheme
import com.cesar.bowlingreservations.viewmodel.ReservationViewModel
import com.cesar.bowlingreservations.viewmodel.ReservationViewModelFactory

// 🔥 Screens
import com.cesar.bowlingreservations.uii.DashboardScreen
import com.cesar.bowlingreservations.uii.ReservationListScreen
import com.cesar.bowlingreservations.uii.AddReservationScreen

// 🔥 Navigation PRO
import com.cesar.bowlingreservations.navigation.Screen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 BASE DE DATOS
        val database = AppDatabase.getDatabase(applicationContext)
        val dao = database.reservationDao()

        // 🔥 FACTORY
        val factory = ReservationViewModelFactory(dao)

        setContent {
            BowlingReservationsTheme {

                val navController = rememberNavController()
                val viewModel: ReservationViewModel = viewModel(factory = factory)

                NavHost(
                    navController = navController,
                    startDestination = Screen.Dashboard.route
                ) {

                    // 🏠 DASHBOARD
                    composable(Screen.Dashboard.route) {
                        DashboardScreen(navController)
                    }

                    // 📋 LISTA
                    composable(Screen.List.route) {
                        ReservationListScreen(navController, viewModel)
                    }

                    // ➕ AGREGAR
                    composable(Screen.Add.route) {
                        AddReservationScreen(navController, viewModel)
                    }

                    // ✏️ EDITAR (🔥 NUEVO)
                    composable(Screen.Edit.route) { backStackEntry ->
                        val id = backStackEntry.arguments
                            ?.getString("id")
                            ?.toInt() ?: 0

                        AddReservationScreen(
                            navController = navController,
                            viewModel = viewModel,
                            reservationId = id
                        )
                    }
                }
            }
        }
    }
}