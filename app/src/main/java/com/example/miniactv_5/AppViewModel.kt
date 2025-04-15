package com.example.miniactv_5

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import java.net.URLDecoder
import java.net.URLEncoder

class AppViewModel : ViewModel() {
    // Estados para GreetingScreen
    private val _byeMessage = mutableStateOf("")
    val byeMessage: State<String> get() = _byeMessage

    private val _repetitions = mutableStateOf("")
    val repetitions: State<String> get() = _repetitions

    private val _imageUri = mutableStateOf<String?>(null)
    val imageUri: State<String?> get() = _imageUri

    private val _byeMessageError = mutableStateOf(false)
    val byeMessageError: State<Boolean> get() = _byeMessageError

    private val _repetitionsError = mutableStateOf(false)
    val repetitionsError: State<Boolean> get() = _repetitionsError

    // Estado para mensagem de boas-vindas (compartilhado entre telas)
    private val _welcomeMessage = mutableStateOf("Bienvenido")
    val welcomeMessage: State<String> get() = _welcomeMessage

    // Constantes
    companion object {
        const val MAX_REPETITIONS = 50
    }

    // Atualiza a mensagem de despedida e valida
    fun updateByeMessage(newMessage: String) {
        _byeMessage.value = newMessage
        _byeMessageError.value = newMessage.isBlank()
    }

    // Atualiza o número de repetições e valida
    fun updateRepetitions(newRepetitions: String) {
        _repetitions.value = newRepetitions
        _repetitionsError.value = newRepetitions.isBlank() || newRepetitions.toIntOrNull()?.let { it <= 0 || it > MAX_REPETITIONS } ?: true
    }

    // Atualiza a URI da imagem
    fun updateImageUri(uri: String?) {
        _imageUri.value = uri
    }

    // Atualiza a mensagem de boas-vindas
    fun updateWelcomeMessage(message: String) {
        _welcomeMessage.value = message
    }

    // Verifica se a navegação para ByeScreen é válida
    fun canNavigateToByeScreen(): Boolean {
        _byeMessageError.value = _byeMessage.value.isBlank()
        _repetitionsError.value = _repetitions.value.isBlank() || _repetitions.value.toIntOrNull()?.let { it <= 0 || it > MAX_REPETITIONS } ?: true
        return !_byeMessageError.value && !_repetitionsError.value
    }

    // Navega para ByeScreen
    fun navigateToByeScreen(navController: NavController) {
        if (canNavigateToByeScreen()) {
            val encodedImageUri = _imageUri.value?.let { URLEncoder.encode(it, "UTF-8") } ?: ""
            val route = "bye_screen/${_byeMessage.value}/${_repetitions.value.toInt()}/$encodedImageUri"
            navController.navigate(route)
        }
    }

    // Navega de volta para GreetingScreen
    fun navigateBack(navController: NavController, message: String) {
        updateWelcomeMessage(message)
        navController.popBackStack()
    }

    // Decodifica a URI da imagem
    fun decodeImageUri(encodedUri: String): String? {
        return if (encodedUri.isNotEmpty()) URLDecoder.decode(encodedUri, "UTF-8") else null
    }
}