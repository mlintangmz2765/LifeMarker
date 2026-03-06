package com.lifemarker.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object IconMapper {
    val predefinedIcons = listOf(
        "Place" to Icons.Default.Place,
        "Restaurant" to Icons.Default.Restaurant,
        "LocalCafe" to Icons.Default.LocalCafe,
        "Wc" to Icons.Default.Wc,
        "Home" to Icons.Default.Home,
        "Work" to Icons.Default.Work,
        "Star" to Icons.Default.Star,
        "Favorite" to Icons.Default.Favorite,
        "ShoppingCart" to Icons.Default.ShoppingCart,
        "LocalGasStation" to Icons.Default.LocalGasStation,
        "LocalHospital" to Icons.Default.LocalHospital,
        "DirectionsCar" to Icons.Default.DirectionsCar,
        "DirectionsBike" to Icons.Default.DirectionsBike,
        "AirplanemodeActive" to Icons.Default.AirplanemodeActive,
        "School" to Icons.Default.School,
        "Park" to Icons.Default.Park,
        "FitnessCenter" to Icons.Default.FitnessCenter,
        "Hotel" to Icons.Default.Hotel
    )

    fun getIconByName(name: String): ImageVector {
        return predefinedIcons.find { it.first == name }?.second ?: Icons.Default.Place
    }
}
