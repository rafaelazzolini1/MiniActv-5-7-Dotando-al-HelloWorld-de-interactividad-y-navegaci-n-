package com.example.miniactv_5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    MaterialTheme {
        val navController = rememberNavController()
        var welcomeMessage by remember { mutableStateOf("Bienvenido") }

        NavHost(navController = navController, startDestination = "greeting_screen") {
            composable("greeting_screen") {
                GreetingScreen(
                    welcomeMessage = welcomeMessage,
                    onNavigateToByeScreen = { byeMessage, repetitions, imageUri ->
                        val encodedImageUri = imageUri?.toString()?.let { java.net.URLEncoder.encode(it, "UTF-8") } ?: ""
                        navController.navigate("bye_screen/$byeMessage/$repetitions/$encodedImageUri")
                    }
                )
            }
            composable("bye_screen/{byeMessage}/{repetitions}/{imageUri}") { backStackEntry ->
                val byeMessage = backStackEntry.arguments?.getString("byeMessage") ?: ""
                val repetitions = backStackEntry.arguments?.getString("repetitions")?.toIntOrNull() ?: 1
                val encodedImageUri = backStackEntry.arguments?.getString("imageUri") ?: ""
                val imageUri = if (encodedImageUri.isNotEmpty()) {
                    java.net.URLDecoder.decode(encodedImageUri, "UTF-8")
                } else {
                    null
                }
                ByeScreen(
                    byeMessage = byeMessage,
                    repetitions = repetitions,
                    imageUri = imageUri,
                    onNavigateBack = { navController, message ->
                        // Atualizar a mensagem de saudação antes de voltar
                        welcomeMessage = message
                        navController.popBackStack()
                    },
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun GreetingScreen(
    welcomeMessage: String,
    onNavigateToByeScreen: (String, Int, String?) -> Unit
) {
    var byeMessage by remember { mutableStateOf(TextFieldValue("")) }
    var repetitions by remember { mutableStateOf(TextFieldValue("")) }
    var byeMessageError by remember { mutableStateOf(false) }
    var repetitionsError by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<String?>(null) }

    // Lançador para selecionar uma imagem da galeria
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri?.toString()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = welcomeMessage, // Usar a mensagem dinâmica
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo para a mensagem de despedida
        Text(
            text = stringResource(R.string.goodbye),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = byeMessage,
            onValueChange = {
                byeMessage = it
                byeMessageError = it.text.isEmpty()
            },
            label = { Text(text = stringResource(R.string.input_message)) },
            isError = byeMessageError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )

        // Campo para o número de repetições
        Text(
            text = stringResource(R.string.input_number),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = repetitions,
            onValueChange = {
                repetitions = it
                repetitionsError = it.text.isEmpty() || it.text.toIntOrNull() == null
            },
            label = { Text(text = stringResource(R.string.input_number)) },
            isError = repetitionsError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )

        // Botão para selecionar uma imagem
        Button(
            onClick = {
                pickImageLauncher.launch("image/*")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = stringResource(R.string.select_image_button))
        }

        // Botão para navegar para a tela de despedida
        Button(
            onClick = {
                byeMessageError = byeMessage.text.isEmpty()
                repetitionsError = repetitions.text.isEmpty() || repetitions.text.toIntOrNull() == null

                if (!byeMessageError && !repetitionsError) {
                    onNavigateToByeScreen(byeMessage.text, repetitions.text.toInt(), imageUri)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = stringResource(R.string.goodbye_button))
        }
    }
}