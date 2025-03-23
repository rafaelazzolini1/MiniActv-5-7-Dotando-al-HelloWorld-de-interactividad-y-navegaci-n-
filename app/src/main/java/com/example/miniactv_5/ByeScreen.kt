package com.example.miniactv_5

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import androidx.navigation.NavController

@Composable
fun ByeScreen(
    byeMessage: String, // Mensagem de despedida digitada pelo usuário
    repetitions: Int, // Número de vezes que a mensagem será repetida
    imageUri: String?, // URI da imagem selecionada (pode ser nula)
    onNavigateBack: (NavController, String) -> Unit, // Função chamada ao voltar para a tela inicial
    navController: NavController // Controlador de navegação
) {

    // Estrutura base da tela usando Scaffold
    Scaffold(
        modifier = Modifier.fillMaxSize(), // Preenche all espaco disponivel
        content = { padding ->
            // Usa LazyColumn para criar uma lista rolável
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Exibe a imagem selecionada, se houver
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

                // Repete a mensagem de despedida o número de vezes especificado
                items(repetitions) {
                    Text(
                        text = byeMessage,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Botão para voltar à tela inicial
                item {
                    Button(
                        onClick = {
                            // Chama a função de navegação para voltar, passando a mensagem de despedida
                            onNavigateBack(navController, byeMessage)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp)
                    ) {
                        Text(text = stringResource(R.string.go_back_button))
                    }
                }
            }
        }
    )
}