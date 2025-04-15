package com.example.miniactv_5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "greeting_screen") {
                    composable("greeting_screen") {
                        GreetingScreen(navController = navController)
                    }
                    composable("bye_screen/{byeMessage}/{repetitions}/{imageUri}") { backStackEntry ->
                        val byeMessage = backStackEntry.arguments?.getString("byeMessage") ?: ""
                        val repetitions = backStackEntry.arguments?.getString("repetitions")?.toIntOrNull() ?: 1
                        val encodedImageUri = backStackEntry.arguments?.getString("imageUri") ?: ""
                        ByeScreen(
                            byeMessage = byeMessage,
                            repetitions = repetitions,
                            encodedImageUri = encodedImageUri,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}