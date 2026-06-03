package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.NavigationTab
import java.util.Locale

@Composable
fun HubScreen(viewModel: AppViewModel, modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ColorBg)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome and Intro Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ColorCardPurpleBg),
            modifier = Modifier.fillMaxWidth().testTag("hub_intro_card")
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.title_hub),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorCardPurpleTitle
                )
                Text(
                    text = stringResource(id = R.string.sub_hub),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = ColorCardPurpleHeader,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.5f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.label_crecer),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorCardPurpleHeader
                    )
                    Text(
                        text = stringResource(id = R.string.desc_proximas_utilidades),
                        fontSize = 12.sp,
                        color = ColorTextMain,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Text(
            text = "HERRAMIENTAS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = ColorTextLabel,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        // Utility List
        // 1. Calculadora de Luz (ACTIVE)
        UtilityCard(
            title = stringResource(id = R.string.title_calculadora_luz),
            description = "Calcula rotaciones de electricidad según el ciclo de 6 horas sin luz y 3 horas con luz sin interrupción.",
            status = "NUEVO • ACTIVO",
            icon = "⚡",
            iconBg = Color(0xFFFFD60A).copy(alpha = 0.2f),
            iconColor = ColorHourLuzText,
            isActive = true,
            onClick = { viewModel.selectTab(NavigationTab.LUZ) },
            modifier = Modifier.testTag("utility_luz_card")
        )

        // 2. Calculadora de Consumo (Upcoming)
        UtilityCard(
            title = "Calculadora de Consumo",
            description = "Suma el consumo en vatios de tus electrodomésticos y estima tu costo de energía mensual.",
            status = "PRÓXIMAMENTE",
            icon = "🔌",
            iconBg = ColorBorderLight,
            iconColor = ColorTextLabel,
            isActive = false,
            onClick = {},
            modifier = Modifier.testTag("utility_consumo_card")
        )

        // 3. Conversor de Unidades (Upcoming)
        UtilityCard(
            title = "Conversor de Unidades",
            description = "Conversiones de temperatura, peso, volumen y energía comunes para el día a día.",
            status = "PRÓXIMAMENTE",
            icon = "📏",
            iconBg = ColorBorderLight,
            iconColor = ColorTextLabel,
            isActive = false,
            onClick = {},
            modifier = Modifier.testTag("utility_conversor_card")
        )
    }
}

@Composable
fun UtilityCard(
    title: String,
    description: String,
    status: String,
    icon: String,
    iconBg: Color,
    iconColor: Color,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color.White else Color.White.copy(alpha = 0.6f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isActive) ColorBorderLight else ColorBorderLight.copy(alpha = 0.3f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isActive, onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 20.sp
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) ColorTextMain else ColorTextMain.copy(alpha = 0.5f)
                    )
                }

                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = if (isActive) ColorTextLabel else ColorTextLabel.copy(alpha = 0.5f),
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isActive) ColorCardPurpleBadgeBg.copy(alpha = 0.5f)
                            else ColorBorderLight.copy(alpha = 0.5f)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = status,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) ColorCardPurpleTitle else ColorTextLabel.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
