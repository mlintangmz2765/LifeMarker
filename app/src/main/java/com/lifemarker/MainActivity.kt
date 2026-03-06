package com.lifemarker

import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lifemarker.ui.categories.CategoryScreen
import com.lifemarker.ui.main.MainScreen
import com.lifemarker.ui.theme.LifeMarkerTheme
import dagger.hilt.android.AndroidEntryPoint

import androidx.appcompat.app.AppCompatActivity
import com.lifemarker.ui.settings.SettingsScreen

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifeMarkerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") {
                            MainScreen(
                                onNavigateToCategories = { navController.navigate("categories") },
                                onNavigateToSettings = { navController.navigate("settings") }
                            )
                        }
                        composable("categories") {
                            CategoryScreen(onNavigateBack = { navController.popBackStack() })
                        }
                        composable("settings") {
                            SettingsScreen(onNavigateBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
