package com.cesar.bowlingreservations.navigation

sealed class Screen(val route: String) {

    object Dashboard : Screen("dashboard")
    object List : Screen("list")
    object Add : Screen("add")

    // 🔥 EDITAR
    object Edit : Screen("edit/{id}") {
        fun createRoute(id: Int) = "edit/$id"
    }
}