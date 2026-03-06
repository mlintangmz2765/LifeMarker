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
        "LocalPharmacy" to Icons.Default.LocalPharmacy,
        "DirectionsCar" to Icons.Default.DirectionsCar,
        "DirectionsBus" to Icons.Default.DirectionsBus,
        "DirectionsBike" to Icons.Default.DirectionsBike,
        "DirectionsWalk" to Icons.Default.DirectionsWalk,
        "AirplanemodeActive" to Icons.Default.AirplanemodeActive,
        "School" to Icons.Default.School,
        "AccountBalance" to Icons.Default.AccountBalance,
        "Park" to Icons.Default.Park,
        "BeachAccess" to Icons.Default.BeachAccess,
        "FitnessCenter" to Icons.Default.FitnessCenter,
        "Hotel" to Icons.Default.Hotel,
        "CameraAlt" to Icons.Default.CameraAlt,
        "Movie" to Icons.Default.Movie,
        "MusicNote" to Icons.Default.MusicNote,
        "Pets" to Icons.Default.Pets,
        "EmojiNature" to Icons.Default.EmojiNature,
        "ChildCare" to Icons.Default.ChildCare,
        "Warning" to Icons.Default.Warning,
        "Recycling" to Icons.Default.Recycling,
        "TakeoutDining" to Icons.Default.TakeoutDining
    )

    fun getIconByName(name: String): ImageVector {
        return predefinedIcons.find { it.first == name }?.second ?: Icons.Default.Place
    }
}
