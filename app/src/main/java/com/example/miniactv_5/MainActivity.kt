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

        // Configura a navegação entre as telas usando NavHost
        NavHost(navController = navController, startDestination = "greeting_screen") {

            // Define a rota para a tela de saudação (GreetingScreen)
            composable("greeting_screen") {
                GreetingScreen(
                    welcomeMessage = welcomeMessage,
                    onNavigateToByeScreen = { byeMessage, repetitions, imageUri ->
                        // Codifica a URI da imagem (se houver) para evitar problemas com caracteres especiais
                        val encodedImageUri = imageUri?.toString()?.let { java.net.URLEncoder.encode(it, "UTF-8") } ?: ""

                        // Navega para a tela de despedida, passando a mensagem, o número de repetições e a URI da imagem
                        navController.navigate("bye_screen/$byeMessage/$repetitions/$encodedImageUri")
                    }
                )
            }

            // Define a rota para a tela de despedida (ByeScreen)
            composable("bye_screen/{byeMessage}/{repetitions}/{imageUri}") { backStackEntry ->

                // Extrai os argumentos passados pela navegação
                val byeMessage = backStackEntry.arguments?.getString("byeMessage") ?: ""
                val repetitions = backStackEntry.arguments?.getString("repetitions")?.toIntOrNull() ?: 1
                val encodedImageUri = backStackEntry.arguments?.getString("imageUri") ?: ""

                // Decodifica a URI da imagem (se houver) para usá-la
                val imageUri = if (encodedImageUri.isNotEmpty()) {
                    java.net.URLDecoder.decode(encodedImageUri, "UTF-8")
                } else {
                    null
                }

                // Exibe a tela de despedida com os dados recebidos
                ByeScreen(
                    byeMessage = byeMessage,
                    repetitions = repetitions,
                    imageUri = imageUri,
                    onNavigateBack = { navController, message ->
                        // Ao voltar, atualiza a mensagem de boas-vindas com a mensagem de despedida
                        welcomeMessage = message

                        // Volta para a tela anterior (GreetingScreen)
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
    // Estados para armazenar a mensagem de despedida e o número de repetições digitados pelo usuário
    var byeMessage by remember { mutableStateOf(TextFieldValue("")) }
    var repetitions by remember { mutableStateOf(TextFieldValue("")) }

    // Estados para controlar erros nos campos (se estão vazios ou inválidos)
    var byeMessageError by remember { mutableStateOf(false) }
    var repetitionsError by remember { mutableStateOf(false) }

    // Estado para armazenar a URI da imagem selecionada da galeria
    var imageUri by remember { mutableStateOf<String?>(null) }

    // Configura um lançador para selecionar uma imagem da galeria
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()

    ) { uri ->

        // Atualiza o estado imageUri com a URI da imagem selecionada
        imageUri = uri?.toString()
    }

    // Layout da tela, usando uma coluna centralizada
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = welcomeMessage,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Texto que instrui o usuário a inserir a mensagem de despedida
        Text(
            text = stringResource(R.string.goodbye),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Campo de texto para inserir a mensagem de despedida
        OutlinedTextField(
            value = byeMessage,
            onValueChange = {
                byeMessage = it // Atualiza o estado com o novo valor digitado
                byeMessageError = it.text.isEmpty() // Define erro se o campo estiver vazio
            },

            label = { Text(text = stringResource(R.string.input_message)) },
            isError = byeMessageError, // Exibe um indicador de erro se o campo estiver vazio
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )

        // Texto que instrui o usuário a inserir o número de repetições
        Text(
            text = stringResource(R.string.input_number),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Campo de texto para inserir o número de repetições
        OutlinedTextField(
            value = repetitions, // Valor atual do campo
            onValueChange = {
                repetitions = it // Atualiza o estado com o novo valor digitado
                repetitionsError = it.text.isEmpty() || it.text.toIntOrNull() == null // Define erro se o campo estiver vazio ou não for um número
            },
            label = { Text(text = stringResource(R.string.input_number)) },
            isError = repetitionsError, // Exibe um indicador de erro se o campo estiver vazio ou inválido
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )

        // Botão para selecionar uma imagem da galeria
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
                // Verifica se há erros nos campos antes de navegar
                byeMessageError = byeMessage.text.isEmpty()
                repetitionsError = repetitions.text.isEmpty() || repetitions.text.toIntOrNull() == null

                // Se não houver erros, navega para a tela de despedida
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