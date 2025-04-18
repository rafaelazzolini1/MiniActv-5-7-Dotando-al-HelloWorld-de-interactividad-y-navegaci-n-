package com.example.miniactv_5

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun GreetingScreen(
    viewModel: AppViewModel,
    navController: NavController
) {
    val welcomeMessage by viewModel.welcomeMessage
    val byeMessage by viewModel.byeMessage
    val repetitions by viewModel.repetitions
    val byeMessageError by viewModel.byeMessageError
    val repetitionsError by viewModel.repetitionsError

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

        InputField(
            value = byeMessage,
            onValueChange = { viewModel.updateByeMessage(it) },
            label = stringResource(R.string.input_message),
            isError = byeMessageError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        InputField(
            value = repetitions,
            onValueChange = { viewModel.updateRepetitions(it) },
            label = stringResource(R.string.input_number),
            isError = repetitionsError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        ImagePickerButton(
            onImageSelected = { viewModel.updateImageUri(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        ActionButton(
            text = stringResource(R.string.goodbye_button),
            onClick = { viewModel.navigateToByeScreen(navController) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}