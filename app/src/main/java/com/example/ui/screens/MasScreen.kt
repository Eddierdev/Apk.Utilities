package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.AppViewModel

@Composable
fun MasScreen(viewModel: AppViewModel, modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ColorBg)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Info Header
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, ColorBorderLight),
            modifier = Modifier.fillMaxWidth().testTag("info_card")
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ColorCardPurpleHeader),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚡",
                        fontSize = 32.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Utilidades Android",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTextMain
                )
                Text(
                    text = "v1.0.0 (Sleek Interface)",
                    fontSize = 12.sp,
                    color = ColorTextLabel
                )

                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Plataforma modular de herramientas del día a día. Tu aplicación para organizar de forma automatizada rutinas y consultas rápidas.",
                    fontSize = 13.sp,
                    color = ColorTextLabel,
                    lineHeight = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Modular Architecture Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, ColorBorderLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Principios de Diseño",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTextMain
                )

                ConceptRow(title = "Módulos Independientes", description = "Cada nueva utilidad se agrega sin alterar en absoluto las utilidades existentes.")
                ConceptRow(title = "Room Database", description = "Persistencia segura y reactiva basada en un patrón de repositorio desacoplado.")
                ConceptRow(title = "Clean Architecture", description = "Flujos de datos asíncronos y desacoplados combinando ViewModel con Kotlin StateFlow.")
            }
        }

        // Action Reset block
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ColorCardPurpleBg),
            modifier = Modifier.fillMaxWidth().testTag("reset_card")
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Mantenimiento",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorCardPurpleHeader,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Reiniciar Configuración",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorCardPurpleTitle
                )
                Text(
                    text = "Esto restablecerá la fecha de inicio del ciclo 6x3 al valor predeterminado del sistema (12 de Marzo de 2024).",
                    fontSize = 12.sp,
                    color = ColorCardPurpleHeader,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Button(
                    onClick = { viewModel.setCycleStartDate("2024-03-12") },
                    colors = ButtonDefaults.buttonColors(containerColor = ColorButtonPurpleBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("reset_db_button")
                ) {
                    Text(text = "Restablecer Ciclo", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ConceptRow(title: String, description: String) {
    Column {
        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorTextMain)
        Text(text = description, fontSize = 12.sp, color = ColorTextLabel, lineHeight = 16.sp)
    }
}
