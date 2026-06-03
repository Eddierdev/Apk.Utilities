package com.example.ui.screens

import android.app.DatePickerDialog
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.util.HourStatus
import com.example.util.LuzEngine
import com.example.viewmodel.AppViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Sleek Interface Theme Colors
val ColorBg = Color(0xFFF3F4F9)
val ColorTextMain = Color(0xFF1B1B1F)
val ColorTextLabel = Color(0xFF44474E)
val ColorBorderLight = Color(0xFFE1E2E9)

val ColorCardPurpleBg = Color(0xFFE8DEF8)
val ColorCardPurpleHeader = Color(0xFF4F378B)
val ColorCardPurpleTitle = Color(0xFF21005D)
val ColorCardPurpleBadgeBg = Color(0xFFD0BCFF)
val ColorButtonPurpleBg = Color(0xFF6750A4)

val ColorActiveChipBg = Color(0xFFDDE1FF)
val ColorActiveChipText = Color(0xFF001452)
val ColorActiveChipBorder = Color(0xFF4A5C92)

val ColorInactiveChipBorder = Color(0xFFC4C6D0)

val ColorHourNoLuzBg = Color(0xFF2B2D31)
val ColorHourLuzBg = Color(0xFFFFF9C4)
val ColorHourLuzBorder = Color(0xFFFFD60A)
val ColorHourLuzText = Color(0xFF725C00)

@Composable
fun LuzScreen(viewModel: AppViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val cycleStartDateStr by viewModel.cycleStartDate.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val scheduleState by viewModel.daySchedule.collectAsState()

    val cycleStartDate = LuzEngine.parseDate(cycleStartDateStr) ?: LocalDate.of(2024, 3, 12)
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("es", "ES"))
    val weekdayFormatter = DateTimeFormatter.ofPattern("EEE dd", Locale("es", "ES"))

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ColorBg)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Configuration Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ColorCardPurpleBg),
            modifier = Modifier.fillMaxWidth().testTag("config_card")
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.label_config_ciclo).uppercase(Locale.getDefault()),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorCardPurpleHeader,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = stringResource(id = R.string.label_sistema_6x3),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorCardPurpleTitle
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ColorCardPurpleBadgeBg)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.tag_activo),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorCardPurpleTitle
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.5f))
                            .border(1.dp, ColorCardPurpleHeader.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = stringResource(id = R.string.label_inicio_ciclo),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = ColorCardPurpleHeader
                            )
                            Text(
                                text = cycleStartDate.format(dateFormatter),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTextMain
                            )
                        }
                    }

                    Button(
                        onClick = {
                            val picker = DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    val newDate = LocalDate.of(year, month + 1, dayOfMonth)
                                    viewModel.setCycleStartDate(newDate.toString())
                                },
                                cycleStartDate.year,
                                cycleStartDate.monthValue - 1,
                                cycleStartDate.dayOfMonth
                            )
                            picker.show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorButtonPurpleBg),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .height(52.dp)
                            .testTag("edit_cycle_button")
                    ) {
                        Text(
                            text = stringResource(id = R.string.btn_editar),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // 2. Date Selector Chips Row
        val chipDates = listOf(
            LocalDate.now() to stringResource(id = R.string.chip_hoy),
            LocalDate.now().plusDays(1) to stringResource(id = R.string.chip_manana),
            LocalDate.now().plusDays(2) to LocalDate.now().plusDays(2).format(weekdayFormatter).replaceFirstChar { it.uppercase() },
            LocalDate.now().plusDays(3) to LocalDate.now().plusDays(3).format(weekdayFormatter).replaceFirstChar { it.uppercase() }
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth().testTag("date_chips_row"),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chipDates) { (date, label) ->
                val isSelected = date == selectedDate
                DateChip(
                    label = label,
                    isSelected = isSelected,
                    onClick = { viewModel.setSelectedDate(date) },
                    modifier = Modifier.testTag("date_chip_${label.lowercase(Locale.getDefault()).replace(" ", "_")}")
                )
            }

            // Custom Date selector chip
            item {
                val isCustomSelected = chipDates.none { it.first == selectedDate }
                val customLabel = if (isCustomSelected) selectedDate.format(dateFormatter) else "Otro..."
                DateChip(
                    label = customLabel,
                    isSelected = isCustomSelected,
                    onClick = {
                        val picker = DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val customDate = LocalDate.of(year, month + 1, dayOfMonth)
                                viewModel.setSelectedDate(customDate)
                            },
                            selectedDate.year,
                            selectedDate.monthValue - 1,
                            selectedDate.dayOfMonth
                        )
                        picker.show()
                    },
                    modifier = Modifier.testTag("date_chip_custom")
                )
            }
        }

        // 3. Hour Visualization Column Grid Card
        scheduleState?.let { schedule ->
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, ColorBorderLight),
                modifier = Modifier.fillMaxWidth().testTag("cronograma_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.title_cronograma),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTextMain
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(ColorHourLuzBorder, CircleShape)
                                )
                                Text(
                                    text = stringResource(id = R.string.label_luz),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = ColorTextLabel
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(ColorHourNoLuzBg, CircleShape)
                                )
                                Text(
                                    text = stringResource(id = R.string.label_sin_luz),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = ColorTextLabel
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 24 Hour Grid: 6 rows of 4 columns
                    for (row in 0 until 6) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (col in 0 until 4) {
                                val hourIdx = row * 4 + col
                                if (hourIdx < schedule.hours.size) {
                                    val hourStatus = schedule.hours[hourIdx]
                                    HourGridCell(
                                        hourStatus = hourStatus,
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("hour_cell_$hourIdx")
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bottom info block
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(BorderStroke(1.dp, ColorBorderLight), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.label_total_diario),
                                fontSize = 12.sp,
                                color = ColorTextLabel
                            )
                            Text(
                                text = stringResource(
                                    id = R.string.format_total_luz_corte,
                                    schedule.totalHoursLuz,
                                    schedule.totalHoursSinLuz
                                ),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTextMain
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DateChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) ColorActiveChipBg else Color.White)
            .border(
                1.dp,
                if (isSelected) ColorActiveChipBorder else ColorInactiveChipBorder,
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) ColorActiveChipText else ColorTextLabel
        )
    }
}

@Composable
fun HourGridCell(hourStatus: HourStatus, modifier: Modifier = Modifier) {
    val formattedHour = String.format(Locale.getDefault(), "%02d:00", hourStatus.hour)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (hourStatus.hasLuz) ColorHourLuzBg else ColorHourNoLuzBg)
            .then(
                if (hourStatus.hasLuz) {
                    Modifier.border(1.5.dp, ColorHourLuzBorder, RoundedCornerShape(12.dp))
                } else Modifier
            )
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = formattedHour,
                fontSize = 10.sp,
                color = if (hourStatus.hasLuz) ColorHourLuzText else Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (hourStatus.hasLuz) "🟡" else "⬛",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
