package com.example.miniactv_5

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import java.net.URLDecoder

class AppViewModel : ViewModel() {
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

    private val _welcomeMessage = mutableStateOf("Bienvenido")
    val welcomeMessage: State<String> get() = _welcomeMessage

    companion object {
        const val MAX_REPETITIONS = 20
    }

    fun updateByeMessage(newMessage: String) {
        _byeMessage.value = newMessage
        _byeMessageError.value = newMessage.isBlank()
    }

    fun updateRepetitions(newRepetitions: String) {
        _repetitions.value = newRepetitions
        _repetitionsError.value = newRepetitions.isBlank() || newRepetitions.toIntOrNull()?.let { it <= 0 || it > MAX_REPETITIONS } ?: true
    }

    fun updateImageUri(uri: String?) {
        _imageUri.value = uri
    }

    fun updateWelcomeMessage(message: String) {
        _welcomeMessage.value = message
    }

    fun canNavigateToByeScreen(): Boolean {
        _byeMessageError.value = _byeMessage.value.isBlank()
        _repetitionsError.value = _repetitions.value.isBlank() || _repetitions.value.toIntOrNull()?.let { it <= 0 || it > MAX_REPETITIONS } ?: true
        return !_byeMessageError.value && !_repetitionsError.value
    }

    fun navigateToByeScreen(navController: NavController) {
        if (canNavigateToByeScreen()) {
            navController.navigate("bye_screen")
        }
    }

    fun navigateBack(navController: NavController, message: String) {
        updateWelcomeMessage(message)
        navController.popBackStack("greeting_screen", inclusive = false)
    }

    fun decodeImageUri(encodedUri: String): String? {
        return if (encodedUri.isNotEmpty()) URLDecoder.decode(encodedUri, "UTF-8") else null
    }
}