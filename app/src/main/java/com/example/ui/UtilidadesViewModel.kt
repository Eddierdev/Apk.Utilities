package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ConfigRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

data class HourStatus(
    val hour: Int,
    val hasLight: Boolean
)

class UtilidadesViewModel(application: Application) : AndroidViewModel(application) {
    private val configRepository: ConfigRepository

    init {
        val database = AppDatabase.getDatabase(application)
        configRepository = ConfigRepository(database.configDao())
    }

    // Default start date is March 12, 2024
    val defaultStartDate = LocalDate.of(2024, 3, 12)

    val startDate: StateFlow<LocalDate> = configRepository.getConfigFlow("cycle_start_date")
        .map { value ->
            if (value != null) {
                try {
                    LocalDate.parse(value)
                } catch (e: Exception) {
                    defaultStartDate
                }
            } else {
                defaultStartDate
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = defaultStartDate
        )

    private val _selectedDate = MutableStateFlow(LocalDate.of(2024, 3, 12)) // Start with the beautiful demo date
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _selectedTab = MutableStateFlow(1) // Default tab: Luz (1)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    fun setTab(index: Int) {
        _selectedTab.value = index
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun updateStartDate(date: LocalDate) {
        viewModelScope.launch {
            configRepository.saveConfig("cycle_start_date", date.toString())
        }
    }

    fun getDaySchedule(startDate: LocalDate, queryDate: LocalDate): List<HourStatus> {
        val daysDiff = ChronoUnit.DAYS.between(startDate, queryDate)
        return (0..23).map { hour ->
            val hoursDiff = daysDiff * 24 + hour
            // Rotative calculation based on the visual HTML example with shift index O=2:
            val cycleHour = ((hoursDiff + 2) % 9 + 9) % 9
            val hasLight = cycleHour >= 6
            HourStatus(hour, hasLight)
        }
    }

    fun formatFullDate(date: LocalDate): String {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("es"))
            date.format(formatter).replaceFirstChar { it.uppercase() }
        } catch (e: Exception) {
            date.toString()
        }
    }

    fun formatChipLabel(date: LocalDate, today: LocalDate): String {
        return when {
            date == today -> "Hoy"
            date == today.plusDays(1) -> "Mañana"
            else -> {
                val formatter = DateTimeFormatter.ofPattern("EEE dd", Locale("es"))
                val formatted = date.format(formatter).replaceFirstChar { it.uppercase() }
                formatted.replace(".", "")
            }
        }
    }
}
