package com.example.miniactv_5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.net.URLEncoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val viewModel: AppViewModel = viewModel()

                NavHost(navController = navController, startDestination = "greeting_screen") {
                    composable("greeting_screen") {
                        GreetingScreen(viewModel = viewModel, navController = navController)
                    }
                    composable("bye_screen/{byeMessage}/{repetitions}/{imageUri}") { backStackEntry ->
                        ByeScreen(
                            viewModel = viewModel,
                            byeMessage = viewModel.byeMessage.value,
                            repetitions = viewModel.repetitions.value.toIntOrNull() ?: 1,
                            encodedImageUri = viewModel.imageUri.value?.let { URLEncoder.encode(it, "UTF-8") } ?: "",
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}