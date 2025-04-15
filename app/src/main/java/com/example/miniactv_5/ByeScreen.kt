package com.example.miniactv_5

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun ByeScreen(
    byeMessage: String,
    repetitions: Int,
    encodedImageUri: String,
    navController: NavController,
    viewModel: AppViewModel = viewModel()
) {
    val imageUri = viewModel.decodeImageUri(encodedImageUri)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Exibe a imagem, se houver
                if (imageUri != null) {
                    item {
                        // Usa AsyncImage (da biblioteca Coil) para carregar e exibir a imagem
                        AsyncImage(
                            model = imageUri, // URI da imagem
                            contentDescription = "Selected image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(bottom = 16.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                // Repete a mensagem
                items(repetitions.coerceAtMost(AppViewModel.MAX_REPETITIONS)) {
                    Text(
                        text = byeMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Botão para voltar
                item {
                    ActionButton(
                        text = stringResource(R.string.go_back_button),
                        onClick = { viewModel.navigateBack(navController, byeMessage) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
//                        contentDescription = stringResource(R.string.go_back_button_description)
                    )
                }
            }
        }
    )
}