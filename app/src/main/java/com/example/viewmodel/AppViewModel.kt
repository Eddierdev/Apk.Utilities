package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ConfigRepository
import com.example.util.DaySchedule
import com.example.util.LuzEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class NavigationTab {
    HUB, LUZ, MAS
}

class AppViewModel(private val repository: ConfigRepository) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _currentTab = MutableStateFlow(NavigationTab.LUZ)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    // Default starting date for the 6x3 cycle is March 12, 2024
    private val defaultStartDate = "2024-03-12"
    
    val cycleStartDate: StateFlow<String> = repository.getConfigFlow("cycle_start_date")
        .combine(MutableStateFlow(defaultStartDate)) { dbValue, default ->
            dbValue ?: default
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = defaultStartDate
        )

    val daySchedule: StateFlow<DaySchedule?> = combine(
        cycleStartDate,
        _selectedDate
    ) { startDate, selectedDate ->
        LuzEngine.getDaySchedule(startDate, selectedDate)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun setCycleStartDate(dateStr: String) {
        viewModelScope.launch {
            repository.saveConfig("cycle_start_date", dateStr)
        }
    }

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun selectTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    class Factory(private val repository: ConfigRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AppViewModel(repository) as T
        }
    }
}
